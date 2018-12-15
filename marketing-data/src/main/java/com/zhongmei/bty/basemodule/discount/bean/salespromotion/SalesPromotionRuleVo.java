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
    //促销活动规则
    private SalesPromotionRule rule;
    //促销活动活动商品
    private List<SalesPromotionDish> activityDishs;
    //促销活动策略商品
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

    /**
     * 判断促销活动当前是否可用
     *
     * @return true为可用；false为不可用
     */
    public boolean isCurrentEnable(CustomerResp customerNew) {
        return checkApplyCrowd(customerNew) && checkWeekday() && checkLimitPeriod();
    }

    /**
     * 判断当前促销活动是否适用指定人群
     *
     * @param customerNew 当前登录会员
     * @return true为适用，false为不适用
     */
    public boolean checkApplyCrowd(CustomerResp customerNew) {
        SalesPromotionRule salesPromotionRule = getRule();

        //判断促销规则是否为null
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

    /**
     * 判断选择的商品条目是否符合当前促销活动
     *
     * @param selectedShopcartItems 选择的商品条目
     * @return true为包含，false为不包含
     */
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
                    //组合优惠
                    if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {  //单品优惠
                        //统计每个菜品的数量
                        Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                        //只要发现一个商品满足，即可返回
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
                    //暂不支持，故返回为false
                    return false;
                }
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE == marketSubjectType) {
                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType && BigDecimal.ZERO.compareTo(logicValue) < 0) {
                    int policySubjectType = salesPromotionRule.getPolicySubjectType();
                    //组合优惠
                    if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {  //单品优惠
                        Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                        //统计每个菜品的数量
                        Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                        //只要发现一个商品满足，即可返回
                        for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                            SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                            if (salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) || salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                                //只要判断有一个商品满足活动，即可返回true
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
                    //暂不支持，故返回为false
                    return false;
                }
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType) {
                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType && BigDecimal.ZERO.compareTo(logicValue) < 0) {
                    int policySubjectType = salesPromotionRule.getPolicySubjectType();
                    //组合优惠
                    if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {  //单品优惠
                        Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(getActivityDishs());
                        //统计每个菜品的数量
                        Map<Long, SalesPromotionDishQuantity> temp = MathSalesPromotionTool.statisticsDispQuantity(selectedShopcartItems);
                        if (temp == null || temp.size() == 0) {
                            return false;
                        }
                        //只要发现一个商品满足，即可返回
                        for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                            SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                            if (!salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) && !salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                                //只要判断有一个商品满足活动，即可返回true
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

    /**
     * 判断当前促销活动是否在限制时段内
     *
     * @return true在，false不在
     */
    public boolean checkLimitPeriod() {
        SalesPromotionRule salesPromotionRule = getRule();
        if (salesPromotionRule != null) {
            SalesPromotionRuleLimitPeriod limitPeriod = salesPromotionRule.getLimitPeriod();
            //limitPeriod为null,则表示不限制时间段
            if (limitPeriod == null) {
                return true;
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                long startTime = getTime(limitPeriod.getStartPeriod(), currentTimeMillis);
                long endTime = getTime(limitPeriod.getEndPeriod(), currentTimeMillis);
                // 判断当前时间是否在有效期以内
                return currentTimeMillis >= startTime && currentTimeMillis <= endTime;
            }
        }

        return false;
    }

    /**
     * 格式：yyyy-MM-dd HH:mm
     */
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

    /**
     * 判断当前促销活动是否在星期设置内
     *
     * @return true在，false不在
     */
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

    //获取今天是星期几
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
