package com.zhongmei.yunfu.db.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum BusinessType implements ValueEnum<Integer> {

    BEAUTY(1),


    ONLINE_RECHARGE(2),


    CARD_TIME(3)


    ,

    SNACK(4),


    DINNER(5),


    TAKEAWAY(6),



    POS_PAY(5),


    WEIXIN_PRODUCT(6),


    QUICK_PAY(7),


    CARD(8),


    CARD_RECHARGE(9),


    ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE(10),


    ANONYMOUS_ENTITY_CARD_SELL(11),


    ANONYMOUS_ENTITY_CARD_RECHARGE(12),


    ANONYMOUS_ENTITY_CARD_RETURN(13),

    WRITEOFF(14),

    GROUP(15),


    BUFFET(16),


    ENTITY_CARD_CHANGE(17),


    BOOKING_LIST(18),


    TAILING_LIST(19),

    BEKEAY_DEPOSIT(21),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private BusinessType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private BusinessType() {
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
