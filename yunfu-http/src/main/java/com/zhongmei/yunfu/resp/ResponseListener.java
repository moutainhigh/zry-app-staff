package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyError;

/**
 *

 *
 */
public interface ResponseListener<T> {

    /**
     * 当有回复时将回调此方法
     *
     * @param response
     * @see {@link Listener#onResponse(Object)}
     */
    void onResponse(ResponseObject<T> response);

    /**
     * 发生异常时将回调此方法(通常是网络不通或程序内部错误)
     *
     * @param error
     * @see {@link ErrorListener#onErrorResponse(VolleyError)}
     */
    void onError(VolleyError error);

    /**
     * @version: 1.0
     * @date 2015年4月17日
     */
    final class Builder {

        public static <T> ResponseListener<T> build(Listener<ResponseObject<T>> successListener,
                                                    ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }

        /**
         * @version: 1.0
         * @date 2015年4月19日
         */
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
