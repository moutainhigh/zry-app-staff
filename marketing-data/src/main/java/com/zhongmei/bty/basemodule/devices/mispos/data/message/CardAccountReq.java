package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class CardAccountReq extends BaseRequest {

    private String cardNum;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
