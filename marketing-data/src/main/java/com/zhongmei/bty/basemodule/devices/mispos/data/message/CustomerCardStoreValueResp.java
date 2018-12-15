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

/**
 * 实体卡储值记录
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerCardStoreValueResp extends CardBaseResp<List<CardStoreValueItem>> {

    public static class CardStoreValueItem {
        private String paymentUuid;

        private Long storeDate;

        /**
         * 卡号
         */
        private String cardNum;

        /**
         * 储值金额
         */
        private BigDecimal addValue;

        /**
         * 赠送金额
         */
        private BigDecimal sendValue;

        /**
         * 每次储值后的余额
         */
        private BigDecimal endValue;

        /**
         * 经手人
         */
        private String userName;

        private String deviceIdenty;

        private Long customerId;

        private BigDecimal remainValue;

        private BigDecimal integral;

        private String cardKindName;

        private Long cardKindId;

        private int cardStatus;

        private Integer addValueType;//储值类型 1：现金，2:银行卡

        private Long tradeId;

        private Long tradeClientCreateTime;

        private Long tradeServerUpdateTime;

        private Long storeId;

        private Long bizDate;

        private Integer padNo;

        private Integer businessType;

        private Integer type;//记录类型 0：储值，1：退款，2：调账,3：消费，4：换卡储值调账

        private Integer tempCardBizType;//临时卡记录类型0：储值，1:消费，2:消费退款，3：退卡退款'

        private Integer cardType;//实体卡类型 1:会员实体卡  2:匿名实体卡

        private BigDecimal beforeRealValue; // 操作前余额实储金额

        private BigDecimal beforeSendValue;    // 操作前余额赠送金额

        private BigDecimal beforePrepareValue;

        private BigDecimal currentSendValue; // 本次操作赠送金额

        private BigDecimal currentRealValue; // 本次操作实储金额

        private BigDecimal currentPrepareValue;

        private BigDecimal endRealValue; // 操作后实储金额

        private BigDecimal endSendValue; // 操作后赠送金额

        private BigDecimal endPrepareValue;

        private Integer accountStatus; //1已到账、0未到账。
        private Integer tradeStatus;
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
            history.setType(0);//储值
            history.setCreateDateTime(getStoreDate());
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
