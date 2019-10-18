package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TradePayStatus implements ValueEnum<Integer> {


    UNPAID(1),

    PAYING(2),

    PAID(3),


    REFUNDING(4),


    REFUNDED(5),


    REFUND_FAILED(6),


    PREPAID(7),


    WAITING_REFUND(8),


    PAID_FAIL(9),


    REPEAT_PAID(10),


    PAID_ERROR(11),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TradePayStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TradePayStatus() {
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
