package com.zhongmei.yunfu.net.request;



import com.zhongmei.yunfu.net.volley.AuthFailureError;
import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyLog;
import com.zhongmei.yunfu.net.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;


public class StringRequest extends StatisticsRequest<String> {
    protected Listener<String> mListener;
    protected String mRequestBody;

    public StringRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }


    public StringRequest(int method, String url, Listener<String> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public StringRequest(int method, String url, String requesString, Listener<String> listener, ErrorListener errorListener) {
        this(method, url, listener, errorListener);
        this.mRequestBody = requesString;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mListener = null;
    }

    @Override
    protected void deliverResponse1(String response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response<String> parseNetworkResponse1(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}