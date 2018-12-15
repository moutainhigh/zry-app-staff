package com.zhongmei.yunfu.http;

import android.content.Context;

import com.zhongmei.yunfu.net.request.StringRequest;
import com.zhongmei.yunfu.net.volley.DefaultRetryPolicy;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;

public class CalmStringRequest extends CalmRequest<String> {

    private int headerType = HeaderType.HEADER_TYPE_SYNC;

    public CalmStringRequest(Context context, int method, String url, Listener<String> successlistener,
                             ErrorListener errorListener) {
        super(context, successlistener, errorListener);
        mRequest = new BusinessStringRequest(method, url, this, this);
    }


    public CalmStringRequest(int method, String url, Listener<String> successlistener, ErrorListener errorListener) {
        super(successlistener, errorListener);
        mRequest = new BusinessStringRequest(method, url, this, this);
    }

    public void setTimeout(int timeout) {
        mRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 0));
    }

    public void setHeaderType(int headerType) {
        this.headerType = headerType;
    }

    @Override
    public void executeRequest(String tag) {
        ((BusinessStringRequest) mRequest).setHeaderType(headerType);
        super.executeRequest(tag);
    }

    @Override
    public void executeRequest(String tag, String hint, Context fm) {
        ((BusinessStringRequest) mRequest).setHeaderType(headerType);
        super.executeRequest(tag, hint, fm);
    }

    public static class BusinessStringRequest extends StringRequest {

        public BusinessStringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        public BusinessStringRequest(int method, String url, Listener<String> listener,
                                     ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            setHeaderType(HeaderType.HEADER_TYPE_SYNC);
            this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, getBodyString());
        }

        public BusinessStringRequest(int method, String url, String request, Listener<String> listener,
                                     ErrorListener errorListener) {
            super(method, url, request, listener, errorListener);
            setHeaderType(HeaderType.HEADER_TYPE_SYNC);
            this.mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, getBodyString());
        }

        public void setHeaderType(int headerType) {
            httpProperties.clear();
            DefaultHeaderInterceptor headerInterceptor = new DefaultHeaderInterceptor(getUrl());
            setHttpProperty(headerInterceptor.getHeaders(headerType));
        }

        @Override
        protected void deliverResponse1(String response) {
            /*if (AppBuildConfig.DEBUG) {
                Log.e("url:" + getUrl(), response);
            }*/
            super.deliverResponse1(response);
        }
    }
}
