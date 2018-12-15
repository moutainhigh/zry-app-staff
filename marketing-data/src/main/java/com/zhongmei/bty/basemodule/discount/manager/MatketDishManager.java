package com.zhongmei.bty.basemodule.discount.manager;

import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2016年4月26日 下午4:45:53
 * @Description: 菜品营销活动
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MatketDishManager {

    /**
     * @Title: getMarketDishVoByRule
     * @Description: 获得指定规则对应的菜品
     * @Param @param ruleId
     * @Param @return
     * @Return MarketDishVo 返回类型
     */
    public static MarketRuleVo getMarketDishVoByRule(Long ruleId) {
        // 过滤时间、时段
        return MarketRuleCache.getMarketDishVoByRule(ruleId);
    }

    /**
     * @param isDinner 是否是正餐，true：正餐 false:快餐
     * @Title: getMarketDishVoListByType
     * @Description: 根据活动类型查询所有的符合规则的活动规则列表
     * @Param @param type 为null时查询所有条件
     * @Param @return
     * @Return List<MarketDishVo> 返回类型
     */
    public static List<MarketRuleVo> getMarketDishVoListByType(ActivityType type, boolean isDinner) {
        // 符合查询条件的方案list
        if (isDinner) {
            return MarketRuleCache.getMarketDishVoListByType(type);
        } else {
            return MarketRuleCache.getMarketDishVoListByType(type);
        }
    }

    /**
     * @Title: filterShoppingCartRule
     * @Description: 根据购物车菜品过滤活动
     * @Param @param marketruleVoList
     * @Param @param items
     * @Return List<MarketRuleVo> 返回类型
     */
    public static List<MarketRuleVo> filterShoppingCartRule(List<MarketRuleVo> marketruleVoList,
                                                            List<IShopcartItem> items) {
        List<MarketRuleVo> ruleList = new ArrayList<MarketRuleVo>();
        if (marketruleVoList != null && !marketruleVoList.isEmpty()) {
            for (MarketRuleVo ruleVo : marketruleVoList) {
                // 如果特价6.9不支持特价菜
				/*if (ruleVo.getPromotionType() == PromotionType.SPECAILPRICE) {
					for (IShopcartItem item : items) {
						if (ruleVo.getTempletVo() != null && item.getDishShop() != null
							&& ruleVo.getTempletVo().isContainnsDish(item.getDishShop().getBrandDishId())) {
							ruleList.add(ruleVo);
							break;
						}
					}
				} else {// 其它优惠
					if (ruleVo.isAllDish()) {
						ruleList.add(ruleVo);
						continue;
					} else {
						for (IShopcartItem item : items) {
							if (item.getDishShop() != null
								&& ruleVo.isContainnsDish(item.getDishShop().getBrandDishId())) {
								ruleList.add(ruleVo);
								break;
							}
						}
					}
				}*/
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
