package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;


public interface QSResponseListener<T> {


    void onResponse(QSResponseObject<T> response);


    void onError(VolleyError error);


    final class Builder {

        public static <T> QSResponseListener<T> build(Response.Listener<QSResponseObject<T>> successListener,
                                                      Response.ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }


        private static class SimpleResponseListener<T> implements QSResponseListener<T> {

            private final Response.Listener<QSResponseObject<T>> successListener;
            private final Response.ErrorListener errorListener;

            SimpleResponseListener(Response.Listener<QSResponseObject<T>> successListener,
                                   Response.ErrorListener errorListener) {
                this.successListener = successListener;
                this.errorListener = errorListener;
            }

            @Override
            public void onResponse(QSResponseObject<T> response) {
                successListener.onResponse(response);
            }

            @Override
            public void onError(VolleyError error) {
                errorListener.onErrorResponse(error);
            }

        }
    }

}
