package com.zhongmei.bty.basemodule.pay.bean;

import java.io.Serializable;



public class PaymentCard implements Serializable {
    private String cardNumber;
    private String cardName;
    private String expireDate;
    private String issNumber;
    private String issName;
    private Long creatorId;
    private String creatorName;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getIssNumber() {
        return issNumber;
    }

    public void setIssNumber(String issNumber) {
        this.issNumber = issNumber;
    }

    public String getIssName() {
        return issName;
    }

    public void setIssName(String issName) {
        this.issName = issName;
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


}
