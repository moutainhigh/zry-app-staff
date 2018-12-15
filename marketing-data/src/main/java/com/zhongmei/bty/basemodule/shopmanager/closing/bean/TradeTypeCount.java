package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;

/**
 * 订单类型 统计
 * Created by demo on 2018/12/15
 */
public class TradeTypeCount implements Serializable {
    private Double tradeAmount = 0.0;
    private String typeName;
    private Integer tradeCount;

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }
}
