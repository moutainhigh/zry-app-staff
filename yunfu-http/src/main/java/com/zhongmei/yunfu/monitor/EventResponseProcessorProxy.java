package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;



public class EventResponseProcessorProxy<T> extends AbsEventListenerProxy implements OpsRequest.ResponseProcessor<T> {

    private final String requestTag;
    private OpsRequest.ResponseProcessor<T> responseProcessor;
    private String method;
    private String url;
    private String body;

    public EventResponseProcessorProxy(OpsRequest.ResponseProcessor<T> responseProcessor, String eventName, String method, String url, String body) {
        super(eventName);
        this.responseProcessor = responseProcessor;
        this.method = method;
        this.url = url;
        this.body = body;
        this.requestTag = AbsEventListener.getNetKey(method, url);
    }

    @Override
    public ResponseObject<T> process(ResponseObject<T> response) {
        UserActionEvent.netStart(eventName, requestTag);
        ResponseObject<T> result = responseProcessor.process(response);
        UserActionEvent.netDbEnd(eventName, requestTag, String.format("[%s]%s", method, url));
        return result;
    }
}
