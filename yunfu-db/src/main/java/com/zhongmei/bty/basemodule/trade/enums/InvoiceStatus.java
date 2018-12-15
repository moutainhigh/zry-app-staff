package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * 发票开具状态
 */
public enum InvoiceStatus implements ValueEnum<Integer> {

    /**
     * 已申请开票
     */
    INVOICE_ALREADY_APPLIED(0),

    /**
     * 开票中
     */
    IN_INVOICE(1),

    /**
     * 开票成功
     */
    INVOICE_SUCCESS(2),

    /**
     * 开票失败
     */
    INVOICE_FAILED(3),

    /**
     * 发票冲红中
     */
    IN_INVOICE_REVOKE(4),

    /**
     * 发票冲红成功
     */
    INVOICE_REVOKE_SUCCESS(5),

    /**
     * 发票冲红失败
     */
    INVOICE_REVOKE_FAILED(6),

    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
