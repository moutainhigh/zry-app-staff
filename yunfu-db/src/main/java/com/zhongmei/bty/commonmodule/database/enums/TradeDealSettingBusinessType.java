package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeDealSettingBusinessType implements ValueEnum<Integer> {


    TAKEAWAY(1),


    BOOKING(2),


    DINNER(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeDealSettingBusinessType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeDealSettingBusinessType() {
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
