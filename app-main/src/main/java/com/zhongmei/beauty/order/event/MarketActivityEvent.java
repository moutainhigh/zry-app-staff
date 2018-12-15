package com.zhongmei.beauty.order.event;

import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;

/**
 * 营销活动事件
 * Created by demo on 2018/12/15
 */

public class MarketActivityEvent {
    public boolean isSelected;
    public MarketRuleVo marketRuleVo;

    public MarketActivityEvent(boolean isSelected, MarketRuleVo marketRuleVo) {
        this.isSelected = isSelected;
        this.marketRuleVo = marketRuleVo;
    }
}
