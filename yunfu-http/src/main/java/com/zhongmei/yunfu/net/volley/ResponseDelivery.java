

package com.zhongmei.yunfu.net.volley;

public interface ResponseDelivery {

    public void postResponse(Request<?> request, Response<?> response);


    public void postResponse(Request<?> request, Response<?> response,
                             Runnable runnable);


    public void postError(Request<?> request, VolleyError error);
}
