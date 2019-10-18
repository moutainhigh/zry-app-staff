package com.zhongmei.bty.mobilepay.enums;

import com.zhongmei.yunfu.util.ValueEnum;

import java.io.Serializable;




public enum PayActionPage implements ValueEnum<Integer>, Serializable {


    BALANCE(1),


    COMPAY(2),


    CHARGE(3),


    WRITEOFF(5),


    BOOKINGDEPOSIT(6),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayActionPage(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayActionPage() {
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