package com.zhongmei.yunfu.net.builder;

import com.zhongmei.yunfu.net.volley.VolleyError;


public class NetError {
    private VolleyError volleyError;

    public NetError(VolleyError volleyError) {
        this.volleyError = volleyError;
    }

    public NetError(String exceptionMessage, Throwable reason) {
        this.volleyError = new VolleyError(exceptionMessage, reason);
    }

    public VolleyError getVolleyError() {
        return volleyError;
    }
}
