package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum InvoiceType implements ValueEnum<Integer> {


    FOOD(1),


    STORED_VALUE(2),


    RETAIL(3),


    OTHER(4),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private InvoiceType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private InvoiceType() {
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
