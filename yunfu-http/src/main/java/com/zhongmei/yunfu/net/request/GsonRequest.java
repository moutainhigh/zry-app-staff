package com.zhongmei.yunfu.net.request;



import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.yunfu.net.RequestManagerCompat;
import com.zhongmei.yunfu.net.volley.AuthFailureError;
import com.zhongmei.yunfu.net.volley.DefaultRetryPolicy;
import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.ParseError;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.net.volley.toolbox.HttpHeaderParser;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.context.util.Gsons;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;


public class GsonRequest<Q, R> extends StatisticsRequest<R> {

    private static final String TAG = GsonRequest.class.getSimpleName();

    private static final boolean isDebug = true;


    public static final String PROTOCOL_CONTENT_TYPE = "application/json; charset=" + PROTOCOL_CHARSET;

    protected static final int TIMEOUT_MS = 10000;

    protected final Q mRequestObject;
    protected final Type mResponseType;
    protected Response.Listener<R> mListener;
    protected final Gson mGson;
    protected String body;

    public GsonRequest(int method, String url, Q requestObject, Type responseType, Response.Listener<R> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        Checks.verifyNotNull(responseType, "responseType");
        Checks.verifyNotNull(listener, "listener");
        this.mRequestObject = requestObject;
        this.mResponseType = responseType;
        this.mListener = listener;
        mGson = Gsons.gsonBuilder().create();
        makeBodyStr();
    }


    public GsonRequest(String url, Q requestObject, Type responseType, Response.Listener<R> listener,
                       Response.ErrorListener errorListener) {

        this(Method.POST, url, requestObject, responseType, listener, errorListener);
    }


    public GsonRequest(String url, String body, Type responseType, Response.Listener<R> listener,
                       Response.ErrorListener errorListener) {

        this(Method.POST, url, body, responseType, listener, errorListener);
    }

    public GsonRequest(int method, String url, String body, Type responseType, Response.Listener<R> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        Checks.verifyNotNull(responseType, "responseType");
        Checks.verifyNotNull(listener, "listener");
        this.body = body;
        this.mListener = listener;
        this.mRequestObject = null;
        this.mResponseType = responseType;
        mGson = Gsons.gsonBuilder().create();
    }


    public void setTimeout(int timeout) {
        this.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 0));
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    public Map<String, String> getHeadersNoError() {
        try {
            return getHeaders();
        } catch (AuthFailureError error) {
            return null;
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            body = getBodyStr();
            return body.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            Log.w("GsonRequest.request", uee);
            return null;
        }
    }


    public String getBodyStr() {
        if (TextUtils.isEmpty(body)) {
            makeBodyStr();
        }

        return body;
    }


    private void makeBodyStr() {
        if (mRequestObject instanceof String) {
            body = (String) mRequestObject;
        } else {
            body = mGson.toJson(mRequestObject);
        }
    }

    @Override
    protected void deliverResponse1(R response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected Response<R> parseNetworkResponse1(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (isDebug) {
                RequestManagerCompat.log("Response: %s", json);
            }
            R responseValue = toResponseValue(json, mResponseType);
            return Response.success(responseValue, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    protected R toResponseValue(String json, Type responseType) throws Exception {
        return mGson.fromJson(json, responseType);
    }

    protected boolean serviceRetry() {
        return false;
    }
}
