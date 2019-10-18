package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum SubjectType implements ValueEnum<Integer> {


    INCOME(1),


    EXPENSES(2),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private SubjectType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private SubjectType() {
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
