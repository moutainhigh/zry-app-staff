package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PayModelGroup implements ValueEnum<Integer> {


    ONLINE(1),


    CASH(2),


    BANK_CARD(3),


    VALUE_CARD(4),


    COUPON(5),


    OTHER(6),


    LAGPAY(119),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayModelGroup(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayModelGroup() {
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
