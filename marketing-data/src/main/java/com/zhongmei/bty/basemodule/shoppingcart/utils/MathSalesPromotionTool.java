package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionDishQuantity;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionPolicyDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRule;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.shoppingcart.event.AddSalesPromotionDishToShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_DISCOUNT;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_FIXED_PRICE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE_LOWEST;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.NEXT_GOODS_DISCOUNT;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.NEXT_GOODS_FIXED_PRICE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.NEXT_GOODS_REBATE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_DISCOUNT;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_FIXED_PRICE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_REBATE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_DISCOUNT;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_FIXED_PRICE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_REBATE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_REBATE_LOWEST;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.GIVE_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.MULTIPLE_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.NEXT_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.RAISE_PRICE_BUY_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.SINGLE_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.PolicySubject.SPECIFIED_GOODS;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE;
import static com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE;

public class MathSalesPromotionTool {

    public static boolean isItemCanJoinPlanActivity(@NonNull IShopcartItemBase shopcartItemBase, @NonNull SalesPromotionRuleVo salesPromotionRuleVo) {
                if (shopcartItemBase.getDishShop() != null
                && salesPromotionRuleVo.isContainDish(shopcartItemBase.getDishShop())
                && !DiscountTool.isSinglePrivilege(shopcartItemBase)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean canAddSalesPromotion(@NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> selectedShopcartItems, CustomerResp customerNew) {
                if (Utils.isEmpty(selectedShopcartItems)) {
            return false;
        }

                if (!salesPromotionRuleVo.checkLimitPeriod()) {
            return false;
        }

                if (!salesPromotionRuleVo.checkWeekday()) {
            return false;
        }

                if (!salesPromotionRuleVo.checkApplyCrowd(customerNew)) {
            return false;
        }

                if (!salesPromotionRuleVo.checkActivityDish(selectedShopcartItems)) {
            return false;
        }

        return true;
    }


    public static boolean mathManualAddSalesPromotion(@NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> selectedShopcartItems, @NonNull TradeVo tradeVo, CustomerResp customerNew) {
        return mathSalesPromotion(tradeVo, salesPromotionRuleVo, selectedShopcartItems, customerNew);
    }

    private static boolean mathSalesPromotion(TradeVo tradeVo, SalesPromotionRuleVo salesPromotionRuleVo, List<IShopcartItem> selectedShopcartItems, CustomerResp customerNew) {
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule == null || Utils.isEmpty(selectedShopcartItems)) {
            return false;
        }

                if (!salesPromotionRuleVo.checkLimitPeriod()) {
            removeTradePlanActivity(tradeVo.getTradePlanActivityList(), tradeVo.getTradeItemPlanActivityList(), salesPromotionRule.getPlanId());
            return false;
        }

                if (!salesPromotionRuleVo.checkWeekday()) {
            removeTradePlanActivity(tradeVo.getTradePlanActivityList(), tradeVo.getTradeItemPlanActivityList(), salesPromotionRule.getPlanId());
            return false;
        }

                if (!salesPromotionRuleVo.checkApplyCrowd(customerNew)) {
            removeTradePlanActivity(tradeVo.getTradePlanActivityList(), tradeVo.getTradeItemPlanActivityList(), salesPromotionRule.getPlanId());
            return false;
        }

                if (!salesPromotionRuleVo.checkActivityDish(selectedShopcartItems)) {
            removeTradePlanActivity(tradeVo.getTradePlanActivityList(), tradeVo.getTradeItemPlanActivityList(), salesPromotionRule.getPlanId());
            return false;
        }

                int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
        switch (ruleSubjectType) {
            case MATCH_QUANTITY_ONCE:
                mathMatchQuantityOnce(tradeVo, salesPromotionRuleVo, selectedShopcartItems);
                break;
            case MATCH_AMOUNT_ONCE:
                mathMatchAmountOnce(tradeVo, salesPromotionRuleVo, selectedShopcartItems);
                break;
            default:
                                break;
        }

        return true;
    }


    private static void mathMatchQuantityOnce(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policySubjectType = salesPromotionRuleVo.getRule().getPolicySubjectType();
        switch (policySubjectType) {
            case MULTIPLE_GOODS:
                mathMatchQuantityOnceMultipleGoods(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case GIVE_GOODS:
                if (BusinessTypeUtils.isRetail()) {
                    mathMatchQuantityOnceSpecifiedGoodsGive(tradeVo, salesPromotionRuleVo, shopcartItems);
                }
                break;
            case RAISE_PRICE_BUY_GOODS:
                if (BusinessTypeUtils.isRetail()) {
                    mathMatchQuantityOnceSpecifiedGoodsRaisePrice(tradeVo, salesPromotionRuleVo, shopcartItems);
                }
                break;
            case SINGLE_GOODS:
                mathMatchQuantityOnceSingleGoods(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case NEXT_GOODS:
                mathMatchQuantityOnceNextGoods(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            default:
                                break;
        }
    }


    private static void mathMatchQuantityOnceMultipleGoods(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        switch (policyDetailType) {
            case MULTIPLE_GOODS_FIXED_PRICE:
                mathMatchQuantityOnceMultipleGoodsFixedPrice(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case MULTIPLE_GOODS_REBATE:
                mathMatchQuantityOnceMultipleGoodsRebate(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case MULTIPLE_GOODS_REBATE_LOWEST:
                mathMatchQuantityOnceMultipleGoodsRebateLowest(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case MULTIPLE_GOODS_DISCOUNT:
                mathMatchQuantityOnceMultipleGoodsDiscount(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            default:
                break;
        }
    }



    private static void mathMatchQuantityOnceSingleGoods(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
                        BigDecimal stackCount = BigDecimal.ONE;
            tradePlanActivity.setPlanUsageCount(stackCount.intValue());
                        Map<Long, SalesPromotionDish> salesPromotionDishMap = convertSalesPromotionDishToMap(salesPromotionRuleVo.getActivityDishs());
                        Map<Long, SalesPromotionDishQuantity> temp = statisticsDispQuantity(shopcartItems);
            if (temp == null || temp.size() == 0) {
                return;
            }

                        BigDecimal offerAmount = BigDecimal.ZERO;
            for (Map.Entry<Long, SalesPromotionDishQuantity> entry : temp.entrySet()) {
                SalesPromotionDishQuantity salesPromotionDishQuantity = entry.getValue();
                int marketSubjectType = salesPromotionRuleVo.getRule().getMarketSubjectType();
                                if (SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE == marketSubjectType
                        || (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType && salesPromotionRuleVo.getActivityDishs() == null)) {
                    if (salesPromotionDishQuantity.singleQty.compareTo(salesPromotionRuleVo.getRule().getLogicValue()) >= 0) {
                        offerAmount = mathOfferAmount(salesPromotionDishQuantity, tradeVo, salesPromotionRuleVo, tradePlanActivity, shopcartItems, offerAmount);
                    }
                } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE == marketSubjectType) {
                                        if (salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) || salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                        if (salesPromotionDishQuantity.singleQty.compareTo(salesPromotionRuleVo.getRule().getLogicValue()) >= 0) {
                            offerAmount = mathOfferAmount(salesPromotionDishQuantity, tradeVo, salesPromotionRuleVo, tradePlanActivity, shopcartItems, offerAmount);
                        }
                    }
                } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType) {
                                        if (!salesPromotionDishMap.containsKey(salesPromotionDishQuantity.brandDishId) && !salesPromotionDishMap.containsKey(salesPromotionDishQuantity.dishTypeId)) {
                        if (salesPromotionDishQuantity.singleQty.compareTo(salesPromotionRuleVo.getRule().getLogicValue()) >= 0) {
                            offerAmount = mathOfferAmount(salesPromotionDishQuantity, tradeVo, salesPromotionRuleVo, tradePlanActivity, shopcartItems, offerAmount);
                        }
                    }
                }
            }

            tradePlanActivity.setOfferValue(offerAmount.negate());

        }

    }


    private static BigDecimal mathOfferAmount(SalesPromotionDishQuantity salesPromotionDishQuantity, @NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, TradePlanActivity tradePlanActivity, @NonNull List<IShopcartItem> shopcartItems, BigDecimal offerAmount) {

        for (IShopcartItem item : shopcartItems) {
            if (salesPromotionDishQuantity.brandDishId == item.getDishShop().getBrandDishId() || salesPromotionDishQuantity.dishTypeId == item.getDishShop().getDishTypeId()) {
                                getTradeItemPlanActivity(item, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity);

                if (SINGLE_GOODS_FIXED_PRICE == salesPromotionRuleVo.getRule().getPolicyDetailType()) {
                                        offerAmount = offerAmount.add(item.getSingleQty().multiply(salesPromotionDishQuantity.price.subtract(salesPromotionRuleVo.getRule().getPolicyValue1())));
                } else if (SINGLE_GOODS_REBATE == salesPromotionRuleVo.getRule().getPolicyDetailType()) {
                                        offerAmount = offerAmount.add(salesPromotionRuleVo.getRule().getPolicyValue1().multiply(item.getSingleQty()));
                } else if (SINGLE_GOODS_DISCOUNT == salesPromotionRuleVo.getRule().getPolicyDetailType()) {
                                        offerAmount = offerAmount.add(item.getSingleQty().multiply(salesPromotionDishQuantity.price.multiply(BigDecimal.ONE.subtract(salesPromotionRuleVo.getRule().getPolicyValue1().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP)))));
                }
            }
        }
        return offerAmount;

    }


    public static Map<Long, SalesPromotionDishQuantity> statisticsDispQuantity(@NonNull List<IShopcartItem> shopcartItems) {
        Map<Long, SalesPromotionDishQuantity> map = new HashMap<>();
        if (Utils.isNotEmpty(shopcartItems)) {
                                    for (int i = 0; i < shopcartItems.size(); i++) {
                IShopcartItem shopcartItem = shopcartItems.get(i);
                if (map.containsKey(shopcartItem.getDishShop().getBrandDishId())) {
                    SalesPromotionDishQuantity salesPromotionDishQuantity = map.get(shopcartItem.getDishShop().getBrandDishId());
                    salesPromotionDishQuantity.singleQty = salesPromotionDishQuantity.singleQty.add(shopcartItem.getSingleQty());
                    map.put(shopcartItem.getDishShop().getBrandDishId(), salesPromotionDishQuantity);
                } else {
                    SalesPromotionDishQuantity dishQuantity = new SalesPromotionDishQuantity();
                    dishQuantity.singleQty = shopcartItem.getSingleQty();
                    dishQuantity.brandDishId = shopcartItem.getDishShop().getBrandDishId();
                    dishQuantity.dishTypeId = shopcartItem.getDishShop().getDishTypeId();
                    dishQuantity.price = shopcartItem.getPrice();
                    dishQuantity.shopcartItem = shopcartItem;
                    map.put(shopcartItem.getDishShop().getBrandDishId(), dishQuantity);
                }
            }
        }
        return map;
    }


    private static Map<Long, SalesPromotionDish> convertSalesPromotionDishToMap(List<SalesPromotionDish> activityDishs) {
        Map<Long, SalesPromotionDish> map = new HashMap<>();
        if (Utils.isNotEmpty(activityDishs)) {
            for (SalesPromotionDish item : activityDishs) {
                map.put(item.getRelateId(), item);
            }
        }
        return map;
    }


    private static void mathMatchQuantityOnceMultipleGoodsFixedPrice(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);

                        BigDecimal notFixedAmount = getNotFixedAmount(shopcartItems, stackCount, salesPromotionRuleVo.getRule().getLogicValue());
            BigDecimal fixedPrice = mathSpecifiedGoodsRebateOfferValue(salesPromotionRuleVo.getRule(), stackCount);
            tradePlanActivity.setOfferValue(getGoodsTotalAmount(shopcartItems).subtract(fixedPrice.add(notFixedAmount)).negate());
        }

    }


    private static void mathMatchQuantityOnceMultipleGoodsRebate(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);

                        BigDecimal offerValue = mathSpecifiedGoodsRebateOfferValue(salesPromotionRuleVo.getRule(), stackCount);
            BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            if (totalAmount.compareTo(offerValue) == -1) {
                offerValue = getGoodsTotalAmount(shopcartItems);
            }

            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }



    private static void mathMatchQuantityOnceMultipleGoodsRebateLowest(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);
                        BigDecimal offerValue = mathMatchQuantityOnceMultipleGoodsRebateLowestOfferValue(salesPromotionRuleVo.getRule(), shopcartItems, stackCount);
            tradePlanActivity.setOfferValue(offerValue);
        }
    }


    private static BigDecimal mathMatchQuantityOnceMultipleGoodsRebateLowestOfferValue(@NonNull SalesPromotionRule salesPromotionRule, List<IShopcartItem> shopcartItems, BigDecimal stackCount) {
        if (stackCount != null) {
            return getRebateAmount(shopcartItems, stackCount).negate();        }

        return BigDecimal.ZERO;
    }

        private static BigDecimal getRebateAmount(List<IShopcartItem> shopcartItems, @NonNull BigDecimal stackCount) {
        List<BigDecimal> amountList = new ArrayList<BigDecimal>();
        if (Utils.isNotEmpty(shopcartItems)) {
            for (int i = shopcartItems.size() - 1; -1 < i; i--) {
                BigDecimal quantity = shopcartItems.get(i).getTotalQty();
                if (quantity != null) {
                    for (int j = 0; j < quantity.intValue(); j++) {
                        amountList.add(shopcartItems.get(i).getPrice());
                    }
                }
            }
        }

                Collections.sort(amountList, new Comparator<BigDecimal>() {
            @Override
            public int compare(BigDecimal o1, BigDecimal o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return -1;
                } else if (o2 == null) {
                    return 1;
                } else {
                    return o1.compareTo(o2);
                }
            }
        });

                if (Utils.isNotEmpty(amountList)) {
            int totalCanDiscountQuantity = stackCount.intValue();
            if (totalCanDiscountQuantity > amountList.size()) {
                totalCanDiscountQuantity = amountList.size();
            }

            List<BigDecimal> subAmountList = amountList.subList(0, totalCanDiscountQuantity);
            BigDecimal rebateAmount = BigDecimal.ZERO;
            for (BigDecimal item : subAmountList) {
                rebateAmount = rebateAmount.add(item);
            }
            return rebateAmount;
        }

        return BigDecimal.ZERO;
    }


    private static BigDecimal mathSpecifiedGoodsRebateOfferValue(@NonNull SalesPromotionRule salesPromotionRule, BigDecimal stackCount) {
        BigDecimal policyValue1 = salesPromotionRule.getPolicyValue1();        if (policyValue1 != null && stackCount != null) {
            return policyValue1.multiply(stackCount);
        }

        return BigDecimal.ZERO;
    }


    private static BigDecimal mathSpecifiedGoodsGiveOfferValue(@NonNull SalesPromotionRuleVo salesPromotionRuleVo, BigDecimal count) {
        List<SalesPromotionPolicyDish> policyDishList = salesPromotionRuleVo.getPolicyDishs();
        if (Utils.isEmpty(policyDishList)) {
            return BigDecimal.ZERO;
        }


        SalesPromotionPolicyDish policyDish = policyDishList.get(0);
        DishManager dishManager = new DishManager();

        DishVo dishVo = null;
        if (policyDish.getType() == 1) {
            dishVo = dishManager.getDishVoByBrandDishId(policyDish.getRelateId());
        } else {
            DishBrandType type = new DishBrandType();
            type.setId(policyDish.getRelateId());
            DishInfo dishInfo = dishManager.switchType(type);
            if (dishInfo != null && Utils.isNotEmpty(dishInfo.dishList)) {
                dishVo = dishInfo.dishList.get(0);
            }
        }

        if (dishVo != null) {
            AddSalesPromotionDishToShoppingCart event = new AddSalesPromotionDishToShoppingCart(salesPromotionRuleVo, dishVo, count, AddSalesPromotionDishToShoppingCart.DISH_POLICY_FREE);
            EventBus.getDefault().post(event);
            return dishVo.getPrice().multiply(count);
        }

        return BigDecimal.ZERO;
    }

        private static BigDecimal getMatchQuantityOnceStackCount(@NonNull SalesPromotionRule salesPromotionRule, List<IShopcartItem> shopcartItems) {
        BigDecimal stackCount = BigDecimal.ONE;
        boolean isStackRule = salesPromotionRule.isStackRule();        if (isStackRule) {
            BigDecimal totalQty = getGoodsTotalQuantity(shopcartItems);
            BigDecimal logicValue = salesPromotionRule.getLogicValue();
            if (logicValue != null && logicValue.compareTo(BigDecimal.ZERO) > 0) {
                stackCount = totalQty.divideToIntegralValue(logicValue);
            }
            return stackCount;
        }

        return stackCount;
    }

        private static BigDecimal getGoodsTotalQuantity(List<IShopcartItem> shopcartItems) {
        BigDecimal totalQty = BigDecimal.ZERO;
        if (Utils.isNotEmpty(shopcartItems)) {
            int size = shopcartItems.size();
            for (int i = 0; i < size; i++) {
                IShopcartItem item = shopcartItems.get(i);
                if (item != null) {
                    BigDecimal singleQty = item.getSingleQty();
                    if (singleQty != null) {
                        totalQty = totalQty.add(item.getSingleQty());
                    }
                }
            }
        }

        return totalQty;
    }


    private static void mathMatchQuantityOnceMultipleGoodsDiscount(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);


                        BigDecimal offerValue = mathMatchQuantityOnceSpecifiedGoodsDiscountOfferValue(salesPromotionRuleVo.getRule(), shopcartItems, stackCount);
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }



    private static BigDecimal mathMatchQuantityOnceSpecifiedGoodsDiscountOfferValue(@NonNull SalesPromotionRule salesPromotionRule, List<IShopcartItem> shopcartItems, BigDecimal stackCount) {
        BigDecimal policyValue1 = salesPromotionRule.getPolicyValue1();        BigDecimal logicValue = salesPromotionRule.getLogicValue();        if (policyValue1 != null && stackCount != null && logicValue != null) {
            BigDecimal canDiscountAmount = getCanDiscountAmount(shopcartItems, stackCount, logicValue);            BigDecimal afterDiscountAmount = canDiscountAmount.multiply(policyValue1.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));            return canDiscountAmount.subtract(afterDiscountAmount);
        }

        return BigDecimal.ZERO;
    }

        private static BigDecimal getNotFixedAmount(List<IShopcartItem> shopcartItems, @NonNull BigDecimal stackCount, @NonNull BigDecimal logicValue) {

        int totalQuantity = getGoodsTotalQuantity(shopcartItems).intValue();
        int totalNotNotFixedQuantity = totalQuantity - logicValue.multiply(stackCount).intValue();

        List<IShopcartItem> tempShopcartItems = new ArrayList<>();
        for (IShopcartItem item : shopcartItems) {
            for (int i = 0; i < item.getSingleQty().intValue(); i++) {
                tempShopcartItems.add(item);
            }
        }

        BigDecimal totalNotNotFixedAmount = BigDecimal.ZERO;
        if (totalNotNotFixedQuantity != 0) {
            tempShopcartItems = tempShopcartItems.subList(totalQuantity - totalNotNotFixedQuantity, totalQuantity);
            for (IShopcartItem item : tempShopcartItems) {
                totalNotNotFixedAmount = totalNotNotFixedAmount.add(item.getPrice());
            }
        }

        return totalNotNotFixedAmount;
    }

        private static BigDecimal getCanDiscountAmount(List<IShopcartItem> shopcartItems, @NonNull BigDecimal stackCount, @NonNull BigDecimal logicValue) {
        List<BigDecimal> amountList = new ArrayList<BigDecimal>();
        if (Utils.isNotEmpty(shopcartItems)) {
            for (IShopcartItem shopcartItem : shopcartItems) {
                BigDecimal quantity = shopcartItem.getTotalQty();
                if (quantity != null) {
                    for (int i = 0; i < quantity.intValue(); i++) {
                        amountList.add(shopcartItem.getPrice());
                    }
                }
            }
        }

                int totalCanDiscountQuantity = logicValue.multiply(stackCount).intValue();
        if (totalCanDiscountQuantity > amountList.size()) {
            totalCanDiscountQuantity = amountList.size();
        }
        if (Utils.isNotEmpty(amountList)) {
            List<BigDecimal> subAmountList = amountList.subList(0, totalCanDiscountQuantity);
            BigDecimal canDiscpountAmount = BigDecimal.ZERO;
            for (BigDecimal item : subAmountList) {
                canDiscpountAmount = canDiscpountAmount.add(item);
            }
            return canDiscpountAmount;
        }

        return BigDecimal.ZERO;
    }


    private static void mathMatchQuantityOnceNextGoods(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        switch (policyDetailType) {
            case NEXT_GOODS_FIXED_PRICE:
                mathMatchQuantityOnceNextGoodsFixedPrice(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case NEXT_GOODS_REBATE:
                mathMatchQuantityOnceNextGoodsRebate(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case NEXT_GOODS_DISCOUNT:
                mathMatchQuantityOnceNextGoodsDiscount(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            default:
                break;
        }
    }


    private static void mathMatchQuantityOnceNextGoodsFixedPrice(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {

    }


    private static void mathMatchQuantityOnceNextGoodsRebate(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {

    }


    private static void mathMatchQuantityOnceNextGoodsDiscount(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {

    }


    private static void mathMatchAmountOnce(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policySubjectType = salesPromotionRuleVo.getRule().getPolicySubjectType();
        switch (policySubjectType) {
            case SPECIFIED_GOODS:
                mathMatchAmountOnceSpecifiedGoods(tradeVo, salesPromotionRuleVo, shopcartItems);
                break;
            case GIVE_GOODS:
                if (BusinessTypeUtils.isRetail()) {
                    mathMatchAmountOnceSpecifiedGoodsGive(tradeVo, salesPromotionRuleVo, shopcartItems);
                }
                break;
            case RAISE_PRICE_BUY_GOODS:
                if (BusinessTypeUtils.isRetail()) {
                    mathMatchAmountOnceSpecifiedGoodsRaisePrice(tradeVo, salesPromotionRuleVo, shopcartItems);
                }
                break;
            case NEXT_GOODS:
                break;
            default:
                break;
        }
    }


    private static void mathMatchAmountOnceSpecifiedGoods(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        switch (policyDetailType) {
            case SPECIFIED_GOODS_FIXED_PRICE:
                break;

                        case SPECIFIED_GOODS_REBATE:
                mathMatchAmountOnceSpecifiedGoodsRebate(tradeVo, salesPromotionRuleVo.getRule(), shopcartItems);
                break;

            case SPECIFIED_GOODS_REBATE_LOWEST:
                break;

                        case SPECIFIED_GOODS_DISCOUNT:
                mathMatchAmountOnceSpecifiedGoodsDiscount(tradeVo, salesPromotionRuleVo.getRule(), shopcartItems);
                break;
            default:
                break;
        }
    }


    private static void mathMatchAmountOnceSpecifiedGoodsGive(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        if (policyDetailType != SalesPromotionConstant.PolicyDetail.GIVE_GOODS) {
            return;
        }

                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchAmountOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindAmountTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity);


                        BigDecimal offerValue = mathSpecifiedGoodsGiveOfferValue(salesPromotionRuleVo, salesPromotionRuleVo.getRule().getPolicyValue1().multiply(stackCount));
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }


    private static void mathMatchQuantityOnceSpecifiedGoodsRaisePrice(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        if (policyDetailType != SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE) {
            return;
        }

                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);


                        BigDecimal offerValue = mathSpecifiedGoodsRebateOfferValue(salesPromotionRuleVo.getRule(), stackCount);
            BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            if (totalAmount.compareTo(offerValue) == -1) {
                offerValue = totalAmount;
            }
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }



    private static void mathMatchQuantityOnceSpecifiedGoodsGive(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        if (policyDetailType != SalesPromotionConstant.PolicyDetail.GIVE_GOODS) {
            return;
        }

                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindQuantityTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity, stackCount);


                        BigDecimal offerValue = mathSpecifiedGoodsGiveOfferValue(salesPromotionRuleVo, salesPromotionRuleVo.getRule().getPolicyValue1().multiply(stackCount));
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }


    private static void mathMatchAmountOnceSpecifiedGoodsRaisePrice(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> shopcartItems) {
        int policyDetailType = salesPromotionRuleVo.getRule().getPolicyDetailType();
        if (policyDetailType != SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE) {
            return;
        }

                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRuleVo.getRule());
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRuleVo.getRule(), shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindAmountTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRuleVo.getRule(), tradePlanActivity);


                        BigDecimal offerValue = mathSpecifiedGoodsRebateOfferValue(salesPromotionRuleVo.getRule(), stackCount);
            BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            if (totalAmount.compareTo(offerValue) == -1) {
                offerValue = totalAmount;
            }
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }


    private static void mathMatchAmountOnceSpecifiedGoodsRebate(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRule);
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRule, shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindAmountTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRule, tradePlanActivity);


                        BigDecimal offerValue = mathSpecifiedGoodsRebateOfferValue(salesPromotionRule, stackCount);
            BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            if (totalAmount.compareTo(offerValue) == -1) {
                offerValue = totalAmount;
            }
            tradePlanActivity.setOfferValue(offerValue.negate());
        }
    }


    private static void mathMatchAmountOnceSpecifiedGoodsDiscount(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule, @NonNull List<IShopcartItem> shopcartItems) {
                TradePlanActivity tradePlanActivity = getTradePlanActivity(tradeVo, salesPromotionRule);
        if (tradePlanActivity != null) {
            BigDecimal stackCount = getMatchQuantityOnceStackCount(salesPromotionRule, shopcartItems);
            if (stackCount != null) {
                tradePlanActivity.setPlanUsageCount(stackCount.intValue());
            }

            bindAmountTradeItemsPlanActivity(shopcartItems, tradeVo, salesPromotionRule, tradePlanActivity);

                        BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            BigDecimal discount = ((BigDecimal.TEN.subtract(salesPromotionRule.getPolicyValue1())).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
            tradePlanActivity.setOfferValue(totalAmount.multiply(discount).negate());
        }
    }

        private static BigDecimal getMatchAmountOnceStackCount(@NonNull SalesPromotionRule salesPromotionRule, List<IShopcartItem> shopcartItems) {
        BigDecimal stackCount = BigDecimal.ONE;
        boolean isStackRule = salesPromotionRule.isStackRule();        if (isStackRule) {
            BigDecimal totalAmount = getGoodsTotalAmount(shopcartItems);
            BigDecimal logicValue = salesPromotionRule.getLogicValue();
            if (logicValue != null && logicValue.compareTo(BigDecimal.ZERO) > 0) {
                stackCount = totalAmount.divideToIntegralValue(logicValue);
            }
            return stackCount;
        }

        return stackCount;
    }

        private static BigDecimal getGoodsTotalAmount(List<IShopcartItem> shopcartItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(shopcartItems)) {
            int size = shopcartItems.size();
            for (int i = 0; i < size; i++) {
                IShopcartItem item = shopcartItems.get(i);
                if (item != null) {
                    BigDecimal amount = item.getAmount();
                    if (amount != null) {
                        totalAmount = totalAmount.add(amount);
                    }
                }
            }
        }

        return totalAmount;
    }

    private static TradePlanActivity getTradePlanActivity(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule) {
                List<TradePlanActivity> tradePlanActivityList = tradeVo.getTradePlanActivityList();
        if (Utils.isNotEmpty(tradePlanActivityList)) {
            for (TradePlanActivity item : tradePlanActivityList) {
                if (item != null
                        && item.getStatusFlag() == StatusFlag.VALID
                        && item.getRuleEffective() == ActivityRuleEffective.VALID
                        && item.getRuleId() != null
                        && item.getRuleId().equals(salesPromotionRule.getId())) {
                    item.setClientUpdateTime(System.currentTimeMillis());
                    item.setChanged(true);
                    return item;
                }
            }
        }

        return buildTradePlanActivity(tradeVo, salesPromotionRule);
    }

    private static TradeItemPlanActivity getTradeItemPlanActivity(@NonNull IShopcartItemBase shopcartItem, @NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule,
                                                                  TradePlanActivity tradePlanActivity) {
                List<TradeItemPlanActivity> tradeItemPlanActivityList = tradeVo.getTradeItemPlanActivityList();
        if (Utils.isNotEmpty(tradeItemPlanActivityList)) {
            for (TradeItemPlanActivity item : tradeItemPlanActivityList) {
                if (item != null
                        && item.getStatusFlag() == StatusFlag.VALID
                        && item.getTradeItemUuid() != null
                        && item.getTradeItemUuid().equals(shopcartItem.getUuid())) {
                    if (tradePlanActivity != null) {
                        item.setRelId(tradePlanActivity.getRuleId());
                        item.setRelUuid(tradePlanActivity.getUuid());
                        item.setClientUpdateTime(System.currentTimeMillis());
                        item.setChanged(true);
                    }
                    return item;
                }
            }
        }

        return buildTradeItemPlanActivity(shopcartItem, tradeVo, salesPromotionRule, tradePlanActivity);
    }

    private static void bindQuantityTradeItemsPlanActivity(@NonNull List<IShopcartItem> shopcartItemList, @NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule,
                                                           TradePlanActivity tradePlanActivity, BigDecimal stackCount) {
        if (Utils.isEmpty(shopcartItemList)) {
            return;
        }

        List<IShopcartItem> tempShopcartItemList = new ArrayList<>();
        for (IShopcartItem item : shopcartItemList) {
            for (int i = 0; i < item.getSingleQty().intValue(); i++) {
                tempShopcartItemList.add(item);
            }
        }

        for (int i = 0; i < tempShopcartItemList.size(); i++) {
            if (i < stackCount.intValue() * salesPromotionRule.getLogicValue().intValue()) {
                getTradeItemPlanActivity(tempShopcartItemList.get(i), tradeVo, salesPromotionRule, tradePlanActivity);
            }
        }
    }

    private static void bindAmountTradeItemsPlanActivity(@NonNull List<IShopcartItem> shopcartItemList, @NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule,
                                                         TradePlanActivity tradePlanActivity) {
        if (Utils.isEmpty(shopcartItemList)) {
            return;
        }

        for (int i = 0; i < shopcartItemList.size(); i++) {
            getTradeItemPlanActivity(shopcartItemList.get(i), tradeVo, salesPromotionRule, tradePlanActivity);
        }
    }


    private static TradePlanActivity buildTradePlanActivity(@NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule) {
        TradePlanActivity planActivity = new TradePlanActivity();
        planActivity.setStatusFlag(StatusFlag.VALID);
        planActivity.setUuid(SystemUtils.genOnlyIdentifier());
        planActivity.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        planActivity.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        planActivity.setPlanId(salesPromotionRule.getPlanId());
        planActivity.setRuleId(salesPromotionRule.getId());
        Trade trade = tradeVo.getTrade();
        if (trade != null) {
            planActivity.setTradeId(trade.getId());
            planActivity.setTradeUuid(trade.getUuid());
        }
        planActivity.setRuleName(generateRuleName(salesPromotionRule));
        planActivity.setOfferValue(BigDecimal.ZERO);
        planActivity.setPlanUsageCount(0);
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

        private static String generateRuleName(SalesPromotionRule rule) {
        String ruleName = "";
        if (rule != null) {
            int ruleSubjectType = rule.getRuleSubjectType();
            BigDecimal logicValue = rule.getLogicValue();
            int policyDetailType = rule.getPolicyDetailType();
            BigDecimal policyValue1 = rule.getPolicyValue1();
            switch (ruleSubjectType) {
                case MATCH_QUANTITY_ONCE:
                    if (MULTIPLE_GOODS_FIXED_PRICE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_multiple_goods_fixed_price, MathDecimal.trimZero(logicValue), MathDecimal.toDecimalFormatString(policyValue1));
                    } else if (MULTIPLE_GOODS_REBATE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_multiple_goods_rebate, MathDecimal.trimZero(logicValue), MathDecimal.toDecimalFormatString(policyValue1));
                    } else if (MULTIPLE_GOODS_REBATE_LOWEST == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_multiple_goods_rebate_lowest, MathDecimal.trimZero(logicValue));
                    } else if (MULTIPLE_GOODS_DISCOUNT == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_multiple_goods_discount, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SINGLE_GOODS_FIXED_PRICE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_single_goods_fixed_price, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SINGLE_GOODS_REBATE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_single_goods_rebate, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SINGLE_GOODS_DISCOUNT == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_single_goods_discount, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SalesPromotionConstant.PolicyDetail.GIVE_GOODS == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_single_goods_give, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_quantity_once_single_goods_raise_price, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1), rule.getPolicyValue2());
                    }
                    break;
                case MATCH_AMOUNT_ONCE:
                    if (SPECIFIED_GOODS_REBATE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_amount_once_specified_goods_rebate, MathDecimal.toDecimalFormatString(logicValue), MathDecimal.toDecimalFormatString(policyValue1));
                    } else if (SPECIFIED_GOODS_DISCOUNT == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_amount_once_specified_goods_discount, MathDecimal.toDecimalFormatString(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SalesPromotionConstant.PolicyDetail.GIVE_GOODS == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_amount_once_single_goods_give, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1));
                    } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE == policyDetailType) {
                        ruleName = BaseApplication.sInstance.getString(R.string.sales_promotion_rule_name_match_amount_once_single_goods_raise_price, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(policyValue1), rule.getPolicyValue2());
                    }
                    break;
                default:
                    ruleName = rule.getPlanName();
                    break;
            }
        }

        return ruleName;
    }


    private static TradeItemPlanActivity buildTradeItemPlanActivity(@NonNull IShopcartItemBase item, @NonNull TradeVo tradeVo, @NonNull SalesPromotionRule salesPromotionRule,
                                                                    TradePlanActivity tradePlanActivity) {
        TradeItemPlanActivity tradeItemPlanActivity = new TradeItemPlanActivity();
        tradeItemPlanActivity.setStatusFlag(StatusFlag.VALID);
        tradeItemPlanActivity.setRuleId(salesPromotionRule.getId());
        tradeItemPlanActivity.setPlanId(salesPromotionRule.getPlanId());
        Trade trade = tradeVo.getTrade();
        if (trade != null) {
            tradeItemPlanActivity.setTradeId(trade.getId());
            tradeItemPlanActivity.setTradeUuid(trade.getUuid());
        }
        tradeItemPlanActivity.setTradeItemUuid(item.getUuid());
        tradeItemPlanActivity.setTradeItemId(item.getId());
        tradeItemPlanActivity.setChanged(true);
        tradeItemPlanActivity.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemPlanActivity.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        if (tradePlanActivity != null) {
            tradeItemPlanActivity.setRelUuid(tradePlanActivity.getUuid());
        }
        if (tradeVo.getTradeItemPlanActivityList() == null) {
            tradeVo.setTradeItemPlanActivityList(new ArrayList<TradeItemPlanActivity>());
        }
        tradeVo.getTradeItemPlanActivityList().add(tradeItemPlanActivity);
        return tradeItemPlanActivity;
    }


    public static void removeTradePlanActivity(List<TradePlanActivity> tradePlanList,
                                               List<TradeItemPlanActivity> tradeItemPlanList, Long planId) {
        if (Utils.isNotEmpty(tradePlanList)) {
            Iterator<TradePlanActivity> iterator = tradePlanList.iterator();
            while (iterator.hasNext()) {
                TradePlanActivity tradePlanActivity = iterator.next();
                if (tradePlanActivity != null
                        && tradePlanActivity.getPlanId() != null
                        && tradePlanActivity.getPlanId().equals(planId)) {
                    if (tradePlanActivity.getId() == null) {
                        iterator.remove();
                        removeTradeItemPlanActivity(tradeItemPlanList, tradePlanActivity.getUuid());
                    } else {
                        tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                        removeTradeItemPlanActivity(tradeItemPlanList, tradePlanActivity.getUuid());
                        tradePlanActivity.setChanged(true);
                    }

                    break;
                }
            }
        }
    }


    public static void removeTradePlanActivity(List<TradePlanActivity> tradePlanList,
                                               List<TradeItemPlanActivity> tradeItemPlanList, String tradePlanUuid) {
        if (Utils.isNotEmpty(tradePlanList)) {
            Iterator<TradePlanActivity> iterator = tradePlanList.iterator();
            while (iterator.hasNext()) {
                TradePlanActivity tradePlanActivity = iterator.next();
                if (tradePlanActivity != null
                        && !TextUtils.isEmpty(tradePlanActivity.getUuid())
                        && tradePlanActivity.getUuid().equals(tradePlanUuid)) {
                    if (tradePlanActivity.getId() == null) {
                        iterator.remove();
                        removeTradeItemPlanActivity(tradeItemPlanList, tradePlanActivity.getUuid());
                    } else {
                        tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                        removeTradeItemPlanActivity(tradeItemPlanList, tradePlanActivity.getUuid());
                        tradePlanActivity.setChanged(true);
                    }

                    break;
                }
            }
        }
    }


    private static void removeTradeItemPlanActivity(List<TradeItemPlanActivity> tradeItemPlanList, String tradePlanUuid) {
        if (Utils.isNotEmpty(tradeItemPlanList)) {
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
    }
}
