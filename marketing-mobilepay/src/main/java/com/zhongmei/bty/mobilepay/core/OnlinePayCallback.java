package com.zhongmei.bty.mobilepay.core;

import android.graphics.Bitmap;

import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;

/**
 * Created by demo on 2018/12/15
 * 生成二维码回调接口
 */

public interface OnlinePayCallback extends IOnlinePayOverCallback {
    void onBarcodeScuess(Long paymentItemId, Bitmap bitmap, String codeUrl, boolean isCodeUrl); //生成二维码成功

    void onBarcodeError();//生成二维码失败

    void onAuthCodeScuess(Long paymentItemId);//付款码成功

    void onAuthCodeError();///付款码失败

}
