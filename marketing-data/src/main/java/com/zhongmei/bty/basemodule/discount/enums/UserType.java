package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum UserType implements ValueEnum<Integer> {

    MEMBER(1),     MEMBERNON(2),    ALL(-100),

    @Deprecated __UNKNOWN__;

    private final Helper<Integer> helper;

    private UserType(Integer value) {
                helper = Helper.valueHelper(value);
    }

    private UserType() {
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