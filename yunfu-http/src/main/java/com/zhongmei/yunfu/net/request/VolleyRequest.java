package com.zhongmei.yunfu.net.request;

import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.net.volley.Response;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class VolleyRequest<T, R> extends GsonRequest<T, R> {
    // 对于返回数据的进一步处理
    private NetworkRequest.ResponseProcessor mProcessor;

    public VolleyRequest(int method, String url, T requestObject, Type responseType, Response.Listener<R> listener, Response.ErrorListener errorListener) {
        super(method, url, requestObject, responseType, listener, errorListener);
    }

    @Override
    protected R toResponseValue(String json, Type responseType) throws Exception {
        R response = super.toResponseValue(json, responseType);
        if (mProcessor != null) {
            return (R) mProcessor.process(response);
        }
        return response;
    }

    public void setHttpHeader(Map<String, String> headerMap) {
        for (String header : headerMap.keySet()) {
            setHttpProperty(header, headerMap.get(header));
        }
    }

    public void setResponseProcessor(NetworkRequest.ResponseProcessor processor) {
        mProcessor = processor;
    }
}
