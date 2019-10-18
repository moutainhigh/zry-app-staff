package com.zhongmei.yunfu.util;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CarryBitRule implements ValueEnum<Integer> {


    ROUND_UP(1),


    CARRY(2),


    MALING(3),


    THREE_EIGHT_UP(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CarryBitRule(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CarryBitRule() {
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
