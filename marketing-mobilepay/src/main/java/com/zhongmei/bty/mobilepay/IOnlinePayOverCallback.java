package com.zhongmei.bty.mobilepay;



public interface IOnlinePayOverCallback {
    public void onPayResult(Long paymentItemId, int payStatus);
}
