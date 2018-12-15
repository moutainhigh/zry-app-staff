package com.zhongmei.bty.basemodule.trade.message;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量查询配送费响应体
 */

public class BatchDeliveryFee implements Serializable {
    private Long tradeId;
    //配送费
    private BigDecimal fee;
    //配送费说明
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
