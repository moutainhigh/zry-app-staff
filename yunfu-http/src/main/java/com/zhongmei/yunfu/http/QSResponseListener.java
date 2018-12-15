package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;

/**
 *

 *
 */
public interface QSResponseListener<T> {

    /**
     * 当有回复时将回调此方法
     *
     * @param response
     * @see {@link Listener#onResponse(Object)}
     */
    void onResponse(QSResponseObject<T> response);

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

        public static <T> QSResponseListener<T> build(Response.Listener<QSResponseObject<T>> successListener,
                                                      Response.ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }

        /**
         * @version: 1.0
         * @date 2015年4月19日
         */
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
