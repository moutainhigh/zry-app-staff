package com.zhongmei.bty.mobilepay.core;

import android.graphics.Bitmap;

import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;



public interface OnlinePayCallback extends IOnlinePayOverCallback {
    void onBarcodeScuess(Long paymentItemId, Bitmap bitmap, String codeUrl, boolean isCodeUrl);
    void onBarcodeError();
    void onAuthCodeScuess(Long paymentItemId);
    void onAuthCodeError();
}
