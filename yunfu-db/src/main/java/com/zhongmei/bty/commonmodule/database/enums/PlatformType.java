package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PlatformType implements ValueEnum<Integer> {


    ON_MIND(1),


    ON_LOYAL(3),


    ON_POS(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PlatformType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PlatformType() {
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
