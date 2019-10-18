package com.zhongmei.bty.data.operates.message.content;

import java.math.BigDecimal;


public class CustomerCardStoreValueRevokeReq {
    private long tradeId;
    private Long reasonId;
    private String reasonContent;
    private long serverUpdateTime;
    private long clientCreateTime;
    private long updatorId;
    private String updatorName;
    private long customerId;
    private long source;
    private BigDecimal addValue;
    private BigDecimal sendValue;
    private Long type;
    private int isCash;
    private String paymentUuid;
    private String entityCardNo;

    private Long storeId;
    private Long bizDate;

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long
    getReasonId() {
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

    public long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSource() {
        return source;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public BigDecimal getAddValue() {
        return addValue;
    }

    public void setAddValue(BigDecimal addValue) {
        this.addValue = addValue;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public int getIsCash() {
        return isCash;
    }

    public void setIsCash(int isCash) {
        this.isCash = isCash;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public String getEntityCardNo() {
        return entityCardNo;
    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
    }

}
