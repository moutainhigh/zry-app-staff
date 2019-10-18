

package com.zhongmei.yunfu.net.volley;


public interface Network {

    public NetworkResponse performRequest(Request<?> request)
            throws VolleyError;
}
