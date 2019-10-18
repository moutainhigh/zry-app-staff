package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum ChildDishType implements ValueEnum<Integer> {


    CHILD(1),


    EXTRA(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ChildDishType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ChildDishType() {
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
