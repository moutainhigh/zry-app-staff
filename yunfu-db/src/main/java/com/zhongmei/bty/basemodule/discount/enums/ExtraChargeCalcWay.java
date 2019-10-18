package com.zhongmei.bty.basemodule.discount.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum ExtraChargeCalcWay implements ValueEnum<Integer> {


    RATE(1),


    NUMBER_OF_PEOPLE(2),


    FIXED_AMOUNT(3),


    MINIMUM_CHARGE(4),


    PER_UNIT_OF_PEOPLE(5),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ExtraChargeCalcWay(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ExtraChargeCalcWay() {
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
