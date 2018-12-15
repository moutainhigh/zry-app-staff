package com.zhongmei.bty.basemodule.trade.manager;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp.Card;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.event.ActionLoginEvent;
import com.zhongmei.bty.commonmodule.database.enums.AuthType;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 结算收银manager
 *
 * @Date：2016-3-15 下午3:56:10
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class DinnerCashManager {

    public static final String CUSTOMERLOGIN = "customerlogin";

    public static final String SOURCE = "source";

    public static final String CARDS = "cards";

    public static final String INTEGRAL = "integral";

    public static final String COUPON = "coupon";

    public static final String ITEMS = "items";

    /**
     * 拷贝原单的整单折扣到拆单购物车
     *
     * @Title: copyDinnerDiscountToSeparator
     * @Return void 返回类型
     */
    public void copyDinnerDiscountToSeparator() {
        DinnerShoppingCart dinnerShoppingCart = DinnerShoppingCart.getInstance();
        TradeVo tradeVo = dinnerShoppingCart.createOrder(dinnerShoppingCart.getShoppingCartVo(), false);
        if (tradeVo != null && tradeVo.getTradePrivileges() != null) {
            for (TradePrivilege oldPrivilege : tradeVo.getTradePrivileges()) {
                if (oldPrivilege.getStatusFlag() == StatusFlag.VALID
                        && TextUtils.equals(tradeVo.getTrade().getUuid(), oldPrivilege.getTradeUuid())
                        && (oldPrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT
                        || oldPrivilege.getPrivilegeType() == PrivilegeType.REBATE
                        || oldPrivilege.getPrivilegeType() == PrivilegeType.FREE
                        || oldPrivilege.getPrivilegeType() == PrivilegeType.MARKTING)) {
                    TradePrivilege privilege = new TradePrivilege();
                    privilege.validateCreate();
                    privilege.setPrivilegeAmount(oldPrivilege.getPrivilegeAmount());
                    privilege.setPrivilegeType(oldPrivilege.getPrivilegeType());
                    privilege.setPrivilegeValue(oldPrivilege.getPrivilegeValue());
                    privilege.setPrivilegeName(oldPrivilege.getPrivilegeName());

                    // 获取免单理由
                    Reason reason = null;
                    if (Utils.isNotEmpty(tradeVo.getTradeReasonRelList())) {
                        for (TradeReasonRel reasonRel : tradeVo.getTradeReasonRelList()) {
                            if ((reasonRel.getOperateType() == OperateType.TRADE_DINNER_FREE || reasonRel.getOperateType() == OperateType.TRADE_DISCOUNT) && reasonRel.getStatusFlag() == StatusFlag.VALID) {
                                reason = new Reason();
                                reason.setContent(reasonRel.getReasonContent());
                                reason.setId(reasonRel.getId());
                                break;
                            }
                        }
                    }
                    SeparateShoppingCart.getInstance().setDefineTradePrivilege(privilege, reason, false, false);
                }
            }
        }
    }

    /**
     * 设置拆单购物车的会员
     *
     * @param customer
     * @param isAddMemberPrivilige 是否添加会员折扣
     */
    private void setSeprateTradeCustomer(CustomerResp customer, boolean isAddMemberPrivilige) {
        CustomerManager.getInstance().setSeparateLoginCustomer(customer);// 把原单的会员设置给拆单
        if (isAddMemberPrivilige) {
            TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customer);
            if (customer.card == null) {
                if (customer.isMember()) {
                    tradeCustomer.setCustomerType(CustomerType.MEMBER);
                } else {
                    tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                }
            } else {
                tradeCustomer.setCustomerType(CustomerType.CARD);
                tradeCustomer.setEntitycardNum(customer.card.getCardNum());
            }
            SeparateShoppingCart.getInstance().setSeparateCustomer(tradeCustomer);// 把正餐的会员设置给拆单
            SeparateShoppingCart.getInstance().memberPrivilege(false, false);
        }
    }

    /**
     * 拷贝原单的积分抵现和优惠券给拆单购物车
     *
     * @Title: copyDinnerIntegralAndCouponsToSeparator
     * @Return void 返回类型
     */
    private void copyDinnerIntegralAndCouponsToSeparator(CustomerResp customer) {
        DinnerShoppingCart dinnerShoppingCart = DinnerShoppingCart.getInstance();
        if (!customer.isDisabled() && dinnerShoppingCart.getOrder() != null) {
            // 拷贝积分抵现
            if (dinnerShoppingCart.getOrder().getIntegralCashPrivilegeVo() != null && dinnerShoppingCart.getOrder().getIntegralCashPrivilegeVo().isValid()) {
                Long integral = customer.integral;
                IntegralCashPrivilegeVo separateIcpv = new IntegralCashPrivilegeVo();
                separateIcpv.setIntegral(new BigDecimal(integral == null ? -1 : integral));
                separateIcpv.setRule(customer.customerLevelRights);
                SeparateShoppingCart.getInstance().setIntegralCash(separateIcpv, true, false);
            }

            copyCouponToSeparator(dinnerShoppingCart);
        }
    }

    private void copyCouponToSeparator(DinnerShoppingCart dinnerShoppingCart) {
        // 拷贝优惠券
        List<CouponPrivilegeVo> couponPrivilegeList = dinnerShoppingCart.getOrder().getCouponPrivilegeVoList();
        if (Utils.isNotEmpty(couponPrivilegeList))
            for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeList) {
                if (couponPrivilegeVo.isValid()) {

                    CouponPrivilegeVo separateCpv = new CouponPrivilegeVo();
                    separateCpv.setCoupon(couponPrivilegeVo.getCoupon());
//                    separateCpv.setCoupRuleList(couponPrivilegeVo.getCoupRuleList());
                    TradePrivilege tradePrivilege = new TradePrivilege();
                    tradePrivilege.validateCreate();
                    tradePrivilege.setPrivilegeType(PrivilegeType.COUPON);
                    tradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);// 默认优惠金额0
                    tradePrivilege.setPrivilegeValue(BigDecimal.ZERO);
                    if (couponPrivilegeVo.getTradePrivilege() != null) {
                        tradePrivilege.setPromoId(couponPrivilegeVo.getTradePrivilege().getPromoId());
                        tradePrivilege.setPrivilegeValue(couponPrivilegeVo.getTradePrivilege().getPrivilegeValue());
                    }

                    separateCpv.setTradePrivilege(tradePrivilege);

                    SeparateShoppingCart.getInstance().setCouponPrivilege(separateCpv, false, false);
                }
            }
    }


    /**
     * 拷贝原单的积分抵现和优惠券给拆单购物车
     *
     * @Title: copyDinnerIntegralAndCouponsToSeparator
     * @Return void 返回类型
     */
    private void copyDinnerIntegralAndCouponsToSeparator(EcCard card) {
        DinnerShoppingCart dinnerShoppingCart = DinnerShoppingCart.getInstance();
        if (card != null && dinnerShoppingCart.getOrder() != null) {
            // 拷贝积分抵现
            IntegralCashPrivilegeVo integralCashPrivilegeVo = dinnerShoppingCart.getOrder().getIntegralCashPrivilegeVo();
            if (integralCashPrivilegeVo != null && integralCashPrivilegeVo.isValid()) {
                IntegralCashPrivilegeVo separateIcpv = new IntegralCashPrivilegeVo();
                Long integral = null;
                if (card.getIntegralAccount() != null) {
                    integral = card.getIntegralAccount().getIntegral();
                }
                separateIcpv.setIntegral(new BigDecimal(integral == null ? -1 : integral));
                if (card.getCardLevelSetting() != null) {
                    separateIcpv.setRule(card.getCardLevelSetting());
                }
                SeparateShoppingCart.getInstance().setIntegralCash(separateIcpv, false, false);
            }

            // 拷贝优惠券(实体卡没有，暂时不需要拷贝)
            copyCouponToSeparator(dinnerShoppingCart);
        }
    }

    /**
     * 复制宴请数据
     */
    public void copyBanquetToSeparator() {
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
            TradeReasonRel reasonRel = tradeVo.getOperateReason(OperateType.TRADE_BANQUET);
            Reason reason = null;
            if (reasonRel != null) {
                reason = new Reason();
                reason.setContent(reasonRel.getReasonContent());
                reason.setId(reasonRel.getId());
            }
            SeparateShoppingCart.getInstance().doBanquet(reason);
        }
    }

    /**
     * 拷贝原单附加费到拆单购物车
     *
     * @Title: copyDinnerExtraToSparator
     * @Return void 返回类型
     */
    @SuppressLint("UseSparseArrays")
    public void copyDinnerExtraToSparator() {
        // 反结账不复制
        if (DinnerShoppingCart.getInstance().isReturnCash()) {
            return;
        }
        Map<Long, ExtraCharge> extraChargeMap = DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap();
        List<ExtraCharge> tempList = ExtraManager.getAutoOrderExtraMap(DinnerShoppingCart.getInstance().getOrder(), extraChargeMap, true);
        if (tempList != null) {
            SeparateShoppingCart.getInstance().addExtraCharge(tempList, false, false);
        }
    }

    /**
     * 拷贝原单会员或实体卡相关信息和优惠到拆单购物车（会员折扣，会员，积分抵现，优惠券等）
     *
     * @Title: copyDinnerCustomerInfosToSeparator
     * @Return Object
     * 返回类型（返回类型：customer表示会员登录，eccard表示实体卡登录，
     * null表示未登录）
     */
    public CustomerResp copyDinnerCustomerInfosToSeparator() {
        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
        if (customer != null) {
            if (customer.card == null) {
                if (!customer.isDisabled()) {
                    setSeprateTradeCustomer(customer, true);
                    copyDinnerIntegralAndCouponsToSeparator(customer);

                    return customer;
                } else {
                    CustomerManager.getInstance().setSeparateLoginCustomer(null);
                    SeparateShoppingCart.getInstance().setSeparateCustomer(null);

                    ToastUtil.showShortToast(R.string.customer_disable);
                }
            } else {
                EcCard card = customer.card;
                setSeprateTradeCustomer(customer, true);
                copyDinnerIntegralAndCouponsToSeparator(card);

                return customer;
            }
        } else {
            CustomerManager.getInstance().setSeparateLoginCustomer(null);
            SeparateShoppingCart.getInstance().setSeparateCustomer(null);
        }

        return null;
    }

    /**
     * @Title: copyMarketActivityToSeparator
     * @Description: copy原单的活动到拆单
     * @Param TODO
     * @Return void 返回类型
     */
    public void copyMarketActivityToSeparator() {
        List<TradePlanActivity> tradePlanList = DinnerShoppingCart.getInstance().getOrder().getTradePlanActivityList();
        List<TradeItemPlanActivity> tradeItemPlanList = DinnerShoppingCart.getInstance().getOrder().getTradeItemPlanActivityList();
        List<TradePlanActivity> nTradePlanList = null;
        List<TradeItemPlanActivity> nTradeItemPlanList = null;
        TradeVo splitTradeVo = SeparateShoppingCart.getInstance().getOrder();
        if (tradePlanList != null && !tradePlanList.isEmpty()) {
            nTradePlanList = new ArrayList<TradePlanActivity>();
            for (TradePlanActivity tradePlanActivity : tradePlanList) {
                if (tradePlanActivity.getStatusFlag() == StatusFlag.INVALID || tradePlanActivity.getRuleEffective() == ActivityRuleEffective.INVALID) {
                    continue;
                }
                TradePlanActivity newPlanActivity = new TradePlanActivity();
                try {
                    newPlanActivity = tradePlanActivity.copyTradePlanActivity(tradePlanActivity, newPlanActivity);
//					newPlanActivity.setTradeId(splitTradeVo.getTrade().getId());
                    newPlanActivity.setTradeUuid(splitTradeVo.getTrade().getUuid());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (newPlanActivity != null) {
                    nTradePlanList.add(newPlanActivity);
                }
            }
        }
        List<IShopcartItem> splitItemList = SeparateShoppingCart.getInstance().mergeShopcartItem(SeparateShoppingCart.getInstance().getShoppingCartVo());
        Map<String, IShopcartItem> itemMap = new HashMap<String, IShopcartItem>();
        if (splitItemList != null && !splitItemList.isEmpty()) {
            for (IShopcartItem item : splitItemList) {
                itemMap.put(item.getRelateTradeItemUuid(), item);
            }
        }
        if (tradeItemPlanList != null && !tradeItemPlanList.isEmpty()) {
            nTradeItemPlanList = new ArrayList<TradeItemPlanActivity>();
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanList) {
                if (tradeItemPlanActivity.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                TradeItemPlanActivity newItemPlanActivity = new TradeItemPlanActivity();
                try {
                    newItemPlanActivity = tradeItemPlanActivity.copyTradeItemPlanActivity(tradeItemPlanActivity, newItemPlanActivity);
                    IShopcartItem item = itemMap.get(newItemPlanActivity.getTradeItemUuid());
                    if (item != null) {
//						新单无id，先保留原单的
//						newItemPlanActivity.setTradeId(splitTradeVo.getTrade().getId());
//						newItemPlanActivity.setTradeItemId(item.getId());

                        newItemPlanActivity.setTradeUuid(splitTradeVo.getTrade().getUuid());
                        newItemPlanActivity.setTradeItemUuid(item.getUuid());
                        if (newItemPlanActivity != null) {
                            nTradeItemPlanList.add(newItemPlanActivity);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        //放到拆单购物车
        SeparateShoppingCart.getInstance().setTradeActivity(nTradePlanList, nTradeItemPlanList);

    }

    /**
     * 自动登录来源跳转 , 不能重置会员需要从购物车获取重新匹配
     *
     * @param customer
     * @param cards    卡列表
     */
    public void jumpAfterLoginByAuth(String source, CustomerResp customer, List<Card> cards) {
        if (TextUtils.isEmpty(source)) {
            source = ITEMS;
        }
        DinnerShopManager.getInstance().setLoginCustomer(customer);// 设置拆单会员
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customer);
        if (customer.card == null) {
            if (customer.isMember())
                tradeCustomer.setCustomerType(CustomerType.MEMBER);
            else
                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
        } else {
            tradeCustomer.setCustomerType(CustomerType.CARD);
            tradeCustomer.setEntitycardNum(customer.card.getCardNum());
        }
        //获取之前的tradecustomer，把id和uuid等值赋值给新的tradecustomer，保证服务器不会重复存储同类型的tradecustomer
        TradeVo tradeVo = DinnerShopManager.getInstance().getShoppingCart().getOrder();
        TradeCustomer oldTradeCustomer = new DinnerCashManager().getTradeCustomer(tradeVo, tradeCustomer.getCustomerType());
        if (oldTradeCustomer != null) {
            tradeCustomer.setId(oldTradeCustomer.getId());
            tradeCustomer.setUuid(oldTradeCustomer.getUuid());
            tradeCustomer.setTradeId(oldTradeCustomer.getTradeId());
            tradeCustomer.setTradeUuid(oldTradeCustomer.getTradeUuid());
            tradeCustomer.setServerUpdateTime(oldTradeCustomer.getServerUpdateTime());
            tradeCustomer.setServerCreateTime(oldTradeCustomer.getServerCreateTime());
        }
        if (DinnerShopManager.getInstance().isSepartShopCart()) {
            SeparateShoppingCart.getInstance().setSeparateCustomer(tradeCustomer);
        } else {
            DinnerShoppingCart.getInstance().setDinnerCustomer(tradeCustomer);
        }

        DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(true, true);

        if (Utils.isNotEmpty(cards)) {
            ActionDinnerPrilivige action = new ActionDinnerPrilivige(DinnerPriviligeType.SWITCH_CARD);
            action.setCards(cards);
            action.setSource(source);
            EventBus.getDefault().post(action);
        } else {
            if (TextUtils.equals(source, INTEGRAL)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.INTEGRAL));
            } else if (TextUtils.equals(source, COUPON)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.COUPON));
            } else if (TextUtils.equals(source, ITEMS)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
            }
        }
        if (customer != null) {
            EventBus.getDefault().post(new ActionLoginEvent());
        }
    }

    /**
     * 登录成功后，根据来源参数跳转
     *
     * @param customer
     */
    public void jumpAfterLogin(String source, CustomerResp customer, List<Card> cards) {
        if (TextUtils.isEmpty(source)) {
            source = ITEMS;
        }
        DinnerShopManager.getInstance().setLoginCustomer(customer);// 设置拆单会员
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customer);
        if (customer.card == null) {
            if (customer.isMember())
                tradeCustomer.setCustomerType(CustomerType.MEMBER);
            else
                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
        } else {
            tradeCustomer.setCustomerType(CustomerType.CARD);
            tradeCustomer.setEntitycardNum(customer.card.getCardNum());
        }
        if (DinnerShopManager.getInstance().isSepartShopCart()) {
            SeparateShoppingCart.getInstance().setSeparateCustomer(tradeCustomer);
        } else {
            DinnerShoppingCart.getInstance().setDinnerCustomer(tradeCustomer);
        }

        DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(true, true);

        if (Utils.isNotEmpty(cards)) {
            ActionDinnerPrilivige action = new ActionDinnerPrilivige(DinnerPriviligeType.SWITCH_CARD);
            action.setCards(cards);
            action.setSource(source);
            EventBus.getDefault().post(action);
        } else {
            if (TextUtils.equals(source, INTEGRAL)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.INTEGRAL));
            } else if (TextUtils.equals(source, COUPON)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.COUPON));
            } else if (TextUtils.equals(source, ITEMS)) {
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
            }
        }
        if (customer != null) {
            EventBus.getDefault().post(new ActionLoginEvent());
        }
    }

    /**
     * 查询某个菜品是否参与营销活动
     *
     * @param tipaFinder
     * @param shopcartItem
     * @return
     */
    public static boolean hasMarketActivity(Map<String, TradeItemPlanActivity> tipaFinder, IShopcartItemBase shopcartItem) {
        TradeItemPlanActivity tipa = tipaFinder.get(shopcartItem.getUuid());
        return tipa != null && tipa.getStatusFlag() == StatusFlag.VALID;
    }

    /**
     * @Title: cloneValidTradeVo
     * @Description: 克隆出StatusFlag.VALID 的tradeVo，主要用于打印
     * @Param @param tradeVo
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */
    public static TradeVo cloneValidTradeVo(TradeVo tradeVo) {
        TradeVo newTradeVo = null;
        if (tradeVo != null) {
            newTradeVo = new TradeVo();
            Trade trade = tradeVo.getTrade();
            newTradeVo.setTrade(trade);
            if (tradeVo.getTradeExtra() != null && tradeVo.getTradeExtra().getStatusFlag() == StatusFlag.VALID) {
                newTradeVo.setTradeExtra(tradeVo.getTradeExtra());
            }
            if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
                newTradeVo.setCouponPrivilegeVoList(new ArrayList<CouponPrivilegeVo>());
                for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                    if (couponPrivilegeVo != null && couponPrivilegeVo.isActived()
                            && couponPrivilegeVo.getTradePrivilege().isValid()) {
                        newTradeVo.getCouponPrivilegeVoList().add(couponPrivilegeVo);
                    }
                }
            }

            if (tradeVo.getIntegralCashPrivilegeVo() != null && tradeVo.getIntegralCashPrivilegeVo().isActived()
                    && tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().getStatusFlag() == StatusFlag.VALID) {
                newTradeVo.setIntegralCashPrivilegeVo(tradeVo.getIntegralCashPrivilegeVo());
            }

            if (tradeVo.getTradePrivileges() != null && !tradeVo.getTradePrivileges().isEmpty()) {

                List<TradePrivilege> listPrivilege = new ArrayList<TradePrivilege>();

                for (TradePrivilege tPrivilege : tradeVo.getTradePrivileges()) {
                    if (tPrivilege.getStatusFlag() == StatusFlag.VALID)
                        listPrivilege.add(tPrivilege);
                }
                if (!listPrivilege.isEmpty())
                    newTradeVo.setTradePrivileges(listPrivilege);
            }
            if (tradeVo.getTradeCustomerList() != null && !tradeVo.getTradeCustomerList().isEmpty()) {
                List<TradeCustomer> listCustomer = new ArrayList<TradeCustomer>();
                for (TradeCustomer customer : tradeVo.getTradeCustomerList()) {
                    if (customer.getStatusFlag() == StatusFlag.VALID)
                        listCustomer.add(customer);
                }
                if (!listCustomer.isEmpty())
                    newTradeVo.setTradeCustomerList(listCustomer);
            }

            if (tradeVo.getTradeTableList() != null && !tradeVo.getTradeTableList().isEmpty()) {
                List<TradeTable> listTable = new ArrayList<TradeTable>();

                for (TradeTable table : tradeVo.getTradeTableList()) {
                    if (table.getStatusFlag() == StatusFlag.VALID)
                        listTable.add(table);
                }

                if (!listTable.isEmpty())
                    newTradeVo.setTradeTableList(listTable);
            }

            if (tradeVo.getMealShellVo() != null) {//如果是自助，需要传入一个没有数据的list 给打印容错。
                newTradeVo.setTradeItemList(new ArrayList<TradeItemVo>());
            }
            // modify tradeItemVo begin add 20180408
            List<TradeItemVo> listItemVo = new ArrayList<TradeItemVo>();
            if (tradeVo.getTradeItemList() != null && !tradeVo.getTradeItemList().isEmpty()) {
                for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {

                    if (tradeItemVo.getTradeItem() != null
                            && tradeItemVo.getTradeItem().getStatusFlag() == StatusFlag.VALID
                            && tradeItemVo.getTradeItem().getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                        listItemVo.add(cloneValidTradeItemVo(tradeItemVo));
                    }
                }
            }
            //构建餐标对象 add 20180408
            if (tradeVo != null && tradeVo.getMealShellVo() != null) {
                MealShellVo shellVo = tradeVo.getMealShellVo();
                if (shellVo != null) {
                    TradeItem item = shellVo.getTradeItem();
                    if (item != null) {
                        TradeGroupInfo info = tradeVo.getTradeGroup();
                        if (info != null) {
                            item.setDishName(info.getName());
                        }
                        TradeItemVo itemVo = new TradeItemVo();
                        itemVo.setTradeItem(item);
                        listItemVo.add(itemVo);
                    }
                }
            }
            if (!listItemVo.isEmpty())
                newTradeVo.setTradeItemList(listItemVo);
            // modify tradeItemVo end 20180408
            //过滤营销活动
            List<TradePlanActivity> planList = tradeVo.getTradePlanActivityList();
            List<TradeItemPlanActivity> itemPlanList = tradeVo.getTradeItemPlanActivityList();
            if (planList != null && planList.size() > 0) {
                List<TradePlanActivity> tempPlanList = new ArrayList<TradePlanActivity>();
                for (TradePlanActivity planActivity : planList) {
                    if (planActivity.getStatusFlag() == StatusFlag.VALID) {
                        tempPlanList.add(planActivity);
                    }
                }
                newTradeVo.setTradePlanActivityList(tempPlanList);
            }

            if (itemPlanList != null && itemPlanList.size() > 0) {
                List<TradeItemPlanActivity> tempItemPlanList = new ArrayList<TradeItemPlanActivity>();
                for (TradeItemPlanActivity itemPlanActivity : itemPlanList) {
                    if (itemPlanActivity.getStatusFlag() == StatusFlag.VALID) {
                        tempItemPlanList.add(itemPlanActivity);
                    }
                }
                newTradeVo.setTradeItemPlanActivityList(tempItemPlanList);
            }

            // clone 附加费
            newTradeVo.setExtraChargeMap(ExtraManager.cloneExtraMap(tradeVo.getExtraChargeMap()));
            if (tradeVo.getmWeiXinCouponsVo() != null) {
                List<WeiXinCouponsVo> newWeixinList = new ArrayList<WeiXinCouponsVo>();
                for (WeiXinCouponsVo couponsVo : tradeVo.getmWeiXinCouponsVo()) {
                    if (couponsVo.isActived() && couponsVo.getmTradePrivilege() != null && couponsVo.getmTradePrivilege().isValid()) {
                        newWeixinList.add(couponsVo);
                    }
                }
                newTradeVo.setmWeiXinCouponsVo(newWeixinList);
            }
            if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
                newTradeVo.setBanquetVo(tradeVo.getBanquetVo());
            }

        }

        //餐标外壳
        if (tradeVo.getMealShellVo() != null)
            newTradeVo.setMealHullVo(tradeVo.getMealShellVo());
        if (tradeVo.getTradeGroup() != null) {
            newTradeVo.setTradeGroup(tradeVo.getTradeGroup());
        }
        newTradeVo.setTradeBuffetPeoples(tradeVo.getTradeBuffetPeoples());
        newTradeVo.setTradeItemMainBatchRelExtraList(tradeVo.getTradeItemMainBatchRelExtraList());
        newTradeVo.setTradeDeposit(tradeVo.getTradeDeposit());
        newTradeVo.setTradeTaxs(tradeVo.getTradeTaxs());
        newTradeVo.setTradeReasonRelList(tradeVo.getTradeReasonRelList());
        newTradeVo.setTradeEarnestMoneys(tradeVo.getTradeEarnestMoneys());//添加预付金克隆
        return newTradeVo;
    }

    public static TradeItemVo cloneValidTradeItemVo(TradeItemVo tradeItemVo) {
        TradeItemVo vo = new TradeItemVo();
        vo.setTradeItem(tradeItemVo.getTradeItem());
        //折扣
        if (tradeItemVo.getTradeItemPrivilege() != null
                && tradeItemVo.getTradeItemPrivilege().getStatusFlag() == StatusFlag.VALID) {

            vo.setTradeItemPrivilege(tradeItemVo.getTradeItemPrivilege());
        }
        //礼品券
        if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege().getStatusFlag() == StatusFlag.VALID) {

            vo.setCouponPrivilegeVo(tradeItemVo.getCouponPrivilegeVo());
        }
        if (tradeItemVo.getTradeItemPropertyList() != null && !tradeItemVo.getTradeItemPropertyList().isEmpty()) {
            List<TradeItemProperty> list = new ArrayList<TradeItemProperty>();

            for (TradeItemProperty property : tradeItemVo.getTradeItemPropertyList()) {

                if (property.getStatusFlag() == StatusFlag.VALID)
                    list.add(property);
            }
            if (!list.isEmpty())
                vo.setTradeItemPropertyList(list);
        }
        if (Utils.isNotEmpty(tradeItemVo.getTradeItemOperations())) {
            List<TradeItemOperation> tradeItemOperationList = new ArrayList<>();
            for (TradeItemOperation tradeItemOperation : tradeItemVo.getTradeItemOperations()) {
                if (!tradeItemOperation.isValid()) {
                    continue;
                }
                tradeItemOperationList.add(tradeItemOperation);
            }
            vo.setTradeItemOperations(tradeItemOperationList);
        }

        if (Utils.isNotEmpty(tradeItemVo.getTradeItemMainBatchRelList())) {
            List<TradeItemMainBatchRel> tradeItemMainBatchRels = new ArrayList<>();
            for (TradeItemMainBatchRel tradeItemMainBatchRel : tradeItemVo.getTradeItemMainBatchRelList()) {
                if (!tradeItemMainBatchRel.isValid()) {
                    continue;
                }
                tradeItemMainBatchRels.add(tradeItemMainBatchRel);
            }
            vo.setTradeItemMainBatchRelList(tradeItemMainBatchRels);
        }
        return vo;
    }


    /**
     * @Title: copyExtraCharge
     * @Description: 拆单或者下单回到界面，复制附加费
     * @Return void 返回类型
     */
    public static void copySeparateExtraCharge(DinnerShoppingCart dinnerShoppingCart) {
        List<ExtraCharge> extraList = null;
        boolean isAllorder =
                SeparateShoppingCart.getInstance()
                        .isAllOrder(dinnerShoppingCart.mergeShopcartItem(dinnerShoppingCart.getShoppingCartVo()));
        if (!isAllorder) {
            // 拆单回来，将原单的附加费和单据上自动带入的附加费，加入拆单购物车
            extraList =
                    ExtraManager.getAutoOrderExtraMap(DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap(), true);
        } else {
            // 下单回来，如果原单有附加费只将原单的附加费加入，没有加入自动加入的附加费
            if (DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap() == null
                    || DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap().size() == 0) {
                extraList =
                        ExtraManager.getAutoOrderExtraMap(DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap(),
                                true);
            } else {
                extraList =
                        ExtraManager.getAutoOrderExtraMap(DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap(),
                                false);
            }
        }
        SeparateShoppingCart.getInstance().addExtraCharge(extraList, true, true);
    }


    // 将原单的附加费加入新单，主要针对从支付界面点返回时处理
    public static void copyExtraChargeSrc(DinnerShoppingCart dinnerShoppingCart) {
        if (dinnerShoppingCart.getShoppingCartVo().getmTradeVo() != null) {
            Map<Long, ExtraCharge> extraMap = dinnerShoppingCart.getShoppingCartVo().getmTradeVo().getExtraChargeMap();
            if (extraMap != null) {
                List<ExtraCharge> extraList = new ArrayList<ExtraCharge>();
                extraList.addAll(extraMap.values());
                SeparateShoppingCart.getInstance().addExtraCharge(extraList, true, true);
            }
        }
    }


    /**
     * 去除无效的授权记录
     *
     * @param tradeVo
     */
    public static void removeInValidAuthLog(TradeVo tradeVo) {
        removeInValidAuthLog(AuthType.TYPE_PRIVILEGE_DISCOUNT, PrivilegeType.DISCOUNT, tradeVo);
        removeInValidAuthLog(AuthType.TYPE_PRIVILEGE_REBETE, PrivilegeType.REBATE, tradeVo);
        removeInValidAuthLog(AuthType.TYPE_PRIVILIGE_FREE, PrivilegeType.FREE, tradeVo);
        removeInValidAuthLog(AuthType.TYPE_PRIVILIGE_FREE, PrivilegeType.GIVE, tradeVo);
    }

    private static void removeInValidAuthLog(AuthType authType, PrivilegeType privilegeType, TradeVo tradeVo) {
        if (AuthLogManager.getInstance().get(authType) != null) {
            if (tradeVo.getTradePrivilege() != null) {
                //是否有对应的优惠
                boolean isHasPrivilege = false;
                for (TradePrivilege tradePrivilege : tradeVo.getTradePrivileges()) {
                    if (tradePrivilege.getPrivilegeType() == privilegeType && tradePrivilege.getId() == null) {
                        isHasPrivilege = true;
                        break;
                    }
                }
                if (!isHasPrivilege) {
                    AuthLogManager.getInstance().removeAuthLog(authType);
                }
            } else {
                AuthLogManager.getInstance().removeAuthLog(authType);
            }
        }
    }


    /**
     * 返回tradevo中对应的customer
     *
     * @param tradeVo
     * @return
     */
    public static CustomerResp getTradeVoCustomer(TradeVo tradeVo) {
        List<TradeCustomer> listCustomer = tradeVo.getTradeCustomerList();
        if (listCustomer != null && listCustomer.size() > 0) {
            for (TradeCustomer tradeCustomer : listCustomer) {
                if (tradeCustomer.getStatusFlag() == StatusFlag.VALID) {
                    if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
//                        String customerPhone = tradeCustomer.getCustomerPhone();
//                        CustomerNew customer = CustomerManager.getInstance().getCustomerAndLevelRights(customerPhone);
                        CustomerResp customer = CustomerManager.getInstance().getCustomer(tradeCustomer);
                        customer.queryLevelRightInfos();
                        return customer;
                    } else if (tradeCustomer.getCustomerType() == CustomerType.CARD) {
                        EcCard card = new EcCard();
                        card.setCardNum(tradeCustomer.getEntitycardNum());
                        card.setName(tradeCustomer.getCustomerName());
                        card.setCustomer(CustomerManager.getInstance().getCustomerV5(tradeCustomer));

//                        String customerPhone = tradeCustomer.getCustomerPhone();
//                        CustomerNew customer = CustomerManager.getInstance().getCustomerAndLevelRights(customerPhone);
                        CustomerResp customer = CustomerManager.getInstance().getCustomer(tradeCustomer);
                        customer.queryLevelRightInfos();
                        customer.card = (card);
                        return customer;
                    }
                }
            }
        }
        return null;
    }


    //上传打印状态
    /*public static void submitPrintStatus(final PrintOperationOperates printOperationOperates, final TradeVo tradeVo) {
        CloudPrintReq req = new CloudPrintReq();
        req.setOpType(PrintOperationOpType.DINNER_PRE_CASH.value());
        req.setTradeId(tradeVo.getTrade().getId());
        String exStr = "{\"tradeId\":" + tradeVo.getTrade().getId() + ",\"source\":" + tradeVo.getTrade().getSource().toString() + " }";
        req.setPrintStatus(PrintStatus.FINISHED.value());
        req.setExtStr(exStr);

        ResponseListener<CloudPrintResp> cloudPrintListener = new EventResponseListener<CloudPrintResp>(UserActionEvent.DINNER_PAY_ORDER_PRE) {

            @Override
            public void onResponse(ResponseObject<CloudPrintResp> response) {
                // 下单成功
                try {
                    if (ResponseObject.isOk(response)) {
                        //  ToastUtil.showLongToast("打印结果保存成功！");

                    }
                    UserActionEvent.end(UserActionEvent.DINNER_PAY_ORDER_PRE);
                } catch (Exception e) {
                    Log.e("PayMain", "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    ToastUtil.showLongToast(error.getMessage());
                    // submitPrintStatus(tradeVo);//失败再次提交
                } catch (Exception e) {
                    Log.e("PayMain", "", e);
                }
            }
        };
        printOperationOperates.doCloudPrint(req, cloudPrintListener);
    }*/

    /**
     * 复制正餐购物车的信息到拆单
     */
    public void copyDinnerToSepart() {
        copyDinnerDiscountToSeparator();
//        copyMarketActivityToSeparator();
        copyDinnerCustomerInfosToSeparator();
        copyBanquetToSeparator();
        copyDinnerExtraToSparator();
        SeparateShoppingCart shoppingCart = SeparateShoppingCart.getInstance();
        MathShoppingCartTool.mathTotalPrice(shoppingCart.mergeShopcartItem(shoppingCart.getShoppingCartVo()),
                shoppingCart.getShoppingCartVo().getmTradeVo());
    }

    /**
     * 加入自动带入的附加费给正餐购物车
     */
    public List<ExtraCharge> copyExtraToDinner() {
        Map<Long, ExtraCharge> extraChargeMap = DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap();
        if (extraChargeMap == null) {
            DinnerShoppingCart.getInstance().getOrder().setExtraChargeMap(new HashMap<Long, ExtraCharge>());
        }
        List<ExtraCharge> tempList = null;
        if (DinnerShoppingCart.getInstance().getOrder().getExtraChargeMap().isEmpty()) {
            tempList = ExtraManager.getAutoOrderExtraMap(DinnerShoppingCart.getInstance().getOrder(), extraChargeMap, true);
        }
        return tempList;
    }

    /**
     * 根据会员生成积分抵现
     *
     * @Title: updateIntegralCash
     * @Return void 返回类型
     */
    public void updateIntegralCash(CustomerResp customer) {
        updateIntegralCash(customer, true);//modify  v8.10
    }

    //add v8.10 begin
    public void updateIntegralCash(CustomerResp customer, boolean isCallback) {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        // 更新购物车，积分抵现的积分值
        if (shoppingCart.getOrder() != null && shoppingCart.getOrder().getIntegralCashPrivilegeVo() != null
                && shoppingCart.getOrder().getIntegralCashPrivilegeVo().getTradePrivilege() != null
                && shoppingCart.getOrder().getIntegralCashPrivilegeVo().getTradePrivilege().isValid()) {
            IntegralCashPrivilegeVo integralCashPrivilegeVo = shoppingCart.getOrder().getIntegralCashPrivilegeVo();
            //规则不为空且开关是打开的  update by dzb  不要这个规则
//            if (customer.customerLevelRights != null
//                    && customer.customerLevelRights.getIsExchangeCash() == TrueOrFalse.TRUE) {
//                integralCashPrivilegeVo.setRule(customer.customerLevelRights);
//            } else {
//                shoppingCart.removeIntegralCash();
//                return;
//            }
            if (customer.integral != null) {
                integralCashPrivilegeVo.setIntegral(new BigDecimal(customer.integral));
            }
            shoppingCart.setIntegralCash(integralCashPrivilegeVo, true, isCallback);
        }
    }
    //add v8.10 end

    /**
     * 根据实体卡生成积分抵现
     *
     * @Title: updateIntegralCash
     * @Return void 返回类型
     */
    public void updateIntegralCash(EcCard card) {
        updateIntegralCash(card, true);
    }

    //add v8.10 begin
    public void updateIntegralCash(EcCard card, boolean isCallback) {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        // 更新购物车，积分抵现的积分值
        if (shoppingCart.getOrder() != null && shoppingCart.getOrder().getIntegralCashPrivilegeVo() != null
                && shoppingCart.getOrder().getIntegralCashPrivilegeVo().getTradePrivilege() != null
                && shoppingCart.getOrder().getIntegralCashPrivilegeVo().getTradePrivilege().isValid()) {
            IntegralCashPrivilegeVo integralCashPrivilegeVo = shoppingCart.getOrder().getIntegralCashPrivilegeVo();
            //规则不为空且开关是打开的
            if (card.getCardLevelSetting() != null
                    && card.getCardLevelSetting().getIsExchangeCash() == Bool.YES) {
                integralCashPrivilegeVo.setRule(card.getCardLevelSetting());
            } else {
                shoppingCart.removeIntegralCash();
                return;
            }
            Long integral = 0L;
            if (card.getIntegralAccount() != null) {
                integral = card.getIntegralAccount().getIntegral();
            }
            integralCashPrivilegeVo.setIntegral(new BigDecimal(integral));
            shoppingCart.setIntegralCash(integralCashPrivilegeVo, true, isCallback);
        }
    }
    //add v8.10 end

    /**
     * 获取订单内指定类型的tradecustomer
     *
     * @param tradeVo
     * @param customerType
     * @return
     */
    public TradeCustomer getTradeCustomer(TradeVo tradeVo, CustomerType customerType) {
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeCustomerList())) {
            for (TradeCustomer tradeCustomer : tradeVo.getTradeCustomerList()) {
                if (tradeCustomer.getCustomerType() == customerType && tradeCustomer.getStatusFlag() == StatusFlag.VALID) {
                    return tradeCustomer;
                }
            }
        }

        return null;
    }

}
