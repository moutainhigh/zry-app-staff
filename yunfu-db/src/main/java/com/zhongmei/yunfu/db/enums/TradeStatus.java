package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeStatus implements ValueEnum<Integer> {


    UNPROCESSED(1),


    TEMPORARY(2),


    CONFIRMED(3),


    FINISH(4),


    RETURNED(5),


    INVALID(6),


    REFUSED(7),


    CANCELLED(8),


    TOREFUND(9),


    REPEATED(10),

    CREDIT(11),

    WRITEOFF(12),

    SQUAREUP(13),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeStatus() {
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
