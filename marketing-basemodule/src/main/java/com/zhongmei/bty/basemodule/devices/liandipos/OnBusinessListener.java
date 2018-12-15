package com.zhongmei.bty.basemodule.devices.liandipos;

/**
 * Created by demo on 2018/12/15
 */
public interface OnBusinessListener {
    /**
     * 交易中的回调
     */
    void onBusiness();

    /**
     * 交易结束后的回调.
     *
     * @param response 结果
     */
    void onComplete(NewLDResponse response);

    /**
     * 结算时的回调
     */
    void onCheckout();
}
