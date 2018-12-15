package com.zhongmei.bty.cashier.ordercenter.bean;

import android.graphics.drawable.Drawable;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 *
 * @Description:订单中心数据
 */
public class TradeDishData {
    public static final int DATA_TYPE_SINGLE = 1;// 单品

    public static final int DATA_TYPE_SINGLE_MEMO_AND_DISCOUNT = 2;// 单品折扣

    public static final int DATA_TYPE_COMBO = 3;// 套餐

    public static final int DATA_TYPE_COMBO_CHILD = 4;// 套餐子菜

    public static final int DATA_TYPE_COMBO_CHILD_MEMO = 5;// 套餐子菜备注

    public static final int DATA_TYPE_COMBO_MEMO_AND_DISCOUNT = 6;// 套餐折扣

    public static final int DATA_TYPE_DESCRIPTION = 7;

    public static final int DATA_TYPE_PRIVILEGE = 8;// 整单优惠

    public static final int DATA_TYPE_COUPON_PRIVILEGE = 9;// 优惠券

    public static final int DATA_TYPE_DEPOSIT = 10;//押金

    public static final int DATA_TYPE_ACTIVITY = 11; //营销活动

    public static final int DATA_TYPE_GIFT = 12; //礼品券

    private int type;

    private TradeItemVo parentTradeItemVo;

    private TradeItemVo tradeItemVo;

    private List<TradeItemVo> extraList;

    private boolean hasAllOrderDiscount;

    private Drawable leftIcon;//图标

    private String description;//描述

    private String amountStr;//金额相关

    private BigDecimal quantity;//数量

    private TradePrivilege tradePrivilege;//优惠

    private CouponPrivilegeVo couponPrivilegeVo;//优惠券

    private Map<Long, ExtraCharge> extraChargeMap;

    public TradeDishData(int dataType) {
        this.type = dataType;
    }

    public int getType() {
        return type;
    }

    public TradeItemVo getTradeItemVo() {
        return tradeItemVo;
    }

    public void setTradeItemVo(TradeItemVo tradeItemVo) {
        this.tradeItemVo = tradeItemVo;
    }

    public boolean isHasAllOrderDiscount() {
        return hasAllOrderDiscount;
    }

    public void setHasAllOrderDiscount(boolean hasAllOrderDiscount) {
        this.hasAllOrderDiscount = hasAllOrderDiscount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }

    public CouponPrivilegeVo getCouponPrivilegeVo() {
        return couponPrivilegeVo;
    }

    public void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo) {
        this.couponPrivilegeVo = couponPrivilegeVo;
    }

    public Drawable getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
    }

    public TradeItemVo getParentTradeItemVo() {
        return parentTradeItemVo;
    }

    public void setParentTradeItemVo(TradeItemVo parentTradeItemVo) {
        this.parentTradeItemVo = parentTradeItemVo;
    }

    public List<TradeItemVo> getExtraList() {
        return extraList;
    }

    public void setExtraList(List<TradeItemVo> extraList) {
        this.extraList = extraList;
    }

    public Map<Long, ExtraCharge> getExtraChargeMap() {
        return extraChargeMap;
    }

    public void setExtraChargeMap(Map<Long, ExtraCharge> extraChargeMap) {
        this.extraChargeMap = extraChargeMap;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}

