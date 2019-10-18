package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;



public enum NotifyOrderType implements ValueEnum<Integer> {


    BOOKING(1),


    QUEUE(2),


    TAKEAWAY(3),

    ORDERALL(4),


    TRADE(5),


    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private NotifyOrderType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private NotifyOrderType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
