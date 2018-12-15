package com.zhongmei.bty.basemodule.devices.handset.http;

import com.zhongmei.yunfu.net.volley.Response;

import org.json.JSONObject;

public interface HandsetOperates {

    /**
     * 验证接口
     */
    void sendVerifyReq(String clientIP, String localHostIp, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    /**
     * 读取手环信息
     */
    void readBraceletReq(String clientIP, String posIP, String uid, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    /**
     * 根据手环ID读取密码
     */
    void readPasswordByBraceletIdReq(String clientIP, String posIP, String uid, String braceletId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    void cancel(String clientIP, String uid, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener);

    void cancelVerify();

    void cancelBracelet();

    void cancelPasswordByBraceletId();

    void cancel();

}
