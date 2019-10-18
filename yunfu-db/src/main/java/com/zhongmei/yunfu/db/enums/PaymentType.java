package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum PaymentType implements ValueEnum<Integer> {


    TRADE_SELL(1),


    TRADE_REFUND(2),


    MEMBER_RECHARGE(3),


    MEMBER_REFUND(4),


    CARD_TIME_SELL(5),


    CARD_TIME_REFUND(6),


    ADJUST(7),




    TRADE_PREPAID(13),


    ENTITY_CARD_RECHARGE(16),


    DEPOSIT_PAY(17),


    DEPOSIT_REFUND(18),

    ANONYMOUS_ENTITY_CARD_RECHARGE(19),

    ANONYMOUS_ENTITY_CARD_SELL_RECHARGE(10),


    ANONYMOUS_ENTITY_CARD_SELL(11),


    ANONYMOUS_ENTITY_CARD_REFUND(12),

    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private PaymentType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private PaymentType() {
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
