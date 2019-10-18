package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum ConnectionType implements ValueEnum<Integer> {


    BY_SERVER(1),


    BY_IP(2),


    EMBEDDED(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ConnectionType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ConnectionType() {
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
