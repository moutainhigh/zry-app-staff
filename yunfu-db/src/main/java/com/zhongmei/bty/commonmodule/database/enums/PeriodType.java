package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PeriodType implements ValueEnum<Integer> {

    BREAKFAST(0),


    LUNCH(1),


    AFTERNOONTEA(2),


    DINNER(3),


    SUPPPER(4),


    OTHERS(5),


    ALLDAY(6),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PeriodType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PeriodType() {
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
