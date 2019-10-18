package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum DeliveryOrderSubStatus implements ValueEnum<Integer> {


    MERCHANT_CANCEL(501),

    DELIVERY_MAN_CANCEL(502),


    DELIVERY_ERROR_RECREATE_ALLOW(503),


    DELIVERY_ERROR_RECREATE_FORBID(504),


    USER_CANCEL(505),


    SYSTEM_CANCEL(506),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DeliveryOrderSubStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DeliveryOrderSubStatus() {
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
