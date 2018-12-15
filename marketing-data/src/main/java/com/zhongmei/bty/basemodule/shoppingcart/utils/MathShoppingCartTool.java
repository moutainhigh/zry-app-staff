package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.PrivilegeApportionManager;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.GiftShopcartItemSingleton;
import com.zhongmei.bty.basemodule.shoppingcart.bean.MathVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.CarryBitRule;
import com.zhongmei.yunfu.util.MathDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date：2015年7月13日 上午11:13:26
 * @Description: 购物车中所有价格的计算
 * @Version: 1.0
 * <p>
 * rights reserved.
 */

public class MathShoppingCartTool {

    //private static int EXTRACHARGEYES = 1;

    //private static int EXTRACHARGENO = 2;
    //private static int EXTRACHARGESERVICE = 3;

    private static BigDecimal mPlanPrivilaegeAmount = BigDecimal.ZERO;
    //优惠劵计算前tradeAmount
    public static BigDecimal couponBeforeAmount = BigDecimal.ZERO;


    /**
     * 计算价格
     *
     * @Title: mathTotalPrice
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */

    public static void mathTotalPrice(List<IShopcartItem> iShopcartItem, TradeVo mTradeVo) {

        if (mTradeVo == null || mTradeVo.getTrade() == null) {
            return;
        }

        if (Utils.isNotEmpty(iShopcartItem)) {
            iShopcartItem.addAll(GiftShopcartItemSingleton.getInstance().getListPolicyDishshopVo());
        }

        mathTotalPriceLand(iShopcartItem, mTradeVo);

    }

    /**
     * 有消费税和服务费
     *
     * @param iShopcartItem
     * @param mTradeVo
     */
    private static void mathTotalPriceLand(List<IShopcartItem> iShopcartItem, TradeVo mTradeVo) {

        PrivilegeApportionManager.getInstance().startMath(); //每次计算时，初始化一次数据

        //如果有宴请，计算时移除所有的优惠
        if (BaseShoppingCart.isHasValidBanquet(mTradeVo)) {
            BaseShoppingCart.removeAllPrivilige(mTradeVo, false, false, false);
        }
        //计算之前初始化
        initTradeMath(mTradeVo.getTrade());

        mPlanPrivilaegeAmount = BigDecimal.ZERO;
        MathVo mathVo = new MathVo();

        mathVo.saleAmount = mathMealShellAmount(mTradeVo);

        mathVo.dishAllAmout = mathVo.dishAllAmout.add(mathVo.saleAmount);
        mathItemAmount(mathVo, iShopcartItem);
        //菜品价格扣除单品优惠
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(mathVo.saleAmount).add(mathVo.totalPrivilegeAmount).add(mathVo.noDiscPrivilegeAmout);

        //用于服务费计算
        mTradeVo.getTrade().setSaleAmount(mathVo.saleAmount);

        // 计算获取附加费，discountExraCharge表示能参与整单折扣的附加费
        BigDecimal afterAmount = BigDecimal.ZERO;//用于计算附加费的基数
        afterAmount = afterAmount.add(mathVo.saleAmount).add(mathVo.noDiscPrivilegeAmout).add(mathVo.totalPrivilegeAmount);
        //Map<Integer, BigDecimal> extraChargePivilege = mathExtraCharge(mTradeVo,iShopcartItem,afterAmount);
        ExtraChargeTool extraChargeTool = ExtraChargeTool.countExtraCharge(mTradeVo, iShopcartItem, afterAmount);

        // 将能参与整单折扣的附加费添加到能参与整单打折的总金额中
        mathVo.dishAllAmout = mathVo.dishAllAmout.add(extraChargeTool.getExtraChargeYes());

        //营销活动前菜品扣除优惠的价格
        BigDecimal dishAllAmoutPlanBefore = mathVo.dishAllAmout.add(mathVo.noDiscountAllAmout).add(mathVo.noDiscPrivilegeAmout).add(mathVo.totalPrivilegeAmount);
        /**
         * 计算营销活动
         */
        mathVo.dishAllAmout = mathPlayActivity(mathVo.dishAllAmout, mTradeVo, iShopcartItem);


        //获取能参与整单打折的菜品总价
        mathVo.dishAllAmout = mathVo.dishAllAmout.add(mathVo.totalPrivilegeAmount);
        if (mathVo.dishAllAmout.compareTo(BigDecimal.ZERO) <= 0) {//当能参与整单金额小于totalPrivilegeAmount时，置为0由于礼品券问题引起才做修改
            mathVo.dishAllAmout = BigDecimal.ZERO;
        }

        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mPlanPrivilaegeAmount);
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(mPlanPrivilaegeAmount);

        //计算最低消费附加费
        BigDecimal consumAmount = BigDecimal.ZERO;
        if (mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET
                || mTradeVo.getTrade().getBusinessType() == BusinessType.DINNER) {
            BigDecimal extrageNo = BigDecimal.ZERO;
            extrageNo = extraChargeTool.getExtraChargeNo();


            consumAmount = consumAmount.add(extrageNo).add(dishAllAmoutPlanBefore).add(mPlanPrivilaegeAmount);
            consumAmount = mathMinConsum(consumAmount, mTradeVo);
            mathVo.saleAmount = mathVo.saleAmount.add(consumAmount);
        }

        //服务费
        /*BigDecimal serviceAmount=extraChargeTool.getPrivilegeMap().get(EXTRACHARGESERVICE);
        if(serviceAmount==null){
			serviceAmount=BigDecimal.ZERO;
		}*/
        //优惠菜的价格
        BigDecimal privilegeDishAmount = BigDecimal.ZERO;
        // 整单打折部分计算
        TradePrivilege mTradePrivilege = mTradeVo.getTradePrivilege();
        if (mTradePrivilege != null && mTradePrivilege.isValid()) {

            BigDecimal privilegeValue = mTradePrivilege.getPrivilegeValue();

            BigDecimal privilegeAmount = BigDecimal.ZERO;
            BigDecimal privilegeAmountBefore = BigDecimal.ZERO;
            privilegeAmountBefore = privilegeAmountBefore.add(mathVo.dishAllAmout).add(consumAmount);

            switch (mTradePrivilege.getPrivilegeType()) {

                case DISCOUNT:

                    // 获取优惠金额 原价*（1-折扣比例）
                    privilegeAmount = MathDecimal.trimZero(
                            MathDecimal.round(privilegeAmountBefore.multiply(MathDecimal.getDiscountValue(privilegeValue)), 2));
                    if (TextUtils.isEmpty(mTradePrivilege.getPrivilegeName())) {
                        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.order_all_discount));
                    }
                    privilegeDishAmount = MathDecimal.round(mathVo.taxableInAmount.multiply(MathDecimal.getDiscountValue(privilegeValue)), 2);
                    break;

                case REBATE:
                    //折让金额大于可折扣的金额，折让设为0
                    if (privilegeValue.compareTo(privilegeAmountBefore) > 0) {
                        privilegeAmount = mathVo.dishAllAmout;
                        privilegeDishAmount = mathVo.taxableInAmount;
                    } else {
                        privilegeAmount = privilegeValue;
                        privilegeDishAmount = privilegeValue;
                    }
                    if (TextUtils.isEmpty(mTradePrivilege.getPrivilegeName())) {
                        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.all_order_rebate));
                    }

                    break;

                case FREE:
                    //if(extraChargePivilege != null){
                    privilegeAmount = dishAllAmoutPlanBefore.add(extraChargeTool.getExtraChargeNo());
                    /*}else{
                        privilegeAmount = dishAllAmoutPlanBefore;
					}*/
                    privilegeAmount = privilegeAmount.add(consumAmount);
                    privilegeAmount = privilegeAmount.add(mPlanPrivilaegeAmount);
                    //privilegeAmount=privilegeAmount.add(serviceAmount);
                    privilegeDishAmount = privilegeDishAmount.add(mathVo.taxableInAmount);
                    if (TextUtils.isEmpty(mTradePrivilege.getPrivilegeName())) {
                        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.free_this_order));
                    }

                    break;

                default:

                    break;

            }

            privilegeAmount = privilegeAmount.negate();

            mTradePrivilege.setPrivilegeAmount(privilegeAmount);
            mTradePrivilege.setChanged(true);
            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(privilegeAmount);

            //计算整单折扣/折让 优惠分摊
            PrivilegeApportionManager.getInstance().updateWholePrivilege(iShopcartItem,
                    privilegeAmount.abs(),
                    mTradePrivilege.getPrivilegeName(),
                    mathVo.taxableInAmount);
        }
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(privilegeDishAmount.negate());

        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mathVo.noDiscPrivilegeAmout);

        // 将不能参与整单折扣的附加费添加到订单总金额中
        mathVo.saleAmount = mathVo.saleAmount.add(extraChargeTool.getExtraChargeYes());
        mathVo.saleAmount = mathVo.saleAmount.add(extraChargeTool.getExtraChargeNo());
        Trade trade = mTradeVo.getTrade();

        //在tradeVo里添加设置优惠卷前的金额
        mTradeVo.setBeforePrivilegeAmount(mathVo.saleAmount);
        // 计算优惠卷前订单的价格
        trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

        trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

        trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));
        couponBeforeAmount = trade.getTradeAmount();
        //优惠劵的优惠金额
        BigDecimal totalCouponAmount = mathCouponPrivilege(iShopcartItem, mTradeVo, mathVo);
        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(totalCouponAmount);

        mathWeixinCode(mTradeVo, mathVo);

        mathIntegral(mTradeVo, mathVo);

		/*if(mTradeVo.getTradeDeposit() != null && mTradeVo.getTradeDeposit().getStatusFlag() == StatusFlag.VALID){
			mathVo.saleAmount = mathVo.saleAmount.add(mTradeVo.getTradeDeposit().getDepositPay());
		}*/

        BigDecimal serviceAmount = extraChargeTool.chargeService(mathVo.saleAmount, mathVo.saleAmount.add(mathVo.totalPrivilegeAmount));
        //如果是免单或要把服务费加入
        if (mTradePrivilege != null && mTradePrivilege.isValid() && mTradePrivilege.getPrivilegeType() == PrivilegeType.FREE) {
            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(serviceAmount.negate());
            mTradePrivilege.setPrivilegeAmount(mTradePrivilege.getPrivilegeAmount().add(serviceAmount.negate()));
        }

        mathVo.saleAmount = mathVo.saleAmount.add(serviceAmount);
        BigDecimal totalPrice = mathVo.saleAmount.add(mathVo.totalPrivilegeAmount);
        //宴请金额
        BigDecimal banquetPrivilege = mathBanquet(mTradeVo, MathDecimal.round(totalPrice, 2));
        if (banquetPrivilege != null) {
            mathVo.totalPrivilegeAmount = banquetPrivilege;
            mathVo.taxableInAmount = BigDecimal.ZERO;
        }
        trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

        //消费税
        BigDecimal taxAmount = mathTax(mTradeVo, mathVo.saleAmount, mathVo.saleAmount.add(mathVo.totalPrivilegeAmount));
        //税、服务费加入saleAmount
        mathVo.saleAmount = mathVo.saleAmount.add(taxAmount);
        if (mTradeVo.getTradeDeposit() != null && mTradeVo.getTradeDeposit().getStatusFlag() == StatusFlag.VALID) {
            mathVo.saleAmount = mathVo.saleAmount.add(mTradeVo.getTradeDeposit().getDepositPay());
        }

        //		宴请金额计算后重新计算一次
        totalPrice = mathVo.saleAmount.add(mathVo.totalPrivilegeAmount);

        trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

        trade.setTradeAmountBefore(MathDecimal.round(totalPrice, 2));

        //尾数保留位数
        trade.setTradeAmount(getAmountByCarryLimit(totalPrice));

        BigDecimal noJoinExtra = extraChargeTool.getExtraChargeYes();
        if (noJoinExtra != null) {
            mTradeVo.setNoJoinDiscount(mathVo.noDiscountAllAmout.add(noJoinExtra));
        } else {
            mTradeVo.setNoJoinDiscount(mathVo.noDiscountAllAmout);
        }

        mTradeVo.itemApportionList = PrivilegeApportionManager.getInstance().getApportionList();
        PrivilegeApportionManager.getInstance().finishMath(); // 结束优惠分摊计算
    }


    /**
     * 计算之前初始化trade
     *
     * @param trade
     */
    private static void initTradeMath(Trade trade) {
        trade.setSaleAmount(BigDecimal.ZERO);
        trade.setPrivilegeAmount(BigDecimal.ZERO);
        trade.setTradeAmountBefore(BigDecimal.ZERO);
        trade.setTradeAmount(BigDecimal.ZERO);
    }

    /**
     * 微信卡卷计算
     *
     * @param mTradeVo
     * @param mathVo
     */
    private static void mathWeixinCode(TradeVo mTradeVo, MathVo mathVo) {
        Trade trade = mTradeVo.getTrade();
        List<WeiXinCouponsVo> listWX = mTradeVo.getmWeiXinCouponsVo();
        if (listWX != null) {
            for (WeiXinCouponsVo wx : listWX) {
                //判断是否有微信卡卷数据信息，当已添加微信卡卷并以下单后再次进入结算就获取不到微信卡卷信息
                // 计算优惠卷前订单的价格
                trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));
                trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));
                trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));
                if (wx.getmWeiXinCouponsInfo() != null && !wx.isUsed()) {

                    //获取微信卡卷起用金额（单位为分）
                    BigDecimal leasetCost = wx.getmWeiXinCouponsInfo().getCash().getLeast_cost();
                    if (trade.getTradeAmount().compareTo(leasetCost) >= 0) {
                        TradePrivilege mTP = wx.getmTradePrivilege();
                        BigDecimal reduceCost = wx.getmWeiXinCouponsInfo().getCash().getReduce_cost();

                        mTP.setPrivilegeValue(reduceCost);
                        BigDecimal privilegeAmount = getCorrectPrivilegeAmount(trade.getTradeAmount(), reduceCost);
                        mTP.setPrivilegeAmount(privilegeAmount);
                        //计入到订单优惠总金额中
                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(privilegeAmount);
                        mathVo.taxableInAmount = mathVo.taxableInAmount.add(privilegeAmount);
                        if (BigDecimal.ZERO.compareTo(privilegeAmount) == 0) {
                            wx.setActived(false);
                        } else {
                            wx.setActived(true);
                        }
                    } else {
                        wx.setActived(false);
                    }
                } else {
                    //保存过的微信卡劵只计算总金额
                    TradePrivilege mTP = wx.getmTradePrivilege();
                    if (mTP != null && mTP.isSaveServer() && mTP.isValid()) {
                        //因保存的privilegeAmount为负，计算时用的正值比较
                        BigDecimal privilegeAmount = getCorrectPrivilegeAmount(trade.getTradeAmount(), mTP.getPrivilegeAmount().negate());
                        mTP.setPrivilegeAmount(privilegeAmount);
                        mathVo.taxableInAmount = mathVo.taxableInAmount.add(privilegeAmount);
                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mTP.getPrivilegeAmount());
                        if (BigDecimal.ZERO.compareTo(privilegeAmount) == 0) {
                            wx.setActived(false);
                        } else {
                            wx.setActived(true);
                        }
                    } else {
                        //没有规则且无效了移除微信卡劵
                        BaseShoppingCart.removeWeiXinCouponsVo(mTradeVo, wx);
                    }
                }
            }
        }
    }

    /**
     * 积分抵现计算
     *
     * @param mTradeVo
     * @param mathVo
     */
    private static void mathIntegral(TradeVo mTradeVo, MathVo mathVo) {
        Trade trade = mTradeVo.getTrade();
        IntegralCashPrivilegeVo mIntegralCashPrivilegeVo = mTradeVo.getIntegralCashPrivilegeVo();
        if (mIntegralCashPrivilegeVo != null && mIntegralCashPrivilegeVo.getTradePrivilege() != null
                && mIntegralCashPrivilegeVo.isValid()) {

            // 计算积分抵现前订单的价格
            trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

            trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

            trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));

            if (mIntegralCashPrivilegeVo.isUsed() && mIntegralCashPrivilegeVo.isValid()) {
                //积分被核销了，保留之前的值
                mIntegralCashPrivilegeVo.setActived(true);
            } else if (mIntegralCashPrivilegeVo.getIntegral() != null
                    && mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) > 0
                    && mIntegralCashPrivilegeVo.hasRule()) {

                BuildPrivilegeTool.mathPrivilege(mIntegralCashPrivilegeVo, mTradeVo);
            } else {
                mIntegralCashPrivilegeVo.setActived(false);
//				mIntegralCashPrivilegeVo.getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeAmount(BigDecimal.ZERO);
            }

            if (mIntegralCashPrivilegeVo.getTradePrivilege() != null && mIntegralCashPrivilegeVo.isActived()
                    && mIntegralCashPrivilegeVo.getTradePrivilege().getPrivilegeAmount() != null) {

                mathVo.totalPrivilegeAmount =
                        mathVo.totalPrivilegeAmount.add(mIntegralCashPrivilegeVo.getTradePrivilege().getPrivilegeAmount());
                mathVo.taxableInAmount = mathVo.taxableInAmount.add(mIntegralCashPrivilegeVo.getTradePrivilege().getPrivilegeAmount());

            } else {

                mIntegralCashPrivilegeVo.setActived(false);

            }

        }
    }

    private static void mathItemAmount(MathVo mathVo, List<IShopcartItem> iShopcartItem) {
        if (Utils.isEmpty(iShopcartItem)) {
            return;
        }
        for (IShopcartItem item : iShopcartItem) {

            if (item.getStatusFlag() == StatusFlag.VALID && !item.isGroupDish()) {

                mathVo.saleAmount = mathVo.saleAmount.add(item.getActualAmount());

                //礼品券
                if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null
                        && item.getCouponPrivilegeVo().getTradePrivilege().getStatusFlag() == StatusFlag.VALID) {
                    if (item.getCouponPrivilegeVo().isActived()) {
                        if (item.getEnableWholePrivilege() == Bool.NO) {
                            mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                            mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                        } else {
                            mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());//礼品券变价后能参与整单部分在后面减掉
                            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                        }
                        PrivilegeApportionManager.getInstance().updateSingleCouponPrivilege(item); //礼品券优惠分摊
                    } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());
                    }

                } else if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                    //次卡服务优惠
                    if (item.getEnableWholePrivilege() == Bool.NO) {
                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                        mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getCardServicePrivilgeVo().getTradePrivilege().getPrivilegeAmount());
                    } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());//礼品券变价后能参与整单部分在后面减掉
                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getCardServicePrivilgeVo().getTradePrivilege().getPrivilegeAmount());
                    }
                } else if (item.getAppletPrivilegeVo() != null && item.getAppletPrivilegeVo().isPrivilegeValid()) {
                    //次卡服务优惠
                    if (item.getEnableWholePrivilege() == Bool.NO) {
                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                        mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getAppletPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                    } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());//礼品券变价后能参与整单部分在后面减掉
                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getAppletPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                    }
                } else {
                    // 判断菜品是否能参与整单打折
                    if (item.getEnableWholePrivilege() == Bool.NO) {

                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());

                        if (item.getPrivilege() != null && item.getPrivilege().getPrivilegeAmount() != null
                                && item.getPrivilege().isValid()) {

                            mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getPrivilege().getPrivilegeAmount());
                            PrivilegeApportionManager.getInstance().updateSingleDiscount(item); // 单商品折让/折扣
                        }

                    } else {

                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());

                        if (item.getPrivilege() != null && item.getPrivilege().getPrivilegeAmount() != null
                                && item.getPrivilege().isValid()) {

                            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getPrivilege().getPrivilegeAmount());
                            PrivilegeApportionManager.getInstance().updateSingleDiscount(item); // 单商品折让/折扣
                        }

                    }
                }


            }

        }
    }

    /**
     * 计算税率:税率金额涉及菜品价格的优惠、折扣都需要扣除
     *
     * @param tradeVo
     * @return
     */
    private static BigDecimal mathTax(TradeVo tradeVo, BigDecimal saleAmount, BigDecimal taxableInAmount) {
        BigDecimal taxAmount = BigDecimal.ZERO;
        TaxRateInfo taxRateInfo = getTaxRateInfo(tradeVo);
        if (taxRateInfo == null || !taxRateInfo.isTaxSupplyOpen() || tradeVo.isUnionSubTrade()) {
            return taxAmount;
        }
        List<TradeTax> tradeTaxList = tradeVo.getTradeTaxs();
        TradeTax tradeTax = null;
        if (Utils.isEmpty(tradeTaxList)) {
            tradeVo.setTradeTaxs(new ArrayList<TradeTax>());
        } else {
            tradeTax = tradeVo.getTradeTaxs().get(0);
        }

        if (tradeTax == null) {
            tradeTax = new TradeTax();
            tradeTax.validateCreate();
            tradeTax.setTradeId(tradeVo.getTrade().getId());
            tradeTax.setUuid(SystemUtils.genOnlyIdentifier());
            tradeTax.setTaxType(taxRateInfo.getTaxCode());
            tradeTax.setTaxPlan(taxRateInfo.getTaxRate().toString());
            tradeTax.setTaxTypeName(taxRateInfo.getTaxDesc());
            tradeTax.setEffectType(taxRateInfo.getEffectType());
            tradeTax.setDiscountType(taxRateInfo.getDiscountType());
            tradeTax.setTaxKind(taxRateInfo.getTaxKind());
            tradeTax.setTaxMethod(taxRateInfo.getTaxMethod());
            tradeVo.getTradeTaxs().add(tradeTax);
        } else {
            tradeTax.validateUpdate();
            tradeTax.setStatusFlag(StatusFlag.VALID);
            //税率可能改变 用最新的
            tradeTax.setTaxType(taxRateInfo.getTaxCode());
            tradeTax.setTaxPlan(taxRateInfo.getTaxRate().toString());
            tradeTax.setTaxTypeName(taxRateInfo.getTaxDesc());
            tradeTax.setEffectType(taxRateInfo.getEffectType());
            tradeTax.setDiscountType(taxRateInfo.getDiscountType());
            tradeTax.setTaxKind(taxRateInfo.getTaxKind());
            tradeTax.setTaxMethod(taxRateInfo.getTaxMethod());
        }

        //判断是否折后计算税率
        if (taxRateInfo.isDiscountAfter()) {
            if (taxableInAmount.compareTo(BigDecimal.ZERO) < 0) {
                taxableInAmount = BigDecimal.ZERO;
            }
            taxAmount = MathDecimal.divDown(taxableInAmount.multiply(taxRateInfo.getTaxRate()), BigDecimal.valueOf(100), 2);
            tradeTax.setTaxAmount(taxAmount);
            tradeTax.setTaxableIncome(taxableInAmount);
        } else {
            if (saleAmount.compareTo(BigDecimal.ZERO) < 0) {
                saleAmount = BigDecimal.ZERO;
            }
            taxAmount = MathDecimal.divDown(saleAmount.multiply(taxRateInfo.getTaxRate()), BigDecimal.valueOf(100), 2);
            tradeTax.setTaxAmount(taxAmount);
            tradeTax.setTaxableIncome(saleAmount);
        }
        return taxAmount;
    }

    private static TaxRateInfo getTaxRateInfo(TradeVo tradeVo) {
        BusinessType businessType = tradeVo.getTrade().getBusinessType();
        if (businessType == BusinessType.DINNER
                || businessType == BusinessType.GROUP
                || businessType == BusinessType.BUFFET) {
            List<TradeTax> tradeTaxes = tradeVo.getTradeTaxs();
            if (tradeTaxes != null) {
                for (TradeTax tradeTax : tradeTaxes) {
                    if (tradeTax.isValid()) {
                        TaxRateInfo rateInfo = new TaxRateInfo();
                        rateInfo.setTaxCode(tradeTax.getTaxType());
                        rateInfo.setTaxRate(new BigDecimal(tradeTax.getTaxPlan()));
                        rateInfo.setTaxDesc(tradeTax.getTaxTypeName());
                        rateInfo.setEffectType(tradeTax.getEffectType());
                        rateInfo.setDiscountType(tradeTax.getDiscountType());
                        rateInfo.setTaxKind(tradeTax.getTaxKind());
                        rateInfo.setTaxMethod(tradeTax.getTaxMethod());
                        return rateInfo;
                    }
                }
            }
            return null;
        }

        return ServerSettingCache.getInstance().getmTaxRateInfo();
    }


    /**
     * 尾数处理
     *
     * @param totalPrice
     * @return
     */
    public static BigDecimal getAmountByCarryLimit(BigDecimal totalPrice) {
        //尾数保留位数
        int limit = ServerSettingManager.getCarryLimit();
		/*if(limit == 2){
			totalPrice=MathDecimal.round(totalPrice, 2);
		}else{*/
        //尾数处理规则
        CarryBitRule mCarryBitRule = ServerSettingManager.getCarryRule();
        if (mCarryBitRule == CarryBitRule.ROUND_UP) {//四舍五入
            totalPrice = MathDecimal.round(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.CARRY) {//无条件进位
            totalPrice = MathDecimal.roundUp(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.MALING) {//无条件抹零
            totalPrice = MathDecimal.roundDown(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.THREE_EIGHT_UP) {
            totalPrice = MathDecimal.threeEightUp(totalPrice, limit);
        }
        //}
        return totalPrice;
    }

    //计算最低消费附加额
    static private BigDecimal mathMinConsum(BigDecimal dishAllAmout, TradeVo tradeVo) {
        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();

        if (listPrivilege == null) {
            listPrivilege = new ArrayList<>();
            tradeVo.setTradePrivileges(listPrivilege);
        }

        ExtraCharge minConsumCharge = tradeVo.getMinconExtraCharge();
        if (minConsumCharge == null) {
            return BigDecimal.ZERO;
        }
        TradePrivilege minPrivilege = null;
        for (int i = listPrivilege.size() - 1; i >= 0; i--) {
            TradePrivilege mTradePrivilege = listPrivilege.get(i);
            if (mTradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL
                    && MathDecimal.isLongEqual(mTradePrivilege.getPromoId(), minConsumCharge.getId())) {
                minPrivilege = mTradePrivilege;
                break;
            }
        }

        if (minPrivilege == null)
            return BigDecimal.ZERO;

        BigDecimal extraValue = minPrivilege.getPrivilegeValue().subtract(dishAllAmout);

        if (extraValue.compareTo(BigDecimal.ZERO) < 1 || !tradeVo.isEnableMinConsum()) {
            minPrivilege.setStatusFlag(StatusFlag.INVALID);
            return BigDecimal.ZERO;
        } else {
            minPrivilege.setPrivilegeAmount(extraValue);
            minPrivilege.setStatusFlag(StatusFlag.VALID);
            return extraValue;
        }

    }

    /**
     * 计算餐标外壳价格
     *
     * @param tradeVo
     */
    public static BigDecimal mathMealShellAmount(TradeVo tradeVo) {
        if (tradeVo.getMealShellVo() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal price = tradeVo.getMealShellVo().getActualAmount();
        return price;
    }

    /**
     * 获取纠正后的金额,如果订单金额为负，归零
     */
    private static BigDecimal getCorrectPrivilegeAmount(BigDecimal tradeAmount, BigDecimal privilegeAmount) {
        BigDecimal newTradeAmount = tradeAmount.subtract(privilegeAmount);
        if (newTradeAmount.compareTo(BigDecimal.ZERO) < 0) {
            return tradeAmount.negate();
        }
        return privilegeAmount.negate();
    }

    /**
     * @Title: mathCoupon
     * @Description: 计算优惠卷价格
     * @Param TODO
     * @Return BigDecimal 优惠劵的
     */

    public static BigDecimal mathCouponPrivilege(List<IShopcartItem> iShopcartItems, TradeVo mTradeVo, MathVo mathVo) {
        if (Utils.isEmpty(mTradeVo.getCouponPrivilegeVoList())) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalPrivilegeAmount = BigDecimal.ZERO;
        BigDecimal oldTradeAmount = mTradeVo.getTrade().getTradeAmount();
        BigDecimal newTradeAmount = oldTradeAmount;
        for (CouponPrivilegeVo mCouponPrivilegeVo : mTradeVo.getCouponPrivilegeVoList()) {
            if (mCouponPrivilegeVo != null && mCouponPrivilegeVo.getTradePrivilege() != null
                    && mCouponPrivilegeVo.isValid()) {
                BigDecimal privilegeAmount;
                //被使用过的保留原来的值
                if (mCouponPrivilegeVo.isUsed()) {
                    privilegeAmount = mCouponPrivilegeVo.getTradePrivilege().getPrivilegeAmount();
                } else {
                    privilegeAmount = mathCoupon(mCouponPrivilegeVo, oldTradeAmount, newTradeAmount, mathVo);
                }
                if (privilegeAmount != null) {

                    // 计算优惠券的分摊
                    PrivilegeApportionManager.getInstance().updateWholeCouponPrivilege(iShopcartItems,
                            privilegeAmount.abs(),
                            mCouponPrivilegeVo.getTradePrivilege().getPromoId(),
                            mCouponPrivilegeVo.getTradePrivilege().getPrivilegeName(),
                            newTradeAmount);

                    newTradeAmount = newTradeAmount.add(privilegeAmount);
                    if (newTradeAmount.compareTo(BigDecimal.ZERO) < 0) {
                        mTradeVo.getTrade().setTradeAmount(BigDecimal.ZERO);
                    } else {
                        mTradeVo.getTrade().setTradeAmount(newTradeAmount);
                    }
                    totalPrivilegeAmount = totalPrivilegeAmount.add(privilegeAmount);
                }
            }
        }

        return totalPrivilegeAmount;
    }

    /**
     * @param tradeAmount    计算优惠劵前的tradeAmount
     * @param newTradeAmount 每次优惠劵处理后的订单金额
     */
    private static BigDecimal mathCoupon(CouponPrivilegeVo mCouponPrivilegeVo, BigDecimal tradeAmount, BigDecimal newTradeAmount, MathVo mathVo) {
        if (mCouponPrivilegeVo != null) {

            Coupon mCoupon = mCouponPrivilegeVo.getCoupon();

            TradePrivilege mTradePrivilege = mCouponPrivilegeVo.getTradePrivilege();

            mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
            //针对税的菜品优惠金额
            BigDecimal taxPrivilegeAmount = BigDecimal.ZERO;

            // 判断订单总金额是否大于优惠卷使用规则中的金额要求

            if (mCoupon != null && mCoupon.getFullValue() != null && mCoupon.getFullValue().compareTo(tradeAmount) <= 0) {

                mCouponPrivilegeVo.setActived(true);

                switch (mCoupon.getCouponType()) {

                    case REBATE:

                        // 满减卷

                        mTradePrivilege.setPrivilegeAmount(MathDecimal.round(mTradePrivilege.getPrivilegeValue(), 2).negate());
                        taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();
                        break;

                    case DISCOUNT:

                        // 折扣券

                        BigDecimal mPrivilegeAmount = tradeAmount.multiply(
                                MathDecimal.makeDiscountView(mTradePrivilege.getPrivilegeValue()));
                        taxPrivilegeAmount = mathVo.taxableInAmount.multiply(MathDecimal.makeDiscountView(mTradePrivilege.getPrivilegeValue()));

                        mPrivilegeAmount = MathDecimal.round(mPrivilegeAmount, 2).negate();

                        mTradePrivilege.setPrivilegeAmount(mPrivilegeAmount);

                        break;

                    case GIFT:

                        // 礼品卷

                        mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);

                        BigDecimal price = mCouponPrivilegeVo.getCoupon().getDiscountValue();

                        BigDecimal count = BigDecimal.ONE;

                        if (price != null && count != null) {

                            mTradePrivilege.setPrivilegeValue(MathDecimal.round(price.multiply(count), 2).negate());
                            taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();
                        }

                        break;

                    case CASH:

                        // 代金卷

                        // 如果订单金额大于等于代金卷金额时 PrivilegeAmount
                        // 等于 PrivilegeValue()
                        if (newTradeAmount.compareTo(mTradePrivilege.getPrivilegeValue()) >= 0) {

                            mTradePrivilege.setPrivilegeAmount(MathDecimal.round(mTradePrivilege.getPrivilegeValue(), 2).negate());

                        } else {

                            mTradePrivilege.setPrivilegeAmount(newTradeAmount.negate());

                        }
                        taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();
                        //如果订单金额为0时，设为无效 v8.15.0 雅座合并 0元菜品
                        /*if (KeyAt.getKeyAtType() != KeyAtType.YAZUO && BigDecimal.ZERO.compareTo(newTradeAmount) == 0) {
                            mCouponPrivilegeVo.setActived(false);
                        }*/
                        break;

                    default:

                        break;

                }

            } else {

                mCouponPrivilegeVo.setActived(false);

            }
            mathVo.taxableInAmount = mathVo.taxableInAmount.add(taxPrivilegeAmount);
            return mTradePrivilege.getPrivilegeAmount();
        }
        return null;
    }

    /**
     *
     *
     *

     *
     * @Title: mathExtraCharge
     *
     * @Description: 计算附加费
     *
     * @Param mTradeVo
     *
     * @Param @return 返回参与整单折扣的附加费金额
     *
     * @Return BigDecimal 返回类型
     *
     */
	
	/*public static Map<Integer, BigDecimal> mathExtraCharge(TradeVo mTradeVo,List<IShopcartItem> iShopcartItem,BigDecimal privilageAfaterAmount) {
		
		List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();
		
		Map<Long, TradePrivilege> temPrivileges = new HashMap<Long, TradePrivilege>();
		
		Map<Long, ExtraCharge> temMap = mTradeVo.getExtraChargeMap();
		
		if (temMap == null) {
			temMap=new HashMap<Long, ExtraCharge>();
		}

		if(listPrivilege==null){
			mTradeVo.setTradePrivileges(new ArrayList<TradePrivilege>());
			listPrivilege = mTradeVo.getTradePrivileges();
		}
		//end
		
		// 参与整单折扣的附加费总金额
		BigDecimal extraChageYes = BigDecimal.ZERO;
		
		// 附加费总金额
		BigDecimal extraChageNo = BigDecimal.ZERO;
		
		if (listPrivilege != null) {
			
			// 先移除所有附加费
			for (int i = listPrivilege.size() - 1; i >= 0; i--) {

				TradePrivilege mTradePrivilege = listPrivilege.get(i);
				// 该折扣信息是附加费折扣信息
				if (mTradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL
						&& mTradePrivilege.getStatusFlag() == StatusFlag.VALID) {
					temPrivileges.put(mTradePrivilege.getPromoId(), mTradePrivilege);
					if(mTradeVo.getMinconExtraCharge()!=null&&MathDecimal.isLongEqual(mTradePrivilege.getPromoId(),mTradeVo.getMinconExtraCharge().getId())){
						continue;
					}
					boolean outTimeFeeEnable= ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();
					ExtraCharge outFee=ServerSettingCache.getInstance().getmOutTimeRule();
					if(outTimeFeeEnable&&outFee!=null&&MathDecimal.isLongEqual(mTradePrivilege.getPromoId(),outFee.getId())){
						continue;
					}
					//服务端附加费被作废,作废tradePrivilege记录
					if(mTradePrivilege.getStatusFlag() == StatusFlag.VALID&&temMap.get(mTradePrivilege.getPromoId())==null){
						mTradePrivilege.setInValid();
					}
				}
				
			}
			
		}
		Map<Integer, BigDecimal> privilegeMap = new HashMap<Integer, BigDecimal>();

		// 添加所有附加费，其中temPrivileges记录已加入过的附加费，这种只需修改附加费金额
		for (Long key : temMap.keySet()) {
			
			ExtraCharge mExtraCharge = temMap.get(key);

			if(mExtraCharge.getCode()!=null&&mExtraCharge.getCode().equals(ExtraManager.mealFee)){
				TradePrivilege oldPrivilege=temPrivileges.get(mExtraCharge.getId());
				TradePrivilege boxTradePrivilege = BuildPrivilegeTool.buildBoxFee(mTradeVo,oldPrivilege, mExtraCharge);
				boxTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
				Map<String,IShopcartItem> temp = new HashMap<String,IShopcartItem>();
				for(IShopcartItem item : iShopcartItem){
					*//**
     * 如果item.getDishShop()为null表示下单后对已经参与附加费的菜品在后台进行了删除操作，然后在重新加入到购物车中是拿不到dishshop数据的。但这类商品是不需要重新计算餐盒费的，直接已经计算过了
     *//*
					if(item.getDishShop() != null && item.getDishShop().getBoxQty() != null && item.getDishShop().getBoxQty().compareTo(BigDecimal.ZERO) != 0){
						if(temp.get(item.getDishShop().getUuid()) == null){
							mathMealFee(mTradeVo,iShopcartItem, boxTradePrivilege, item, mExtraCharge);
						}
						temp.put(item.getDishShop().getUuid(), item);
					}
				}
				//是否参与折扣 1：是 2：否
				if (mExtraCharge.getDiscountFlag() == Bool.YES) {
					extraChageYes = extraChageYes.add(boxTradePrivilege.getPrivilegeAmount());
				}else{
					extraChageNo = extraChageNo.add(boxTradePrivilege.getPrivilegeAmount());
				}
				if(boxTradePrivilege.getId()==null&&oldPrivilege==null) {
					listPrivilege.add(boxTradePrivilege);
				}
			}else{
				// 其中temPrivileges.get(mExtraCharge.getId())也可能返回null，返回null表示该种附加费是新增附加费
				TradePrivilege oldPrivilege=temPrivileges.get(mExtraCharge.getId());
				//这儿不处理最低消费
				if(ExtraManager.isMinConsum(mExtraCharge))
					continue;
				//服务费，服务费不参与整单折扣，只计算商品金额
				TradePrivilege mTradePrivilege=null;
				if(isServiceCharge(mTradeVo.getTrade(),mExtraCharge)){
					//快餐不使用服务费
					if(mTradeVo.getTrade().getBusinessType()==BusinessType.SNACK||mTradeVo.getTrade().getBusinessType()==BusinessType.TAKEAWAY){
						continue;
					}
					mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
							oldPrivilege,
							mExtraCharge,mTradeVo.getTrade().getSaleAmount());
					privilegeMap.put(EXTRACHARGESERVICE,mTradePrivilege.getPrivilegeAmount());
				}else {
					mTradePrivilege = BuildPrivilegeTool.buildExtraChargePrivilege(mTradeVo,
							oldPrivilege,
							mExtraCharge,privilageAfaterAmount);
					// 是否参与折扣 1：是 2：否
					if (mExtraCharge.getDiscountFlag() == Bool.YES) {

						extraChageYes = extraChageYes.add(mTradePrivilege.getPrivilegeAmount());

					} else {

						extraChageNo = extraChageNo.add(mTradePrivilege.getPrivilegeAmount());

					}
				}

				if(mTradePrivilege!=null&&mTradePrivilege.getId()==null&&oldPrivilege==null) {
					listPrivilege.add(mTradePrivilege);
				}
			}
			
		}
		

		privilegeMap.put(EXTRACHARGEYES, extraChageYes);
		
		privilegeMap.put(EXTRACHARGENO, extraChageNo);

		//计算超时费
		privilegeMap=mathOutTimeExtraCharge(mTradeVo,privilegeMap);

		mTradeVo.setDiscountExtracharge(extraChageYes);

		return privilegeMap;
		
	}*/

    /**
     * 是否是服务费,快餐和外卖不使用服务费
     * @param mExtraCharge
     * @return
     */
	/*private static boolean isServiceCharge(Trade trade,ExtraCharge mExtraCharge){
		if(mExtraCharge.getCode()!=null&&mExtraCharge.getCode().equalsIgnoreCase(ExtraManager.SERVICE_CONSUM)&&mExtraCharge.isAutoJoinTrade()){
			return true;
		}
		return false;
	}*/


    /**
     * 超时费计算
     * @param mTradeVo
     * @param extraChargePrivilegeMap
     * @return
     */
	/*public static Map<Integer, BigDecimal> mathOutTimeExtraCharge(TradeVo mTradeVo,Map<Integer, BigDecimal> extraChargePrivilegeMap) {
		boolean outTimeEnable=ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

		if(!outTimeEnable){
			return extraChargePrivilegeMap;
		}


		ExtraCharge mOutTimeRule=ServerSettingCache.getInstance().getmOutTimeRule();

		// 参与整单折扣的附加费总金额
		BigDecimal extraChageYes = extraChargePrivilegeMap.get(EXTRACHARGEYES);

		// 附加费总金额
		BigDecimal extraChageNo = extraChargePrivilegeMap.get(EXTRACHARGENO);

		List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();

		for (TradePrivilege outTimePrivilege : listPrivilege) {
			if(outTimePrivilege.getPrivilegeType()==PrivilegeType.ADDITIONAL && outTimePrivilege.getPromoId()!=null && outTimePrivilege.getPromoId().longValue()==mOutTimeRule.getId().longValue()){
				// 是否参与折扣 1：是 2：否
				if (mOutTimeRule.getDiscountFlag() == Bool.YES) {
					extraChageYes = extraChageYes.add(outTimePrivilege.getPrivilegeAmount());
				} else {
					extraChageNo = extraChageNo.add(outTimePrivilege.getPrivilegeAmount());
				}
			}
		}

		extraChargePrivilegeMap.put(EXTRACHARGENO,extraChageNo);
		extraChargePrivilegeMap.put(EXTRACHARGEYES,extraChageYes);

		return extraChargePrivilegeMap;
	}*/

    /**
     *

     * @Title: mathMealFee
     * @Description: 计算餐盒费
     * @Param mTradeVo
     * @Param mTradePrivilege
     * @Param item TODO
     * @Return void 返回类型
     */
	/*public static void mathMealFee(TradeVo tradeVo,List<IShopcartItem> iShopcartItem, TradePrivilege mTradePrivilege, IShopcartItem item,ExtraCharge mExtraCharge){
		//一个点了多少个菜
		BigDecimal totalQty = BigDecimal.ZERO;
		DeliveryType deliveryType = tradeVo.getTrade().getDeliveryType();
		if(iShopcartItem != null){
			for(IShopcartItem mIShopcartItem : iShopcartItem){
				if(mIShopcartItem.getSkuUuid().equals(item.getSkuUuid()) && mIShopcartItem.getStatusFlag() == StatusFlag.VALID
						&& (((deliveryType == DeliveryType.HERE || deliveryType == DeliveryType.TAKE) && mIShopcartItem.getPack())
						||(deliveryType == DeliveryType.CARRY || deliveryType == DeliveryType.SEND ||tradeVo.getIsSalesReturn()))){//内用、自取有打包标记才计算、外带外送无单退直接计算
					totalQty = totalQty.add(mIShopcartItem.getTotalQty());
				}
			}
		}
		//需要餐盒的份数
		BigDecimal part = BigDecimal.ONE;
		//一组需配置的餐盒数量
		BigDecimal boxQty = item.getDishShop().getBoxQty();
		//多少个菜配置一个餐盒
		BigDecimal dishQtyp = item.getDishShop().getDishQty();
		
		
		//单菜数量大于配置一组餐盒费要求的菜品数据
		if(totalQty.compareTo(dishQtyp) > 0){
			//取余
			BigDecimal remainder = totalQty.divideAndRemainder(dishQtyp)[1];
			//整除
			BigDecimal count = totalQty.divideToIntegralValue(dishQtyp);
			//如count>0表示当前所点菜品数量满足搭配多次次餐盒,
			if(remainder.compareTo(BigDecimal.ZERO) == 0){
				part = count;
			}else{
				part = count.add(BigDecimal.ONE);
			}
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0){
			part = BigDecimal.ZERO;
		}
		boxQty = boxQty.multiply(part);
		
		if(mExtraCharge != null){
			BigDecimal amount = mTradePrivilege.getPrivilegeAmount();
			BigDecimal boxfee = boxQty.multiply(new BigDecimal(mExtraCharge.getContent()));
			amount = amount.add(boxfee);
			mTradePrivilege.setPrivilegeAmount(amount);
		}

	}*/

    /**
     * 获取订单菜品总额，不包含附加费和优惠
     *
     * @param mTrade
     * @param listItem
     */
    public static void setTradeDishAmount(Trade mTrade, List<TradeItem> listItem) {
        if (listItem != null) {
            BigDecimal amount = BigDecimal.ZERO;
            for (TradeItem item : listItem) {
                if (TextUtils.isEmpty(item.getParentUuid()) && item.getStatusFlag() != StatusFlag.INVALID) {
                    amount = amount.add(item.getActualAmount());
                }
            }
            mTrade.setDishAmount(amount);
        } else {
            mTrade.setDishAmount(BigDecimal.ZERO);
        }
    }

    /**
     * 计算宴请
     */
    public static BigDecimal mathBanquet(TradeVo tradeVo, BigDecimal totoalPrice) {
        if (tradeVo.getBanquetVo() == null || tradeVo.getBanquetVo().getTradePrivilege() == null) {
            return null;
        }
        TradePrivilege tradePrivilege = tradeVo.getBanquetVo().getTradePrivilege();
        if (tradePrivilege.isValid()) {
            tradePrivilege.setPrivilegeAmount(totoalPrice.negate());
            tradePrivilege.setPrivilegeValue(BigDecimal.ZERO);
            return totoalPrice.negate();
        }
        return null;
    }

    /**
     * @Title: covertItemPlanListToMap
     * @Description: 将list转换成map
     * @Param @param tradeItemPlanList
     * @Param @return TODO
     * @Return Map<String       ,       List       <       TradeItemPlanActivity>> 返回类型
     */
    private static Map<String, List<TradeItemPlanActivity>> covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        Map<String, List<TradeItemPlanActivity>> itemPlanMap = new HashMap<String, List<TradeItemPlanActivity>>();
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
            }
        }
        return itemPlanMap;
    }

    /**
     * @Title: mathPlayActivity
     * @Description: 计算营销活动
     * @Param @param dishAllAmount
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private static BigDecimal mathPlayActivity(BigDecimal dishAllAmout, TradeVo mTradeVo, List<IShopcartItem> iShopcartItem) {
        List<TradePlanActivity> palnAmount = mTradeVo.getTradePlanActivityList();
        if (palnAmount == null) {
            return dishAllAmout;
        }

        //能参与整单折扣与营销活动并存的菜品
        Map<String, IShopcartItem> allActivityItemMap = new HashMap<>();
        Map<String, IShopcartItem> discountActivityItemMap = new HashMap<>();
        for (IShopcartItem item : iShopcartItem) {
            allActivityItemMap.put(item.getUuid(), item);
            if (item.getStatusFlag() == StatusFlag.VALID && item.getEnableWholePrivilege() == Bool.YES) {
                discountActivityItemMap.put(item.getUuid(), item);
            }
        }

        //减去可参与整单折扣的促销的金额
        for (TradePlanActivity mTradePlanActivity : palnAmount) {
            if (mTradePlanActivity.getStatusFlag() == StatusFlag.VALID && mTradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                BigDecimal offerValue = mTradePlanActivity.getOfferValue();
                mPlanPrivilaegeAmount = mPlanPrivilaegeAmount.add(offerValue);

                Map<String, List<TradeItemPlanActivity>> tradeItemPlanActivityMap = covertItemPlanListToMap(mTradeVo.getTradeItemPlanActivityList());
                List<TradeItemPlanActivity> tradeItemPlanActivities = tradeItemPlanActivityMap.get(mTradePlanActivity.getUuid());
                if (Utils.isEmpty(tradeItemPlanActivities)) {
                    continue;
                }

                BigDecimal allDishAmount = BigDecimal.ZERO;//该活动下所有菜品金额
                BigDecimal isDiscountAll = BigDecimal.ZERO;//该活动下能参与于整单的金额

                List<IShopcartItem> privileageItems = new ArrayList<>(); //参与该活动的商品列表，用于计算优惠分摊

                for (TradeItemPlanActivity itemPlanActivity : tradeItemPlanActivities) {
                    allDishAmount = allDishAmount.add(allActivityItemMap.get(itemPlanActivity.getTradeItemUuid()).getActualAmount());
                    IShopcartItem item = discountActivityItemMap.get(itemPlanActivity.getTradeItemUuid());
                    if (item != null) {
                        privileageItems.add(item);
                        isDiscountAll = isDiscountAll.add(item.getActualAmount());
                    }
                }

                PrivilegeApportionManager.getInstance().updatePlanActivity(privileageItems,
                        mTradePlanActivity.getRuleId(),
                        mTradePlanActivity.getRuleName(),
                        offerValue.abs(),
                        isDiscountAll); //计算优惠分摊

                if (allDishAmount.compareTo(isDiscountAll) != 0) {
                    BigDecimal discountAmount = MathDecimal.divDown(isDiscountAll.multiply(offerValue), allDishAmount, 2);
                    dishAllAmout = dishAllAmout.add(discountAmount);
                } else {
                    dishAllAmout = dishAllAmout.add(offerValue);
                }
            }
        }
        return dishAllAmout;
    }

}
