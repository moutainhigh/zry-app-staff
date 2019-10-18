package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum BuffetType implements ValueEnum<Integer> {


    TRADITION(1),


    DISHMENU(2),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BuffetType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BuffetType() {
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
