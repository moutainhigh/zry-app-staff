package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.resp.UserActionEvent;



public class CalmEventProcessorProxy<T> extends AbsEventListenerProxy implements NetworkRequest.ResponseProcessor<T> {

    private final String requestTag;
    private NetworkRequest.ResponseProcessor<T> responseProcessor;
    private String method;
    private String url;
    private Object body;

    public static <T> CalmEventProcessorProxy<T> newProxy(NetworkRequest.ResponseProcessor<T> listener, String eventName, int method, String url, Object body) {
        return new CalmEventProcessorProxy(listener, eventName, CalmEventSuccessListenerProxy.getMethodString(method), url, null);
    }

    public CalmEventProcessorProxy(NetworkRequest.ResponseProcessor<T> responseProcessor, String eventName, String method, String url, Object body) {
        super(eventName);
        this.responseProcessor = responseProcessor;
        this.method = method;
        this.url = url;
        this.body = body;
        this.requestTag = getNetKey(method, url);
    }

    @Override
    public T process(T response) {
        UserActionEvent.netStart(eventName, requestTag);
        T result = responseProcessor.process(response);
        UserActionEvent.netDbEnd(eventName, requestTag, String.format("[%s]%s", method, url));
        return result;
    }
}
