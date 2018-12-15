package com.zhongmei.yunfu.resp;

/**
 * Created by demo on 2018/12/15
 */
public abstract class EventResponseListener<T> extends AbsEventListener implements ResponseListener<T> {

    public EventResponseListener() {
        this((String) null);
    }

    public EventResponseListener(UserActionEvent event) {
        super(event);
    }

    public EventResponseListener(String eventName) {
        super(eventName);
    }
}
