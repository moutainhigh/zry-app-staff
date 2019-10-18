package com.zhongmei.bty.basemodule.trade.message;

import java.io.Serializable;
import java.math.BigDecimal;



public class BatchDeliveryFee implements Serializable {
    private Long tradeId;
        private BigDecimal fee;
        private String feeTip;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeTip() {
        return feeTip;
    }

    public void setFeeTip(String feeTip) {
        this.feeTip = feeTip;
    }
}
