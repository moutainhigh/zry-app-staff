package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradeType implements ValueEnum<Integer> {


    SELL(1),


    REFUND(2),


    SPLIT(3),


    SELL_FOR_REPEAT(4),


    REFUND_FOR_REPEAT(5),


    SELL_FOR_REVERSAL(6),


    REFUND_FOR_REVERSAL(7),


    UNOIN_TABLE_MAIN(9),


    UNOIN_TABLE_SUB(10),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradeType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradeType() {
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

    public static boolean hasUnionTable(TradeType type) {
        return type != null && (type == UNOIN_TABLE_MAIN || type == UNOIN_TABLE_SUB);
    }

    @Override
    public String toString() {
        return "" + value();
    }

}
