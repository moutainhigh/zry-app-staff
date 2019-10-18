package com.zhongmei.bty.basemodule.shopmanager.handover.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum CashHandoverConfigKey implements ValueEnum<Integer> {


    HANDOVERSTYLE(1),

    PRINTISSUESETTING(2),

    PRINTTIME(3),

    CARRYLIMIT(4),

    CARRYRULE(5),

    DINNERBILLORISTYLE(6),

    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private CashHandoverConfigKey(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private CashHandoverConfigKey() {
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
