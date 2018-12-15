package com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;

import java.math.BigDecimal;

/**
 * 交接收款明细
 */
public class HandoverItem extends CashHandoverItem {
    private static final long serialVersionUID = 1L;

    private Integer payCount;

    private Integer refundCount;

    private BigDecimal refundAmount;

    private BigDecimal consumeStoreMoney; //实储消费金额(只有当支付方式为储值消费时可能有值，其余支付方式为null)

    private BigDecimal consumeStoreSendMoney; //储值赠送消费金额(只有当支付方式为储值消费时可能有值，其余支付方式为null)

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
