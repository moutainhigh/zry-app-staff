package com.zhongmei.beauty.order.event;

import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;



public class MarketActivityEvent {
    public boolean isSelected;
    public MarketRuleVo marketRuleVo;

    public MarketActivityEvent(boolean isSelected, MarketRuleVo marketRuleVo) {
        this.isSelected = isSelected;
        this.marketRuleVo = marketRuleVo;
    }
}
