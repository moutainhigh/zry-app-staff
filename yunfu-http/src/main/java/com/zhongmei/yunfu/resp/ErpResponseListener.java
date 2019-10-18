package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;


public interface ErpResponseListener<T> {


    void onResponse(ErpResponseObject<T> response);


    void onError(VolleyError error);


    final class Builder {

        public static <T> ErpResponseListener<T> build(Response.Listener<ErpResponseObject<T>> successListener,
                                                       Response.ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }


        private static class SimpleResponseListener<T> implements ErpResponseListener<T> {

            private final Response.Listener<ErpResponseObject<T>> successListener;
            private final Response.ErrorListener errorListener;

            SimpleResponseListener(Response.Listener<ErpResponseObject<T>> successListener,
                                   Response.ErrorListener errorListener) {
                this.successListener = successListener;
                this.errorListener = errorListener;
            }

            @Override
            public void onResponse(ErpResponseObject<T> response) {
                successListener.onResponse(response);
            }

            @Override
            public void onError(VolleyError error) {
                errorListener.onErrorResponse(error);
            }

        }
    }

}
