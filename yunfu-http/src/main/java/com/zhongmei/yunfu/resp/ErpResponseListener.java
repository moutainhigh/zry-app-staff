package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;

/**
 * Created by demo on 2018/12/15
 */
public interface ErpResponseListener<T> {

    /**
     * 当有回复时将回调此方法
     *
     * @param response
     * @see {@link Response.Listener#onResponse(Object)}
     */
    void onResponse(ErpResponseObject<T> response);

    /**
     * 发生异常时将回调此方法(通常是网络不通或程序内部错误)
     *
     * @param error
     * @see {@link Response.ErrorListener#onErrorResponse(VolleyError)}
     */
    void onError(VolleyError error);

    /**
     * @version: 1.0
     * @date 2015年4月17日
     */
    final class Builder {

        public static <T> ErpResponseListener<T> build(Response.Listener<ErpResponseObject<T>> successListener,
                                                       Response.ErrorListener errorListener) {
            return new SimpleResponseListener<T>(successListener, errorListener);
        }

        /**
         * @version: 1.0
         * @date 2015年4月19日
         */
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
