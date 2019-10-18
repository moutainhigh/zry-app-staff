package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueResp.CardStoreValueItem;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.math.BigDecimal;
import java.util.List;


public class CustomerCardStoreValueResp extends CardBaseResp<List<CardStoreValueItem>> {

    public static class CardStoreValueItem {
        private String paymentUuid;

        private Long storeDate;


        private String cardNum;


        private BigDecimal addValue;


        private BigDecimal sendValue;


        private BigDecimal endValue;


        private String userName;

        private String deviceIdenty;

        private Long customerId;

        private BigDecimal remainValue;

        private BigDecimal integral;

        private String cardKindName;

        private Long cardKindId;

        private int cardStatus;

        private Integer addValueType;
        private Long tradeId;

        private Long tradeClientCreateTime;

        private Long tradeServerUpdateTime;

        private Long storeId;

        private Long bizDate;

        private Integer padNo;

        private Integer businessType;

        private Integer type;
        private Integer tempCardBizType;
        private Integer cardType;
        private BigDecimal beforeRealValue;
        private BigDecimal beforeSendValue;
        private BigDecimal beforePrepareValue;

        private BigDecimal currentSendValue;
        private BigDecimal currentRealValue;
        private BigDecimal currentPrepareValue;

        private BigDecimal endRealValue;
        private BigDecimal endSendValue;
        private BigDecimal endPrepareValue;

        private Integer accountStatus;         private Integer tradeStatus;
        private Integer tradePayStatus;

        public AccountStatus getAccountStatus() {
            return ValueEnums.toEnum(AccountStatus.class, accountStatus);
        }

        public TradeStatus getTradeStatus() {
            return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
        }

        public TradePayStatus getTradePayStatus() {
            return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
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

        public BigDecimal getCurrentSendValue() {
            return currentSendValue;
        }

        public void setCurrentSendValue(BigDecimal currentSendValue) {
            this.currentSendValue = currentSendValue;
        }

        public BigDecimal getCurrentRealValue() {
            return currentRealValue;
        }

        public void setCurrentRealValue(BigDecimal currentRealValue) {
            this.currentRealValue = currentRealValue;
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

        public String getPaymentUuid() {
            return paymentUuid;
        }

        public void setPaymentUuid(String paymentUuid) {
            this.paymentUuid = paymentUuid;
        }

        public Long getStoreDate() {
            return storeDate;
        }

        public void setStoreDate(Long storeDate) {
            this.storeDate = storeDate;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDeviceIdenty() {
            return deviceIdenty;
        }

        public void setDeviceIdenty(String deviceIdenty) {
            this.deviceIdenty = deviceIdenty;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public BigDecimal getRemainValue() {
            return remainValue;
        }

        public void setRemainValue(BigDecimal remainValue) {
            this.remainValue = remainValue;
        }

        public BigDecimal getIntegral() {
            return integral;
        }

        public void setIntegral(BigDecimal integral) {
            this.integral = integral;
        }


        public String getCardKindName() {
            return cardKindName;
        }

        public void setCardKindName(String cardKindName) {
            this.cardKindName = cardKindName;
        }

        public int getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(int cardStatus) {
            this.cardStatus = cardStatus;
        }

        public Integer getAddValueType() {
            return addValueType;
        }

        public void setAddValueType(Integer addValueType) {
            this.addValueType = addValueType;
        }

        public BigDecimal getEndValue() {
            return endValue;
        }

        public void setEndValue(BigDecimal endValue) {
            this.endValue = endValue;
        }

        public Long getTradeId() {
            return tradeId;
        }

        public void setTradeId(Long tradeId) {
            this.tradeId = tradeId;
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

        public Long getCardKindId() {
            return cardKindId;
        }

        public void setCardKindId(Long cardKindId) {
            this.cardKindId = cardKindId;
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

        public Integer getPadNo() {
            return padNo;
        }

        public void setPadNo(Integer padNo) {
            this.padNo = padNo;
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

        public Integer getTempCardBizType() {
            return tempCardBizType;
        }

        public void setTempCardBizType(Integer tempCardBizType) {
            this.tempCardBizType = tempCardBizType;
        }

        public EntityCardType getCardType() {
            return ValueEnums.toEnum(EntityCardType.class, cardType);
        }

        public void setCardType(EntityCardType entityCardType) {
            this.cardType = ValueEnums.toValue(entityCardType);
        }

        public CustomerStoredBalanceResp getMemberValuecardHistory() {
            CustomerStoredBalanceResp history = new CustomerStoredBalanceResp();
            history.setType(0);            history.setCreateDateTime(getStoreDate());
            history.setAddValuecard(getAddValue());
            if (getAddValueType() == 1) {
                history.setCashValuecard(getAddValue().toPlainString());
            } else {
                history.setBankValuecard(getAddValue().toPlainString());
            }
            if (getSendValue() == null) {
                history.setSendValuecard(new BigDecimal("0.00"));
            } else {
                history.setSendValuecard(getSendValue());
            }
            history.setEndValuecard(getEndValue());
            history.setUserName(getUserName());
            history.setBeforeRealValue(getBeforeRealValue());
            history.setBeforeSendValue(getBeforeSendValue());
            history.setCurrentRealValue(getCurrentRealValue());
            history.setCurrentSendValue(getCurrentSendValue());
            history.setEndRealValue(getEndRealValue());
            history.setEndSendValue(getEndSendValue());
            history.setCardNum(getCardNum());
            return history;
        }
    }

}
