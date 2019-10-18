package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum WorkStatus implements ValueEnum<Integer> {


    AVAILABLE_AFTER_ACTIVATION(1),

    AVAILABLE_AFTER_SALE(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    WorkStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    WorkStatus() {
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