package com.zhongmei.bty.basemodule.devices.mispos.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum EntityCardType implements ValueEnum<Integer> {

    CUSTOMER_ENTITY_CARD(1),


    ANONYMOUS_ENTITY_CARD(2),


    GENERAL_CUSTOMER_CARD(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private EntityCardType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private EntityCardType() {
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
