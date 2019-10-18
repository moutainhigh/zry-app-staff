package com.zhongmei.yunfu.monitor;

import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.resp.AbsEventListener;
import com.zhongmei.yunfu.resp.UserActionEvent;

import java.net.URI;
import java.util.UUID;


public class CalmEventSuccessListenerProxy<T> extends AbsEventListenerProxy implements NetworkRequest.OnSuccessListener<T> {

    private final String requestTag;
    private final NetworkRequest.OnSuccessListener<T> mListener;
    private String method;
    private String url;
    private String body;

    public static <T> CalmEventSuccessListenerProxy<T> newProxy(NetworkRequest.OnSuccessListener<T> listener, int method, String url, Object body) {
        return new CalmEventSuccessListenerProxy(listener, getMethodString(method), url, null);
    }

    public CalmEventSuccessListenerProxy(NetworkRequest.OnSuccessListener<T> listener, String method, String url, String body) {
        super(AbsEventListener.getEventName(listener));
        this.mListener = listener;
        this.method = method;
        this.url = url;
        this.body = body;
        this.requestTag = getNetKey(method, url);
        if (eventName != null) {
            UserActionEvent.netStart(eventName, requestTag);
        }
    }


    public static String getMethodString(int method) {
        switch (method) {
            case NetworkRequest.HttpMethod.DEPRECATED_GET_OR_POST:
                return "DEPRECATED_GET_OR_POST";
            case NetworkRequest.HttpMethod.GET:
                return "GET";
            case NetworkRequest.HttpMethod.POST:
                return "POST";
            case NetworkRequest.HttpMethod.PUT:
                return "PUT";
            case NetworkRequest.HttpMethod.DELETE:
                return "DELETE";
            case NetworkRequest.HttpMethod.HEAD:
                return "HEAD";
            case NetworkRequest.HttpMethod.OPTIONS:
                return "OPTIONS";
            case NetworkRequest.HttpMethod.TRACE:
                return "TRACE";
            case NetworkRequest.HttpMethod.PATCH:
                return "PATCH";
            default:
                return "UNKNOWN";
        }
    }

    public NetworkRequest.OnSuccessListener<T> getListener() {
        return mListener;
    }

    @Override
    public void onSuccess(T data) {
        UserActionEvent.netEnd(eventName, requestTag, String.format("[%s]%s", method, url));
        if (mListener != null) {
            mListener.onSuccess(data);
        }
    }

    public static String getNetKey(String method, String url) {
        URI uri = URI.create(url);
        url = uri.getScheme() + ":" + uri.getHost() + uri.getPath();
        return String.format("[%s]%s:%s", UUID.randomUUID().toString().replaceAll("-", ""), method, url);
    }


}
