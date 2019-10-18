package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CardKindStatus implements ValueEnum<Integer> {


    UNISSUED(1),


    ISSUED(2),


    DISABLED(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CardKindStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CardKindStatus() {
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
