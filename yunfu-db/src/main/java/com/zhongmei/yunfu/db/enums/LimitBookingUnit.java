package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum LimitBookingUnit implements ValueEnum<Integer> {

    DAY(1),


    WEEK(2),


    MONTH(3),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private LimitBookingUnit(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private LimitBookingUnit() {
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
