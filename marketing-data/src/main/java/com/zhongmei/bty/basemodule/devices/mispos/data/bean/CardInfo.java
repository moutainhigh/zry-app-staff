package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import java.io.Serializable;


public class CardInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String cardStatus;
    private String cardNum;

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
