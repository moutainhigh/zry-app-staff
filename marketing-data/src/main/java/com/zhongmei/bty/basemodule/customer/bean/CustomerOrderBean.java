package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.math.BigDecimal;

public class CustomerOrderBean {

    private String mobile;
    private String sellTime;    private String cardNo;    private String sellMoney;    private String operater;    private String deviceNo;    private EntityCardType cardType;
    private Integer accountStatus;     private Integer tradeStatus;
    private Integer tradePayStatus;

    private Integer storeType;     private Integer addValueType;    private BigDecimal addValue;     private BigDecimal sendValue;     private BigDecimal beforeValue;    private BigDecimal endValue;    private BigDecimal beforeRealValue;     private BigDecimal beforeSendValue;        private BigDecimal currentRealValue;     private BigDecimal currentSendValue;     private BigDecimal endRealValue;     private BigDecimal endSendValue;
    private Long tradeId;
        private String paymentUuid;
    private Long customerId;
    private Long historyId;     private Long clientCreateTime;
    private Long serverUpdateTime;
    private Long storeId;
    private Long bizDate;

        private BigDecimal integral;
    private Long cardKindId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(String sellMoney) {
        this.sellMoney = sellMoney;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public EntityCardType getCardType() {
        return cardType;
    }

    public void setCardType(EntityCardType cardType) {
        this.cardType = cardType;
    }

    public AccountStatus getAccountStatus() {
        return ValueEnums.toEnum(AccountStatus.class, accountStatus);
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = ValueEnums.toValue(accountStatus);
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = ValueEnums.toValue(tradeStatus);
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public void setTradePayStatus(TradePayStatus tradePayStatus) {
        this.tradePayStatus = ValueEnums.toValue(tradePayStatus);
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getAddValueType() {
        return addValueType;
    }

    public void setAddValueType(Integer addValueType) {
        this.addValueType = addValueType;
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

    public BigDecimal getBeforeValue() {
        return beforeSendValue == null ? beforeRealValue : (beforeRealValue == null ? BigDecimal.ZERO : beforeRealValue.add(beforeSendValue));
    }

    public BigDecimal getEndValue() {
        return endSendValue == null ? endRealValue : (endRealValue == null ? BigDecimal.ZERO : endRealValue.add(endSendValue));
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

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

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

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
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
}
