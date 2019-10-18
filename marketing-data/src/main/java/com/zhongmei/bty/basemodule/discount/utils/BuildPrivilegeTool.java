package com.zhongmei.bty.basemodule.discount.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.enums.ExtraChargeCalcWay;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.enums.ChargePrivilegeType;
import com.zhongmei.yunfu.db.enums.MemberPrivilegeType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.commonmodule.database.enums.LimitType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuildPrivilegeTool {


    public static TradePrivilege buildPrivilege(IShopcartItemBase mShopcartItemBase, String uuid) {

        TradePrivilege mPrivilege = mShopcartItemBase.getPrivilege();

        if (mPrivilege != null) {
            mPrivilege.validateCreate();
            mPrivilege.setUuid(SystemUtils.genOnlyIdentifier());
            mPrivilege.setTradeUuid(uuid);
            mPrivilege.setTradeItemUuid(mShopcartItemBase.getUuid());
            mPrivilege.setCreatorId(Session.getAuthUser().getId());
            mPrivilege.setCreatorName(Session.getAuthUser().getName());

            int privilegeType = mPrivilege.getPrivilegeType().value();

            BigDecimal dishDiscountValue = mPrivilege.getPrivilegeValue();
                        BigDecimal costValue = mShopcartItemBase.getActualAmount();

            if (privilegeType == PrivilegeType.DISCOUNT.value()) {

                BigDecimal discountPrice = costValue.multiply(MathDecimal.getDiscountValue(dishDiscountValue));
                discountPrice = MathDecimal.trimZero(discountPrice);
                mPrivilege.setPrivilegeAmount(MathDecimal.round(discountPrice, 2).negate());
                mPrivilege.setPrivilegeType(PrivilegeType.DISCOUNT);
                mPrivilege.setPrivilegeValue(dishDiscountValue);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.dish_discount));
                }
            } else if (privilegeType == PrivilegeType.REBATE.value()) {
                mPrivilege.setPrivilegeAmount(MathDecimal.round(dishDiscountValue, 2).negate());
                mPrivilege.setPrivilegeType(PrivilegeType.REBATE);
                mPrivilege.setPrivilegeValue(dishDiscountValue);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.dish_discount_let));
                }
            } else if (privilegeType == PrivilegeType.FREE.value()) {
                BigDecimal value = MathDecimal.round(costValue, 2);
                mPrivilege.setPrivilegeValue(value);
                mPrivilege.setPrivilegeAmount(value.negate());
                mPrivilege.setPrivilegeType(PrivilegeType.FREE);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.order_free));
                }
            } else if (privilegeType == PrivilegeType.GIVE.value()) {
                BigDecimal value = MathDecimal.round(costValue, 2);
                mPrivilege.setPrivilegeValue(value);
                mPrivilege.setPrivilegeAmount(value.negate());
                mPrivilege.setPrivilegeType(PrivilegeType.GIVE);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.dish_free));
                }
            } else if (privilegeType == PrivilegeType.COUPON.value()) {
                costValue = mShopcartItemBase.getActualAmount().multiply(mShopcartItemBase.getSingleQty());
                BigDecimal value = MathDecimal.round(costValue, 2);
                mPrivilege.setPromoId(mShopcartItemBase.getCouponPrivilegeVo().getCoupon().getId());
                mPrivilege.setPrivilegeValue(value);
                mPrivilege.setPrivilegeAmount(value.negate());
                mPrivilege.setPrivilegeType(PrivilegeType.COUPON);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.coupon_type_gift));
                }
            } else if (privilegeType == PrivilegeType.PROBLEM.value()) {
                mPrivilege.setPrivilegeAmount(MathDecimal.round(dishDiscountValue, 2).negate());
                mPrivilege.setPrivilegeType(PrivilegeType.PROBLEM);
                mPrivilege.setPrivilegeValue(dishDiscountValue);
                if (TextUtils.isEmpty(mPrivilege.getPrivilegeName())) {
                    mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.dish_problems));
                }
            }else if(privilegeType == PrivilegeType.AUTO_DISCOUNT.value()){                BigDecimal privilegeAmount = mShopcartItemBase.getActualAmount().multiply(MathDecimal.getDiscountValue(mPrivilege.getPrivilegeValue().multiply(BigDecimal.TEN))).negate();
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }else if(privilegeType == PrivilegeType.MEMBER_PRICE.value()){
                BigDecimal  privilegeAmount =  new BigDecimal(mPrivilege.getPrivilegeValue().toString()).multiply(mShopcartItemBase.getTotalQty());
                privilegeAmount = privilegeAmount.subtract(mShopcartItemBase.getActualAmount());
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }else if(privilegeType == PrivilegeType.MEMBER_REBATE.value()){
                BigDecimal  privilegeAmount = new BigDecimal(mPrivilege.getPrivilegeValue().toString()).multiply(mShopcartItemBase.getTotalQty());
                privilegeAmount = MathDecimal.round(privilegeAmount, 2).negate();                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }
            correctGroupDishPrivilege(mShopcartItemBase);
        } else {
            mPrivilege = null;
        }

        return mPrivilege;
    }


    private static void correctGroupDishPrivilege(IShopcartItemBase mIShopcartItemBase) {
        if (mIShopcartItemBase.isGroupDish() && mIShopcartItemBase.getPrivilege() != null && mIShopcartItemBase.getId() == null) {
            mIShopcartItemBase.getPrivilege().setPrivilegeAmount(BigDecimal.ZERO);
            mIShopcartItemBase.getPrivilege().setPrivilegeValue(BigDecimal.ZERO);
        }
    }





    public static void buildCouponPrivilege(TradeVo mTradeVo, CouponPrivilegeVo mCouponPrivilegeVo) {
        if (mCouponPrivilegeVo.getTradePrivilege() == null) {
            return;
        }
        mCouponPrivilegeVo.setActived(false);
        mCouponPrivilegeVo.getTradePrivilege().setPrivilegeType(PrivilegeType.COUPON);
        mCouponPrivilegeVo.getTradePrivilege().setPrivilegeName(mCouponPrivilegeVo.getCoupon().getName());

        if (Utils.isEmpty(mTradeVo.getCouponPrivilegeVoList())) {
            mTradeVo.setCouponPrivilegeVoList(new ArrayList<CouponPrivilegeVo>());
        }



        List<CouponPrivilegeVo> couponPrivilegeVoList = mTradeVo.getCouponPrivilegeVoList();
        Long promoId = mCouponPrivilegeVo.getTradePrivilege().getPromoId();
        for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
            if (couponPrivilegeVo.getTradePrivilege() == null) {
                continue;
            }
                        if (promoId.equals(couponPrivilegeVo.getTradePrivilege().getPromoId())) {
                resetCouponPrivilege(mTradeVo, mCouponPrivilegeVo, couponPrivilegeVo);
                return;
            }
        }
                mCouponPrivilegeVo.getTradePrivilege().setUuid(SystemUtils.genOnlyIdentifier());
        mCouponPrivilegeVo.getTradePrivilege().validateCreate();
        mCouponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
        couponPrivilegeVoList.add(mCouponPrivilegeVo);
    }


    private static void resetCouponPrivilege(TradeVo mTradeVo, CouponPrivilegeVo mCouponPrivilegeVo, CouponPrivilegeVo couponPrivilegeVo) {
        couponPrivilegeVo.getTradePrivilege().validateUpdate();
        couponPrivilegeVo.getTradePrivilege().setStatusFlag(StatusFlag.VALID);
        couponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
        couponPrivilegeVo.getTradePrivilege().setTradeId(mTradeVo.getTrade().getId());
        couponPrivilegeVo.getTradePrivilege().setPrivilegeType(PrivilegeType.COUPON);
        couponPrivilegeVo.getTradePrivilege().setPrivilegeName(mCouponPrivilegeVo.getCoupon().getName());
        couponPrivilegeVo.getTradePrivilege().setPromoId(mCouponPrivilegeVo.getTradePrivilege().getPromoId());
    }


    public static void buildCashPrivilege(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo, TradeVo mTradeVo) {
        mIntegralCashPrivilegeVo.setActived(false);
        TradePrivilege mTradePrivilege = new TradePrivilege();
        mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        mTradePrivilege.validateCreate();
        mTradePrivilege.setCreatorId(Session.getAuthUser().getId());
        mTradePrivilege.setCreatorName(Session.getAuthUser().getName());
        mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradePrivilege.setPrivilegeType(PrivilegeType.INTEGRALCASH);
        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.intergral_privilege));
        mTradePrivilege.setPromoId(mIntegralCashPrivilegeVo.getRuleId());
        if (mTradeVo.getIntegralCashPrivilegeVo() != null && mTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege() != null && mTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege().getId() != null) {
            copyServerTradePrivilege(mTradePrivilege, mTradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
        }
        mIntegralCashPrivilegeVo.setTradePrivilege(mTradePrivilege);
        mTradeVo.setIntegralCashPrivilegeVo(mIntegralCashPrivilegeVo);
    }


    public static void buildBanquetPrivilege(BanquetVo banquetVo, TradeVo mTradeVo) {
        TradePrivilege mTradePrivilege = null;
        if (mTradeVo.getBanquetVo() != null && mTradeVo.getBanquetVo().getTradePrivilege() != null) {
            mTradePrivilege = mTradeVo.getBanquetVo().getTradePrivilege();
        } else {
            mTradePrivilege = new TradePrivilege();
            mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        }
        mTradePrivilege.validateCreate();
        mTradePrivilege.setCreatorId(Session.getAuthUser().getId());
        mTradePrivilege.setCreatorName(Session.getAuthUser().getName());
        mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradePrivilege.setPrivilegeType(PrivilegeType.BANQUET);
        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.banquet_privilege));
        banquetVo.setTradePrivilege(mTradePrivilege);
        mTradeVo.setBanquetVo(banquetVo);
    }


    public static void mathPrivilege(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo, TradeVo mTradeVo) {
        mIntegralCashPrivilegeVo.setActived(false);
        BigDecimal integra = mIntegralCashPrivilegeVo.getUseInteger();         if (integra == null || mIntegralCashPrivilegeVo.getConvertValue() == null) {
            return;
        }
        BigDecimal privilegeValue = BigDecimal.ZERO;
        BigDecimal privilegeAmount = BigDecimal.ZERO;

        BigDecimal orderTotalPrice = mTradeVo.getTrade().getTradeAmount();

                BigDecimal maxIntegralMulriple = integra.divideToIntegralValue(mIntegralCashPrivilegeVo.getConvertValue());
                BigDecimal maxCashPrice = maxIntegralMulriple.multiply(BigDecimal.ONE);                BigDecimal maxTotalIntegral = maxIntegralMulriple.multiply(mIntegralCashPrivilegeVo.getConvertValue());

                BigDecimal cashShare = orderTotalPrice.divideToIntegralValue(BigDecimal.ONE);

                if (integra.compareTo(mIntegralCashPrivilegeVo.getConvertValue()) < 0) {
            mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeValue(privilegeValue);
            mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeAmount(MathDecimal.round(privilegeAmount, 2).negate());
            return;
        }

        if (orderTotalPrice.compareTo(BigDecimal.ONE) >= 0) {
                        if (cashShare.compareTo(maxIntegralMulriple) >= 0) {
                privilegeAmount = maxIntegralMulriple;
                privilegeValue = maxTotalIntegral;
            } else {
                privilegeAmount = cashShare.multiply(BigDecimal.ONE);                privilegeValue = cashShare.multiply(mIntegralCashPrivilegeVo.getConvertValue());
            }
            mIntegralCashPrivilegeVo.setActived(true);
        }


        mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeValue(privilegeValue);
        mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeAmount(MathDecimal.round(privilegeAmount, 2).negate());
    }


    public static TradePrivilege buildChargePrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer){
        BigDecimal tradeAmout= DinnerShoppingCart.getInstance().getTradeAmoutCanDiscount(mShoppingCartVo);
        BigDecimal fullValue=mCustomer.storedFullAmount;
        ChargePrivilegeType type=mCustomer.getStoredPrivilegeType();

                if(tradeAmout.compareTo(fullValue)<=0 || type==null){
            return null;
        }


        BigDecimal privilegeValue=mCustomer.storedPrivilegeValue;

        TradePrivilege chargePrivilege=new TradePrivilege();
        chargePrivilege.validateCreate();
        chargePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        chargePrivilege.setTradeUuid(mShoppingCartVo.getmTradeVo().getTrade().getUuid());
        chargePrivilege.setCreatorId(Session.getAuthUser().getId());
        chargePrivilege.setCreatorName(Session.getAuthUser().getName());

        chargePrivilege.setPrivilegeValue(privilegeValue);

        BigDecimal mPrivilegeAmount=BigDecimal.ZERO;

        switch (type){
            case DISCOUNT:
                chargePrivilege.setPrivilegeType(PrivilegeType.CHARGE_DISCOUNT);
                mPrivilegeAmount=tradeAmout.multiply(MathDecimal.getDiscountValue(privilegeValue.multiply(BigDecimal.TEN)));
                chargePrivilege.setPrivilegeName(String.format(BaseApplication.sInstance.getResources().getString(R.string.member_dish_charge_discount),privilegeValue.doubleValue()+""));
                break;
            case REBATE:
                chargePrivilege.setPrivilegeType(PrivilegeType.CHARGE_REBATE);
                mPrivilegeAmount=privilegeValue;
                chargePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_charge_rebate));
                break;
        }

        mPrivilegeAmount=MathDecimal.round(mPrivilegeAmount, 2).negate();        chargePrivilege.setPrivilegeAmount(mPrivilegeAmount);

        return chargePrivilege;
    }


    public static TradePrivilege mathMemberPrice(IShopcartItemBase mIShopcartItemBase, DishMemberPrice mDishMemberPrice,
                                                 String tradeUUID) {
        TradePrivilege mTradePrivilege = new TradePrivilege();

        mTradePrivilege.validateCreate();
        mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        mTradePrivilege.setTradeUuid(tradeUUID);
        mTradePrivilege.setTradeItemUuid(mIShopcartItemBase.getUuid());
        mTradePrivilege.setCreatorId(Session.getAuthUser().getId());
        mTradePrivilege.setCreatorName(Session.getAuthUser().getName());

        BigDecimal privilegeValue = BigDecimal.ZERO;
        BigDecimal privilegeAmount = BigDecimal.ZERO;

        BigDecimal amount = mIShopcartItemBase.getAmount();

        switch(mDishMemberPrice.getPriceType()){
            case DISCOUNT:                privilegeValue = new BigDecimal(mDishMemberPrice.getDiscount().toString());
                privilegeValue = privilegeValue.multiply(BigDecimal.TEN);
                privilegeAmount = amount.multiply(MathDecimal.getDiscountValue(privilegeValue)).negate();
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mTradePrivilege.setPrivilegeType(PrivilegeType.AUTO_DISCOUNT);
                mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_discount));
                break;
            case REBATE:                if (mDishMemberPrice.getMemberPrice() == null) {
                    privilegeValue = amount;
                } else {
                    privilegeValue = new BigDecimal(mDishMemberPrice.getMemberPrice().toString()).multiply(mIShopcartItemBase.getTotalQty());
                }
                privilegeAmount = MathDecimal.round(privilegeValue, 2).negate();                mTradePrivilege.setPrivilegeType(PrivilegeType.MEMBER_REBATE);
                mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_rebate));
                break;
            case PRICE:                 if (mDishMemberPrice.getMemberPrice() == null) {
                    privilegeValue = amount;
                } else {
                    privilegeValue =
                            new BigDecimal(mDishMemberPrice.getMemberPrice().toString()).multiply(mIShopcartItemBase.getTotalQty());
                }
                privilegeAmount = privilegeValue.subtract(amount);
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mTradePrivilege.setPrivilegeType(PrivilegeType.MEMBER_PRICE);
                mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_price));
                break;
        }

        mTradePrivilege.setPrivilegeValue(new BigDecimal(mDishMemberPrice.getDiscount()));        mTradePrivilege.setPrivilegeAmount(privilegeAmount);
        return mTradePrivilege;
    }



    public static TradePrivilege buildExtraChargePrivilege(TradeVo mTradeVo, TradePrivilege privilege,
                                                           ExtraCharge mExtraCharge, BigDecimal saleAmount) {
        if (privilege == null) {

            privilege = new TradePrivilege();

            privilege.validateCreate();

            privilege.setUuid(SystemUtils.genOnlyIdentifier());
            privilege.setTradeId(mTradeVo.getTrade().getId());
            privilege.setTradeUuid(mTradeVo.getTrade().getUuid());

            privilege.setCreatorId(Session.getAuthUser().getId());

            privilege.setCreatorName(Session.getAuthUser().getName());

            privilege.setPrivilegeType(PrivilegeType.ADDITIONAL);

        }

        BigDecimal mPrivilegeAmount = BigDecimal.ZERO;

        BigDecimal mPrivilegeValue = BigDecimal.ZERO;

        if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.RATE) {
            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());
            mPrivilegeAmount = saleAmount.multiply(mPrivilegeValue.divide(new BigDecimal(100)));

        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.NUMBER_OF_PEOPLE) {
            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());
            if (mTradeVo.getTrade().getTradePeopleCount() == null) {
                mPrivilegeAmount = BigDecimal.ZERO;
            } else {
                mPrivilegeAmount = MathDecimal.mul(mPrivilegeValue, mTradeVo.getTrade().getTradePeopleCount());
            }


        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.FIXED_AMOUNT) {
            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());

            mPrivilegeAmount = mPrivilegeValue;

        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.MINIMUM_CHARGE) {
        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.PER_UNIT_OF_PEOPLE) {
            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());

            if (mTradeVo.getTrade().getTradePeopleCount() == null) {
                mPrivilegeAmount = BigDecimal.ZERO;
            } else {
                mPrivilegeAmount = MathDecimal.mul(mPrivilegeValue, mTradeVo.getTrade().getTradePeopleCount());
            }
        } else {

        }
        privilege.setPrivilegeName(mExtraCharge.getName());
        privilege.setPrivilegeAmount(MathDecimal.round(mPrivilegeAmount, 2));
        privilege.setPrivilegeValue(mPrivilegeValue);

        privilege.setPromoId(mExtraCharge.getId());
        privilege.setChanged(true);

        return privilege;

    }


    public static TradePrivilege buildBoxFee(TradeVo mTradeVo, TradePrivilege privilege,
                                             ExtraCharge mExtraCharge) {
        if (privilege == null) {
            privilege = new TradePrivilege();

            privilege.validateCreate();

            privilege.setUuid(SystemUtils.genOnlyIdentifier());

            privilege.setTradeUuid(mTradeVo.getTrade().getUuid());

            privilege.setCreatorId(Session.getAuthUser().getId());

            privilege.setCreatorName(Session.getAuthUser().getName());

            privilege.setPrivilegeType(PrivilegeType.ADDITIONAL);
            privilege.setPromoId(mExtraCharge.getId());
            privilege.setPrivilegeValue(new BigDecimal(mExtraCharge.getContent()));
            privilege.setPrivilegeAmount(BigDecimal.ZERO);
            privilege.setPrivilegeName(mExtraCharge.getName());
        }
        return privilege;
    }


    public static TradePrivilege buildWeiXinCouponsPrivilege(TradeVo mTradeVo, WeiXinCouponsInfo mWeiXinCouponsInfo) {

        TradePrivilege mTradePrivilege = new TradePrivilege();
        mTradePrivilege.validateCreate();
        mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            mTradePrivilege.setCreatorId(authUser.getId());
            mTradePrivilege.setCreatorName(authUser.getName());
        }
        mTradePrivilege.setPrivilegeType(PrivilegeType.WECHAT_CARD_COUPONS);
        mTradePrivilege.setPromoId(Long.parseLong(mWeiXinCouponsInfo.getCode()));
        mTradePrivilege.setPrivilegeValue(BigDecimal.ZERO);
        mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
        mTradePrivilege.setPrivilegeName(mWeiXinCouponsInfo.getCash().getTitle());
        return mTradePrivilege;
    }


    public static TradePrivilege buildGiftCouponsPrivilege(IShopcartItemBase mShopcartItemBase, String uuid) {
        TradePrivilege mTradePrivilege = null;
        if (mShopcartItemBase.getCouponPrivilegeVo() != null && mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null) {
            mTradePrivilege = mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege();
        }
        if (mTradePrivilege == null || mTradePrivilege.getId() == null) {
            mTradePrivilege = new TradePrivilege();
            mTradePrivilege.validateCreate();
            mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        } else {
            mTradePrivilege.validateUpdate();
            mTradePrivilege.setStatusFlag(StatusFlag.VALID);
        }
        mTradePrivilege.setTradeUuid(uuid);
        mTradePrivilege.setTradeItemUuid(mShopcartItemBase.getUuid());
        mTradePrivilege.setCreatorId(Session.getAuthUser().getId());
        mTradePrivilege.setCreatorName(Session.getAuthUser().getName());
        mTradePrivilege.setPrivilegeType(PrivilegeType.COUPON);
        mTradePrivilege.setPrivilegeValue(BigDecimal.ZERO);
        mTradePrivilege.setPrivilegeAmount(BigDecimal.ZERO);
        mTradePrivilege.setPromoId(mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPromoId());
        mTradePrivilege.setPrivilegeAmount(mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().negate());
        mTradePrivilege.setPrivilegeValue(mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
        mTradePrivilege.setPrivilegeName(mShopcartItemBase.getCouponPrivilegeVo().getCoupon().getName());
        mTradePrivilege.setChanged(true);
        return mTradePrivilege;
    }


    public static TradePrivilege buildGiftCouponsPrivilege(IShopcartItemBase mShopcartItemBase, CouponPrivilegeVo mCouponPrivilegeVo, String uuid) {
                if (mShopcartItemBase.getCouponPrivilegeVo() != null && mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null) {
            if (mCouponPrivilegeVo.getTradePrivilege() != null) {
                TradePrivilege oldPrivilege = mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege();
                copyServerTradePrivilege(mCouponPrivilegeVo.getTradePrivilege(), oldPrivilege);
            }
        }
        mShopcartItemBase.setCouponPrivilegeVo(mCouponPrivilegeVo);
        return buildGiftCouponsPrivilege(mShopcartItemBase, uuid);
    }


    public static void copyServerTradePrivilege(TradePrivilege newTradePrivilege, TradePrivilege oldPrivilege) {
        if (oldPrivilege == null) {
            return;
        }
        newTradePrivilege.setId(oldPrivilege.getId());
        newTradePrivilege.setUuid(oldPrivilege.getUuid());
        newTradePrivilege.setClientCreateTime(oldPrivilege.getClientCreateTime());
        newTradePrivilege.setClientUpdateTime(oldPrivilege.getClientUpdateTime());
        newTradePrivilege.setServerCreateTime(oldPrivilege.getServerCreateTime());
        newTradePrivilege.setServerUpdateTime(oldPrivilege.getServerUpdateTime());
    }

    public static TradePrivilege buildWxActivityPrivige(Long tradeId, String tradeUuid, IShopcartItem shopcartItem) {
        return buildPrivige(tradeId, tradeUuid, shopcartItem, PrivilegeType.CARD_SERVICE, BaseApplication.sInstance.getString(R.string.card_service_privilege_wx));
    }

    public static TradePrivilege buildCardServicePrivige(Long tradeId, String tradeUuid, IShopcartItem shopcartItem) {
        return buildPrivige(tradeId, tradeUuid, shopcartItem, PrivilegeType.CARD_SERVICE, BaseApplication.sInstance.getString(R.string.card_service_privilege_name));
    }

    public static TradePrivilege buildPrivige(Long serverRecordId, String tradeUuid, IShopcartItem shopcartItem, PrivilegeType privilegeType, String privilegeName) {
        TradePrivilege mPrivilege = new TradePrivilege();
        mPrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        mPrivilege.setTradeUuid(tradeUuid);
        mPrivilege.setTradeItemUuid(shopcartItem.getUuid());
        mPrivilege.setCreatorId(Session.getAuthUser().getId());
        mPrivilege.setCreatorName(Session.getAuthUser().getName());
        mPrivilege.setPrivilegeType(privilegeType);
        mPrivilege.validateCreate();
        BigDecimal actualAmount = shopcartItem.getActualAmount();
        mPrivilege.setPrivilegeName(privilegeName);
        mPrivilege.setPrivilegeValue(actualAmount);
        mPrivilege.setPrivilegeAmount(actualAmount.negate());
        mPrivilege.setPromoId(serverRecordId);
        return mPrivilege;
    }

}
