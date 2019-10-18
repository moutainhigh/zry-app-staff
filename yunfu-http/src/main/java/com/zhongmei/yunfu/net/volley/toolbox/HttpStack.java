

package com.zhongmei.yunfu.net.volley.toolbox;

import com.zhongmei.yunfu.net.volley.AuthFailureError;
import com.zhongmei.yunfu.net.volley.Request;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;


public interface HttpStack {

    public HttpResponse performRequest(Request<?> request,
                                       Map<String, String> additionalHeaders) throws IOException,
            AuthFailureError;

}
