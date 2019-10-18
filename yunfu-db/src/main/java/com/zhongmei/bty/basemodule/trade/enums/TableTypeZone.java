package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TableTypeZone implements ValueEnum<Integer> {


    HALL(0),

    ROOM(1),

    SEAT(2),

    BALCONY(3),

    OTHER(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TableTypeZone(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TableTypeZone() {
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
