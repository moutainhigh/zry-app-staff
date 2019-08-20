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

    /**
     * @Title: getDishDiscount
     * @Description: 菜品优惠信息
     * @Param dish
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
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
            // 菜品销售价格
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
            }else if(privilegeType == PrivilegeType.AUTO_DISCOUNT.value()){//重新计算会员折扣  商品总价*（1-（会员折扣*10）/100）
                BigDecimal privilegeAmount = mShopcartItemBase.getActualAmount().multiply(MathDecimal.getDiscountValue(mPrivilege.getPrivilegeValue().multiply(BigDecimal.TEN))).negate();
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }else if(privilegeType == PrivilegeType.MEMBER_PRICE.value()){
                BigDecimal  privilegeAmount =  new BigDecimal(mPrivilege.getPrivilegeValue().toString()).multiply(mShopcartItemBase.getTotalQty());
                privilegeAmount = privilegeAmount.subtract(mShopcartItemBase.getActualAmount());
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }else if(privilegeType == PrivilegeType.MEMBER_REBATE.value()){
                BigDecimal  privilegeAmount = new BigDecimal(mPrivilege.getPrivilegeValue().toString()).multiply(mShopcartItemBase.getTotalQty());
                privilegeAmount = MathDecimal.round(privilegeAmount, 2).negate();//折让的价格就是，折让价格x商品数量
                mPrivilege.setPrivilegeAmount(privilegeAmount);
            }
            correctGroupDishPrivilege(mShopcartItemBase);
        } else {
            mPrivilege = null;
        }

        return mPrivilege;
    }

    /**
     * 团餐或者自助餐标下的菜品价格为0,只有未保存的菜品有这个问题,重新设置优惠价格
     *
     * @param mIShopcartItemBase
     */
    private static void correctGroupDishPrivilege(IShopcartItemBase mIShopcartItemBase) {
        if (mIShopcartItemBase.isGroupDish() && mIShopcartItemBase.getPrivilege() != null && mIShopcartItemBase.getId() == null) {
            mIShopcartItemBase.getPrivilege().setPrivilegeAmount(BigDecimal.ZERO);
            mIShopcartItemBase.getPrivilege().setPrivilegeValue(BigDecimal.ZERO);
        }
    }


    /**
     *

     * @Title: buildMemberPrivilege
     * @Description: 构建会员折扣
     * @Param mShopcartItemBase
     * @Param uuid
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
//	public static TradePrivilege buildMemberPrivilege(IShopcartItemBase mShopcartItemBase,
//		CrmCustomerLevelRights mCrmCustomerLevelRights, String uuid) {
//		TradePrivilege mPrivilege = new TradePrivilege();
//		mPrivilege.validateCreate();
//		mPrivilege.setUuid(SystemUtils.genOnlyIdentifier());
//		mPrivilege.setTradeUuid(uuid);
//		mPrivilege.setTradeItemUuid(mShopcartItemBase.getUuid());
//		mPrivilege.setCreatorId(AuthUserCache.getAuthUser().getId());
//		mPrivilege.setCreatorName(AuthUserCache.getAuthUser().getName());
//		
//		mPrivilege.setPrivilegeType(PrivilegeType.AUTO_DISCOUNT);
//		mPrivilege.setPrivilegeValue(mCrmCustomerLevelRights.getDiscount().multiply(BigDecimal.TEN));
//		// 菜品销售价格
//		BigDecimal costValue = mShopcartItemBase.getActualAmount();
//		BigDecimal discountPrice = costValue
//			.multiply(MathDecimal.getDiscountValue(mCrmCustomerLevelRights.getDiscount().multiply(BigDecimal.TEN)));
//			
//		discountPrice = MathDecimal.trimZero(discountPrice);
//		
//		mPrivilege.setPrivilegeAmount(MathDecimal.round(discountPrice, 2).negate());
//		mPrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.));
//		return mPrivilege;
//	}

    /**
     * @Title: buildCouponPrivilege
     * @Description: 设置优惠劵信息
     * @Param mTradeVo
     * @Param mCouponPrivilegeVo TODO
     * @Return void 返回类型
     */
    public static void buildCouponPrivilege(TradeVo mTradeVo, CouponPrivilegeVo mCouponPrivilegeVo) {
        if (mCouponPrivilegeVo.getTradePrivilege() == null) {
            return;
        }
        mCouponPrivilegeVo.setActived(false);
//        mCouponPrivilegeVo.getTradePrivilege().setCreatorId(AuthUserCache.getAuthUser().getId());
//        mCouponPrivilegeVo.getTradePrivilege().setCreatorName(AuthUserCache.getAuthUser().getName());
        mCouponPrivilegeVo.getTradePrivilege().setPrivilegeType(PrivilegeType.COUPON);
        mCouponPrivilegeVo.getTradePrivilege().setPrivilegeName(mCouponPrivilegeVo.getCoupon().getName());

        if (Utils.isEmpty(mTradeVo.getCouponPrivilegeVoList())) {
            mTradeVo.setCouponPrivilegeVoList(new ArrayList<CouponPrivilegeVo>());
        }

        //设置索引


        List<CouponPrivilegeVo> couponPrivilegeVoList = mTradeVo.getCouponPrivilegeVoList();
        Long promoId = mCouponPrivilegeVo.getTradePrivilege().getPromoId();
        for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
            if (couponPrivilegeVo.getTradePrivilege() == null) {
                continue;
            }
            //保存过的代金券
            if (promoId.equals(couponPrivilegeVo.getTradePrivilege().getPromoId())) {
                resetCouponPrivilege(mTradeVo, mCouponPrivilegeVo, couponPrivilegeVo);
                return;
            }
//            if(couponPrivilegeVo.getCoupon()!=null&&couponPrivilegeVo.getCoupon().getCouponType()!= CouponType.CASH
//                    &&couponPrivilegeVo.getCoupon().getCouponType()==mCouponPrivilegeVo.getCoupon().getCouponType()
//                    ){
//                //不是代金券的其它优惠劵
//                resetCouponPrivilege(mTradeVo,mCouponPrivilegeVo,couponPrivilegeVo);
//            }
        }
        //新添加的代金券
        mCouponPrivilegeVo.getTradePrivilege().setUuid(SystemUtils.genOnlyIdentifier());
        mCouponPrivilegeVo.getTradePrivilege().validateCreate();
        mCouponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
        couponPrivilegeVoList.add(mCouponPrivilegeVo);
    }

    /**
     * 更改以前保存过的优惠劵
     * couponPrivilegeVo  保存过的优惠劵
     */
    private static void resetCouponPrivilege(TradeVo mTradeVo, CouponPrivilegeVo mCouponPrivilegeVo, CouponPrivilegeVo couponPrivilegeVo) {
        couponPrivilegeVo.getTradePrivilege().validateUpdate();
        couponPrivilegeVo.getTradePrivilege().setStatusFlag(StatusFlag.VALID);
        couponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
        couponPrivilegeVo.getTradePrivilege().setTradeId(mTradeVo.getTrade().getId());
        couponPrivilegeVo.getTradePrivilege().setPrivilegeType(PrivilegeType.COUPON);
        couponPrivilegeVo.getTradePrivilege().setPrivilegeName(mCouponPrivilegeVo.getCoupon().getName());
        couponPrivilegeVo.getTradePrivilege().setPromoId(mCouponPrivilegeVo.getTradePrivilege().getPromoId());
    }

    /**
     * @Title: buildCashPrivilege
     * @Description: 积分抵现
     * @Param mCrmCustomerLevelRights
     * @Param integra
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: buildBanquetPrivilege
     * @Description: 宴请优惠
     * @Param mTradeVo TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: mathPrivilege
     * @Description: 计算积分抵现金额
     * @Param mCrmCustomerLevelRights
     * @Param integra
     * @Param mIntegralCashPrivilegeVo TODO
     * @Return void 返回类型
     */
    public static void mathPrivilege(IntegralCashPrivilegeVo mIntegralCashPrivilegeVo, TradeVo mTradeVo) {
        mIntegralCashPrivilegeVo.setActived(false);
        BigDecimal integra = mIntegralCashPrivilegeVo.getUseInteger(); //获取最多可以使用的积分
        if (integra == null || mIntegralCashPrivilegeVo.getConvertValue() == null) {
            return;
        }
        BigDecimal privilegeValue = BigDecimal.ZERO;
        BigDecimal privilegeAmount = BigDecimal.ZERO;

        BigDecimal orderTotalPrice = mTradeVo.getTrade().getTradeAmount();

        // 客户当前积分最多可抵用多少份积分
        BigDecimal maxIntegralMulriple = integra.divideToIntegralValue(mIntegralCashPrivilegeVo.getConvertValue());
        // 客户当前积分最多可抵用多少现金
        BigDecimal maxCashPrice = maxIntegralMulriple.multiply(BigDecimal.ONE);//以一块钱来算
        // 客户最多可抵用消耗积分
        BigDecimal maxTotalIntegral = maxIntegralMulriple.multiply(mIntegralCashPrivilegeVo.getConvertValue());

        // 当前订单金额最多可抵现多少份金额
        BigDecimal cashShare = orderTotalPrice.divideToIntegralValue(BigDecimal.ONE);//1块钱1份


        // 客户当前积分是否大于一份抵用积分，如果不大于则不能满足抵现规则
        if (integra.compareTo(mIntegralCashPrivilegeVo.getConvertValue()) < 0) {
            mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeValue(privilegeValue);
            mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeAmount(MathDecimal.round(privilegeAmount, 2).negate());
            return;
        }

        if (orderTotalPrice.compareTo(BigDecimal.ONE) >= 0) { //单份的抵现金额为1块钱

            // 客户当前积分可抵现份数小于订单总金额可抵用总份数
            if (cashShare.compareTo(maxIntegralMulriple) >= 0) {
                privilegeAmount = maxIntegralMulriple;
                privilegeValue = maxTotalIntegral;
            } else {
                privilegeAmount = cashShare.multiply(BigDecimal.ONE);//每份抵用1块钱
                privilegeValue = cashShare.multiply(mIntegralCashPrivilegeVo.getConvertValue());
            }
            mIntegralCashPrivilegeVo.setActived(true);
        }

//        if (mIntegralCashPrivilegeVo.getLimitType() == LimitType.NO_LIMIT) {
//            // 当前订单总金额是否大于抵现单份金额
//            if (orderTotalPrice.compareTo(mIntegralCashPrivilegeVo.getExchangeCashValue()) >= 0) {
//
//                // 客户当前积分可抵现份数小于订单总金额可抵用总份数
//                if (cashShare.compareTo(maxIntegralMulriple) >= 0) {
//                    privilegeAmount = maxCashPrice;
//                    privilegeValue = maxTotalIntegral;
//                } else {
//                    privilegeAmount = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeCashValue());
//                    privilegeValue = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeIntegralValue());
//                }
//                mIntegralCashPrivilegeVo.setActived(true);
//            }
//        } else if (mIntegralCashPrivilegeVo.getLimitType() == LimitType.INTEGRAL_LIMIT) {
//            // 客户当前积分是否大于抵现一份的积分数量 && 客户当前订单金额是否大于积分最低积分抵用金额
//            if (integra.compareTo(mIntegralCashPrivilegeVo.getExchangeIntegralValue()) >= 0
//                    && orderTotalPrice.compareTo(mIntegralCashPrivilegeVo.getExchangeCashValue()) >= 0) {
//                // 客户当前积分是否大于限制抵用积分
//                if (integra.compareTo(mIntegralCashPrivilegeVo.getLimitIntegral()) >= 0) {
//                    // 上限可抵用积分份数
//                    BigDecimal limitShare = mIntegralCashPrivilegeVo.getLimitIntegral()
//                            .divideToIntegralValue(mIntegralCashPrivilegeVo.getExchangeIntegralValue());
//                    BigDecimal limitMaxPrice = limitShare.multiply(mIntegralCashPrivilegeVo.getExchangeCashValue());
//                    BigDecimal limitMacIntegral =
//                            limitShare.multiply(mIntegralCashPrivilegeVo.getExchangeIntegralValue());
//                    // 如果订单金额大于可抵现金额
//                    if (orderTotalPrice.compareTo(limitMaxPrice) >= 0) {
//                        privilegeAmount = limitMaxPrice;
//                        privilegeValue = limitMacIntegral;
//                    } else {
//                        privilegeAmount = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeCashValue());
//                        privilegeValue = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeIntegralValue());
//                    }
//                    mIntegralCashPrivilegeVo.setActived(true);
//                } else {
//                    // 当前订单金额大于可以当前积分可抵现最高金额
//                    if (orderTotalPrice.compareTo(maxCashPrice) >= 0) {
//                        privilegeAmount = maxCashPrice;
//                        privilegeValue = maxTotalIntegral;
//                        mIntegralCashPrivilegeVo.setActived(true);
//                    } else if (orderTotalPrice.compareTo(BigDecimal.ZERO) > 0) {
//                        privilegeAmount = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeCashValue());
//                        privilegeValue = cashShare.multiply(mIntegralCashPrivilegeVo.getExchangeIntegralValue());
//                        mIntegralCashPrivilegeVo.setActived(true);
//                    } else {
//
//                    }
//                }
//
//            }
//
//        } else {
//
//        }

        mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeValue(privilegeValue);
        mIntegralCashPrivilegeVo.getTradePrivilege().setPrivilegeAmount(MathDecimal.round(privilegeAmount, 2).negate());
    }

    /**
     * 根据pribilegeType 计算储值优惠
     * @param mCustomer 会员信息
     * @return
     * type 1，储值折扣，2，储值赠送
     */
    public static TradePrivilege buildChargePrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer){
        BigDecimal tradeAmout= DinnerShoppingCart.getInstance().getTradeAmoutCanDiscount(mShoppingCartVo);
        BigDecimal fullValue=mCustomer.storedFullAmount;

        //打折金额限制
        if(tradeAmout.compareTo(fullValue)<=0){
            return null;
        }

        ChargePrivilegeType type=mCustomer.getStoredPrivilegeType();
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

        mPrivilegeAmount=MathDecimal.round(mPrivilegeAmount, 2).negate();//取反
        chargePrivilege.setPrivilegeAmount(mPrivilegeAmount);

        return chargePrivilege;
    }

    /**
     * @Title: mathMemberPrice
     * @Description: 计算获取会员价优惠数据TradePrivilege
     * @Param mIShopcartItemBase
     * @Param mDishMemberPrice
     * @Param tradeUUID
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
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
            case DISCOUNT://折扣
                privilegeValue = new BigDecimal(mDishMemberPrice.getDiscount().toString());
                privilegeValue = privilegeValue.multiply(BigDecimal.TEN);
                privilegeAmount = amount.multiply(MathDecimal.getDiscountValue(privilegeValue)).negate();
                privilegeAmount = MathDecimal.round(privilegeAmount, 2);
                mTradePrivilege.setPrivilegeType(PrivilegeType.AUTO_DISCOUNT);
                mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_discount));
                break;
            case REBATE://折让
                if (mDishMemberPrice.getMemberPrice() == null) {
                    privilegeValue = amount;
                } else {
                    privilegeValue = new BigDecimal(mDishMemberPrice.getMemberPrice().toString()).multiply(mIShopcartItemBase.getTotalQty());
                }
                privilegeAmount = MathDecimal.round(privilegeValue, 2).negate();//折让的价格就是，折让价格x商品数量
                mTradePrivilege.setPrivilegeType(PrivilegeType.MEMBER_REBATE);
                mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getResources().getString(R.string.member_dish_rebate));
                break;
            case PRICE: //特价
                if (mDishMemberPrice.getMemberPrice() == null) {
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

        mTradePrivilege.setPrivilegeValue(new BigDecimal(mDishMemberPrice.getDiscount()));//mDishMemberPrice.getDiscount()与mDishMemberPrice.getMemberPrice()数据一直
        mTradePrivilege.setPrivilegeAmount(privilegeAmount);
        return mTradePrivilege;
    }

    /**
     * @Title: buildExtraChargePrivilege
     * @Description: 构建附加费优惠信息
     * @Param privilege
     * @Param mExtraCharge
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */

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

        if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.RATE) {// 按比例

            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());
            mPrivilegeAmount = saleAmount.multiply(mPrivilegeValue.divide(new BigDecimal(100)));

        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.NUMBER_OF_PEOPLE) {// 按人数

            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());
            if (mTradeVo.getTrade().getTradePeopleCount() == null) {
                mPrivilegeAmount = BigDecimal.ZERO;
            } else {
                mPrivilegeAmount = MathDecimal.mul(mPrivilegeValue, mTradeVo.getTrade().getTradePeopleCount());
            }


        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.FIXED_AMOUNT) {// 固定金额

            mPrivilegeValue = new BigDecimal(mExtraCharge.getContent());

            mPrivilegeAmount = mPrivilegeValue;

        } else if (mExtraCharge.getCalcWay() == ExtraChargeCalcWay.MINIMUM_CHARGE) {// 最低消费

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

    /**
     * @Title: buildBoxFee
     * @Description: 构建餐盒费
     * @Param mTradeVo
     * @Param privilege
     * @Param mExtraCharge
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
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

    /**
     * @Title: buildWeiXinCouponsPrivilege
     * @Description: 构建微信卡卷的tradePrivilege
     * @Param mTradeVo
     * @Param mWeiXinCouponsInfo
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
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

    /**
     * @Title: buildGiftCouponsPrivilege
     * @Description: 构建礼品卷的tradePrivilege
     * @Param mTradeVo
     * @Param CouponPrivilegeVo
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
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
        mTradePrivilege.setPromoId(mShopcartItemBase.getCouponPrivilegeVo().getCouponInfoId());
        mTradePrivilege.setPrivilegeAmount(mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().negate());
        mTradePrivilege.setPrivilegeValue(mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount());
        mTradePrivilege.setPrivilegeName(mShopcartItemBase.getCouponPrivilegeVo().getCoupon().getName());
        mTradePrivilege.setChanged(true);
        return mTradePrivilege;
    }

    /**
     * mCouponPrivilegeVo 新加的礼品劵
     */
    public static TradePrivilege buildGiftCouponsPrivilege(IShopcartItemBase mShopcartItemBase, CouponPrivilegeVo mCouponPrivilegeVo, String uuid) {
        //将新的优惠关联到已有的优惠上
        if (mShopcartItemBase.getCouponPrivilegeVo() != null && mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null) {
            if (mCouponPrivilegeVo.getTradePrivilege() != null) {
                TradePrivilege oldPrivilege = mShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege();
                copyServerTradePrivilege(mCouponPrivilegeVo.getTradePrivilege(), oldPrivilege);
            }
        }
        mShopcartItemBase.setCouponPrivilegeVo(mCouponPrivilegeVo);
        return buildGiftCouponsPrivilege(mShopcartItemBase, uuid);
    }

    /**
     * 将保存过服务器的优惠的id等信息复制给新的对象
     */
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
