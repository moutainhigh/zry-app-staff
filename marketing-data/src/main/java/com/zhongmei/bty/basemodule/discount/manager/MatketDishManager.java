package com.zhongmei.bty.basemodule.discount.manager;

import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.util.ArrayList;
import java.util.List;


public class MatketDishManager {


    public static MarketRuleVo getMarketDishVoByRule(Long ruleId) {
                return MarketRuleCache.getMarketDishVoByRule(ruleId);
    }


    public static List<MarketRuleVo> getMarketDishVoListByType(ActivityType type, boolean isDinner) {
                if (isDinner) {
            return MarketRuleCache.getMarketDishVoListByType(type);
        } else {
            return MarketRuleCache.getMarketDishVoListByType(type);
        }
    }


    public static List<MarketRuleVo> filterShoppingCartRule(List<MarketRuleVo> marketruleVoList,
                                                            List<IShopcartItem> items) {
        List<MarketRuleVo> ruleList = new ArrayList<MarketRuleVo>();
        if (marketruleVoList != null && !marketruleVoList.isEmpty()) {
            for (MarketRuleVo ruleVo : marketruleVoList) {

                for (IShopcartItem item : items) {
                    if (item.getDishShop() != null && ruleVo.isDishEnableUsed(item.getDishShop())) {
                        ruleList.add(ruleVo);
                        break;
                    }
                }
            }
        }

        return ruleList;
    }
}
