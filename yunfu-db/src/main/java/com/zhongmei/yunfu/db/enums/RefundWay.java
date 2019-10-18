package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum RefundWay implements ValueEnum<Integer> {


    NONEED_REFUND(1),

    AUTO_REFUND(2),

    HAND_REFUND(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private RefundWay(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private RefundWay() {
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
