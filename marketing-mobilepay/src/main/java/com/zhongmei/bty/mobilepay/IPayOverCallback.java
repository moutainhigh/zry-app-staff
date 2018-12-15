package com.zhongmei.bty.mobilepay;

/**
 * Created by demo on 2018/12/15
 */

public interface IPayOverCallback {
    public void onFinished(boolean isOK, int statusCode);
}
