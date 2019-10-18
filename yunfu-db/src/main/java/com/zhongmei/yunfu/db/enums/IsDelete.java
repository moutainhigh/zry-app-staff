package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum IsDelete implements ValueEnum<Integer> {


    VALID(0),


    INVALID(-1),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private IsDelete(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private IsDelete() {
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
