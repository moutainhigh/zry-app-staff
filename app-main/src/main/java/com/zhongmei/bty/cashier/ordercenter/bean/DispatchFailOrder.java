package com.zhongmei.bty.cashier.ordercenter.bean;

import java.io.Serializable;

public class DispatchFailOrder implements Serializable {
        private String tradeNo;
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
