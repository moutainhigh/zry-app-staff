package com.zhongmei.bty.basemodule.discount.entity;

import java.io.Serializable;



public class TradeItemActivityGift implements Serializable {

    private String tradeUuid;
    private String tradeItemUuid;
    private Long ruleId;
    private String relUuid;

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRelUuid() {
        return relUuid;
    }

    public void setRelUuid(String relUuid) {
        this.relUuid = relUuid;
    }
}
