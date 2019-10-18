package com.zhongmei.yunfu.bean.req;

import com.zhongmei.yunfu.bean.YFPageReq;


public class CustomerCardTimeStoreReq extends YFPageReq {

    private Integer tradeStatus;     private String cardNum;
    public CustomerCardTimeStoreReq(Integer tradeStatus, String cardNum, Integer page, Integer pageSize) {
        super(page, pageSize);
        this.tradeStatus = tradeStatus;
        this.cardNum = cardNum;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public String getCardNum() {
        return cardNum;
    }
}
