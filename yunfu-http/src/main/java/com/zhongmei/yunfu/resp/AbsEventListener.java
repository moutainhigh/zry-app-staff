package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.IUserEvent;

import java.net.URI;
import java.util.UUID;


public abstract class AbsEventListener implements IUserEvent {

    protected String eventName;

    public AbsEventListener(UserActionEvent event) {
        this(event.eventName);
    }

    public AbsEventListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    public static String getEventName(Object listener) {
        if (listener instanceof IUserEvent) {
            return ((IUserEvent) listener).getEventName();
        }
        return null;
    }

    public static String getNetKey(String method, String url) {
        URI uri = URI.create(url);
        url = uri.getScheme() + ":" + uri.getHost() + uri.getPath();
        return String.format("[%s]%s:%s", UUID.randomUUID().toString().replaceAll("-", ""), method, url);
    }
}
