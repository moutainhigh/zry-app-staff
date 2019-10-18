package com.zhongmei.bty.mobilepay;



public interface IPayOverCallback {
    public void onFinished(boolean isOK, int statusCode);
}
