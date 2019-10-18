package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CommercialStatus implements ValueEnum<Integer> {


    AVAILABLE(0),

    UNAVAILABLE(-1),

    PRE(1),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CommercialStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CommercialStatus() {
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
