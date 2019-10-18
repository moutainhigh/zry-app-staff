package com.zhongmei.bty.mobilepay.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum TicketInfoStatus implements ValueEnum<Integer> {


    NON_EXISTENT(-1),


    NOT_USED(1),


    USED(2),


    EXPIRED(3),


    REFUNDED(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private TicketInfoStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private TicketInfoStatus() {
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
