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



public class MathShoppingCartTool {



    private static BigDecimal mPlanPrivilaegeAmount = BigDecimal.ZERO;
        public static BigDecimal couponBeforeAmount = BigDecimal.ZERO;




    public static void mathTotalPrice(List<IShopcartItem> iShopcartItem, TradeVo mTradeVo) {

        if (mTradeVo == null || mTradeVo.getTrade() == null) {
            return;
        }

        if (Utils.isNotEmpty(iShopcartItem)) {
            iShopcartItem.addAll(GiftShopcartItemSingleton.getInstance().getListPolicyDishshopVo());
        }

        mathTotalPriceLand(iShopcartItem, mTradeVo);

    }


    private static void mathTotalPriceLand(List<IShopcartItem> iShopcartItem, TradeVo mTradeVo) {

        PrivilegeApportionManager.getInstance().startMath();
                if (BaseShoppingCart.isHasValidBanquet(mTradeVo)) {
            BaseShoppingCart.removeAllPrivilige(mTradeVo, false, false, false);
        }
                initTradeMath(mTradeVo.getTrade());

        mPlanPrivilaegeAmount = BigDecimal.ZERO;
        MathVo mathVo = new MathVo();

        mathVo.saleAmount = mathMealShellAmount(mTradeVo);

        mathVo.dishAllAmout = mathVo.dishAllAmout.add(mathVo.saleAmount);
        mathItemAmount(mathVo, iShopcartItem);
                mathVo.taxableInAmount = mathVo.taxableInAmount.add(mathVo.saleAmount).add(mathVo.totalPrivilegeAmount).add(mathVo.noDiscPrivilegeAmout);

                mTradeVo.getTrade().setSaleAmount(mathVo.saleAmount);

                BigDecimal afterAmount = BigDecimal.ZERO;        afterAmount = afterAmount.add(mathVo.saleAmount).add(mathVo.noDiscPrivilegeAmout).add(mathVo.totalPrivilegeAmount);
                ExtraChargeTool extraChargeTool = ExtraChargeTool.countExtraCharge(mTradeVo, iShopcartItem, afterAmount);

                mathVo.dishAllAmout = mathVo.dishAllAmout.add(extraChargeTool.getExtraChargeYes());

                BigDecimal dishAllAmoutPlanBefore = mathVo.dishAllAmout.add(mathVo.noDiscountAllAmout).add(mathVo.noDiscPrivilegeAmout).add(mathVo.totalPrivilegeAmount);

        mathVo.dishAllAmout = mathPlayActivity(mathVo.dishAllAmout, mTradeVo, iShopcartItem);


                mathVo.dishAllAmout = mathVo.dishAllAmout.add(mathVo.totalPrivilegeAmount);
        if (mathVo.dishAllAmout.compareTo(BigDecimal.ZERO) <= 0) {            mathVo.dishAllAmout = BigDecimal.ZERO;
        }

        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mPlanPrivilaegeAmount);
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(mPlanPrivilaegeAmount);

                BigDecimal consumAmount = BigDecimal.ZERO;
        if (mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET
                || mTradeVo.getTrade().getBusinessType() == BusinessType.DINNER) {
            BigDecimal extrageNo = BigDecimal.ZERO;
            extrageNo = extraChargeTool.getExtraChargeNo();


            consumAmount = consumAmount.add(extrageNo).add(dishAllAmoutPlanBefore).add(mPlanPrivilaegeAmount);
            consumAmount = mathMinConsum(consumAmount, mTradeVo);
            mathVo.saleAmount = mathVo.saleAmount.add(consumAmount);
        }


                BigDecimal privilegeDishAmount = BigDecimal.ZERO;
                TradePrivilege mTradePrivilege = mTradeVo.getTradePrivilege();
        if (mTradePrivilege != null && mTradePrivilege.isValid()) {

            BigDecimal privilegeValue = mTradePrivilege.getPrivilegeValue();

            BigDecimal privilegeAmount = BigDecimal.ZERO;
            BigDecimal privilegeAmountBefore = BigDecimal.ZERO;
            privilegeAmountBefore = privilegeAmountBefore.add(mathVo.dishAllAmout).add(consumAmount);

            switch (mTradePrivilege.getPrivilegeType()) {

                case DISCOUNT:

                                        privilegeAmount = MathDecimal.trimZero(
                            MathDecimal.round(privilegeAmountBefore.multiply(MathDecimal.getDiscountValue(privilegeValue)), 2));
                    if (TextUtils.isEmpty(mTradePrivilege.getPrivilegeName())) {
                        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.order_all_discount));
                    }
                    privilegeDishAmount = MathDecimal.round(mathVo.taxableInAmount.multiply(MathDecimal.getDiscountValue(privilegeValue)), 2);
                    break;

                case REBATE:
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
                                        privilegeAmount = dishAllAmoutPlanBefore.add(extraChargeTool.getExtraChargeNo());

                    privilegeAmount = privilegeAmount.add(consumAmount);
                    privilegeAmount = privilegeAmount.add(mPlanPrivilaegeAmount);
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

                        PrivilegeApportionManager.getInstance().updateWholePrivilege(iShopcartItem,
                    privilegeAmount.abs(),
                    mTradePrivilege.getPrivilegeName(),
                    mathVo.taxableInAmount);
        }
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(privilegeDishAmount.negate());
        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mathVo.noDiscPrivilegeAmout);


                BigDecimal dishAmount=mathVo.saleAmount.add(mathVo.totalPrivilegeAmount);        calculateChargePrivile(mTradeVo,mathVo,dishAmount);
        mathVo.taxableInAmount = mathVo.taxableInAmount.add(mathVo.chargePriviegeAmount.negate());
        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(mathVo.chargePriviegeAmount.negate());

                mathVo.saleAmount = mathVo.saleAmount.add(extraChargeTool.getExtraChargeYes());
        mathVo.saleAmount = mathVo.saleAmount.add(extraChargeTool.getExtraChargeNo());
        Trade trade = mTradeVo.getTrade();

                mTradeVo.setBeforePrivilegeAmount(mathVo.saleAmount);
                trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

        trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

        trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));
        couponBeforeAmount = trade.getTradeAmount();
                BigDecimal totalCouponAmount = mathCouponPrivilege(iShopcartItem, mTradeVo, mathVo);
        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(totalCouponAmount);

        mathWeixinCode(mTradeVo, mathVo);

        mathIntegral(mTradeVo, mathVo);



        BigDecimal serviceAmount = extraChargeTool.chargeService(mathVo.saleAmount, mathVo.saleAmount.add(mathVo.totalPrivilegeAmount));
                if (mTradePrivilege != null && mTradePrivilege.isValid() && mTradePrivilege.getPrivilegeType() == PrivilegeType.FREE) {
            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(serviceAmount.negate());
            mTradePrivilege.setPrivilegeAmount(mTradePrivilege.getPrivilegeAmount().add(serviceAmount.negate()));
        }

        mathVo.saleAmount = mathVo.saleAmount.add(serviceAmount);
        BigDecimal totalPrice = mathVo.saleAmount.add(mathVo.totalPrivilegeAmount);
                BigDecimal banquetPrivilege = mathBanquet(mTradeVo, MathDecimal.round(totalPrice, 2));
        if (banquetPrivilege != null) {
            mathVo.totalPrivilegeAmount = banquetPrivilege;
            mathVo.taxableInAmount = BigDecimal.ZERO;
        }
        trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

                BigDecimal taxAmount = mathTax(mTradeVo, mathVo.saleAmount, mathVo.saleAmount.add(mathVo.totalPrivilegeAmount));
                mathVo.saleAmount = mathVo.saleAmount.add(taxAmount);
        if (mTradeVo.getTradeDeposit() != null && mTradeVo.getTradeDeposit().getStatusFlag() == StatusFlag.VALID) {
            mathVo.saleAmount = mathVo.saleAmount.add(mTradeVo.getTradeDeposit().getDepositPay());
        }

                totalPrice = mathVo.saleAmount.add(mathVo.totalPrivilegeAmount);

        trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

        trade.setTradeAmountBefore(MathDecimal.round(totalPrice, 2));

                trade.setTradeAmount(getAmountByCarryLimit(totalPrice));

        BigDecimal noJoinExtra = extraChargeTool.getExtraChargeYes();
        if (noJoinExtra != null) {
            mTradeVo.setNoJoinDiscount(mathVo.noDiscountAllAmout.add(noJoinExtra));
        } else {
            mTradeVo.setNoJoinDiscount(mathVo.noDiscountAllAmout);
        }

        mTradeVo.itemApportionList = PrivilegeApportionManager.getInstance().getApportionList();
        PrivilegeApportionManager.getInstance().finishMath();     }



    private static void calculateChargePrivile(TradeVo tradeVo,MathVo mathVo,BigDecimal dishAmount){
        TradePrivilege mTradePrivilege = tradeVo.getTradeChargePrivilege();
        BigDecimal privilegeAmount=BigDecimal.ZERO;
        if(mTradePrivilege!=null){
                        switch (mTradePrivilege.getPrivilegeType()){
                case CHARGE_DISCOUNT:                    privilegeAmount=dishAmount.multiply(MathDecimal.getDiscountValue(mTradePrivilege.getPrivilegeValue().multiply(BigDecimal.TEN)));
                    break;
                case CHARGE_REBATE:                    privilegeAmount=mTradePrivilege.getPrivilegeValue();
                    break;
            }

            privilegeAmount=MathDecimal.round(privilegeAmount, 2);
            mathVo.chargePriviegeAmount=privilegeAmount;
            mTradePrivilege.setPrivilegeAmount(privilegeAmount.negate());
            mTradePrivilege.setChanged(true);

        }
    }



    private static void initTradeMath(Trade trade) {
        trade.setSaleAmount(BigDecimal.ZERO);
        trade.setPrivilegeAmount(BigDecimal.ZERO);
        trade.setTradeAmountBefore(BigDecimal.ZERO);
        trade.setTradeAmount(BigDecimal.ZERO);
    }


    private static void mathWeixinCode(TradeVo mTradeVo, MathVo mathVo) {
        Trade trade = mTradeVo.getTrade();
        List<WeiXinCouponsVo> listWX = mTradeVo.getmWeiXinCouponsVo();
        if (listWX != null) {
            for (WeiXinCouponsVo wx : listWX) {
                                                trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));
                trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));
                trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));
                if (wx.getmWeiXinCouponsInfo() != null && !wx.isUsed()) {

                                        BigDecimal leasetCost = wx.getmWeiXinCouponsInfo().getCash().getLeast_cost();
                    if (trade.getTradeAmount().compareTo(leasetCost) >= 0) {
                        TradePrivilege mTP = wx.getmTradePrivilege();
                        BigDecimal reduceCost = wx.getmWeiXinCouponsInfo().getCash().getReduce_cost();

                        mTP.setPrivilegeValue(reduceCost);
                        BigDecimal privilegeAmount = getCorrectPrivilegeAmount(trade.getTradeAmount(), reduceCost);
                        mTP.setPrivilegeAmount(privilegeAmount);
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
                                        TradePrivilege mTP = wx.getmTradePrivilege();
                    if (mTP != null && mTP.isSaveServer() && mTP.isValid()) {
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
                                                BaseShoppingCart.removeWeiXinCouponsVo(mTradeVo, wx);
                    }
                }
            }
        }
    }


    private static void mathIntegral(TradeVo mTradeVo, MathVo mathVo) {
        Trade trade = mTradeVo.getTrade();
        IntegralCashPrivilegeVo mIntegralCashPrivilegeVo = mTradeVo.getIntegralCashPrivilegeVo();
        if (mIntegralCashPrivilegeVo != null && mIntegralCashPrivilegeVo.getTradePrivilege() != null
                && mIntegralCashPrivilegeVo.isValid()) {

                        trade.setSaleAmount(MathDecimal.round(mathVo.saleAmount, 2));

            trade.setPrivilegeAmount(MathDecimal.round(mathVo.totalPrivilegeAmount, 2));

            trade.setTradeAmount(MathDecimal.round(mathVo.saleAmount.add(mathVo.totalPrivilegeAmount), 2));

            if (mIntegralCashPrivilegeVo.isUsed() && mIntegralCashPrivilegeVo.isValid()) {
                                mIntegralCashPrivilegeVo.setActived(true);
            } else if (mIntegralCashPrivilegeVo.getIntegral() != null
                    && mIntegralCashPrivilegeVo.getIntegral().compareTo(BigDecimal.ZERO) > 0
                    && mIntegralCashPrivilegeVo.hasRule()) {

                BuildPrivilegeTool.mathPrivilege(mIntegralCashPrivilegeVo, mTradeVo);
            } else {
                mIntegralCashPrivilegeVo.setActived(false);
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

                                if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null
                        && item.getCouponPrivilegeVo().getTradePrivilege().getStatusFlag() == StatusFlag.VALID) {
                    if (item.getCouponPrivilegeVo().isActived()) {
                        if (item.getEnableWholePrivilege() == Bool.NO) {
                            mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                            mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                        } else {
                            mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());                            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                        }
                        PrivilegeApportionManager.getInstance().updateSingleCouponPrivilege(item);                     } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());
                    }

                } else if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                                        if (item.getEnableWholePrivilege() == Bool.NO) {
                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                        mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getCardServicePrivilgeVo().getTradePrivilege().getPrivilegeAmount());
                    } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getCardServicePrivilgeVo().getTradePrivilege().getPrivilegeAmount());
                    }
                } else if (item.getAppletPrivilegeVo() != null && item.getAppletPrivilegeVo().isPrivilegeValid()) {
                                        if (item.getEnableWholePrivilege() == Bool.NO) {
                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());
                        mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getAppletPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                    } else {
                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());                        mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getAppletPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
                    }
                } else {
                                        if (item.getEnableWholePrivilege() == Bool.NO) {

                        mathVo.noDiscountAllAmout = mathVo.noDiscountAllAmout.add(item.getActualAmount());

                        if (item.getPrivilege() != null && item.getPrivilege().getPrivilegeAmount() != null
                                && item.getPrivilege().isValid()) {

                            mathVo.noDiscPrivilegeAmout = mathVo.noDiscPrivilegeAmout.add(item.getPrivilege().getPrivilegeAmount());
                            PrivilegeApportionManager.getInstance().updateSingleDiscount(item);                         }

                    } else {

                        mathVo.dishAllAmout = mathVo.dishAllAmout.add(item.getActualAmount());

                        if (item.getPrivilege() != null && item.getPrivilege().getPrivilegeAmount() != null
                                && item.getPrivilege().isValid()) {

                            mathVo.totalPrivilegeAmount = mathVo.totalPrivilegeAmount.add(item.getPrivilege().getPrivilegeAmount());
                            PrivilegeApportionManager.getInstance().updateSingleDiscount(item);                         }

                    }
                }


            }

        }
    }


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
                        tradeTax.setTaxType(taxRateInfo.getTaxCode());
            tradeTax.setTaxPlan(taxRateInfo.getTaxRate().toString());
            tradeTax.setTaxTypeName(taxRateInfo.getTaxDesc());
            tradeTax.setEffectType(taxRateInfo.getEffectType());
            tradeTax.setDiscountType(taxRateInfo.getDiscountType());
            tradeTax.setTaxKind(taxRateInfo.getTaxKind());
            tradeTax.setTaxMethod(taxRateInfo.getTaxMethod());
        }

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



    public static BigDecimal getAmountByCarryLimit(BigDecimal totalPrice) {
                int limit = ServerSettingManager.getCarryLimit();

                CarryBitRule mCarryBitRule = ServerSettingManager.getCarryRule();
        if (mCarryBitRule == CarryBitRule.ROUND_UP) {            totalPrice = MathDecimal.round(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.CARRY) {            totalPrice = MathDecimal.roundUp(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.MALING) {            totalPrice = MathDecimal.roundDown(totalPrice, limit);
        } else if (mCarryBitRule == CarryBitRule.THREE_EIGHT_UP) {
            totalPrice = MathDecimal.threeEightUp(totalPrice, limit);
        }
                return totalPrice;
    }

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


    public static BigDecimal mathMealShellAmount(TradeVo tradeVo) {
        if (tradeVo.getMealShellVo() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal price = tradeVo.getMealShellVo().getActualAmount();
        return price;
    }


    private static BigDecimal getCorrectPrivilegeAmount(BigDecimal tradeAmount, BigDecimal privilegeAmount) {
        BigDecimal newTradeAmount = tradeAmount.subtract(privilegeAmount);
        if (newTradeAmount.compareTo(BigDecimal.ZERO) < 0) {
            return tradeAmount.negate();
        }
        return privilegeAmount.negate();
    }



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
                                if (mCouponPrivilegeVo.isUsed()) {
                    privilegeAmount = mCouponPrivilegeVo.getTradePrivilege().getPrivilegeAmount();
                } else {
                    privilegeAmount = mathCoupon(mCouponPrivilegeVo, oldTradeAmount, newTradeAmount, mathVo);
                }
                if (privilegeAmount != null) {

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


    private static BigDecimal mathCoupon(CouponPrivilegeVo mCouponPrivilegeVo, BigDecimal tradeAmount, BigDecimal newTradeAmount, MathVo mathVo) {
        if (mCouponPrivilegeVo != null) {

            Coupon mCoupon = mCouponPrivilegeVo.getCoupon();

            TradePrivilege mTradePrivilege = mCouponPrivilegeVo.getTradePrivilege();

            mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
                        BigDecimal taxPrivilegeAmount = BigDecimal.ZERO;


            if (mCoupon != null && mCoupon.getFullValue() != null && mCoupon.getFullValue().compareTo(tradeAmount) <= 0) {

                mCouponPrivilegeVo.setActived(true);

                switch (mCoupon.getCouponType()) {

                    case REBATE:


                        mTradePrivilege.setPrivilegeAmount(MathDecimal.round(mTradePrivilege.getPrivilegeValue(), 2).negate());
                        taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();
                        break;

                    case DISCOUNT:


                        BigDecimal mPrivilegeAmount = tradeAmount.multiply(
                                MathDecimal.makeDiscountView(mTradePrivilege.getPrivilegeValue()));
                        taxPrivilegeAmount = mathVo.taxableInAmount.multiply(MathDecimal.makeDiscountView(mTradePrivilege.getPrivilegeValue()));

                        mPrivilegeAmount = MathDecimal.round(mPrivilegeAmount, 2).negate();

                        mTradePrivilege.setPrivilegeAmount(mPrivilegeAmount);

                        break;

                    case GIFT:


                        mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);

                        BigDecimal price = mCouponPrivilegeVo.getCoupon().getDiscountValue();

                        BigDecimal count = BigDecimal.ONE;

                        if (price != null && count != null) {

                            mTradePrivilege.setPrivilegeValue(MathDecimal.round(price.multiply(count), 2).negate());
                            taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();
                        }

                        break;

                    case CASH:


                                                                        if (newTradeAmount.compareTo(mTradePrivilege.getPrivilegeValue()) >= 0) {

                            mTradePrivilege.setPrivilegeAmount(MathDecimal.round(mTradePrivilege.getPrivilegeValue(), 2).negate());

                        } else {

                            mTradePrivilege.setPrivilegeAmount(newTradeAmount.negate());

                        }
                        taxPrivilegeAmount = mTradePrivilege.getPrivilegeAmount();

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


    private static Map<String, List<TradeItemPlanActivity>> covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        Map<String, List<TradeItemPlanActivity>> itemPlanMap = new HashMap<String, List<TradeItemPlanActivity>>();
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


    private static BigDecimal mathPlayActivity(BigDecimal dishAllAmout, TradeVo mTradeVo, List<IShopcartItem> iShopcartItem) {
        List<TradePlanActivity> palnAmount = mTradeVo.getTradePlanActivityList();
        if (palnAmount == null) {
            return dishAllAmout;
        }

                Map<String, IShopcartItem> allActivityItemMap = new HashMap<>();
        Map<String, IShopcartItem> discountActivityItemMap = new HashMap<>();
        for (IShopcartItem item : iShopcartItem) {
            allActivityItemMap.put(item.getUuid(), item);
            if (item.getStatusFlag() == StatusFlag.VALID && item.getEnableWholePrivilege() == Bool.YES) {
                discountActivityItemMap.put(item.getUuid(), item);
            }
        }

                for (TradePlanActivity mTradePlanActivity : palnAmount) {
            if (mTradePlanActivity.getStatusFlag() == StatusFlag.VALID && mTradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                BigDecimal offerValue = mTradePlanActivity.getOfferValue();
                mPlanPrivilaegeAmount = mPlanPrivilaegeAmount.add(offerValue);

                Map<String, List<TradeItemPlanActivity>> tradeItemPlanActivityMap = covertItemPlanListToMap(mTradeVo.getTradeItemPlanActivityList());
                List<TradeItemPlanActivity> tradeItemPlanActivities = tradeItemPlanActivityMap.get(mTradePlanActivity.getUuid());
                if (Utils.isEmpty(tradeItemPlanActivities)) {
                    continue;
                }

                BigDecimal allDishAmount = BigDecimal.ZERO;                BigDecimal isDiscountAll = BigDecimal.ZERO;
                List<IShopcartItem> privileageItems = new ArrayList<>();
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
                        isDiscountAll);
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
