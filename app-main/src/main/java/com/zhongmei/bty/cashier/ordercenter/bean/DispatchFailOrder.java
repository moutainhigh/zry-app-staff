package com.zhongmei.bty.cashier.ordercenter.bean;

import java.io.Serializable;

public class DispatchFailOrder implements Serializable {
    //订单号
    private String tradeNo;
    //失败原因
    private String reason;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
