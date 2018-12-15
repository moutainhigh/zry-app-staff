package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.monitor.AbsEventListenerProxy;
import com.zhongmei.yunfu.monitor.EventListener;
import com.zhongmei.yunfu.net.volley.IUserEvent;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.resp.UserActionEvent;

/**
 * Created by demo on 2018/12/15
 */
public class EventListenerProxy<T> extends AbsEventListenerProxy implements Response.Listener<T> {

    private final String requestTag;
    private final Response.Listener<T> mListener;
    private String method;
    private String url;
    private String body;

    public static <T> EventListenerProxy<T> newListenerProxy(Response.Listener<T> listener, String method, String url, String body) {
        return new EventListenerProxy(listener, method, url, body);
    }

    public EventListenerProxy(Response.Listener<T> listener, String method, String url, String body) {
        super(EventListener.getEventName(listener));
        this.mListener = listener;
        this.method = method;
        this.url = url;
        this.body = body;
        this.requestTag = EventListener.getNetKey(method, url);
        if (listener instanceof IUserEvent) {
            UserActionEvent.netStart(eventName, requestTag);
        }
    }

    public Response.Listener<T> getListener() {
        return mListener;
    }

    @Override
    public void onResponse(T response) {
        UserActionEvent.netEnd(eventName, requestTag, String.format("[%s]%s", method, url));
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    /*public static String getEventName(Response.Listener listener) {
        if (listener instanceof EventListenerProxy) {
            return ((EventListenerProxy) listener).eventName;
        }
        return EventListener.getEventName(listener);
    }*/
}
