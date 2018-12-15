package com.zhongmei.bty.basemodule.trade.message;


import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class LagReq {
    TradeReq tradeModifyRequest;

    Long tradeId;

    BigDecimal amount;

    Long creatorId;

    String creatorName;

    Long customerId;

    String uuid;

    Long reasonId;

    String reasonContent;

    String customerName;

    String customerPhone;

    public TradeReq getTradeModifyRequest() {
        return tradeModifyRequest;
    }

    public void setTradeModifyRequest(TradeReq tradeModifyRequest) {
        this.tradeModifyRequest = tradeModifyRequest;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
