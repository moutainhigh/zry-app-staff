package com.zhongmei.bty.customer.util;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.net.volley.Request.Method;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CustomerRequestManager {
    private static final String TAG = CustomerRequestManager.class.getSimpleName();



    private static final String MEMBER_STATISTICS = "/CalmRouter/memberController/findMemberConsumeInfoForIpad";





    private static CustomerRequestManager sCustomerRequestManager = new CustomerRequestManager();

    private CustomerRequestManager() {

    }

    public static CustomerRequestManager getInstance() {
        return sCustomerRequestManager;
    }

    public static JSONObject post(JSONObject json) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", json);
        jsonObject.put("versionCode", SystemUtils.getVersionCode());
        return jsonObject;
    }

    public static String genRequest(String path, String json) {
        try {
            Log.d("genRequest", json);
            return ShopInfoCfg.getInstance().getServerKey() + path + "?&content="
                    + URLEncoder.encode(json, "UTF-8") + "&versionCode=" + SystemUtils.getVersionCode()
                    + "&versionName=" + SystemUtils.getVersionName() + "&macAddress="
                    + SystemUtils.getMacAddress() + "&shopID="
                    + ShopInfoCfg.getInstance().shopId + "&deviceID="
                    + SystemUtils.getMacAddress();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }


        private void executeCustmerStringRequest(String urlHead, JSONObject json, String hinting,
                                             Listener<String> successListener, ErrorListener errorListener, Context fm) {

        String value = genRequest(urlHead, json.toString());
        CustomerCalmStringRequest request =
                new CustomerCalmStringRequest(Method.POST, value, successListener, errorListener);
        request.executeRequest("1", hinting, fm);
    }
	




	




	


    public void getMemberStatistics(String serverId, Listener<String> successListener, ErrorListener errorListener,
                                    Context fm) {

        JSONObject json = new JSONObject();
        try {
            json.put("memberId", serverId);
            json.put("commercialGroupId", ShopInfoCfg.getInstance().commercialGroupId);
            json.put("commercialId", ShopInfoCfg.getInstance().shopId);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        executeCustmerStringRequest(MEMBER_STATISTICS,
                json,
                MainApplication.getInstance().getString(R.string.customer_get_info),
                successListener,
                errorListener,
                fm);

    }
	

	



}
