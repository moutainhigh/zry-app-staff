package com.zhongmei.bty.mobilepay.v1.event;

import com.zhongmei.yunfu.db.enums.PayModelGroup;



public class UpdateNotPayEvent {
    public PayModelGroup getPayModelType() {
        return payModelType;
    }

    public void setPayModelType(PayModelGroup payModelType) {
        this.payModelType = payModelType;
    }

    private PayModelGroup payModelType;}
