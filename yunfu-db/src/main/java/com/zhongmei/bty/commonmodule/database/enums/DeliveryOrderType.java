package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum DeliveryOrderType implements ValueEnum<Integer> {


    DELIVERY_NOTE(1),

    PICK_UP_NOTE(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryOrderType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryOrderType() {
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
