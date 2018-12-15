package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityWeekday;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Date：2016-5-20 下午1:49:40
 * @Description: 营销活动展示主业务信息
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MarketRuleVo implements java.io.Serializable {
    /**
     * @date：2016-5-20 下午1:40:27
     * @Description
     */
    private static final long serialVersionUID = 1L;

    private MarketActivityRule marketActivityRule;// 规则

    private MarketPlan marketPlan;// 活动主表

    // 优惠类型
    private PromotionType promotionType;

    // 营销方式：1单商品 2多商品
    private ActivityType activityType;

    // 用户类型
    private Set<UserType> userTypes;

    // 订单类型（正餐、快餐）
    private Set<BusinessType> businessTypes;

    // 单据类型
    private Set<DeliveryType> deliveryTypes;

    // 特价菜（可能为空）
    private MarketTempletVo templetVo;

    // 部分参与菜品（可能为空）
    private Map<Long, MarketActivityDish> marketActivityDishMap;

    // 参与终端
    private List<Integer> activityPos;

    // 星期几
    private List<Integer> weekdayList;

    public MarketRuleVo() {
    }

    public MarketRuleVo(MarketPlanVo planVo) {
        this.activityType = planVo.getActivityType();
        this.promotionType = planVo.getPromotionType();
        this.userTypes = planVo.getUserTypes();
        this.businessTypes = planVo.getBusinessTypes();
        this.deliveryTypes = planVo.getDeliveryTypes();
        this.marketPlan = planVo.getMarketPlan();
        this.activityPos = planVo.getActivityPos();
    }

    public MarketRuleVo(MarketPlanVo planVo, MarketActivityRule marketActivityRule) {
        this(planVo);
        this.marketActivityRule = marketActivityRule;
    }

    public MarketActivityRule getMarketActivityRule() {
        return marketActivityRule;
    }

    public void setMarketActivityRule(MarketActivityRule marketActivityRule) {
        this.marketActivityRule = marketActivityRule;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    // 参与人群
    public boolean isContainsUserType(UserType type) {
        if (userTypes != null) {
            return userTypes.contains(type);
        } else {
            return false;
        }
    }

    public Long getRuleId() {
        if (marketActivityRule != null) {
            return marketActivityRule.getId();
        } else {
            return -1L;
        }
    }

    public Set<UserType> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<UserType> userTypes) {
        this.userTypes = userTypes;
    }

    // 单据类型
    public boolean isContainsBusinessType(BusinessType type) {
        if (businessTypes != null) {
            return businessTypes.contains(type);
        } else {
            return false;
        }
    }

    // 就餐方式
    public boolean isContainsDeliveryType(DeliveryType type) {
        if (deliveryTypes != null) {
            return deliveryTypes.contains(type);
        } else {
            return false;
        }
    }

    // 是否所有商品参与
    public boolean isAllDish() {
        if (marketActivityRule != null && marketActivityRule.getAllDish() != null) {
            return marketActivityRule.getAllDish() == 1;
        }
        return false;
    }

    public List<Integer> getWeekdayList() {
        return weekdayList;
    }

    public boolean isContainsWeekDay(Integer weekDay) {
        if (weekdayList != null) {
            return weekdayList.contains(weekDay);
        } else {
            return false;
        }
    }

    public void setWeekdayList(List<MarketActivityWeekday> marketActivityWeekdayList) {
        if (marketActivityWeekdayList != null && !marketActivityWeekdayList.isEmpty()) {
            weekdayList = new ArrayList<Integer>();
            for (MarketActivityWeekday weekday : marketActivityWeekdayList) {
                weekdayList.add(weekday.getWeekday());
            }
        }
    }

    public MarketTempletVo getTempletVo() {
        return templetVo;
    }

    public void setTempletVo(MarketTempletVo templetVo) {
        this.templetVo = templetVo;
    }

    // 部分商品 判断是否存在某个菜品
    private boolean isContainsDish(DishShop dishShop) {
        if (dishShop == null) {
            return false;
        }

        if (marketActivityDishMap == null) {
            return true;
        }

        //判断菜品类型的营销活动
        MarketActivityDish activityDish = marketActivityDishMap.get(dishShop.getBrandDishId());
        if (activityDish != null && activityDish.getType() != null && activityDish.getType() == MarketActivityDish.MARKET_ACTIVITY_DISH) {
            return true;
        }

        //判断中类类型的营销活动
        MarketActivityDish activityDishType = marketActivityDishMap.get(dishShop.getDishTypeId());
        if (activityDishType != null && activityDishType.getType() != null && activityDishType.getType() == MarketActivityDish.MARKET_ACTIVITY_DISH_TYPE) {
            return true;
        }

        return false;
    }

    public List<MarketActivityDish> getMarketActivityDishList() {
        List<MarketActivityDish> dishlist = null;
        if (marketActivityDishMap != null && !marketActivityDishMap.isEmpty()) {
            dishlist = new ArrayList<MarketActivityDish>();
            dishlist.addAll(marketActivityDishMap.values());
        }
        return dishlist;
    }

    public void setMarketActivityDishList(List<MarketActivityDish> list) {
        if (list != null && !list.isEmpty()) {
            marketActivityDishMap = new HashMap<Long, MarketActivityDish>();
            for (MarketActivityDish dish : list) {
                marketActivityDishMap.put(dish.getDishId(), dish);
            }
        }
    }

    public MarketPlan getMarketPlan() {
        return marketPlan;
    }

    public void setMarketPlan(MarketPlan marketPlan) {
        this.marketPlan = marketPlan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marketActivityRule == null) ? 0 : marketActivityRule.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketRuleVo other = (MarketRuleVo) obj;
        if (marketActivityRule == null) {
            if (other.marketActivityRule != null)
                return false;
        } else if (!marketActivityRule.equals(other.marketActivityRule))
            return false;
        return true;
    }

    // 当前时间是否可用
    public boolean IsEnableCurrent() {
        MarketActivityRule rule = this.marketActivityRule;
        if (rule != null) {
            Long currTimeStap = System.currentTimeMillis();
            // 如果有时间段限制
            if (rule.getLimitPeriod() == 2) {
                if (currTimeStap > DateTimeUtils.getTime(rule.getPeriodEnd())
                        || currTimeStap < DateTimeUtils.getTime(rule.getPeriodStart())) {
                    return false;
                }
            }
            // 如果有星期限制(目前默认都限制)
            if (true) {
                int currentWeek = DateTimeUtils.getCurrentDayOfWeekName();
                if (!this.isContainsWeekDay(currentWeek)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据菜品判断该活动是否可用
     *
     * @param dishShop 菜品
     * @return
     */
    public boolean isDishEnableUsed(DishShop dishShop) {
        if (dishShop == null) {
            return false;
        }

        //是否是特价菜优惠
        if (isSpecialPriceDish(dishShop.getBrandDishId())) {
            return true;
        }

        //商品营销优惠
        if (isAllDish()) {//所有菜品/中类都参加
            return true;
        }

        if (marketActivityRule == null || marketActivityRule.getAllDish() == null) {
            return false;
        }
        if (marketActivityRule.getAllDish() == 2) {  //部分商品可用
            if (isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        } else if (marketActivityRule.getAllDish() == 3) { //部分商品不可用
            if (!isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpecialPriceDish(Long brandDishId) {
        if (this.getPromotionType() == PromotionType.SPECAILPRICE) {
            if (templetVo != null) {
                return templetVo.isContainsDish(brandDishId);
            }
        }

        return false;
    }

    /**
     * 商品是否可以参加当前营销活动
     *
     * @param dishShop
     * @return
     */
    public boolean isPlanActivityDishOrType(DishShop dishShop) {
        return isContainsDish(dishShop);
    }

    public List<Integer> getActivityPos() {
        return activityPos;
    }
}
