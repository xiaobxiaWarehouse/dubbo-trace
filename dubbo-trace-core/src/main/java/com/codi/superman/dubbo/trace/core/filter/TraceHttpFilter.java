package com.codi.superman.dubbo.trace.core.filter;

import com.codi.base.config.ConfigurationMgr;
import com.codi.base.util.StringUtil;
import com.codi.superman.dubbo.trace.core.TraceAgent;
import com.codi.superman.dubbo.trace.core.TraceContext;
import com.codi.superman.dubbo.trace.core.util.Ids;
import com.codi.superman.dubbo.trace.core.util.ServerInfo;
import com.codi.superman.dubbo.trace.core.util.Times;
import com.google.common.base.Stopwatch;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 前端入口跟踪请求服务
 *
 * @author shi.pengyan
 * @date 2017-02-15 10:38
 */
public class TraceHttpFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TraceHttpFilter.class);

    private TraceAgent agent;

    private Boolean enableTrace = false;
    private String appName = null;
    private String appOwner = null;
    private Set<String> traceUrls = null;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ConfigurationMgr configMgr = ConfigurationMgr.getInstance();
        enableTrace = configMgr.getBoolean("dubbo.codi.trace.enable", false);
        log.info("Dubbo Trace enable={}", enableTrace);

        if (enableTrace) {
            String urls = configMgr.getString("dubbo.codi.trace.monitorUrls");
            if (StringUtil.isNotEmpty(urls)) {
                traceUrls = new HashSet<>(Arrays.asList(urls.split(",")));
            }
            appName = configMgr.getString("dubbo.codi.trace.appName");
            appOwner = configMgr.getString("dubbo.codi.trace.appOwner");

            agent = new TraceAgent(configMgr.getString("dubbo.codi.trace.address", "localhost:9411"));
            log.info("start Trace Agent.");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!enableTrace) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        boolean tracePoint = traceUrls.contains(uri);

        if (tracePoint) {
            // do trace

            Stopwatch watch = Stopwatch.createStarted();

            // start root span
            Span rootSpan = startTrace(req, uri);

            // prepare trace context
            TraceContext.start();
            TraceContext.setTraceId(rootSpan.getTrace_id());
            TraceContext.setSpanId(rootSpan.getId());
            TraceContext.addSpan(rootSpan);

            // executor other filters
            try {
                chain.doFilter(request, response);

            } catch (RuntimeException e) {
                log.error("fail to trace", e);
            } finally {
                // end root span
                endTrace(req, rootSpan, watch);
            }


        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        TraceContext.clear();
    }

    private Span startTrace(HttpServletRequest req, String point) {

        String apiName = req.getRequestURI();
        Span apiSpan = new Span();

        Long id = Ids.get(); //TODO

        apiSpan.setId(id);
        apiSpan.setTrace_id(id);
        apiSpan.setName(point);
        long timestamp = Times.currentMicros();
        apiSpan.setTimestamp(timestamp);


        // sr annotation
        apiSpan.addToAnnotations(
                Annotation.create(timestamp, Constants.SERVER_RECV,
                        Endpoint.create(apiName, ServerInfo.IP4, req.getLocalPort())));

        // app name
        apiSpan.addToBinary_annotations(BinaryAnnotation.create("name", appName, null));

        // app owner
        apiSpan.addToBinary_annotations(BinaryAnnotation.create("owner", appOwner, null));

        // trace desc
//        if (!Strings.isNullOrEmpty(point.getDesc())) {
//            apiSpan.addToBinary_annotations(BinaryAnnotation.create(
//                    "description", point.getDesc(), null
//            ));
//        }

        return apiSpan;
    }

    private void endTrace(HttpServletRequest req, Span span, Stopwatch watch) {
        // ss annotation
        span.addToAnnotations(
                Annotation.create(Times.currentMicros(), Constants.SERVER_SEND,
                        Endpoint.create(span.getName(), ServerInfo.IP4, req.getLocalPort())));

        span.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));

        // send trace spans
        agent.send(TraceContext.getSpans());
    }

}
