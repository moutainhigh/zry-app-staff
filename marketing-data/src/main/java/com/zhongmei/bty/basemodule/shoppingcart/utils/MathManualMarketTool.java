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

/**
 * 计算手动点击营销活动
 *
 * @date:2016年4月26日下午5:29:18
 */
public class MathManualMarketTool {

    // tradeplan和tradeitemPlanActivity的map
    private static Map<String, List<TradeItemPlanActivity>> itemPlanMap = null;

    // shopcartitem和tradeitemPlanActivity相关联的map
    private static Map<String, TradeItemPlanActivity> itemMap = null;

    // 订单中活动的map ruleId,TradePlanActivity
    private static Map<Long, TradePlanActivity> tradePlanMap = null;

    /**
     * @param selectedShopcartItems 购物车中的所有菜品
     * @Title: mathManualAddMarket
     * @Description: 正餐为拆单界面调用
     * @Param @param iShopcartItem 所有勾选的菜品,不管之前参加活动没有
     * @Param @param mTradeVo
     * @Param @param marketDishVo 营销活动规则的vo
     * @Return boolean 返回类型 是否计算成功，活动是否生效; true:计算活动成功,false:活动计算失败
     */
    public static boolean mathManualAddMarket(List<IShopcartItem> selectedShopcartItems, TradeVo mTradeVo,
                                              MarketRuleVo marketDishVo, boolean isDinner
    ) {
        return mathMarket(null, selectedShopcartItems, mTradeVo, marketDishVo, isDinner, DinnerShopManager.getInstance().isSepartShopCart());
    }

    /**
     * @Title: isCanAddMarket
     * @Description: 是否可以加入活动, 拆单界面调用
     * @Param @param selectedShopcartItems
     * @Param @param mTradeVo
     * @Param @param marketDishVo
     * @Param @param isDinner
     * @Param @return TODO
     * @Return boolean 返回类型
     */
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

        // 没有设置参与的菜品，不继续计算;特价另外处理
        if ((mDishList == null || mDishList.size() == 0) && (promotionType != PromotionType.SPECAILPRICE)
                && (activityRule.getAllDish() != 1)) {
            return false;
        }
        //拆单界面验证会员
        if (!validateMember(marketDishVo, isDinner, DinnerShopManager.getInstance().isSepartShopCart())) {
            // 针对会员的活动，没有会员
            return false;
        }
        // 单商品活动
        if (activityType == ActivityType.SINGLE) {
            // 要参与活动的数量
            BigDecimal totalCount = BigDecimal.ZERO;
            for (IShopcartItem shopcartItem : selectedShopcartItems) {
                totalCount = totalCount.add(shopcartItem.getSingleQty());
            }

            if (activityRule.getDishNum() == null) {
                return false;
            }

            DishShop dishShop = selectedShopcartItems.get(0).getDishShop();
            if (selectedShopcartItems.size() > 0 && dishShop != null && !dishShopCanAddMarket(marketDishVo, dishShop)) {
                //判断规则中是否包含该菜品
                return false;
            }

            if (totalCount.compareTo(activityRule.getDishNum()) < 0) {
                // 数量不足，不满足活动,解绑已经生成的tradePlanActivity
                return false;
            }

        } else {
            // 满减金额
            BigDecimal payment = activityRule.getPayment();
            if (payment == null) {
                return false;
            }
            // 商品优惠后的总金额
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
                // 不满足活动
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

        if (marketActivityRule.getAllDish() == 1) {//全部可用
            return true;
        } else if (marketActivityRule.getAllDish() == 2) {  //部分商品可用
            if (marketDishVo.isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        } else if (marketActivityRule.getAllDish() == 3) { //部分商品不可用
            if (!marketDishVo.isPlanActivityDishOrType(dishShop)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Title: mathMarket
     * @Description: 真正计算活动的类
     * @Param @param tradePlanActivity null是创建计算，非null是解绑计算
     * @Param @param selectedShopcartItems 当次活动选择的菜品item
     * @Param @param mTradeVo
     * @Param @param marketDishVo
     * @Param @param isDinner
     * @Param @return TODO
     * @Return boolean 返回类型
     */
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

        // 没有设置参与的菜品，不继续计算;特价另外处理
        if ((mDishList == null || mDishList.size() == 0) && (promotionType != PromotionType.SPECAILPRICE)
                && (activityRule.getAllDish() != 1)) {
            return false;
        }

        if (!validateMember(marketDishVo, isDinner, isSplitPage)) {
            // 针对会员的活动，没有会员
            unBindTradePlanByRuleId(mTradeVo.getTradePlanActivityList(),
                    mTradeVo.getTradeItemPlanActivityList(),
                    activityRule.getId(),
                    isDinner);
            return false;
        }
        convertTradePlanToMap(mTradeVo.getTradePlanActivityList());
        covertItemPlanListToMap(mTradeVo.getTradeItemPlanActivityList());
        // 活动优惠总额
        BigDecimal privilegeAmount = BigDecimal.ZERO;
        if (tradePlanActivity == null) {
            tradePlanActivity = isHasTradePlan(activityRule.getId());
        }
        // 是否新建
        boolean isNew = (tradePlanActivity == null);
        // 单商品活动
        if (activityType == ActivityType.SINGLE) {
            // 要参与活动的数量
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
                // 数量不足，不满足活动,解绑已经生成的tradePlanActivity
                if (tradePlanActivity != null) {
                    unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                            mTradeVo.getTradeItemPlanActivityList(),
                            tradePlanActivity.getUuid(),
                            isDinner);
                }
                return false;
            }

            // 循环生效次数,抹小数
//			BigDecimal cycleCount = MathDecimal.divDown(totalCount, activityRule.getDishNum(), 0);
            BigDecimal cycleCount = BigDecimal.ONE;//v7.3单商品、多商品循环生效次数都为1
            if (isNew) {
                tradePlanActivity =
                        buildPlanActivity(mTradeVo, activityRule, privilegeAmount, cycleCount, promotionType);
            }
            switch (promotionType) {
                case MINUS:// 立减
                    //菜品的总价
                    BigDecimal totalOriginalAmount = BigDecimal.ZERO;
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
                case DISCOUNT:// 折扣
                    tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                    mathMarketDiscount(selectedShopcartItems, mTradeVo, tradePlanActivity, activityRule, isNew);
                    break;
                case GIFT:// 赠送
                    if (isDinner) {
                    } else {
                        // 快餐自动带入赠送的商品
                    }
                    break;
                case SPECAILPRICE:// 特价
                    for (IShopcartItem item : selectedShopcartItems) {
                        if (itemMap.get(item.getUuid()) == null) {
                            buildItemPlanActivity(item, mTradeVo, activityRule, tradePlanActivity);
                        }
                        privilegeAmount = privilegeAmount.add(mathItemSpecail(item));
                    }
                    // 查询特价
                    // 合并价格
                    break;

                default:
                    break;
            }
        } else {
            // 满减金额
            BigDecimal payment = activityRule.getPayment();
            if (payment == null) {
                return false;
            }
            BigDecimal cycleCount = BigDecimal.ONE;//v7.3单商品、多商品循环生效次数都为1
            // 商品优惠后的总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (IShopcartItem item : selectedShopcartItems) {
                if (item.getPrivilege() != null && item.getPrivilege().isValid()) {
                    BigDecimal currentAmount = item.getActualAmount().add(item.getPrivilege().getPrivilegeAmount());
                    totalAmount = totalAmount.add(currentAmount);
                } else {
                    totalAmount = totalAmount.add(item.getActualAmount());
                }
            }
            if (totalAmount.compareTo(payment) < 0) {
                // 不满足活动了，解绑活动
                if (tradePlanActivity != null) {
                    unBindTradePlanByTradePlanUuid(mTradeVo.getTradePlanActivityList(),
                            mTradeVo.getTradeItemPlanActivityList(),
                            tradePlanActivity.getUuid(),
                            isDinner);
                }
                return false;
            }
//			cycleCount = MathDecimal.divDown(totalAmount, payment, 0);
            switch (promotionType) {
                case MINUS:// 满减
                    privilegeAmount = cycleCount.multiply(activityRule.getReduce());
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
                case DISCOUNT:// 折扣
                    if (isNew) {
                        tradePlanActivity =
                                buildPlanActivity(mTradeVo, activityRule, privilegeAmount, cycleCount, promotionType);
                    }
                    tradePlanActivity.setPlanUsageCount(cycleCount.intValue());
                    mathMarketDiscount(selectedShopcartItems, mTradeVo, tradePlanActivity, activityRule, isNew);
                    break;
                case GIFT:// 赠送

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

    /**
     * @Title: getPrivilegeAfterPrice
     * @Description: 活动优惠后的价格
     * @Param @param item
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
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

    /**
     * @Title: unbindUnSelectedItem
     * @Description: 解绑加入的活动对应的未选择的菜品
     * @Param @param tradePlanActivity
     * @Param @param shopcartItemList TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: mathItemSpecail
     * @Description: 商品特价计算
     * @Param @param item
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private static BigDecimal mathItemSpecail(IShopcartItem item) {
        MarketDal marketDal = OperatesFactory.create(MarketDal.class);
        DishMemberPrice dishMemberPrice = marketDal.queryDishSpecailPrice(item.getDishShop().getBrandDishId());
        BigDecimal itemPrivilegeAmount = BigDecimal.ZERO;
        TradePrivilege tradePrivilege = item.getPrivilege();
        if (dishMemberPrice != null) {
            if (dishMemberPrice.getPriceType() == 1) {
                // 特价折扣
                if (tradePrivilege != null && tradePrivilege.isValid()) {
                    // 有优惠时计算方式,用会员优惠后的价格计算
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(getPrivilegeAfterPrice(item)
                            .multiply(MathDecimal.getDiscountValueTen(dishMemberPrice.getDiscount())), 2));
                } else {
                    // 无优惠时，用商品的价格计算
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(dishMemberPrice.getDiscount())),
                            2));
                }
            } else {
                // 特价价格
                itemPrivilegeAmount = item.getActualAmount().subtract(new BigDecimal(dishMemberPrice.getMemberPrice()));
            }
        }
        return itemPrivilegeAmount;
    }

    /**
     * @Title: mathMarketDiscount
     * @Description: 单商品或多商品折扣计算
     * @Param @param selectedShopcartItems
     * @Param @param mTradeVo
     * @Param @param tradePlanActivity
     * @Param @param activityRule TODO
     * @Return void 返回类型
     */
    private static void mathMarketDiscount(List<IShopcartItem> selectedShopcartItems, TradeVo mTradeVo,
                                           TradePlanActivity tradePlanActivity, MarketActivityRule activityRule, boolean isNew
    ) {
        BigDecimal privilegeAmount = BigDecimal.ZERO;
        // v8.7.0 修改营销方案，打折个数限制
        BigDecimal maxCount = activityRule.getDishNum();
        int count = 0;
        for (IShopcartItem item : selectedShopcartItems) {
            TradePrivilege tradePrivilege = item.getPrivilege();
            count += item.getSingleQty().intValue();
            BigDecimal itemPrivilegeAmount = BigDecimal.ZERO;
            if (tradePrivilege != null && tradePrivilege.isValid()) {
                BigDecimal itemPrice = BigDecimal.ZERO;
                // 有优惠时计算方式,用会员优惠后的价格计算
                itemPrice = item.getActualAmount();
                itemPrice = itemPrice.add(tradePrivilege.getPrivilegeAmount());
                // v8.7.0 超过多商品折扣数量限制
                // 单品价格
                BigDecimal price = item.getPrice();
                // 当前总数量
                BigDecimal counts = new BigDecimal(count);
                // 计算金额
                if (maxCount == null) {
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                } else {
                    // 数量多于限制或者等于限制时
                    if ((privilegeAmount.compareTo(BigDecimal.ZERO) == 0 && maxCount.compareTo(counts) == -1) || maxCount.compareTo(counts) == 0) {
                        // 计算打折金额
                        itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal
                                .round(price.multiply(maxCount).multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                    } else {
                        itemPrivilegeAmount = itemPrice;
                    }
                }
            } else {
                // v8.7.0 超过多商品折扣数量限制
                // 单品价格
                BigDecimal price = item.getPrice();
                // 当前总数量
                BigDecimal counts = new BigDecimal(count);
                // 计算金额
                if (maxCount == null) {
                    itemPrivilegeAmount = MathDecimal.trimZero(MathDecimal.round(
                            item.getActualAmount().multiply(MathDecimal.getDiscountValueTen(activityRule.getDiscount())), 2));
                } else {
                    // 数量多于限制或者等于限制时
                    if ((privilegeAmount.compareTo(BigDecimal.ZERO) == 0 && maxCount.compareTo(counts) == -1) || maxCount.compareTo(counts) == 0) {
                        // 计算打折金额
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

    /**
     * @Title: mathMarketPlan
     * @Description: 此方法主要用于拆单界面的计算
     * @Param @param tradeVo
     * @Param @param shopcartItemList
     * @Param @param isDinner TODO
     * @Return void 返回类型
     */
    public static void mathMarketPlanSplit(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, boolean isDinner) {
        mathMarketPlan(tradeVo, shopcartItemList, isDinner, true);
    }

    /**
     * @param isDinner    是否是正餐O
     * @param isSplitPage 是否是拆单界面 true：为拆单界面 false：不为
     * @Title: mathMarketPlan
     * @Description: 验证tradevo活动的解绑和重新计算活动的值, 不满足时如果已在服务器中保存过statusFlag设为invilad，否则删除 会员登录、登出 拆单后都需要验证,活动是否已经失效验证; 从点菜界面进入结算界面也要重新计算活动的解绑
     * @Param @param shopcartItemList 当前单据中所有的菜品
     * @Return BigDecimal 返回类型 订单中活动的总金额
     */
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
        // 订单菜品缓存map,减少比较次数
        Map<String, IShopcartItem> shopcartItemMap = new HashMap<String, IShopcartItem>();
        for (IShopcartItem shopcartItem : shopcartItemList) {
            shopcartItemMap.put(shopcartItem.getUuid(), shopcartItem);
        }

        for (int i = tradePlanList.size() - 1; i >= 0; i--) {
            TradePlanActivity tradePlanActivity = tradePlanList.get(i);

            if (!validateTradePlan(tradePlanActivity, isDinner, isSplitPage)) {
                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
            }
            // 一个活动下的IShopcartItem
            List<IShopcartItem> mixShopcartItemList = new ArrayList<IShopcartItem>();
            List<TradeItemPlanActivity> itemPlanList = itemPlanMap.get(tradePlanActivity.getUuid());
            // 有tradePlanActivity
            // 无TradeItemPlanActivity时，解绑活动
            if (itemPlanList == null || itemPlanList.size() == 0) {
                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
                return;
            }

            MarketRuleVo marketDishVo = MatketDishManager.getMarketDishVoByRule(tradePlanActivity.getRuleId());
            if (marketDishVo == null || !marketDishVo.IsEnableCurrent()) {
                // 活动失效了解绑
                unBindTradePlanByTradePlanUuid(tradePlanList, tradeItemPlanList, tradePlanActivity.getUuid(), isDinner);
                continue;
            }

            // 查找一个活动中的IShopcartItem
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

    /**
     * @Title: validateTradePlan
     * @Description: 验证添加的活动是否有效
     * @Param @param tradePlanActivity TODO
     * @Return boolean 类型 true:有效，false：无效
     */
    public static boolean validateTradePlan(TradePlanActivity tradePlanActivity, boolean isDinner, boolean isSplitPage) {
        MarketRuleVo marketRuleVo = MatketDishManager.getMarketDishVoByRule(tradePlanActivity.getRuleId());
        // 过期
        if (marketRuleVo == null) {
            return false;
        }

        if (!validateMember(marketRuleVo, isDinner, isSplitPage)) {
            return false;
        }
        return true;
    }

    /**
     * @param isSplitPage 是否是添加活动操作
     * @Title: validateMember
     * @Description: 验证活动是否只针对会员
     * @Param @param tradePlanActivity
     * @Param @param marketRuleVo
     * @Param @param isDinner
     * @Param @return TODO
     * @Return boolean 返回类型 true：只针对会员 false:所有
     */
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
            // 针对会员的活动，而会员登出了，活动失效
            if (marketRuleVo.getUserTypes().contains(UserType.MEMBER)
                    && !(marketRuleVo.getUserTypes().contains(UserType.MEMBERNON))) {
                return false;
            }
        } else {
            //只是针对非会员的活动,又登录了会员，解绑活动
            if (marketRuleVo.getUserTypes().contains(UserType.MEMBERNON) && marketRuleVo.getUserTypes().size() == 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * @Title: removeTradePlan
     * @Description: 解绑指定tradePlanUuid的活动
     * @Param @param tradePlanUuid
     * @Param @param isDinner TODO
     * @Return void 返回类型
     */
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
                // 未保存到服务器过
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

    /**
     * @Title: unBindTradePlanByRuleId
     * @Description: 通过ruleId解绑活动
     * @Param @param tradePlanList
     * @Param @param tradeItemPlanList
     * @Param @param ruleId 活动规则id
     * @Param @param isDinner TODO
     * @Return void 返回类型
     */
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
                // 未保存到服务器过
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

    /**
     * @Title: removeTradePlanItemById
     * @Description: 将tradeItemPlanList中指定plan 删除或者设为无效
     * @Param @param tradeItemPlanList
     * @Param @param tradePlanUuid tradePlanActivity 表的uuid
     * @Return void 返回类型
     */
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

    /**
     * @Title: removeTradePlanItemByItemUuid
     * @Description: 通过tradeItemUuid 移除tradeItemPlanList中的关联关系
     * @Param @param tradeItemPlanList
     * @Param @param tradeItemUuid TODO
     * @Return void 返回类型
     */
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

    /**
     * @param shopcartItemList 订单中所有菜品的list
     * @Title: removeShopcartItem
     * @Description: 移除购物车菜品时，重新计算、解绑活动，只针对拆单购物车和快餐购物车中有活动时
     * @Param @param tradeVo
     * @Param @param iShopcartItem
     * @Param @param isDinner TODO
     * @Return void 返回类型
     */
    public static void removeShopcartItem(TradeVo tradeVo, List<IShopcartItem> shopcartItemList,
                                          IShopcartItem iShopcartItem, boolean isDinner, boolean isSplitPage
    ) {
        List<TradeItemPlanActivity> tradeItemPlanList = tradeVo.getTradeItemPlanActivityList();
        List<TradePlanActivity> tradePlanList = tradeVo.getTradePlanActivityList();
        if (tradeItemPlanList == null || tradeItemPlanList.size() == 0) {
            return;
        }

        if (shopcartItemList == null || shopcartItemList.size() == 0) {
            // 如果订单中菜品移除完,把所有活动移除或者置为无效
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
        // 查找tradeitem对应的tradePlanActivity
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
            // 活动失效了,移除活动
            unBindTradePlanByRuleId(tradePlanList, tradeItemPlanList, mTradeplan.getRuleId(), isDinner);
            return;
        }
        // 一个活动下的IShopcartItem
        List<IShopcartItem> mixShopcartItemList = new ArrayList<IShopcartItem>();
        List<TradeItemPlanActivity> itemPlanList = itemPlanMap.get(mTradeplan.getUuid());
        if (itemPlanList == null) {
            return;
        }
        // 订单菜品缓存map,减少比较次数
        Map<String, IShopcartItem> shopcartItemMap = new HashMap<String, IShopcartItem>();
        for (IShopcartItem shopcartItem : shopcartItemList) {
            shopcartItemMap.put(shopcartItem.getUuid(), shopcartItem);
        }

        // 查找一个活动中的IShopcartItem
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

    /**
     * @Title: removeAllTradePlan
     * @Description: 移除所有的订单活动或者置为无效
     * @Param @param tradePlanList TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: buildItemPlanActivity
     * @Description: 构建tradeItem和活动的关系
     * @Param @param item
     * @Param @param tradeVo
     * @Param @param activityRule TODO
     * @Return void 返回类型
     */
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

    /**
     * @param cycleCount 循环生效次数 默认为1
     * @Title: buildPlanActivity
     * @Description: 保存trade和活动的关系
     * @Param @param item
     * @Param @param tradeVo
     * @Param @param activityRule TODO
     * @Return void 返回类型
     */
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
            case MINUS: // 满减
                ruleName = ruleName + "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.trimZero(rule.getReduce());
                break;
            case DISCOUNT: // 折扣
                ruleName = ruleName + MathDecimal.trimZero(rule.getDiscount()) + BaseApplication.sInstance.getString(R.string.discount1);
                break;
            case GIFT: // 赠送
                ruleName = ruleName + BaseApplication.sInstance.getString(R.string.give);
                break;
            case SPECAILPRICE: // 特价
                ruleName = ruleName + BaseApplication.sInstance.getString(R.string.special_price);
                break;
            default:
                break;
        }
        return ruleName;
    }

    /**
     * @Title: covertItemPlanListToMap
     * @Description: 将list转换成map
     * @Param @param tradeItemPlanList
     * @Param @return TODO
     * @Return Map<String       ,       TradeItemPlanActivity> 返回类型
     */
    private static void covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        itemPlanMap = new HashMap<String, List<TradeItemPlanActivity>>();
        itemMap = new HashMap<String, TradeItemPlanActivity>();
        // 将已保存的活动list转为map方便获取
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

    /**
     * @Title: isHasTradePlan
     * @Description: 订单中是否有活动
     * @Param @param ruleId
     * @Param @return TODO
     * @Return TradePlanActivity 返回类型
     */
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

    /**
     * @Title: removeAllActivity
     * @Description: 移除所有的活动包含活动下的tradeItemPlanActivity
     * @Param @param tradeVo TODO
     * @Return void 返回类型
     */
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
        // 没有数据了，将list置null
        if (tradePlanList != null && tradePlanList.isEmpty()) {
            tradePlanList = null;
        }

        if (tradeItemPlanList != null && tradeItemPlanList.isEmpty()) {
            tradeItemPlanList = null;
        }
    }

    /**
     * @Title: isHasTradePlan
     * @Description: 订单中是否有优惠活动
     * @Param @param tradeVo
     * @Param @return TODO
     * @Return boolean 返回类型
     */
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

    /**
     * 菜品是否可以参与指定的营销活动
     *
     * @param shopcartItemBase 菜品
     * @param ruleVo           指定的营销活动规则
     * @return
     */
    public static boolean isItemCanJoinPlanActivity(IShopcartItemBase shopcartItemBase, MarketRuleVo ruleVo) {
        // 菜品被删除或者，循环菜单过期dishshop为null，有单品优惠不能参加优惠活动
        if (shopcartItemBase != null && shopcartItemBase.getDishShop() != null
                && ruleVo.isDishEnableUsed(shopcartItemBase.getDishShop())
                && !DiscountTool.isSinglePrivilege(shopcartItemBase)) {

            return true;
        } else {
            return false;
        }
    }

}
