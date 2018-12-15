package com.zhongmei.bty.basemodule.devices.mispos.data.message;

public class CardLoginReq {

    private long source;

    private long userId;

    private String cardNum;

    public CardLoginReq(String cardNum) {
        this.cardNum = cardNum;
    }

    public long getSource() {
        return source;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

}
