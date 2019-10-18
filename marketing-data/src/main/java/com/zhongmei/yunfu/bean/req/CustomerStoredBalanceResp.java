package com.zhongmei.yunfu.bean.req;

import java.math.BigDecimal;

public class CustomerStoredBalanceResp {

    private int type;     private int source;    private String userId;    private String userName;
    private Long createDateTime;    private int paymentMode;     private String paymentModeName;     private BigDecimal addValuecard;    private BigDecimal sendValuecard;    private BigDecimal endValuecard;
    private String seq;    private String memo;    private int isChange;
    private String beforeValuecard;    private String cashValuecard;    private String bankValuecard;

    private BigDecimal beforeRealValue;     private BigDecimal beforeSendValue;        private BigDecimal currentSendValue;     private BigDecimal currentRealValue;     private BigDecimal endRealValue;     private BigDecimal endSendValue;     private String cardNum;
    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getBeforeValuecard() {
        return beforeValuecard;
    }

    public void setBeforeValuecard(String beforeValuecard) {
        this.beforeValuecard = beforeValuecard;
    }

    public String getCashValuecard() {
        return cashValuecard;
    }

    public void setCashValuecard(String cashValuecard) {
        this.cashValuecard = cashValuecard;
    }

    public String getBankValuecard() {
        return bankValuecard;
    }

    public void setBankValuecard(String bankValuecard) {
        this.bankValuecard = bankValuecard;
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

    public BigDecimal getEndValuecard() {
        return endValuecard;
    }

    public void setEndValuecard(BigDecimal endValuecard) {
        this.endValuecard = endValuecard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getIsChange() {
        return isChange;
    }

    public void setIsChange(int isChange) {
        this.isChange = isChange;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }


    public boolean filterDataByType() {
        if (type == 0 || type == 1 || type == 2 || type == 3) {
            return true;
        } else {
            return false;
        }
    }

}
