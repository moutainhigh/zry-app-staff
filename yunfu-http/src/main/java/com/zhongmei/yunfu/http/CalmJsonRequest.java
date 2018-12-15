package com.zhongmei.yunfu.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.net.request.JsonRequest;
import com.zhongmei.yunfu.net.volley.DefaultRetryPolicy;
import com.zhongmei.yunfu.net.volley.NetworkResponse;
import com.zhongmei.yunfu.net.volley.ParseError;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CalmJsonRequest extends CalmRequest<JSONObject> {
    private static final String TAG = CalmJsonRequest.class.getSimpleName();

    public CalmJsonRequest(Context context, int method, String url, JSONObject jsonRequest,
                           Listener<JSONObject> successListener, ErrorListener errorListener, boolean changeOld) {
        super(context, successListener, errorListener);
        mRequest = new BusinessJsonObjectRequest(method, url, jsonRequest, this, this, changeOld);
    }

    /**
     * @param timeout 单位ms
     */
    public void setTimeout(int timeout) {
        mRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 0));
    }

    @Override
    public void onResponse(JSONObject response) {
        status = STATUS_FINISH;
        dismissHintDialog();
        int status;
        try {
            status = response.getInt("status");

            // Luo for the donot have this message content
            if (response.has("message")) {
                // 无论成功或者失败，都要提示用户相应的服务器返回的信息
                String message = response.getString("message");
                if (!TextUtils.isEmpty(message)) {
                    Log.d(TAG, "info:打印服务器返回的数据是:" + message + ";position:BusinessJsonRequest->onResponse");
                    // ToastUtil.showShortToast(message);
                }
            }

            if (mSuccessListener != null) {
                mSuccessListener.onResponse(response);
            }
        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
    }

    private class BusinessJsonObjectRequest extends JsonRequest<JSONObject> {


        public BusinessJsonObjectRequest(int method, String url, JSONObject requestBody,
                                         Listener<JSONObject> listener, ErrorListener errorListener, boolean changeOld) {
            super(method, url, errorListener);
            DefaultHeaderInterceptor headerInterceptor = new DefaultHeaderInterceptor(url);
            this.setHttpProperty(headerInterceptor.getDefaultHeader());
            if (changeOld) {
                JSONObject json = new JSONObject();
                try {
                    json.put("shopID", ShopInfoCfg.getInstance().shopId);
                    json.put("brandID", ShopInfoCfg.getInstance().commercialGroupId);
                    json.put("deviceID", SystemUtils.getMacAddress());
                    json.put("versionCode", SystemUtils
                            .getVersionCode());
                    json.put("versionName", SystemUtils
                            .getVersionName());
                    json.put("appType", SystemUtils.getAppType());
                    json.put("systemType", SystemUtils
                            .getSystemType());
                    json.put("content", requestBody);
                    json.put("additions", null);
                    Log.d("tjy", "-----json=" + json.toString() + "------url="
                            + url);
                } catch (JSONException e) {
                    Log.e(TAG, "", e);
                }
                mRequestBody = json.toString();
            } else {
                mRequestBody = requestBody == null ? "" : requestBody.toString();
            }
            mListener = EventListenerProxy.newListenerProxy(listener, getMethodString(), url, getBodyString());
        }

        @Override
        protected void deliverResponse1(JSONObject response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse1(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }
}
