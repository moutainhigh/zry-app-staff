package com.zhongmei.yunfu.resp;

import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;

public interface YFResponseListener<T> {


    void onResponse(T response);


    void onError(VolleyError error);

}
