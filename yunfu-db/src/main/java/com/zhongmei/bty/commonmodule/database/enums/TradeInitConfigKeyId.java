package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;

public enum TradeInitConfigKeyId implements ValueEnum<Integer> {

    CURRENCY_TYPE(1),     SERVICE_CHARGE_RATE(2),
    KOU_BEI_NUMBER_TYPE(3),

    @Deprecated
    __UNKNOWN__;

    private final ValueEnum.Helper<Integer> helper;

    private TradeInitConfigKeyId(Integer value) {

        helper = ValueEnum.Helper.valueHelper(value);
    }

    private TradeInitConfigKeyId() {
        helper = ValueEnum.Helper.unknownHelper();
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
