package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum OperateType implements ValueEnum<Integer> {


    TRADE_HAVENOORDER_RETURN(1),

    TRADE_RETURNED(2),

    ITEM_RETURN_QTY(3),

    TRADE_REPEATED(4),

    TRADE_DINNER_INVALID(5),

    TRADE_FASTFOOD_INVALID(6),


    TRADE_DINNER_REFUSE(7),


    TRADE_FASTFOOD_REFUSE(8),


    TRADE_DINNER_FREE(9),


    TRADE_FASTFOOD_FREE(10),


    TRADE_CANCELLED(11),



    TRADE_CREDIT(12),


    TRADE_DEPOSIT(13),


    TRADE_DISCOUNT(14),


    ITEM_GIVE(15),


    TRADE_BANQUET(16),


    TRADE_REBATE(17),


    TRADE_SINGLE_DISCOUNT(18),


    TRADE_SINGLE_REBATE(19),

    TRADE_SINGLE_PROBLEM(20),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private OperateType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private OperateType() {
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
