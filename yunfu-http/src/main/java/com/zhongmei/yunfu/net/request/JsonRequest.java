package com.zhongmei.yunfu.net.request;



import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyLog;

import java.io.UnsupportedEncodingException;


public abstract class JsonRequest<T> extends StatisticsRequest<T> {



    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    protected Listener<T> mListener;
    protected String mRequestBody;

    protected JsonRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }


    public JsonRequest(String url, String requestBody, Listener<T> listener,
                       ErrorListener errorListener) {
        this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
    }

    public JsonRequest(int method, String url, String requestBody, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;
    }

    @Override
    protected void deliverResponse1(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected abstract Response<T> parseNetworkResponse1(NetworkResponse response);

    @Override
    protected void onFinish() {
        super.onFinish();
        mListener = null;
    }


    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }


    @Override
    public byte[] getPostBody() {
        return getBody();
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}
