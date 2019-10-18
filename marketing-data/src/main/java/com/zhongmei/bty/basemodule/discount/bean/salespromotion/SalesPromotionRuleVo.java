package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathSalesPromotionTool;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SalesPromotionRuleVo implements Serializable {
        private SalesPromotionRule rule;
        private List<SalesPromotionDish> activityDishs;
        private List<SalesPromotionPolicyDish> policyDishs;

    public SalesPromotionRule getRule() {
        return rule;
    }

    public void setRule(SalesPromotionRule rule) {
        this.rule = rule;
    }

    public List<SalesPromotionDish> getActivityDishs() {
        return activityDishs;
    }

    public void setActivityDishs(List<SalesPromotionDish> activityDishs) {
        this.activityDishs = activityDishs;
    }

    public List<SalesPromotionPolicyDish> getPolicyDishs() {
        return policyDishs;
    }

    public void setPolicyDishs(List<SalesPromotionPolicyDish> policyDishs) {
        this.policyDishs = policyDishs;
    }


    public boolean isCurrentEnable(CustomerResp customerNew) {
        return checkApplyCrowd(customerNew) && checkWeekday() && checkLimitPeriod();
    }


    public boolean checkApplyCrowd(CustomerResp customerNew) {
        SalesPromotionRule salesPromotionRule = getRule();

                if (salesPromotionRule == null) {
            return false;
        }

        int applyCrowd = salesPromotionRule.getApplyCrowd();
        if (SalesPromotionConstant.ApplyCrowd.MEMBER == applyCrowd) {
            return customerNew != null && customerNew.levelId != null;
        } else if (SalesPromotionConstant.ApplyCrowd.NONMEMBER == applyCrowd) {
            return customerNew != null && customerNew.levelId == null;
        }

        return true;
    }


    public boolean checkActivityDish(@NonNull List<IShopcartItem> selectedShopcartItems) {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule != null) {
            int marketSubjectType = salesPromotionRule.getMarketSubjectType();
            if (SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE == marketSubjectType
                    || (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType && getActivityDishs() == null)) {
                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType && BigDecimal.ZERO.compareTo(logicValue) < 0) {
                    int policySubjectType = salesPromotionRule.getPolicySubjectType();
                                        if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {                                                  Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                                                for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                            SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                            if (salesPromotionDishQuantity.singleQty.compareTo(logicValue) >= 0) {
                                return true;
                            }
                        }
                    } else {
                        BigDecimal quantity = BigDecimal.ZERO;
                        for (IShopcartItem item : selectedShopcartItems) {
                            quantity = quantity.add(item.getSingleQty());
                        }
                        return quantity.compareTo(logicValue) >= 0;
                    }
                } else if (SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE == ruleSubjectType) {
                    BigDecimal amount = BigDecimal.ZERO;
                    for (IShopcartItem item : selectedShopcartItems) {
                        amount = amount.add(item.getAmount());
                    }
                    return amount.compareTo(logicValue) >= 0;
                } else {
                                        return false;
                }
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE == marketSubjectType) {
                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType && BigDecimal.ZERO.compareTo(logicValue) < 0) {
                    int policySubjectType = salesPromotionRule.getPolicySubjectType();
                                        if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {                          Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                                                Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                                                for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                            SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                            if (salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) || salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                                                                if (salesPromotionDishQuantity.singleQty.compareTo(logicValue) >= 0) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        BigDecimal quantity = BigDecimal.ZERO;
                        Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                        if (activityDishs != null) {
                            Iterator<IShopcartItem> iterator = selectedShopcartItems.iterator();
                            while (iterator.hasNext()) {
                                IShopcartItem item = iterator.next();
                                DishShop dishShop = item.getDishShop();
                                if (dishShop != null
                                        && (dishShop.getDishTypeId() != null && salesPromotionDishMap.containsKey(dishShop.getDishTypeId()) || dishShop.getBrandDishId() != null && salesPromotionDishMap.containsKey(dishShop.getBrandDishId()))) {
                                    quantity = quantity.add(item.getSingleQty());
                                } else {
                                    iterator.remove();
                                }
                            }
                        } else {
                            selectedShopcartItems.clear();
                        }

                        return quantity.compareTo(logicValue) >= 0;
                    }

                } else if (SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE == ruleSubjectType) {
                    BigDecimal amount = BigDecimal.ZERO;
                    Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                    if (activityDishs != null) {
                        Iterator<IShopcartItem> iterator = selectedShopcartItems.iterator();
                        while (iterator.hasNext()) {
                            IShopcartItem item = iterator.next();
                            DishShop dishShop = item.getDishShop();
                            if (dishShop != null
                                    && (dishShop.getDishTypeId() != null && salesPromotionDishMap.containsKey(dishShop.getDishTypeId()) || dishShop.getBrandDishId() != null && salesPromotionDishMap.containsKey(dishShop.getBrandDishId()))) {
                                amount = amount.add(item.getAmount());
                            } else {
                                iterator.remove();
                            }
                        }
                    } else {
                        selectedShopcartItems.clear();
                    }

                    return amount.compareTo(logicValue) >= 0;
                } else {
                                        return false;
                }
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType) {
                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType && BigDecimal.ZERO.compareTo(logicValue) < 0) {
                    int policySubjectType = salesPromotionRule.getPolicySubjectType();
                                        if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {                          Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                                                Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                                                for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                            SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                            if (!salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) && !salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                                                                if (salesPromotionDishQuantity.singleQty.compareTo(logicValue) >= 0) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        BigDecimal quantity = BigDecimal.ZERO;
                        Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                        if (activityDishs != null) {
                            Iterator<IShopcartItem> iterator = selectedShopcartItems.iterator();
                            while (iterator.hasNext()) {
                                IShopcartItem item = iterator.next();
                                DishShop dishShop = item.getDishShop();
                                if (dishShop != null
                                        && (dishShop.getDishTypeId() != null && !salesPromotionDishMap.containsKey(dishShop.getDishTypeId()) && (dishShop.getBrandDishId() != null && !salesPromotionDishMap.containsKey(dishShop.getBrandDishId())))) {
                                    quantity = quantity.add(item.getSingleQty());
                                } else {
                                    iterator.remove();
                                }
                            }
                        }
                        return quantity.compareTo(logicValue) >= 0;
                    }
                } else if (SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE == ruleSubjectType) {
                    BigDecimal amount = BigDecimal.ZERO;
                    Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                    if (salesPromotionDishMap != null) {
                        Iterator<IShopcartItem> iterator = selectedShopcartItems.iterator();
                        while (iterator.hasNext()) {
                            IShopcartItem item = iterator.next();
                            DishShop dishShop = item.getDishShop();
                            if (dishShop != null
                                    && (dishShop.getDishTypeId() != null && !salesPromotionDishMap.containsKey(dishShop.getDishTypeId()) && (dishShop.getBrandDishId() != null && !salesPromotionDishMap.containsKey(dishShop.getBrandDishId())))) {
                                amount = amount.add(item.getAmount());
                            } else {
                                iterator.remove();
                            }
                        }
                    }
                    return amount.compareTo(logicValue) >= 0;
                }
            }
        }


        return false;
    }

    private Map<Long, SalesPromotionDish> convertSalesPromotionDishToMap(List<SalesPromotionDish> activityDishs) {
        Map<Long, SalesPromotionDish> map = new HashMap<>();
        if (Utils.isNotEmpty(activityDishs)) {
            for (SalesPromotionDish item : activityDishs) {
                map.put(item.getRelateId(), item);
            }
        }

        return map;
    }


    public boolean checkLimitPeriod() {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule != null) {
            SalesPromotionRuleLimitPeriod limitPeriod = salesPromotionRule.getLimitPeriod();
                        if (limitPeriod == null) {
                return true;
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                long startTime = getTime(limitPeriod.getStartPeriod(), currentTimeMillis);
                long endTime = getTime(limitPeriod.getEndPeriod(), currentTimeMillis);
                                return currentTimeMillis >= startTime && currentTimeMillis <= endTime;
            }
        }

        return false;
    }


    public long getTime(String time, long defaultTimeMillis) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String s = dfs.format(defaultTimeMillis);
        s += " " + time;
        SimpleDateFormat dfs2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = dfs2.parse(s);
        } catch (ParseException e) {
            return defaultTimeMillis;
        }
        return date.getTime();
    }


    public boolean checkWeekday() {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule != null) {
            SparseArray<SalesPromotionWeekday> weekdaySparseArray = salesPromotionRule.getWeekDay();
            if (weekdaySparseArray != null) {
                SalesPromotionWeekday weekday = weekdaySparseArray.get(getCurrentDayNumber());
                return weekday != null && weekday.isEnable();
            }
        }

        return false;
    }

        private int getCurrentDayNumber() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (cal.getFirstDayOfWeek() == Calendar.MONDAY) {
            dayOfWeek = dayOfWeek + 1;
            if (dayOfWeek == 8) {
                dayOfWeek = 1;
            }
        }

        return dayOfWeek;
    }

    public boolean isContainDish(DishShop dishShop) {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule == null) {
            return false;
        }

        int marketSubjectType = salesPromotionRule.getMarketSubjectType();
        switch (marketSubjectType) {
            case SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE:
                return true;
            case SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE:
                Map<Long, SalesPromotionDish> partUseActivityDishesMap = convertSalesPromotionDishToMap(getActivityDishs());
                return partUseActivityDishesMap.containsKey(dishShop.getBrandDishId()) || partUseActivityDishesMap.containsKey(dishShop.getDishTypeId());
            case SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE:
                Map<Long, SalesPromotionDish> partUnuseActivityDishesMap = convertSalesPromotionDishToMap(getActivityDishs());
                return !partUnuseActivityDishesMap.containsKey(dishShop.getBrandDishId()) && !partUnuseActivityDishesMap.containsKey(dishShop.getDishTypeId());
        }

        return false;
    }

    public boolean isAllDish(DishShop dishShop) {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule == null) {
            return false;
        }

        int marketSubjectType = salesPromotionRule.getMarketSubjectType();
        switch (marketSubjectType) {
            case SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE:
                return true;
            case SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE:
                Map<Long, SalesPromotionDish> partUnuseActivityDishesMap = convertSalesPromotionDishToMap(getActivityDishs());
                return !partUnuseActivityDishesMap.containsKey(dishShop.getBrandDishId()) && !partUnuseActivityDishesMap.containsKey(dishShop.getDishTypeId());
        }

        return false;
    }

    public boolean isPartDish(DishShop dishShop) {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule == null) {
            return false;
        }

        int marketSubjectType = salesPromotionRule.getMarketSubjectType();
        if (marketSubjectType == SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE) {
            Map<Long, SalesPromotionDish> partUseActivityDishesMap = convertSalesPromotionDishToMap(getActivityDishs());
            return partUseActivityDishesMap.containsKey(dishShop.getBrandDishId()) || partUseActivityDishesMap.containsKey(dishShop.getDishTypeId());
        }

        return false;
    }
}
