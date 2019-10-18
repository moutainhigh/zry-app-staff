package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum LayoutType implements ValueEnum<Integer> {


    AREA(1),


    FLOOR(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private LayoutType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private LayoutType() {
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
