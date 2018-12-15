package com.zhongmei.bty.mobilepay.v1.event;

import com.zhongmei.yunfu.db.enums.PayModelGroup;

/**
 * Created by demo on 2018/12/15
 * 刷新未收消息
 */

public class UpdateNotPayEvent {
    public PayModelGroup getPayModelType() {
        return payModelType;
    }

    public void setPayModelType(PayModelGroup payModelType) {
        this.payModelType = payModelType;
    }

    private PayModelGroup payModelType;// 支付方式id
}
