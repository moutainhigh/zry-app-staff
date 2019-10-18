package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PayType implements ValueEnum<Integer> {


    WEB(1),

    QCODE(2),

    SCAN(3),


    APP(4),


    QUICK_PAY(99),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PayType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PayType() {
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
