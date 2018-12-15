package com.zhongmei.bty.mobilepay.event;

/**
 * Created by demo on 2018/12/15
 */

public class SeparateEvent {
    public static final int EVENT_SEPARATE_SAVE = 1;//拆单操作成功
    public static final int EVENT_SEPARATE_PAYED = 2;//拆单支付成功
    public static final int EVENT_SEPARATE_PAYING = 3;//拆单支付中（已经支付了部分）
    public static final int EVENT_RESOURCE_PAYING = 4;//原单支付中（已经支付了部分）

    public SeparateEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    int status = 0;
}
