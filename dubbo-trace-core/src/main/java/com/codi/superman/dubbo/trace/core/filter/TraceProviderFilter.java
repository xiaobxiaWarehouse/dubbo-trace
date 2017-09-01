package com.codi.superman.dubbo.trace.core.filter;

import com.alibaba.dubbo.rpc.*;
import com.codi.base.config.ConfigurationMgr;
import com.codi.superman.dubbo.trace.core.TraceAgent;
import com.codi.superman.dubbo.trace.core.TraceContext;
import com.codi.superman.dubbo.trace.core.config.TraceConst;

import java.util.Map;

/**
 * 服务提供者过滤
 *
 * @author shi.pengyan
 * @date 2017-02-15 10:48
 */
public class TraceProviderFilter implements Filter {

    private ConfigurationMgr configMgr = ConfigurationMgr.getInstance();
    private Boolean enableTrace = configMgr.getBoolean("dubbo.codi.trace.enable", false);
    private String zipkinServerUrl = configMgr.getString("dubbo.codi.trace.address", "http://localhost:9411/");
    private TraceAgent agent = new TraceAgent(zipkinServerUrl);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        if (!enableTrace) {
            // not enable tracing
            return invoker.invoke(invocation);
        }

        Map<String, String> attaches = invocation.getAttachments();
        if (!attaches.containsKey(TraceConst.TRACE_ID)) {
            // don't need tracing
            return invoker.invoke(invocation);
        }

        // prepare trace context
        startTrace(attaches);

        Result result = invoker.invoke(invocation);

        endTrace();

        return result;
    }

    private void startTrace(Map<String, String> attaches) {

        long traceId = Long.parseLong(attaches.get(TraceConst.TRACE_ID));
        long parentSpanId = Long.parseLong(attaches.get(TraceConst.SPAN_ID));

        // start tracing
        TraceContext.start();
        TraceContext.setTraceId(traceId);
        TraceContext.setSpanId(parentSpanId);
    }

    private void endTrace() {
        agent.send(TraceContext.getSpans());
        TraceContext.clear();
    }
}
