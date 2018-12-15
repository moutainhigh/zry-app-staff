package com.zhongmei.bty.basemodule.devices.handset.http;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HandsetOperatesImpl implements HandsetOperates {

    private final String TAG = "HandsetOperatesImpl";

    private final String HTTP = "http://";

    private HandsetVolleyUtil mVolleyUtil;

    private String verfityTag = "sendVerifyReq";

    private String cancelTag = "cancelReq";

    private String readTag = "readBraceletReq";

    private String passwordTag = "readPasswordByBraceletIdReq";


    public HandsetOperatesImpl(final Context context) {
        mVolleyUtil = new HandsetVolleyUtil() {
            @Override
            public Context getContext() {
                return context;
            }
        };
    }

    @Override
    public void sendVerifyReq(String clientIP, String localHostIp, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = HTTP + clientIP + "/handset/band/v1/bind";
        Log.i(TAG, "sendVerifyReq -> url : " + url);
        Map<String, Object> params = new HashMap<>();
        params.put("hip", localHostIp);
        params.put("ip", clientIP.split(":")[0]);
        mVolleyUtil.byJson(Request.Method.POST, url, params, listener, errorListener, verfityTag);
    }

    @Override
    public void readBraceletReq(String clientIP, String posIP, String uid, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = HTTP + clientIP + "/handset/band/v1/read";
        mVolleyUtil.byJson(Request.Method.POST, url, getBaseParamsData(uid), listener, errorListener, readTag);
    }

    @Override
    public void readPasswordByBraceletIdReq(String clientIP, String posIP, String uid, String braceletId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = HTTP + clientIP + "/handset/band/v1/pwd";
        Map<String, Object> params = getBaseParamsData(uid);
        params.put("band_sn", braceletId);
        params.put("pwd_info", "");
        mVolleyUtil.byJson(Request.Method.POST, url, params, listener, errorListener, passwordTag);
    }

    @Override
    public void cancel(String clientIP, String uid, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = HTTP + clientIP + "/handset/band/v1/cancel";
        Map<String, Object> params = getBaseParamsData(uid);
        mVolleyUtil.byJson(Request.Method.POST, url, params, listener, errorListener, passwordTag);
    }

    @Override
    public void cancelVerify() {
        mVolleyUtil.cancelPendingRequests(verfityTag);
    }

    @Override
    public void cancelBracelet() {
        mVolleyUtil.cancelPendingRequests(readTag);
    }

    @Override
    public void cancelPasswordByBraceletId() {
        mVolleyUtil.cancelPendingRequests(passwordTag);
    }

    @Override
    public void cancel() {
        mVolleyUtil.cancelPendingRequests(cancelTag);
    }

    private Map<String, Object> getBaseParamsData(String uid) {
        Map<String, Object> params = new HashMap<>();
        params.put("msg", "pos read bracelet info");
        params.put("uid", uid);
        params.put("auth", "");
        return params;
    }
}
