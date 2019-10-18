package com.zhongmei.bty.basemodule.commonbusiness.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum ReasonType implements ValueEnum<Integer> {


    TRADE_INVALID(4),

    TRADE_REFUSED(1),

    TRADE_REPEATED(5),

    TRADE_FREE(7),

    ITEM_GIVE(6),

    ITEM_RETURN_QTY(3),

    TRADE_RETURNED(8),


    BOOKING_REFUSED(21),

    BOOKING_CANCEL(20),


    REFUSE_RETURN(-1),


    AGREE_RETURN(-2),


    LAG_REASON(9),


    DEPOSIT_RETURN(40),


    TRADE_DISCOUNT(51),


    TRADE_SINGLE_DISCOUNT(52),


    TRADE_BANQUET(30),


    INTEGRAL_MODIFY(60),

    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private ReasonType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private ReasonType() {
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


    public static ReasonType newReason(int type) {
        for (ReasonType reasonType : ReasonType.values()) {
            if (reasonType.value() == type) {
                return reasonType;
            }
        }
        return __UNKNOWN__;
    }
}
