package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyError;


public interface ResponseListener<T> {


    void onResponse(ResponseObject<T> response);


    void onError(VolleyError error);


    final class Builder {

        public static <T> ResponseListener<T> build(Listener<ResponseObject<T>> successListener,
                                                    ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }


        private static class SimpleResponseListener<T> implements ResponseListener<T> {

            private final Listener<ResponseObject<T>> successListener;
            private final ErrorListener errorListener;

            SimpleResponseListener(Listener<ResponseObject<T>> successListener,
                                   ErrorListener errorListener) {
                this.successListener = successListener;
                this.errorListener = errorListener;
            }

            @Override
            public void onResponse(ResponseObject<T> response) {
                successListener.onResponse(response);
            }

            @Override
            public void onError(VolleyError error) {
                errorListener.onErrorResponse(error);
            }

        }
    }

}
