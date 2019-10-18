package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum SetDeliveryType implements ValueEnum<Integer> {


    NONE(0),


    HERE(1),


    CARRY(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private SetDeliveryType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private SetDeliveryType() {
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
