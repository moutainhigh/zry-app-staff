package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum DayNumber implements ValueEnum<Integer> {


    MONDAY(1),


    TUESDAY(2),


    WEDNESDAY(3),


    THURSDAY(4),


    FRIDAY(5),


    SATURDAY(6),


    SUNDAY(7),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private DayNumber(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private DayNumber() {
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
