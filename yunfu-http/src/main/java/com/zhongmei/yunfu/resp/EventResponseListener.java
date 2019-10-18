package com.zhongmei.yunfu.resp;


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
