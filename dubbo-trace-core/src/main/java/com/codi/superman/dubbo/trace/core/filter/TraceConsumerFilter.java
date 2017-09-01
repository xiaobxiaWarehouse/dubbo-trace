package com.codi.superman.dubbo.trace.core.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.codi.superman.dubbo.trace.core.TraceContext;
import com.codi.superman.dubbo.trace.core.config.TraceConst;
import com.codi.superman.dubbo.trace.core.util.Ids;
import com.codi.superman.dubbo.trace.core.util.Networks;
import com.codi.superman.dubbo.trace.core.util.Times;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.Constants;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务消费者过滤
 *
 * @author shi.pengyan
 * @date 2017-02-15 10:47
 */
public class TraceConsumerFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TraceConsumerFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if (TraceContext.getTraceId() == null) {
            // not need tracing
            return invoker.invoke(invocation);
        }

        // start the watch
        Stopwatch watch = Stopwatch.createStarted();

        Span consumeSpan = startTrace(invoker, invocation);

        logger.info("consumer invoke before: ");
        TraceContext.print();

        Result result = invoker.invoke(invocation);
        RpcResult rpcResult = (RpcResult) result;

        logger.info("consumer invoke after: ");
        TraceContext.print();


        logger.info("sr time: {} ", rpcResult.getAttachment(TraceConst.SERVER_RECEIVE_TIME));
        logger.info("ss time: {}", rpcResult.getAttachment(TraceConst.SERVER_SEND_TIME));

        endTrace(invoker, rpcResult, consumeSpan, watch);

        return rpcResult;
    }

    private Span startTrace(Invoker<?> invoker, Invocation invocation) {

        // start consume span
        Span consumeSpan = new Span();
        consumeSpan.setId(Ids.get());
        long traceId = TraceContext.getTraceId();
        long parentId = TraceContext.getSpanId();
        consumeSpan.setTrace_id(traceId);
        consumeSpan.setParent_id(parentId);
        String serviceName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
        consumeSpan.setName(serviceName);
        long timestamp = Times.currentMicros();
        consumeSpan.setTimestamp(timestamp);

        // cs annotation
        URL provider = invoker.getUrl();
        int providerHost = Networks.ip2Num(provider.getHost());
        int providerPort = provider.getPort();
        consumeSpan.addToAnnotations(
                Annotation.create(timestamp, Constants.CLIENT_SEND, Endpoint.create(serviceName, providerHost, providerPort)));

        String providerOwner = provider.getParameter("owner");
        if (!Strings.isNullOrEmpty(providerOwner)) {
            // app owner
            consumeSpan.addToBinary_annotations(BinaryAnnotation.create("owner", providerOwner, null));
        }

        // attach trace data
        Map<String, String> attaches = invocation.getAttachments();
        attaches.put(TraceConst.TRACE_ID, String.valueOf(consumeSpan.getTrace_id()));
        attaches.put(TraceConst.SPAN_ID, String.valueOf(consumeSpan.getId()));

        return consumeSpan;
    }

    private void endTrace(Invoker invoker, Result result, Span consumeSpan, Stopwatch watch) {
        consumeSpan.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));

        // cr annotation
        URL provider = invoker.getUrl();
        consumeSpan.addToAnnotations(
                Annotation.create(Times.currentMicros(), Constants.CLIENT_RECV,
                        Endpoint.create(consumeSpan.getName(), Networks.ip2Num(provider.getHost()), provider.getPort())));

        // exception catch
        Throwable throwable = result.getException();
        if (throwable != null) {
            // attach exception
            consumeSpan.addToBinary_annotations(BinaryAnnotation.create(
                    Constants.ERROR, Throwables.getStackTraceAsString(throwable), null
            ));
        }

        // collect the span
        TraceContext.addSpan(consumeSpan);
    }
}
