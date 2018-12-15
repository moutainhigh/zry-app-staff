package com.zhongmei.bty.basemodule.discount.entity;

import java.io.Serializable;

/**
 * Created by demo on 2018/12/15
 */

public class TradeItemActivityGift implements Serializable {

    private String tradeUuid; // 关联tradeUUID

    private String tradeItemUuid; // 关联tradeItemUuid

    private Long ruleId; // 营销活动ID

    private String relUuid; // 关联trade_plan_activity的uuid


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
