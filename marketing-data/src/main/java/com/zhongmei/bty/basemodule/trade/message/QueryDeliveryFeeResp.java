package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;



public class QueryDeliveryFeeResp {
        private BigDecimal fee;

        private String feeTip;

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
