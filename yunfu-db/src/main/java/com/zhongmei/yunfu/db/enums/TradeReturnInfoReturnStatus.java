package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeReturnInfoReturnStatus implements ValueEnum<Integer> {


    APPLY(1),


    AGREE(2),


    REFUSE(3),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeReturnInfoReturnStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeReturnInfoReturnStatus() {
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
