package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;

import java.math.BigDecimal;


public class CustomerSellOrderBean extends CustomerOrderBean {
    private Long tradeId;

        private Long serverUpdateTime;

        private String paymentUuid;
    private long customerId;

        private BigDecimal sendValue;    private BigDecimal beforeValue;    private BigDecimal addValue;    private BigDecimal endValue;
    private BigDecimal beforeRealValue;     private BigDecimal beforeSendValue;        private BigDecimal currentRealValue;     private BigDecimal currentSendValue;     private BigDecimal endRealValue;     private BigDecimal endSendValue;
    private BigDecimal integral;
    private String cardKindKame;
    private Long cardKindId;
    private Integer cardStatus;
    private BigDecimal remainValue;
    private Integer type;
    private Integer addValueType;
    private Long tradeClientCreateTime;
    private Long tradeServerUpdateTime;

    private Long historyId;
    private Long storeId;
    private Long bizDate;

    private Integer businessType;







    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public void setPaymentUuid(String paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public void setAddValueType(int addValueType) {
        this.addValueType = addValueType;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getCardKindKame() {
        return cardKindKame;
    }

    public void setCardKindKame(String cardKindKame) {
        this.cardKindKame = cardKindKame;
    }



    public Integer getAddValueType() {
        return addValueType;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setAddValueType(Integer addValueType) {
        this.addValueType = addValueType;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public Long getTradeClientCreateTime() {
        return tradeClientCreateTime;
    }

    public void setTradeClientCreateTime(Long tradeClientCreateTime) {
        this.tradeClientCreateTime = tradeClientCreateTime;
    }

    public Long getTradeServerUpdateTime() {
        return tradeServerUpdateTime;
    }

    public void setTradeServerUpdateTime(Long tradeServerUpdateTime) {
        this.tradeServerUpdateTime = tradeServerUpdateTime;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;

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

    public BigDecimal getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(BigDecimal beforeValue) {
        this.beforeValue = beforeValue;
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

    public BigDecimal getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(BigDecimal remainValue) {
        this.remainValue = remainValue;
    }

    public BusinessType getBusinessType() {
        return ValueEnums.toEnum(BusinessType.class, businessType);
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = ValueEnums.toValue(businessType);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public void setBeforeRealValue(BigDecimal beforeRealValue) {
        this.beforeRealValue = beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public void setBeforeSendValue(BigDecimal beforeSendValue) {
        this.beforeSendValue = beforeSendValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public void setCurrentRealValue(BigDecimal currentRealValue) {
        this.currentRealValue = currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public void setCurrentSendValue(BigDecimal currentSendValue) {
        this.currentSendValue = currentSendValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public void setEndRealValue(BigDecimal endRealValue) {
        this.endRealValue = endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public void setEndSendValue(BigDecimal endSendValue) {
        this.endSendValue = endSendValue;
    }
}
