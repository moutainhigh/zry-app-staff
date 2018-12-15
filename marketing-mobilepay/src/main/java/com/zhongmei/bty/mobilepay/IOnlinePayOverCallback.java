package com.zhongmei.bty.mobilepay;

/**
 * Created by demo on 2018/12/15
 * 在线支付结果回调
 */

public interface IOnlinePayOverCallback {
    public void onPayResult(Long paymentItemId, int payStatus);
}
