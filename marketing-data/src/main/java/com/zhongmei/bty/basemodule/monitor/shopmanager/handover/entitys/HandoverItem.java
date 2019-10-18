package com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;

import java.math.BigDecimal;


public class HandoverItem extends CashHandoverItem {
    private static final long serialVersionUID = 1L;

    private Integer payCount;

    private Integer refundCount;

    private BigDecimal refundAmount;

    private BigDecimal consumeStoreMoney;
    private BigDecimal consumeStoreSendMoney;
    public Integer getPayCount() {
        return payCount;
    }

    public void setPayCount(Integer payCount) {
        this.payCount = payCount;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public BigDecimal getConsumeStoreMoney() {
        return consumeStoreMoney;
    }

    public void setConsumeStoreMoney(BigDecimal consumeStoreMoney) {
        this.consumeStoreMoney = consumeStoreMoney;
    }

    public BigDecimal getConsumeStoreSendMoney() {
        return consumeStoreSendMoney;
    }

    public void setConsumeStoreSendMoney(BigDecimal consumeStoreSendMoney) {
        this.consumeStoreSendMoney = consumeStoreSendMoney;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}
