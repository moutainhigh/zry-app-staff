package com.zhongmei.yunfu.net.request;

import com.zhongmei.yunfu.net.volley.AuthFailureError;
import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.util.Checks;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */
public abstract class StatisticsRequest<T> extends Request<T> {
    public static final String PROTOCOL_CHARSET = "utf-8";
    protected final Map<String, String> httpProperties;

    public StatisticsRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        httpProperties = new LinkedHashMap<String, String>();
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        return parseNetworkResponse1(response);
    }

    protected abstract Response<T> parseNetworkResponse1(NetworkResponse response);

    @Override
    protected final void deliverResponse(T response) {
        deliverResponse1(response);
    }

    protected abstract void deliverResponse1(T response);

    public String getMethodString() {
        switch (getMethod()) {
            case Method.DEPRECATED_GET_OR_POST:
                return "DEPRECATED_GET_OR_POST";
            case Method.GET:
                return "GET";
            case Method.POST:
                return "POST";
            case Method.PUT:
                return "PUT";
            case Method.DELETE:
                return "DELETE";
            case Method.HEAD:
                return "HEAD";
            case Method.OPTIONS:
                return "OPTIONS";
            case Method.TRACE:
                return "TRACE";
            case Method.PATCH:
                return "PATCH";
            default:
                return "";
        }
    }

    public String getBodyString() {
        try {
            return new String(getBody(), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public void setHttpProperty(String key, String value) {
        Checks.verifyNotNull(key, "key");
        Checks.verifyNotNull(value, "value");
        httpProperties.put(key, value);
    }

    public void setHttpProperty(Map<String, String> heards) {
        Checks.verifyNotNull(heards, "Map");
        httpProperties.putAll(heards);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return httpProperties;
    }
}
