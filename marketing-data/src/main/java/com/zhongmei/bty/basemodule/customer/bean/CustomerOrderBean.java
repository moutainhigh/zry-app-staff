package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.math.BigDecimal;

public class CustomerOrderBean {

    private String mobile;
    private String sellTime;//售卡时间
    private String cardNo;//卡号
    private String sellMoney;//售价
    private String operater;//经手人
    private String deviceNo;//设备号
    private EntityCardType cardType;//实体卡类型 1:会员实体卡 2:匿名实体卡

    private Integer accountStatus; //1已到账、0未到账。
    private Integer tradeStatus;
    private Integer tradePayStatus;

    private Integer storeType; //储值类型：0储值、1储值退款、2消费
    private Integer addValueType;//储值类型 1：现金，2:银行卡
    private BigDecimal addValue; //储值金额
    private BigDecimal sendValue; //赠送金额
    private BigDecimal beforeValue;//充值前金额
    private BigDecimal endValue;//当前金额
    private BigDecimal beforeRealValue; // 操作前余额实储金额
    private BigDecimal beforeSendValue;    // 操作前余额赠送金额
    private BigDecimal currentRealValue; // 本次操作实储金额
    private BigDecimal currentSendValue; // 本次操作赠送金额
    private BigDecimal endRealValue; // 操作后实储金额
    private BigDecimal endSendValue; // 操作后赠送金额

    private Long tradeId;
    //实体卡和会员储值需获取到
    private String paymentUuid;
    private Long customerId;
    private Long historyId; //储值记录id
    private Long clientCreateTime;
    private Long serverUpdateTime;
    private Long storeId;
    private Long bizDate;

    //明细里返回
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
