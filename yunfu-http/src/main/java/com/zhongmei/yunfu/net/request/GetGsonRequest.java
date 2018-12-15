package com.zhongmei.yunfu.net.request;/*
package com.zhongmei.bty.baseservice.net.request;

import android.util.Log;

import com.google.gson.Gson;
import AuthFailureError;
import DefaultRetryPolicy;
import NetworkResponse;
import ParseError;
import Response;
import VolleyError;
import HttpHeaderParser;
import com.zhongmei.bty.baseservice.util.Checks;
import Gsons;

import java.lang.reflect.Type;
import java.util.Map;

*/
/**
 * Created by demo on 2018/12/15
 *
 * @param method
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 * @param timeout 单位ms
 * @param method
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 * @param timeout 单位ms
 *//*

public class GetGsonRequest<R> extends StatisticsRequest<R> {
    private static final String TAG = GetGsonRequest.class.getSimpleName();

    private static final boolean isDebug = true;

    public static final String PROTOCOL_CHARSET = "utf-8";

    public static final String PROTOCOL_CONTENT_TYPE = "application/json; charset=" + PROTOCOL_CHARSET;

    protected static final int TIMEOUT_MS = 10000;

    protected final Type mResponseType;
    protected Response.Listener<R> mListener;
    protected final Gson mGson;

    */
/**
 * @param method
 * @param url
 * @param responseType  Response数据要转成的对象类型
 * @param listener
 * @param errorListener
 *//*

    public GetGsonRequest(int method,String url, Type responseType, Response.Listener<R> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        Checks.verifyNotNull(responseType, "responseType");
        Checks.verifyNotNull(listener, "listener");
        this.mResponseType = responseType;
        mGson = Gsons.gsonBuilder().create();
        mListener = listener;
    }


    */
/**
 * @param timeout 单位ms
 *//*

    public void setTimeout(int timeout) {
        this.setRetryPolicy(new DefaultRetryPolicy(timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
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
                Log.i(TAG, json);
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
}
*/
