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

/**
 * Created by demo on 2018/12/15
 * 在线支付相关接口定义模板(R轮训结果)
 */

public abstract class OnlinePayApi<R> implements Serializable {

    private static final long serialVersionUID = 1L;
    //add v8.10 标识在线支付界面
    public static boolean OnlineDialogShowing = false;
    // 微信二维码宽度
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

    // add v8.16 抽取二维码回调公共方法
    public final void barcodeScuessCallback(final FragmentActivity context, final OnlinePayCallback onlinePayCallback, final PayModelItem payModelItem, final Long paymentItemId, final String codeUrl) {
        if (context == null || onlinePayCallback == null || payModelItem == null || codeUrl == null) {
            return;
        }
        try {
            Bitmap bitmap = null;
            Bitmap logo = null;
            switch (payModelItem.getPayMode()) {

                case ALIPAY:  // 支付宝方式
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_alipay);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case BAIFUBAO:// 百度钱包
                    if (!TextUtils.isEmpty(codeUrl) && URLUtil.isValidUrl(codeUrl)) {
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
                case WEIXIN_PAY:// 微信
                    // 生成二维码
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    // 客显显示二维码
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_weixin);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;
                case MEITUAN_FASTPAY://点评闪付

                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case JIN_CHENG:// 金诚
                    // 生成二维码
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_other_jin_cheng);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case MEMBER_CARD:
                    // 生成二维码
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_member_card);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                case DIANXIN_YIPAY://add v8.16 电信翼支付
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    logo = BitmapFactory.decodeResource(context.getResources(), com.zhongmei.yunfu.mobilepay.R.drawable.pay_online_yipay);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;

                default:
                    // 生成二维码
                    bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                    bitmap = EncodingHandler.addQRCodeLogo(bitmap, logo);
                    onlinePayCallback.onBarcodeScuess(paymentItemId, bitmap, codeUrl, true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成付款二维码接口（生成一码付接口）
    public abstract void getOnlinePayBarcode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback callback);

    //付款码支付接口（付款结果，就是支付接口）
    public abstract void onlinePayByAuthCode(final FragmentActivity context, final IPaymentInfo paymentInfo, final PayModelItem payModelItem, final OnlinePayCallback callback);

    //轮训订单支付结果接口（移动支付轮训结果，只在自己的服务器）
    public abstract void getOnlinePayState(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback);

    //查询单笔支付状态接口（查询第三方库）
    public abstract void getOnlinePayStateOfThird(final FragmentActivity context, final IPaymentInfo paymentInfo, final long paymentItemId, final String paymentItemUuid, final IOnlinePayOverCallback onlinePayOverCallback);

    //在线支付结果验证接口
    public abstract void doVerifyPayResp(final FragmentActivity context, final IPaymentInfo paymentInfo, final R result, final IOnlinePayOverCallback payOverCallback);
}
