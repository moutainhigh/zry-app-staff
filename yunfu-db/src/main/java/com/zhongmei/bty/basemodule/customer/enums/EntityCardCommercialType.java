package com.zhongmei.bty.basemodule.customer.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum EntityCardCommercialType implements ValueEnum<Integer> {


    SELL_SHOP(1),


    USE_SHOP(2),


    STORED_VALUE_SHOP(3),


    CONSUME_SHOP(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private EntityCardCommercialType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private EntityCardCommercialType() {
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
