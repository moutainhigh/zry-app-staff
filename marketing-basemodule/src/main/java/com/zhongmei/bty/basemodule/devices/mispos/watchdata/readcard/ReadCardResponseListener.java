package com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard;

/**
 * 读卡响应回调
 */

public interface ReadCardResponseListener {

    void onActive();

    void onStart();

    void onResponse(String cardNumber);

    void onError(String errorCode, String errorCodeExplain);
}
