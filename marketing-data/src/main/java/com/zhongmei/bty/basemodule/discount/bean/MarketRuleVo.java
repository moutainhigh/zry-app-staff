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


public class MarketRuleVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private MarketActivityRule marketActivityRule;
    private MarketPlan marketPlan;
        private PromotionType promotionType;

        private ActivityType activityType;

        private Set<UserType> userTypes;

        private Set<BusinessType> businessTypes;

        private Set<DeliveryType> deliveryTypes;

        private MarketTempletVo templetVo;

        private Map<Long, MarketActivityDish> marketActivityDishMap;

        private List<Integer> activityPos;

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

        public boolean isContainsBusinessType(BusinessType type) {
        if (businessTypes != null) {
            return businessTypes.contains(type);
        } else {
            return false;
        }
    }

        public boolean isContainsDeliveryType(DeliveryType type) {
        if (deliveryTypes != null) {
            return deliveryTypes.contains(type);
        } else {
            return false;
        }
    }

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

        private boolean isContainsDish(DishShop dishShop) {
        if (dishShop == null) {
            return false;
        }

        if (marketActivityDishMap == null) {
            return true;
        }

                MarketActivityDish activityDish = marketActivityDishMap.get(dishShop.getBrandDishId());
        if (activityDish != null && activityDish.getType() != null && activityDish.getType() == MarketActivityDish.MARKET_ACTIVITY_DISH) {
            return true;
        }

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

        public boolean IsEnableCurrent() {
        MarketActivityRule rule = this.marketActivityRule;
        if (rule != null) {
            Long currTimeStap = System.currentTimeMillis();
                        if (rule.getLimitPeriod() == 2) {
                if (currTimeStap > DateTimeUtils.getTime(rule.getPeriodEnd())
                        || currTimeStap < DateTimeUtils.getTime(rule.getPeriodStart())) {
                    return false;
                }
            }
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


    public boolean isDishEnableUsed(DishShop dishShop) {
        if (dishShop == null) {
            return false;
        }

                if (isSpecialPriceDish(dishShop.getBrandDishId())) {
            return true;
        }

                if (isAllDish()) {            return true;
        }

        if (marketActivityRule == null || marketActivityRule.getAllDish() == null) {
            return false;
        }
        if (marketActivityRule.getAllDish() == 2) {              if (isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        } else if (marketActivityRule.getAllDish() == 3) {             if (!isPlanActivityDishOrType(dishShop)) {
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


    public boolean isPlanActivityDishOrType(DishShop dishShop) {
        return isContainsDish(dishShop);
    }

    public List<Integer> getActivityPos() {
        return activityPos;
    }
}
