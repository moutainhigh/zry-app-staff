package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;

public interface YFResponseListener<T> {

    /**
     * 当有回复时将回调此方法
     *
     * @param response
     * @see {@link Response.Listener#onResponse(Object)}
     */
    void onResponse(T response);

    /**
     * 发生异常时将回调此方法(通常是网络不通或程序内部错误)
     *
     * @param error
     * @see {@link Response.ErrorListener#onErrorResponse(VolleyError)}
     */
    void onError(VolleyError error);

}
