package com.zhongmei.bty.basemodule.devices.handset.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.net.request.JsonRequest;
import com.zhongmei.yunfu.net.request.StringRequest;
import com.zhongmei.yunfu.net.volley.AuthFailureError;
import com.zhongmei.yunfu.net.volley.DefaultRetryPolicy;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.RequestQueue;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyLog;
import com.zhongmei.yunfu.net.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @description (Vollery 网络连接 ， POST和GET方法)
 * @time 2015年5月27日
 */
public abstract class HandsetVolleyUtil {

    private final String TAG = HandsetVolleyUtil.class.getSimpleName();

    private RequestQueue mRequestQueue;

    public Context mContext;

    /**
     * 超时
     **/
    private int SOCK_TIME_OUT = 3 * 1000;
    /**
     * 重试次数
     */
    public static final int MAX_RETRIES = 2;
    /**
     * 退避算法系数
     */
    public static final float BACKOFF_MULT = 2f;

    public abstract Context getContext();

    /**
     * 获取 请求队列
     *
     * @return
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getContext());
        }
        return mRequestQueue;
    }

    /**
     * 添加请求到队列中，并标记tag
     *
     * @param req 请求
     * @param tag 标记
     */
    private <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    /**
     * 添加请求到队列中，使用默认的tag
     *
     * @param req 请求
     */
    private <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            if (TextUtils.isEmpty(tag)) {
                mRequestQueue.cancelAll(TAG);
            } else {
                mRequestQueue.cancelAll(tag);
            }
        }
    }

    /**
     * 通过Url 方法获得String 数据
     *
     * @param url               地址
     * @param listener          成功事件
     * @param errorListener     失败的监听事件
     * @param initialTimeoutMs  超时时间
     * @param maxNumRetries
     * @param backoffMultiplier
     */
    public void byUrl(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, int initialTimeoutMs, int maxNumRetries, float backoffMultiplier, String tag) {
        StringRequest request = new StringRequest(method, url, listener, errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier));
        if (TextUtils.isEmpty(tag)) {
            addToRequestQueue(request);
        } else {
            addToRequestQueue(request, tag);
        }
    }

    /**
     * 通过Url 方法获得String 数据
     *
     * @param method        http 方法  Request.Method.GET，POST，DELETE....
     * @param url           地址
     * @param listener      成功事件
     * @param errorListener 失败的监听事件
     * @param tag           标记 可以为空
     */
    public void byUrl(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, String tag) {
        this.byUrl(method, url, listener, errorListener, SOCK_TIME_OUT, MAX_RETRIES, BACKOFF_MULT, tag);
    }

    /**
     * 通过form表单传递数据
     *
     * @param mehtod        http 方法  Request.Method.GET，POST，DELETE....
     * @param url           地址
     * @param params        请求参数
     * @param listener      成功事件
     * @param errorListener 失败的监听事件
     * @param tag           标记 可以为空
     */
    public void byParams(int mehtod, String url, final Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener, String tag) {
        StringRequest request = new StringRequest(mehtod, url, listener, errorListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeaderMap();
            }
        };
        //重试2次，3秒超时，第一次超时9秒(3+3*2 = 9)，第二次27秒(9 + 9*2 = 27)
        request.setRetryPolicy(new DefaultRetryPolicy(SOCK_TIME_OUT, MAX_RETRIES, BACKOFF_MULT));
        if (TextUtils.isEmpty(tag)) {
            addToRequestQueue(request);
        } else {
            addToRequestQueue(request, tag);
        }
    }


    /**
     * 通过POST params 传递Json 数据
     *
     * @param method        http 方法  Request.Method.GET，POST，DELETE....
     * @param url           地址
     * @param params        请求参数
     * @param listener      成功事件
     * @param errorListener 失败的监听事件
     * @param tag           标记 可以为空
     */
    public void byJson(int method, String url, Map<String, Object> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, String tag) {
        JSONObject jsonObject = new JSONObject(params);
        Log.d(TAG, url + jsonObject.toString());
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(method, url, jsonObject, listener, errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                return getHeaderMap();
            }
        };
        //重试2次，3秒超时，第一次超时9秒(3+3*2 = 9)，第二次27秒(9 + 9*2 = 27)
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(SOCK_TIME_OUT, MAX_RETRIES, BACKOFF_MULT));
        if (TextUtils.isEmpty(tag)) {
            addToRequestQueue(jsonRequest);
        } else {
            addToRequestQueue(jsonRequest, tag);
        }
    }

    private Map<String, String> getHeaderMap() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("Accept", "application/json");
        return params;
    }
}
