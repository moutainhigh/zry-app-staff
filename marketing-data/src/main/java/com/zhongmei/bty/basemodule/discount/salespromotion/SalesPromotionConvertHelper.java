package com.zhongmei.bty.basemodule.discount.salespromotion;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionPolicyDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRule;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleLimitPeriod;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionValidityPeriod;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionWeekday;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.SalesPromotionCache;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleActivityDish;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleMarketRule;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePlan;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePolicyDish;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SalesPromotionConvertHelper {
    private static final int WEEK_DAY_LENGTH = 7;
    private static SalesPromotionConvertHelper mInstance = new SalesPromotionConvertHelper();

    private List<SalesPromotionRuleVo> mSalesPromotionRuleVos = new ArrayList<>();

    private SalesPromotionConvertHelper() {
    }

    public static SalesPromotionConvertHelper getInstance() {
        return mInstance;
    }

    public void clearData() {
        mSalesPromotionRuleVos.clear();
    }

    public void buildSalesPromotionRuleVo() {
        mSalesPromotionRuleVos.clear();
        List<LoytMruleActivityDish> loytMruleActivityDishes = new ArrayList<>(SalesPromotionCache.getRuleActivityDishHolder().getAll());
        List<LoytMruleMarketRule> loytMruleMarketRules = new ArrayList<>(SalesPromotionCache.getRuleMarketRuleHolder().getAll());
        List<LoytMrulePlan> loytMrulePlans = new ArrayList<>(SalesPromotionCache.getRulePlanHolder().getAll());
        List<LoytMrulePolicyDish> loytMrulePolicyDishes = new ArrayList<>(SalesPromotionCache.getRulePolicyDishHolder().getAll());
        List<SalesPromotionRuleVo> salesPromotionRuleVoList = convertToSalesPromotionRuleVo(loytMruleActivityDishes, loytMruleMarketRules, loytMrulePlans, loytMrulePolicyDishes);
        mSalesPromotionRuleVos.addAll(salesPromotionRuleVoList);
    }


    public List<SalesPromotionRuleVo> getSalesPromotionRuleVos() {
        if (mSalesPromotionRuleVos.size() == 0) {
            buildSalesPromotionRuleVo();
        }
        return mSalesPromotionRuleVos;
    }


    private static List<SalesPromotionRuleVo> convertToSalesPromotionRuleVo(List<LoytMruleActivityDish> loytMruleActivityDishes, List<LoytMruleMarketRule> loytMruleMarketRules, List<LoytMrulePlan> loytMrulePlans, List<LoytMrulePolicyDish> loytMrulePolicyDishes) {
        List<SalesPromotionRuleVo> salesPromotionRuleVos = new ArrayList<>();

        Map<Long, LoytMrulePlan> loytMrulePlanMap = new HashMap<>();
        Map<Long, List<LoytMruleActivityDish>> loytMruleActivityDishesMap = new HashMap<>();
        Map<Long, List<LoytMrulePolicyDish>> loytMrulePolicyDishesMap = new HashMap<>();

        storeLoytMrulePlanMap(loytMrulePlans, loytMrulePlanMap);
        storeLoytMruleActivityDishesMap(loytMruleActivityDishes, loytMruleActivityDishesMap);
        storeLoytMrulePolicyDishesMap(loytMrulePolicyDishes, loytMrulePolicyDishesMap);

        if (loytMruleMarketRules != null) {
            SalesPromotionRuleVo salesPromotionRuleVo;
            for (LoytMruleMarketRule loytMruleMarketRule : loytMruleMarketRules) {
                salesPromotionRuleVo = new SalesPromotionRuleVo();
                LoytMrulePlan loytMrulePlan = loytMrulePlanMap.get(loytMruleMarketRule.getPlanId());
                if (loytMrulePlan != null && inCurrentDate(loytMrulePlan.getStartTime(), loytMrulePlan.getEndTime())) {
                    SalesPromotionRule salesPromotionRule = convertLoytMruleMarketRuleToSalesPromotionRule(loytMruleMarketRule, loytMrulePlan);
                    salesPromotionRuleVo.setRule(salesPromotionRule);
                    salesPromotionRuleVo.setActivityDishs(convertLoytMruleActivityDishToSalesPromotionDish(loytMruleActivityDishesMap.get(loytMruleMarketRule.getPlanId())));
                    salesPromotionRuleVo.setPolicyDishs(convertLoytMrulePolicyDishToSalesPromotionPolicyDish(loytMrulePolicyDishesMap.get(loytMruleMarketRule.getPlanId())));
                    salesPromotionRuleVos.add(salesPromotionRuleVo);
                }
            }
        }
        return salesPromotionRuleVos;
    }


    private static void storeLoytMrulePolicyDishesMap(List<LoytMrulePolicyDish> loytMrulePolicyDishes, Map<Long, List<LoytMrulePolicyDish>> loytMrulePolicyDishesMap) {
        for (LoytMrulePolicyDish loytMrulePolicyDish : loytMrulePolicyDishes) {
            Long planId = loytMrulePolicyDish.getPlanId();
            List<LoytMrulePolicyDish> loytMrulePolicyDishPlans = loytMrulePolicyDishesMap.get(planId);
            if (loytMrulePolicyDishPlans == null) {
                loytMrulePolicyDishPlans = new ArrayList<>();
                loytMrulePolicyDishPlans.add(loytMrulePolicyDish);
                loytMrulePolicyDishesMap.put(planId, loytMrulePolicyDishPlans);
            } else {
                loytMrulePolicyDishPlans.add(loytMrulePolicyDish);
            }
        }

    }


    private static void storeLoytMruleActivityDishesMap(List<LoytMruleActivityDish> loytMruleActivityDishes, Map<Long, List<LoytMruleActivityDish>> loytMruleActivityDishesMap) {
        for (LoytMruleActivityDish loytMruleActivityDish : loytMruleActivityDishes) {
            Long planId = loytMruleActivityDish.getPlanId();
            List<LoytMruleActivityDish> loytMruleActivityDishesPlans = loytMruleActivityDishesMap.get(planId);
            if (loytMruleActivityDishesPlans == null) {
                loytMruleActivityDishesPlans = new ArrayList<>();
                loytMruleActivityDishesPlans.add(loytMruleActivityDish);
                loytMruleActivityDishesMap.put(planId, loytMruleActivityDishesPlans);
            } else {
                loytMruleActivityDishesPlans.add(loytMruleActivityDish);
            }
        }
    }


    private static void storeLoytMrulePlanMap(List<LoytMrulePlan> loytMrulePlans, Map<Long, LoytMrulePlan> loytMrulePlanMap) {
        for (LoytMrulePlan loytMrulePlan : loytMrulePlans) {
            loytMrulePlanMap.put(loytMrulePlan.getId(), loytMrulePlan);
        }
    }


    private static List<SalesPromotionDish> convertLoytMruleActivityDishToSalesPromotionDish(List<LoytMruleActivityDish> loytMruleActivityDishes) {
        List<SalesPromotionDish> salesPromotionDishes = new ArrayList<>();
        SalesPromotionDish salesPromotionDish;
        if (loytMruleActivityDishes != null) {
            DishManager dishManager = new DishManager();
            for (LoytMruleActivityDish loytMruleActivityDish : loytMruleActivityDishes) {
                if (loytMruleActivityDish.getDishType() == 1) {                    DishVo dishVo = dishManager.getDishVoByBrandDishId(loytMruleActivityDish.getDishId());
                    if (dishVo != null) {
                        salesPromotionDish = new SalesPromotionDish();
                        salesPromotionDish.setRelateId(loytMruleActivityDish.getDishId());
                        salesPromotionDish.setType(loytMruleActivityDish.getDishType());
                        salesPromotionDishes.add(salesPromotionDish);
                    }
                } else {
                    DishBrandType type = new DishBrandType();
                    type.setId(loytMruleActivityDish.getMidDishTypeId());
                    DishInfo dishInfo = dishManager.switchType(type);
                    if (dishInfo != null && Utils.isNotEmpty(dishInfo.dishList)) {
                        salesPromotionDish = new SalesPromotionPolicyDish();
                        salesPromotionDish.setRelateId(loytMruleActivityDish.getMidDishTypeId());
                        salesPromotionDish.setType(loytMruleActivityDish.getDishType());
                        salesPromotionDishes.add(salesPromotionDish);
                    }
                }

            }
        }

        return salesPromotionDishes;
    }


    private static List<SalesPromotionPolicyDish> convertLoytMrulePolicyDishToSalesPromotionPolicyDish(List<LoytMrulePolicyDish> loytMruleActivityDishes) {
        List<SalesPromotionPolicyDish> salesPromotionDishes = new ArrayList<>();
        SalesPromotionPolicyDish salesPromotionDish;
        DishManager dishManager = new DishManager();
        if (loytMruleActivityDishes != null) {
            for (LoytMrulePolicyDish loytMrulePolicyDish : loytMruleActivityDishes) {
                if (loytMrulePolicyDish.getDishType() == 1) {                    DishVo dishVo = dishManager.getDishVoByBrandDishId(loytMrulePolicyDish.getDishId());
                    if (dishVo != null) {
                        salesPromotionDish = new SalesPromotionPolicyDish();
                        salesPromotionDish.setRelateId(loytMrulePolicyDish.getDishId());
                        salesPromotionDish.setType(loytMrulePolicyDish.getDishType());
                        salesPromotionDishes.add(salesPromotionDish);
                    }
                } else {
                    DishBrandType type = new DishBrandType();
                    type.setId(loytMrulePolicyDish.getMidDishTypeId());
                    DishInfo dishInfo = dishManager.switchType(type);
                    if (dishInfo != null && Utils.isNotEmpty(dishInfo.dishList)) {
                        salesPromotionDish = new SalesPromotionPolicyDish();
                        salesPromotionDish.setRelateId(loytMrulePolicyDish.getMidDishTypeId());
                        salesPromotionDish.setType(loytMrulePolicyDish.getDishType());
                        salesPromotionDishes.add(salesPromotionDish);
                    }
                }
            }
        }

        return salesPromotionDishes;
    }


    private static SalesPromotionRule convertLoytMruleMarketRuleToSalesPromotionRule(LoytMruleMarketRule loytMruleMarketRule, LoytMrulePlan loytMrulePlan) {
        if (loytMruleMarketRule != null) {
            SalesPromotionRule salesPromotionRule = new SalesPromotionRule();
            salesPromotionRule.setId(loytMruleMarketRule.getId());

            if (1 == loytMrulePlan.getLimitPeriod()) {
                SalesPromotionRuleLimitPeriod salesPromotionRuleLimitPeriod = new SalesPromotionRuleLimitPeriod();
                salesPromotionRuleLimitPeriod.setStartPeriod(loytMrulePlan.getStartPeriod());
                salesPromotionRuleLimitPeriod.setEndPeriod(loytMrulePlan.getEndPeriod());
                salesPromotionRule.setLimitPeriod(salesPromotionRuleLimitPeriod);
            }

            salesPromotionRule.setWeekDay(createSalesPromotionWeekdaySparseArray(loytMrulePlan.getWeekDay()));
            salesPromotionRule.setStackRule(1 == loytMrulePlan.getStackRule());
            salesPromotionRule.setApplyCrowd(loytMrulePlan.getApplyCrowd());

            salesPromotionRule.setMarketSubjectType(loytMruleMarketRule.getMarketSubjectId());

            salesPromotionRule.setRuleSubjectType(loytMruleMarketRule.getRuleSubjectId());

            salesPromotionRule.setRuleLogic(loytMruleMarketRule.getRuleLogic());

            salesPromotionRule.setLogicValue(loytMruleMarketRule.getLogicValue());
            salesPromotionRule.setPolicySubjectType(loytMruleMarketRule.getPolicySubjectId());
            salesPromotionRule.setPolicyDetailType(loytMruleMarketRule.getPolicyDetailId());

            if (!TextUtils.isEmpty(loytMruleMarketRule.getPolicyValue1())) {
                salesPromotionRule.setPolicyValue1(new BigDecimal(loytMruleMarketRule.getPolicyValue1()));
            }

            salesPromotionRule.setPolicyValue2(loytMruleMarketRule.getPolicyValue2());
            salesPromotionRule.setServerCreateTime(loytMruleMarketRule.getServerCreateTime());

                        salesPromotionRule.setPlanName(loytMrulePlan.getName());
            salesPromotionRule.setPlanId(loytMrulePlan.getId());
            salesPromotionRule.setCouponShare(1 == loytMrulePlan.getCouponShared());
                        SalesPromotionValidityPeriod salesPromotionValidityPeriod = new SalesPromotionValidityPeriod();
            salesPromotionValidityPeriod.setStartDate(loytMrulePlan.getStartTime());
            salesPromotionValidityPeriod.setEndDate(loytMrulePlan.getEndTime());
            salesPromotionRule.setValidityPeriod(salesPromotionValidityPeriod);

            return salesPromotionRule;
        }

        return null;
    }

    private static SparseArray<SalesPromotionWeekday> createSalesPromotionWeekdaySparseArray(String weekDay) {
        SparseArray<SalesPromotionWeekday> weekdaySparseArray = createDefaultSalesPromotionWeekdaySparseArray();
        if (weekdaySparseArray != null
                && !TextUtils.isEmpty(weekDay) && weekDay.length() == WEEK_DAY_LENGTH) {
            for (int i = 0; i < WEEK_DAY_LENGTH; i++) {
                int key = i + 1;
                SalesPromotionWeekday salesPromotionWeekday = weekdaySparseArray.get(key);
                if (salesPromotionWeekday != null) {
                    int value = weekDay.charAt(i) - (int) ('0');                    boolean enable = (value == 1);                    salesPromotionWeekday.setEnable(enable);
                }
            }
        }
        return weekdaySparseArray;
    }

    private static SparseArray<SalesPromotionWeekday> createDefaultSalesPromotionWeekdaySparseArray() {
        SparseArray<SalesPromotionWeekday> weekday = new SparseArray<SalesPromotionWeekday>();
        weekday.put(1, createSalesPromotionWeekday(1, true));        weekday.put(2, createSalesPromotionWeekday(2, true));        weekday.put(3, createSalesPromotionWeekday(3, true));        weekday.put(4, createSalesPromotionWeekday(4, true));        weekday.put(5, createSalesPromotionWeekday(5, true));        weekday.put(6, createSalesPromotionWeekday(6, true));        weekday.put(7, createSalesPromotionWeekday(7, true));        return weekday;
    }

    private static SalesPromotionWeekday createSalesPromotionWeekday(int number, boolean enable) {
        SalesPromotionWeekday salesPromotionWeekday = new SalesPromotionWeekday();
        salesPromotionWeekday.setNumber(number);
        salesPromotionWeekday.setName(getWeekDayName(number));
        salesPromotionWeekday.setEnable(enable);
        return salesPromotionWeekday;
    }

    private static String getWeekDayName(int number) {
        switch (number) {
            case 1:
                return BaseApplication.sInstance.getString(R.string.week_day_sunday);
            case 2:
                return BaseApplication.sInstance.getString(R.string.week_day_monday);
            case 3:
                return BaseApplication.sInstance.getString(R.string.week_day_tuesday);
            case 4:
                return BaseApplication.sInstance.getString(R.string.week_day_wednesday);
            case 5:
                return BaseApplication.sInstance.getString(R.string.week_day_thursday);
            case 6:
                return BaseApplication.sInstance.getString(R.string.week_day_friday);
            case 7:
                return BaseApplication.sInstance.getString(R.string.week_day_saturday);
            default:
                return "";
        }
    }

    private static boolean inCurrentDate(String startDate, String endData) {

        try {
            startDate = startDate + " 00:00:00";
            endData = endData + " 23:59:59";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endData);
            long current = System.currentTimeMillis();

            return start.getTime() <= current && current <= end.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
