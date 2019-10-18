package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CustomerType implements ValueEnum<Integer> {


    BOOKING(1),


    PAY(2),


    MEMBER(3),




    CUSTOMER(-1),


    CARD(13),


    OWNER(4),


    BOOKING_PEOPLE(-6),


    PAY_PEOPLE(-4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CustomerType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CustomerType() {
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
