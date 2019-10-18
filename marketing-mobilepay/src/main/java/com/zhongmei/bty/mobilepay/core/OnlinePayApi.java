package com.zhongmei.bty.mobilepay.core;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.yunfu.http.CalmImageRequest;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import java.io.Serializable;



public abstract class OnlinePayApi<R> implements Serializable {

    private static final long serialVersionUID = 1L;
        public static boolean OnlineDialogShowing = false;
        public static int barcodeWH = 300;

    private String mCurrentPaymentInfoId = null;
    private String mOnlinePaymentItemUuid = null;

    public final String getOnlinePaymentItemUuid() {
        return mOnlinePaymentItemUuid;
    }

    public final void setOnlinePaymentItemUuid(String mOnlinePaymentItemUuid) {
        this.mOnlinePaymentItemUuid = mOnlinePaymentItemUuid;
    }

    public final String getCurrentPaymentInfoId() {
        return mCurrentPaymentInfoId;
    }

    public final void setCurrentPaymentInfoId(String mCurrentPaymentInfoId) {
        this.mCurrentPaymentInfoId = mCurrentPaymentInfoId;
    }

    public final UserActionEvent getUserActionEventName(PayModeId payModeId) {
        if (payModeId != null) {
            switch (payModeId) {
                case ALIPAY:
                    return UserActionEvent.DINNER_PAY_SETTLE_ALIPAY;
                case BAIFUBAO:
                    return UserActionEvent.DINNER_PAY_SETTLE_BAIDU_PAY;
                case MEITUAN_FASTPAY:
                    return UserActionEvent.DINNER_PAY_SETTLE_MEITUAN_CASH_PAY;
                case JIN_CHENG:
                    return UserActionEvent.DINNER_PAY_SETTLE_JIN_CHENG_PAY;
                case WEIXIN_PAY:
                default:
                    return UserActionEvent.DINNER_PAY_SETTLE_WECHAT_PAY;
            }
        }
        return UserActionEvent.DINNER_PAY_SETTLE_WECHAT_PAY;
    }

        public final void barcodeScuessCallback(final FragmentActivity context, final OnlinePayCallback onlinePayCallback, final PayModelItem payModelItem, final Long paymentItemId, final String codeUrl) {
        if (context == null || onlinePayCallback == null || payModelItem == null || codeUrl == null) {
            return;
        }
        try {
            Bitmap bitmap = null;
            Bitmap logo = null;
            switch (payModelItem.getPayMode()) {

                case ALIPAY:                      bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_alipay);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case BAIFUBAO:                    if (!TextUtils.isEmpty(codeUrl) && URLUtil.isValidUrl(codeUrl)) {
                        CalmImageRequest imageRequest =
                                new CalmImageRequest(codeUrl, new Response.Listener<Bitmap>() {

                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, false);
                                    }
                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ToastUtil.showLongToast(error.getMessage());
                                        onlinePayCallback.onBarcodeError();

                                    }
                                });
                        imageRequest.executeRequest("3", false, "");

                    } else {
                        onlinePayCallback.onBarcodeError();
                    }
                    break;
                case WEIXIN_PAY:                                        bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                        logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_weixin);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;
                case MEITUAN_FASTPAY:
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case JIN_CHENG:                                        bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_other_jin_cheng);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case MEMBER_CARD:
                                        bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_member_card);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case DIANXIN_YIPAY:                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_yipay);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                default:
                                        bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public abstract void getOnlinePayBarcode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback callback);

        public abstract void onlinePayByAuthCode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback callback);

        public abstract void getOnlinePayState(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback);

        public abstract void getOnlinePayStateOfThird(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback);

        public abstract void doVerifyPayResp(final FragmentActivity context, final IPaymentInfo paymentInfo, final R result, final IOnlinePayOverCallback payOverCallback);
}
