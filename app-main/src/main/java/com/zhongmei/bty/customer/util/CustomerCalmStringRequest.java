package com.zhongmei.bty.customer.util;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.http.CalmStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerCalmStringRequest extends CalmStringRequest {
    private static final String TAG = CustomerCalmStringRequest.class.getSimpleName();

    public CustomerCalmStringRequest(int method, String url, Listener<String> successlistener,
                                     ErrorListener errorListener) {
        super(method, url, successlistener, errorListener);
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG + "response", response);
        dismissHintDialog();
        int status;
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("status");
                        if (status == 1000 || status == 0) {
                if (mSuccessListener != null) {
                    mSuccessListener.onResponse(response);
                }
            } else {
                if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.showShortToast(message);
                }
            }
			

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
    }
}
