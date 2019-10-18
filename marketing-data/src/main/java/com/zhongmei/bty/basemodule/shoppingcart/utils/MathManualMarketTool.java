package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.util.Log;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.manager.MatketDishManager;
import com.zhongmei.bty.basemodule.discount.operates.interfaces.MarketDal;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.db.enums.MemberPrivilegeType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MathManualMarketTool {

        private static Map<String, List<TradeItemPlanActivity>> itemPlanMap = null;

        private static Map<String, TradeItemPlanActivity> itemMap = null;

        private static Map<Long, TradePlanActivity> tradePlanMap = null;


    public static boolean mathManualAddMarket(List<IShopcartItem> selectedShopcartItems, TradeVo mTradeVo,
                                              MarketRuleVo marketDishVo, boolean isDinner
    ) {
        return mathMarket(null, selectedShopcartItems, mTradeVo, marketDishVo, isDinner, DinnerShopManager.getInstance().isSepartShopCart());
    }


    public static boolean isCanAddMarket(List<IShopcartItem> selectedShopcartItems, TradeVo mTradeVo,
                                         MarketRuleVo marketDishVo, boolean isDinner
    ) {
        ActivityType activityType = marketDishVo.getActivityType();
        if (marketDishVo == null || marketDishVo.getMarketActivityRule() == null || !marketDishVo.IsEnableCurrent()) {
            return false;
        }
        if (selectedShopcartItems == null) {
            return false;
        }
        PromotionType promotionType = marketDishVo.getPromotionType();
        MarketActivityRule activityRule = marketDishVo.getMarketActivityRule();
        List<MarketActivityDish> mDishList = marketDishVo.getMarketActivityDishList();

                if ((mDishList == null || mDishList.size() == 0) && (promotionType != PromotionType.SPECAILPRICE)
                && (activityRule.getAllDish() != 1)) {
            return false;
        }
                if (!validateMember(marketDishVo, isDinner, DinnerShopManager.getInstance().isSepartShopCart())) {
                        return false;
        }
                if (activityType == ActivityType.SINGLE) {
                        BigDecimal totalCount = BigDecimal.ZERO;
            for (IShopcartItem shopcartItem : selectedShopcartItems) {
                totalCount = totalCount.add(shopcartItem.getSingleQty());
            }

            if (activityRule.getDishNum() == null) {
                return false;
            }

            DishShop dishShop = selectedShopcartItems.get(0).getDishShop();
            if (selectedShopcartItems.size() > 0 && dishShop != null && !dishShopCanAddMarket(marketDishVo, dishShop)) {
                                return false;
            }

            if (totalCount.compareTo(activityRule.getDishNum()) < 0) {
                                return false;
            }

        } else {
                        BigDecimal payment = activityRule.getPayment();
            if (payment == null) {
                return false;
            }
                        BigDecimal totalAmount = BigDecimal.ZERO;
            for (IShopcartItem item : selectedShopcartItems) {
                DishShop dishShop = item.getDishShop();

                if (dishShopCanAddMarket(marketDishVo, dishShop)) {

                    if (item.getPrivilege() != null && item.getPrivilege().isValid()) {
                        BigDecimal currentAmount = item.getActualAmount().add(item.getPrivilege().getPrivilegeAmount());
                        totalAmount = totalAmount.add(currentAmount);
                    } else {
                        totalAmount = totalAmount.add(item.getActualAmount());
                    }
                }
            }
            if (totalAmount.compareTo(payment) < 0) {
                                return false;
            }
        }
        return true;
    }


    private static boolean dishShopCanAddMarket(MarketRuleVo marketDishVo, DishShop dishShop) {
        if (marketDishVo == null || dishShop == null)
            return false;


        MarketActivityRule marketActivityRule = marketDishVo.getMarketActivityRule();
        if (marketActivityRule == null) {
            return false;
        }

        if (marketActivityRule.getAllDish() == 1) {            return true;
        } else if (marketActivityRule.getAllDish() == 2) {              if (marketDishVo.isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        } else if (marketActivityRule.getAllDish() == 3) {             if (!marketDishVo.isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        }
        return false;
    }


    private static boolean mathMarket(TradePlanActivity tradePlanActivity, List<IShopcartItem> selectedShopcartItems,
                                      TradeVo mTradeVo, MarketRuleVo marketDishVo, boolean isDinner, boolean isSplitPage
    ) {
        ActivityType activityType = marketDishVo.getActivityType();
        if (marketDishVo == null || marketDishVo.getMarketActivityRule() == null || !marketDishVo.IsEnableCurrent()) {
            return false;
        }
        if (selectedShopcartItems == null || selectedShopcartItems.isEmpty()) {
            if (tradePlanActivity != null) {
                unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                        mTradeVo.getTradeItemPlanActivityList(),
                        tradePlanActivity.getUuid(),
                        isDinner);
            }
            return false;
        }
        PromotionType promotionType = marketDishVo.getPromotionType();
        MarketActivityRule activityRule = marketDishVo.getMarketActivityRule();
        List<MarketActivityDish> mDishList = marketDishVo.getMarketActivityDishList();

                if ((mDishList == null || mDishList.size() == 0) && (promotionType != PromotionType.SPECAILPRICE)
                && (activityRule.getAllDish() != 1)) {
            return false;
        }

        if (!validateMember(marketDishVo, isDinner, isSplitPage)) {
                        unBindTradePlanByRuleId(mTradeVo.getTradePlanActivityList(),
                    mTradeVo.getTradeItemPlanActivityList(),
                    activityRule.getId(),
                    isDinner);
            return false;
        }
        convertTradePlanToMap(mTradeVo.getTradePlanActivityList());
        covertItemPlanListToMap(mTradeVo.getTradeItemPlanActivityList());
                BigDecimal privilegeAmount = BigDecimal.ZERO;
        if (tradePlanActivity == null) {
            tradePlanActivity = isHasTradePlan(activityRule.getId());
        }
                boolean isNew = (tradePlanActivity == null);
                if (activityType == ActivityType.SINGLE) {
                        BigDecimal totalCount = BigDecimal.ZERO;
            for (int i = selectedShopcartItems.size() - 1; i >= 0; i--) {
                IShopcartItem shopcartItem = selectedShopcartItems.get(i);
                DishShop dishShop = selectedShopcartItems.get(0).getDishShop();
                if (shopcartItem != null && dishShop != null
                        && marketDishVo.getActivityType() == ActivityType.SINGLE
                        && !dishShopCanAddMarket(marketDishVo, dishShop)) {
                    removeTradePlanItemByItemUuid(mTradeVo.getTradeItemPlanActivityList(), shopcartItem.getUuid());
                    selectedShopcartItems.remove(i);
                    continue;
                }
                if (shopcartItem.getSingleQty() != null) {
                    totalCount = totalCount.add(shopcartItem.getSingleQty());
                }
            }

            if (activityRule.getDishNum() == null) {
                return false;
            }

            if (totalCount.compareTo(activityRule.getDishNum()) < 0) {
                                if (tradePlanActivity != null) {
                    unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                            mTradeVo.getTradeItemPlanActivityList(),
                            tradePlanActivity.getUuid(),
                            isDinner);
                }
                return false;
            }

                        BigDecimal cycleCount = BigDecimal.ONE;            if (isNew) {
                tradePlanActivity =
                        buildPlanActivity(mTradeVo, activityRule, privilegeAmount, cycleCount, promotionType);
            }
            switch (promotionType) {
                case MINUS:                                        BigDecimal totalOriginalAmount = BigDecimal.ZERO;
                    for (IShopcartItem item : selectedShopcartItems) {
                        buildItemPrePlanActivity(item, mTradeVo, activityRule, tradePlanActivity);
                        TradePrivilege tradePrivilege = item.getPrivilege();
                        BigDecimal itemAmount = BigDecimal.ZERO;
                        if (tradePrivilege == null || !tradePrivilege.isValid()) {
                            itemAmount = item.getActualAmount();
                        } else {
                            itemAmount = item.getActualAmount().add(tradePrivilege.getPrivilegeAmount());
                        }
                        totalOriginalAmount = totalOriginalAmount.add(itemAmount);
                    }
                    privilegeAmount = activityRule.getReduce();
                    privilegeAmount = privilegeAmount.multiply(cycleCount);
                    tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                    if (totalOriginalAmount.compareTo(privilegeAmount) == -1) {
                        privilegeAmount = totalOriginalAmount;
                    }
                    tradePlanActivity.setOfferValue(privilegeAmount.negate());
                    break;
                case DISCOUNT:                    tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                    mathMarketDiscount(selectedShopcartItems, mTradeVo, tradePlanActivity, activityRule, isNew);
                    break;
                case GIFT:                    if (isDinner) {
                    } else {
                                            }
                    break;
                case SPECAILPRICE:                    for (IShopcartItem item : selectedShopcartItems) {
                        if (itemMap.get(item.getUuid()) == null) {
                            buildItemPlanActivity(item, mTradeVo, activityRule, tradePlanActivity);
                        }
                        privilegeAmount = privilegeAmount.add(mathItemSpecail(item));
                    }
                                                            break;

                default:
                    break;
            }
        } else {
                        BigDecimal payment = activityRule.getPayment();
            if (payment == null) {
                return false;
            }
            BigDecimal cycleCount = BigDecimal.ONE;                        BigDecimal totalAmount = BigDecimal.ZERO;
            for (IShopcartItem item : selectedShopcartItems) {
                if (item.getPrivilege() != null && item.getPrivilege().isValid()) {
                    BigDecimal currentAmount = item.getActualAmount().add(item.getPrivilege().getPrivilegeAmount());
                    totalAmount = totalAmount.add(currentAmount);
                } else {
                    totalAmount = totalAmount.add(item.getActualAmount());
                }
            }
            if (totalAmount.compareTo(payment) < 0) {
                                if (tradePlanActivity != null) {
                    unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                            mTradeVo.getTradeItemPlanActivityList(),
                            tradePlanActivity.getUuid(),
                            isDinner);
                }
                return false;
            }
            switch (promotionType) {
                case MINUS:                    privilegeAmount = cycleCount.multiply(activityRule.getReduce());
                    privilegeAmount = privilegeAmount.negate();
                    if (isNew) {
                        tradePlanActivity =
                                buildPlanActivity(mTradeVo, activityRule, privilegeAmount, cycleCount, promotionType);
                        Log.d("IShopcartItem", "活动有=====");
                    } else {
                        tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                        tradePlanActivity.setOfferValue(privilegeAmount);
                        Log.d("IShopcartItem", "活动没有-----");
                    }
                    for (IShopcartItem item : selectedShopcartItems) {
                        buildItemPrePlanActivity(item, mTradeVo, activityRule, tradePlanActivity);
                    }
                    break;
                case DISCOUNT:                    if (isNew) {
                        tradePlanActivity =
                                buildPlanActivity(mTradeVo, activityRule, privilegeAmount, cycleCount, promotionType);
                    }
                    tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                    mathMarketDiscount(selectedShopcartItems, mTradeVo, tradePlanActivity, activityRule, isNew);
                    break;
                case GIFT:
                    break;

                default:
                    break;
            }
        }
        unbindUnSelectedItem(tradePlanActivity,
                selectedShopcartItems,
                mTradeVo.getTradeItemPlanActivityList(),
                activityRule.getId());
        if (!isNew && tradePlanActivity != null) {
            tradePlanActivity.setChanged(true);
            tradePlanActivity.setClientUpdateTime(System.currentTimeMillis());
        }
        return true;
    }


    private static BigDecimal getPrivilegeAfterPrice(IShopcartItem item) {
        TradePrivilege tradePrivilege = item.getPrivilege();
        if (tradePrivilege == null || !tradePrivilege.isValid()) {
            return item.getActualAmount();
        }
        BigDecimal itemPrice = BigDecimal.ZERO;
        itemPrice = item.getActualAmount();
        itemPrice = itemPrice.add(tradePrivilege.getPrivilegeAmount());
        return itemPrice;
    }


    private static void unbindUnSelectedItem(TradePlanActivity tradePlanActivity, List<IShopcartItem> shopcartItemList,
                                             List<TradeItemPlanActivity> itemPlanList, Long ruleId
    ) {
        if (itemPlanList == null || itemPlanList.isEmpty() || tradePlanActivity == null
                || tradePlanActivity.getStatusFlag() == StatusFlag.INVALID || shopcartItemList == null || ruleId == null) {
            return;
        }
        Map<String, IShopcartItem> shopcartItemMap = new HashMap<String, IShopcartItem>();
        for (IShopcartItem item : shopcartItemList) {
            shopcartItemMap.put(item.getUuid(), item);
        }
        for (int i = itemPlanList.size() - 1; i >= 0; i--) {
            TradeItemPlanActivity itemPlanActivity = itemPlanList.get(i);
            if (shopcartItemMap.get(itemPlanActivity.getTradeItemUuid()) == null && itemPlanActivity.getRuleId() != null
                    && ruleId.longValue() == itemPlanActivity.getRuleId().longValue()) {
                if (itemPlanActivity.getId() == null) {
                    itemPlanList.remove(i);
                } else {
                    itemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    itemPlanActivity.setChanged(true);
                }
            }
        }
    }


    private static BigDecimal mathItemSpecail(IShopcartItem item) {
        MarketDal marketDal = OperatesFactory.create(MarketDal.class);
        DishMemberPrice dishMemberPrice = marketDal.queryDishSpecailPrice(item.getDishShop().getBrandDishId());
        BigDecimal itemPrivilegeAmount = BigDecimal.ZERO;
        TradePrivilege tradePrivilege = item.getPrivilege();
        if (dishMemberPrice != null) {
            if (dishMemberPrice.getPriceType() == MemberPrivilegeType.DISCOUNT) {
                                if (tradePrivilege != null && tradePrivilege.isValid()) {
                                        itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(getPrivilegeAfterPrice(item)
                            .multiply(MathDecimal.getDiscountValueTen(dishMemberPrice.getDiscount())), 2));
                } else {
                                        itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(dishMemberPrice.getDiscount())),
                            2));
                }
            } else {
                                itemPrivilegeAmount = item.getActualAmount().subtract(new BigDecimal(dishMemberPrice.getMemberPrice()));
            }
        }
        return itemPrivilegeAmount;
    }


    private static void mathMarketDiscount(List<IShopcartItem> selectedShopcartItems, TradeVo mTradeVo,
                                           TradePlanActivity tradePlanActivity, MarketActivityRule activityRule, boolean isNew
    ) {
        BigDecimal privilegeAmount = BigDecimal.ZERO;
                BigDecimal maxCount = activityRule.getDishNum();
        int count = 0;
        for (IShopcartItem item : selectedShopcartItems) {
            TradePrivilege tradePrivilege = item.getPrivilege();
            count += item.getSingleQty().intValue();
            BigDecimal itemPrivilegeAmount = BigDecimal.ZERO;
            if (tradePrivilege != null && tradePrivilege.isValid()) {
                BigDecimal itemPrice = BigDecimal.ZERO;
                                itemPrice = item.getActualAmount();
                itemPrice = itemPrice.add(tradePrivilege.getPrivilegeAmount());
                                                BigDecimal price = item.getPrice();
                                BigDecimal counts = new BigDecimal(count);
                                if (maxCount == null) {
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                } else {
                                        if ((privilegeAmount.compareTo(BigDecimal.ZERO) == 0 && maxCount.compareTo(counts) == -1) || maxCount.compareTo(counts) == 0) {
                                                itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal
                                .round(price.multiply(maxCount).multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                    } else {
                        itemPrivilegeAmount = itemPrice;
                    }
                }
            } else {
                                                BigDecimal price = item.getPrice();
                                BigDecimal counts = new BigDecimal(count);
                                if (maxCount == null) {
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                } else {
                                        if ((privilegeAmount.compareTo(BigDecimal.ZERO) == 0 && maxCount.compareTo(counts) == -1) || maxCount.compareTo(counts) == 0) {
                                                itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal
                                .round(price.multiply(maxCount).multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                    }
                }
            }
            privilegeAmount = privilegeAmount.add(itemPrivilegeAmount);
            buildItemPrePlanActivity(item, mTradeVo, activityRule, tradePlanActivity);
        }
        tradePlanActivity.setOfferValue(privilegeAmount.negate());
    }


    public static void mathMarketPlanSplit(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, boolean isDinner) {
        mathMarketPlan(tradeVo, shopcartItemList, isDinner, true);
    }


    public static void mathMarketPlan(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, boolean isDinner,
                                      boolean isSplitPage
    ) {
        List<TradePlanActivity> tradePlanList = tradeVo.getTradePlanActivityList();
        List<TradeItemPlanActivity> tradeItemPlanList = tradeVo.getTradeItemPlanActivityList();
        if (tradePlanList == null || tradePlanList.size() == 0) {
            return;
        }
        if (shopcartItemList == null || shopcartItemList.size() == 0) {
            removeAllActivity(tradeVo);
            return;
        }
        covertItemPlanListToMap(tradeItemPlanList);
                Map<String, IShopcartItem> shopcartItemMap = new HashMap<String, IShopcartItem>();
        for (IShopcartItem shopcartItem : shopcartItemList) {
            shopcartItemMap.put(shopcartItem.getUuid(), shopcartItem);
        }

        for (int i = tradePlanList.size() - 1; i >= 0; i--) {
            TradePlanActivity tradePlanActivity = tradePlanList.get(i);

            if (!validateTradePlan(tradePlanActivity, isDinner, isSplitPage)) {
                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
            }
                        List<IShopcartItem> mixShopcartItemList = new ArrayList<IShopcartItem>();
            List<TradeItemPlanActivity> itemPlanList = itemPlanMap.get(tradePlanActivity.getUuid());
                                    if (itemPlanList == null || itemPlanList.size() == 0) {
                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
                return;
            }

            MarketRuleVo marketDishVo = MatketDishManager.getMarketDishVoByRule(tradePlanActivity.getRuleId());
            if (marketDishVo == null || !marketDishVo.IsEnableCurrent()) {
                                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
                continue;
            }

                        for (TradeItemPlanActivity tradeItemPlanActivity : itemPlanList) {
                if (tradeItemPlanActivity.getStatusFlag() == StatusFlag.VALID) {
                    IShopcartItem shopcartItem = shopcartItemMap.get(tradeItemPlanActivity.getTradeItemUuid());
                    if (shopcartItem != null && shopcartItem.getStatusFlag() == StatusFlag.INVALID
                            && (!isSplitPage || shopcartItem.getInvalidType() != InvalidType.SPLIT)) {
                        removeTradePlanItemByItemUuid(tradeItemPlanList, tradeItemPlanActivity.getTradeItemUuid());
                        continue;
                    }
                    if (shopcartItem != null && shopcartItemMap.containsKey(tradeItemPlanActivity.getTradeItemUuid())) {
                        mixShopcartItemList.add(shopcartItem);
                    }
                }
            }

            mathMarket(tradePlanActivity, mixShopcartItemList, tradeVo, marketDishVo, isDinner, isSplitPage);
        }
    }


    public static boolean validateTradePlan(TradePlanActivity tradePlanActivity, boolean isDinner, boolean isSplitPage) {
        MarketRuleVo marketRuleVo = MatketDishManager.getMarketDishVoByRule(tradePlanActivity.getRuleId());
                if (marketRuleVo == null) {
            return false;
        }

        if (!validateMember(marketRuleVo, isDinner, isSplitPage)) {
            return false;
        }
        return true;
    }


    private static boolean validateMember(MarketRuleVo marketRuleVo, boolean isDinner, boolean isSplitPage) {
        CustomerResp mCustomer = null;
        if (isDinner) {
            if (isSplitPage) {
                mCustomer = CustomerManager.getInstance().getSeparateLoginCustomer();
            } else {
                mCustomer = CustomerManager.getInstance().getDinnerLoginCustomer();
            }
        } else {
            mCustomer = CustomerManager.getInstance().getLoginCustomer();
        }
        if (mCustomer == null) {
                        if (marketRuleVo.getUserTypes().contains(UserType.MEMBER)
                    && !(marketRuleVo.getUserTypes().contains(UserType.MEMBERNON))) {
                return false;
            }
        } else {
                        if (marketRuleVo.getUserTypes().contains(UserType.MEMBERNON) && marketRuleVo.getUserTypes().size() == 1) {
                return false;
            }
        }

        return true;
    }


    public static void unBindTradePlanByTradePlanUuid(List<TradePlanActivity> tradePlanList,
                                                      List<TradeItemPlanActivity> tradeItemPlanList, String tradePlanUuid, boolean isDinner
    ) {
        if (tradePlanList == null || tradePlanList.size() == 0 || tradePlanUuid == null) {
            return;
        }
        for (int i = tradePlanList.size() - 1; i >= 0; i--) {
            TradePlanActivity tradePlanActivity = tradePlanList.get(i);
            if (!tradePlanActivity.getUuid().equals(tradePlanUuid)) {
                continue;
            }
            if (tradePlanActivity.getId() == null) {
                                tradePlanList.remove(i);
                removeTradePlanItemByUuid(tradeItemPlanList, tradePlanActivity.getUuid());
            } else {
                tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                tradePlanActivity.setChanged(true);
                removeTradePlanItemByUuid(tradeItemPlanList, tradePlanActivity.getUuid());
            }
            return;
        }
    }


    public static void unBindTradePlanByRuleId(List<TradePlanActivity> tradePlanList,
                                               List<TradeItemPlanActivity> tradeItemPlanList, Long ruleId, boolean isDinner
    ) {
        if (tradePlanList == null || tradePlanList.size() == 0 || ruleId == null) {
            return;
        }
        for (TradePlanActivity tradePlanActivity : tradePlanList) {
            if (tradePlanActivity.getRuleId().longValue() != ruleId.longValue()) {
                continue;
            }
            if (tradePlanActivity.getId() == null) {
                                tradePlanList.remove(tradePlanActivity);
                removeTradePlanItemByUuid(tradeItemPlanList, tradePlanActivity.getUuid());
            } else {
                tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                tradePlanActivity.setChanged(true);
                removeTradePlanItemByUuid(tradeItemPlanList, tradePlanActivity.getUuid());
            }
            return;
        }
    }


    private static void removeTradePlanItemByUuid(List<TradeItemPlanActivity> tradeItemPlanList, String tradePlanUuid) {
        if (tradeItemPlanList == null || tradeItemPlanList.size() == 0) {
            return;
        }
        Iterator<TradeItemPlanActivity> iterator = tradeItemPlanList.iterator();
        while (iterator.hasNext()) {
            TradeItemPlanActivity tradeItemPlanActivity = iterator.next();
            if (tradeItemPlanActivity.getRelUuid().equals(tradePlanUuid)) {
                if (tradeItemPlanActivity.getId() == null) {
                    iterator.remove();
                } else {
                    tradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradeItemPlanActivity.setChanged(true);
                }
            }
        }
    }


    private static void removeTradePlanItemByItemUuid(List<TradeItemPlanActivity> tradeItemPlanList,
                                                      String tradeItemUuid
    ) {
        if (tradeItemPlanList == null || tradeItemPlanList.size() == 0) {
            return;
        }
        Iterator<TradeItemPlanActivity> iterator = tradeItemPlanList.iterator();
        while (iterator.hasNext()) {
            TradeItemPlanActivity tradeItemPlanActivity = iterator.next();
            if (tradeItemPlanActivity.getTradeItemUuid().equals(tradeItemUuid)) {
                if (tradeItemPlanActivity.getId() == null) {
                    iterator.remove();
                } else {
                    tradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradeItemPlanActivity.setChanged(true);
                }
            }
        }
    }


    public static void removeShopcartItem(TradeVo tradeVo, List<IShopcartItem> shopcartItemList,
                                          IShopcartItem iShopcartItem, boolean isDinner, boolean isSplitPage
    ) {
        List<TradeItemPlanActivity> tradeItemPlanList = tradeVo.getTradeItemPlanActivityList();
        List<TradePlanActivity> tradePlanList = tradeVo.getTradePlanActivityList();
        if (tradeItemPlanList == null || tradeItemPlanList.size() == 0) {
            return;
        }

        if (shopcartItemList == null || shopcartItemList.size() == 0) {
                        removeTradePlanItemByItemUuid(tradeItemPlanList, iShopcartItem.getUuid());
            removeAllTradePlan(tradePlanList);
            return;
        }

        covertItemPlanListToMap(tradeItemPlanList);
        TradeItemPlanActivity itemPlan = itemMap.get(iShopcartItem.getUuid());
        removeTradePlanItemByItemUuid(tradeItemPlanList, iShopcartItem.getUuid());

        if (itemPlan == null || tradePlanList == null) {
            return;
        }

        TradePlanActivity mTradeplan = null;
                for (TradePlanActivity tradePlanActivity : tradePlanList) {
            if ((tradePlanActivity.getUuid().equals(itemPlan.getRelUuid()))
                    && (tradePlanActivity.getStatusFlag() == StatusFlag.VALID)) {
                mTradeplan = tradePlanActivity;
                break;
            }
        }
        if (mTradeplan == null) {
            return;
        }
        MarketRuleVo marketDishVo = MatketDishManager.getMarketDishVoByRule(mTradeplan.getRuleId());
        if (marketDishVo == null || !marketDishVo.IsEnableCurrent()) {
                        unBindTradePlanByRuleId(tradePlanList, tradeItemPlanList, mTradeplan.getRuleId(), isDinner);
            return;
        }
                List<IShopcartItem> mixShopcartItemList = new ArrayList<IShopcartItem>();
        List<TradeItemPlanActivity> itemPlanList = itemPlanMap.get(mTradeplan.getUuid());
        if (itemPlanList == null) {
            return;
        }
                Map<String, IShopcartItem> shopcartItemMap = new HashMap<String, IShopcartItem>();
        for (IShopcartItem shopcartItem : shopcartItemList) {
            shopcartItemMap.put(shopcartItem.getUuid(), shopcartItem);
        }

                for (TradeItemPlanActivity tradeItemPlanActivity : itemPlanList) {
            if (tradeItemPlanActivity.getStatusFlag() == StatusFlag.VALID) {
                IShopcartItem shopcartItem = shopcartItemMap.get(tradeItemPlanActivity.getTradeItemUuid());
                if (shopcartItem != null && shopcartItem.getDishShop() != null
                        && marketDishVo.getActivityType() == ActivityType.SINGLE
                        && !dishShopCanAddMarket(marketDishVo, shopcartItem.getDishShop())) {
                    continue;
                }
                if (shopcartItem != null && shopcartItemMap.containsKey(tradeItemPlanActivity.getTradeItemUuid())
                        && shopcartItem.getStatusFlag() == StatusFlag.VALID) {
                    mixShopcartItemList.add(shopcartItem);
                }
            }
        }

        mathMarket(mTradeplan, mixShopcartItemList, tradeVo, marketDishVo, isDinner, isSplitPage);
    }


    private static void removeAllTradePlan(List<TradePlanActivity> tradePlanList) {
        if (tradePlanList == null || tradePlanList.isEmpty()) {
            return;
        }
        for (int i = tradePlanList.size() - 1; i >= 0; i--) {
            TradePlanActivity tradePlanActivity = tradePlanList.get(i);
            if (tradePlanActivity.getId() == null) {
                tradePlanList.remove(i);
            } else {
                tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                tradePlanActivity.setChanged(true);
            }
        }
    }

    private static void buildItemPrePlanActivity(IShopcartItem item, TradeVo tradeVo, MarketActivityRule activityRule,
                                                 TradePlanActivity tradePlanActivity
    ) {
        TradeItemPlanActivity tradeItemPlanActivity = itemMap.get(item.getUuid());
        if (tradeItemPlanActivity == null) {
            buildItemPlanActivity(item, tradeVo, activityRule, tradePlanActivity);
        } else {
            tradeItemPlanActivity.setRelId(tradePlanActivity.getId());
            tradeItemPlanActivity.setRelUuid(tradePlanActivity.getUuid());
        }
    }


    public static void buildItemPlanActivity(IShopcartItem item, TradeVo tradeVo, MarketActivityRule activityRule,
                                             TradePlanActivity tradePlanActivity
    ) {
        TradeItemPlanActivity itemPlanActivity = new TradeItemPlanActivity();
        itemPlanActivity.setStatusFlag(StatusFlag.VALID);
        itemPlanActivity.setRuleId(activityRule.getId());
        itemPlanActivity.setPlanId(activityRule.getPlanId());
        itemPlanActivity.setTradeId(tradeVo.getTrade().getId());
        itemPlanActivity.setTradeUuid(tradeVo.getTrade().getUuid());
        itemPlanActivity.setTradeItemUuid(item.getUuid());
        itemPlanActivity.setTradeItemId(item.getId());
        itemPlanActivity.setChanged(true);
        itemPlanActivity.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        itemPlanActivity.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        if (tradePlanActivity != null) {
            itemPlanActivity.setRelUuid(tradePlanActivity.getUuid());
        }
        if (tradeVo.getTradeItemPlanActivityList() == null) {
            tradeVo.setTradeItemPlanActivityList(new ArrayList<TradeItemPlanActivity>());
        }
        tradeVo.getTradeItemPlanActivityList().add(itemPlanActivity);
    }


    public static TradePlanActivity buildPlanActivity(TradeVo tradeVo, MarketActivityRule activityRule,
                                                      BigDecimal privilegeAmount, BigDecimal cycleCount, PromotionType type
    ) {
        if (activityRule == null || tradeVo == null) {
            return null;
        }
        TradePlanActivity planActivity = new TradePlanActivity();
        planActivity.setStatusFlag(StatusFlag.VALID);
        planActivity.setUuid(SystemUtils.genOnlyIdentifier());
        planActivity.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        planActivity.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        planActivity.setPlanId(activityRule.getPlanId());
        planActivity.setRuleId(activityRule.getId());
        planActivity.setPlanId(activityRule.getPlanId());
        planActivity.setTradeId(tradeVo.getTrade().getId());
        planActivity.setTradeUuid(tradeVo.getTrade().getUuid());
        planActivity.setRuleName(getPromotionName(type, activityRule));
        planActivity.setOfferValue(privilegeAmount);
        planActivity.setPlanUsageCount(cycleCount.intValue());
        planActivity.setClientCreateTime(System.currentTimeMillis());
        planActivity.setClientUpdateTime(System.currentTimeMillis());
        planActivity.setRuleEffective(ActivityRuleEffective.VALID);
        planActivity.setChanged(true);
        if (tradeVo.getTradePlanActivityList() == null) {
            tradeVo.setTradePlanActivityList(new ArrayList<TradePlanActivity>());
        }
        tradeVo.getTradePlanActivityList().add(planActivity);
        return planActivity;
    }

    private static String getPromotionName(PromotionType type, MarketActivityRule rule) {
        String ruleName;
        if (rule.getName() == null) {
            ruleName = "";
        } else {
            ruleName = rule.getName();
        }
        switch (type) {
            case MINUS:                 ruleName = ruleName + "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.trimZero(rule.getReduce());
                break;
            case DISCOUNT:                 ruleName = ruleName + MathDecimal.trimZero(rule.getDiscount()) + BaseApplication.sInstance.getString(R.string.discount1);
                break;
            case GIFT:                 ruleName = ruleName + BaseApplication.sInstance.getString(R.string.give);
                break;
            case SPECAILPRICE:                 ruleName = ruleName + BaseApplication.sInstance.getString(R.string.special_price);
                break;
            default:
                break;
        }
        return ruleName;
    }


    private static void covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        itemPlanMap = new HashMap<String, List<TradeItemPlanActivity>>();
        itemMap = new HashMap<String, TradeItemPlanActivity>();
                if (tradeItemPlanList != null && tradeItemPlanList.size() > 0) {

            for (TradeItemPlanActivity tradeItemPlan : tradeItemPlanList) {
                if (tradeItemPlan.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                List<TradeItemPlanActivity> itemPlanList = itemPlanMap.get(tradeItemPlan.getRelUuid());
                if (itemPlanList == null) {
                    itemPlanList = new ArrayList<TradeItemPlanActivity>();
                    itemPlanMap.put(tradeItemPlan.getRelUuid(), itemPlanList);
                }
                itemPlanList.add(tradeItemPlan);
                itemMap.put(tradeItemPlan.getTradeItemUuid(), tradeItemPlan);
            }
        }
    }

    private static void convertTradePlanToMap(List<TradePlanActivity> tradePlanList) {
        if (tradePlanList == null || tradePlanList.isEmpty()) {
            tradePlanMap = null;
            return;
        }
        tradePlanMap = new HashMap<Long, TradePlanActivity>();
        for (TradePlanActivity planActivity : tradePlanList) {
            if (planActivity.getStatusFlag() == StatusFlag.VALID) {
                tradePlanMap.put(planActivity.getRuleId(), planActivity);
            }
        }
    }


    private static TradePlanActivity isHasTradePlan(Long ruleId) {
        if (tradePlanMap == null) {
            return null;
        }
        TradePlanActivity tradePlanActivity = tradePlanMap.get(ruleId);
        if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                && tradePlanActivity.getRuleEffective() != ActivityRuleEffective.INVALID) {
            return tradePlanActivity;
        }
        return null;
    }


    public static void removeAllActivity(TradeVo tradeVo) {
        List<TradePlanActivity> tradePlanList = tradeVo.getTradePlanActivityList();
        List<TradeItemPlanActivity> tradeItemPlanList = tradeVo.getTradeItemPlanActivityList();
        if (tradePlanList != null) {
            for (int i = tradePlanList.size() - 1; i >= 0; i--) {
                TradePlanActivity tradePlanActivity = tradePlanList.get(i);
                if (tradePlanActivity.getId() == null) {
                    tradePlanList.remove(i);
                } else {
                    tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradePlanActivity.setChanged(true);
                    tradePlanActivity.setClientUpdateTime(System.currentTimeMillis());
                }
            }
        }

        if (tradeItemPlanList != null) {
            for (int i = tradeItemPlanList.size() - 1; i >= 0; i--) {
                TradeItemPlanActivity tradeItemPlanActivity = tradeItemPlanList.get(i);
                if (tradeItemPlanActivity.getId() == null) {
                    tradeItemPlanList.remove(i);
                } else {
                    tradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradeItemPlanActivity.setChanged(true);
                }
            }
        }
                if (tradePlanList != null && tradePlanList.isEmpty()) {
            tradePlanList = null;
        }

        if (tradeItemPlanList != null && tradeItemPlanList.isEmpty()) {
            tradeItemPlanList = null;
        }
    }


    public static boolean isHasTradePlan(TradeVo tradeVo) {
        List<TradePlanActivity> planList = tradeVo.getTradePlanActivityList();
        if (planList == null || planList.size() == 0) {
            return false;
        }
        for (TradePlanActivity planActivity : planList) {
            if (planActivity.getStatusFlag() == StatusFlag.VALID && planActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                return true;
            }
        }
        return false;
    }


    public static boolean isItemCanJoinPlanActivity(IShopcartItemBase shopcartItemBase, MarketRuleVo ruleVo) {
                if (shopcartItemBase != null && shopcartItemBase.getDishShop() != null
                && ruleVo.isDishEnableUsed(shopcartItemBase.getDishShop())
                && !DiscountTool.isSinglePrivilege(shopcartItemBase)) {

            return true;
        } else {
            return false;
        }
    }

}
