package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum UserDeliveryStatus implements ValueEnum<Integer> {


    FREE(1),

    BUSY(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private UserDeliveryStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private UserDeliveryStatus() {
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
