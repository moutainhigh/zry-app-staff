package com.zhongmei.bty.basemodule.customer.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.Trade;


public class CustomerMemberStoreValueResp {
    private List<ValuecardHistoryAndMobile> valuecardHistorys;

    public List<ValuecardHistoryAndMobile> getValuecardHistorys() {
        return valuecardHistorys;
    }

    public void setValuecardHistorys(List<ValuecardHistoryAndMobile> valuecardHistorys) {
        this.valuecardHistorys = valuecardHistorys;
    }

    public class ValuecardHistory {
        private long id;
        private long createDateTime;
        private BigDecimal cashValuecard;
        private BigDecimal bankValuecard;
        private BigDecimal beforeValuecard;
        private BigDecimal addValuecard;
        private BigDecimal sendValuecard;
        private BigDecimal endValuecard;
        private String userId;
        private String deviceIdenty;

        private String paymentUuid;
        private long commercialMemberId;
        private String uuid;

        private String serverCreateTime;
        private String serverUpdateTime;

        private BigDecimal beforeRealValue;         private BigDecimal beforeSendValue;            private BigDecimal currentRealValue;         private BigDecimal currentSendValue;         private BigDecimal endRealValue;         private BigDecimal endSendValue;
        public BigDecimal getCashValuecard() {
            return cashValuecard;
        }

        public void setCashValuecard(BigDecimal cashValuecard) {
            this.cashValuecard = cashValuecard;
        }

        public BigDecimal getBankValuecard() {
            return bankValuecard;
        }

        public void setBankValuecard(BigDecimal bankValuecard) {
            this.bankValuecard = bankValuecard;
        }

        public long getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(long createDateTime) {
            this.createDateTime = createDateTime;
        }

        public BigDecimal getBeforeValuecard() {
            return beforeValuecard;
        }

        public void setBeforeValuecard(BigDecimal beforeValuecard) {
            this.beforeValuecard = beforeValuecard;
        }

        public BigDecimal getAddValuecard() {
            return addValuecard;
        }

        public void setAddValuecard(BigDecimal addValuecard) {
            this.addValuecard = addValuecard;
        }

        public BigDecimal getSendValuecard() {
            return sendValuecard;
        }

        public void setSendValuecard(BigDecimal sendValuecard) {
            this.sendValuecard = sendValuecard;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDeviceIdenty() {
            return deviceIdenty;
        }

        public void setDeviceIdenty(String deviceIdenty) {
            this.deviceIdenty = deviceIdenty;
        }

        public String getPaymentUuid() {
            return paymentUuid;
        }

        public void setPaymentUuid(String paymentUuid) {
            this.paymentUuid = paymentUuid;
        }

        public long getCommercialMemberId() {
            return commercialMemberId;
        }

        public void setCommercialMemberId(long commercialMemberId) {
            this.commercialMemberId = commercialMemberId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getServerCreateTime() {
            return serverCreateTime;
        }

        public void setServerCreateTime(String serverCreateTime) {
            this.serverCreateTime = serverCreateTime;
        }

        public String getServerUpdateTime() {
            return serverUpdateTime;
        }

        public void setServerUpdateTime(String serverUpdateTime) {
            this.serverUpdateTime = serverUpdateTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public BigDecimal getEndValuecard() {
            return endValuecard;
        }

        public void setEndValuecard(BigDecimal endValuecard) {
            this.endValuecard = endValuecard;
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

    public class ValuecardHistoryAndMobile {
        private String mobile;
        private Integer padNo;
        private String userName;
        private Integer accountStatus;         private ValuecardHistory valuecardHistory;
        private Trade trade;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public ValuecardHistory getValuecardHistory() {
            return valuecardHistory;
        }

        public void setValuecardHistory(ValuecardHistory valuecardHistory) {
            this.valuecardHistory = valuecardHistory;
        }

        public Trade getTrade() {
            return trade;
        }

        public void setTrade(Trade trade) {
            this.trade = trade;
        }

        public Integer getPadNo() {
            return padNo;
        }

        public void setPadNo(Integer padNo) {
            this.padNo = padNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public AccountStatus getAccountStatus() {
            return ValueEnums.toEnum(AccountStatus.class, accountStatus);
        }
    }


}
