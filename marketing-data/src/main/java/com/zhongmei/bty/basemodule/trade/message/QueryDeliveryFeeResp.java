package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;

/**
 * 查询配送费响应体
 */

public class QueryDeliveryFeeResp {
    //配送费
    private BigDecimal fee;

    //配送费说明
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
