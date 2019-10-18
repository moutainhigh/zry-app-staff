package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum InvoiceStatus implements ValueEnum<Integer> {


    INVOICE_ALREADY_APPLIED(0),


    IN_INVOICE(1),


    INVOICE_SUCCESS(2),


    INVOICE_FAILED(3),


    IN_INVOICE_REVOKE(4),


    INVOICE_REVOKE_SUCCESS(5),


    INVOICE_REVOKE_FAILED(6),


    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private InvoiceStatus(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private InvoiceStatus() {
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
