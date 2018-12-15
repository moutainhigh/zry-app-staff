package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */
@Deprecated
public class DiscountDetailVo {
    private String bizDate; //营业日期
    private BigDecimal privilegeAmount;//总优惠金额
    private BigDecimal goodsDiscount; //手动商品折扣
    private BigDecimal orderDiscount; //手动整单折扣
    private BigDecimal autoDiscount; //会员折扣
    private Map goodsMap;//手动商品折扣
    private Map orderMap;//手动整单折扣


    private BigDecimal integral; //积分抵现
    private BigDecimal promoDiscount; //折扣券
    private BigDecimal promoVoucher; //现金券
    private BigDecimal promoDebt; //满减券
    private BigDecimal promoGift; //礼品券
    private BigDecimal activeConsume; //满立减活动
    private BigDecimal privilegeThirdPrty; //第三方平台优惠
    private BigDecimal dishMarketing;//营销活动优惠
    private BigDecimal shukePrivilege;//熟客优惠
    private BigDecimal weixinCardPrivilege;//微信卡劵优惠
    private BigDecimal entertainPrivilege;//宴请


    public BigDecimal getDishMarketing() {
        return dishMarketing;
    }

    public BigDecimal getShukePrivilege() {
        return shukePrivilege;
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public BigDecimal getPrivilegeAmount() {
        if (privilegeAmount == null) {
            privilegeAmount = new BigDecimal("0");
        }
        return privilegeAmount;
    }

    public void setPrivilegeAmount(BigDecimal privilegeAmount) {
        this.privilegeAmount = privilegeAmount;
    }

    public BigDecimal getGoodsDiscount() {
        if (goodsDiscount == null) {
            goodsDiscount = new BigDecimal("0");
        }
        return goodsDiscount;
    }

    public void setGoodsDiscount(BigDecimal goodsDiscount) {
        this.goodsDiscount = goodsDiscount;
    }

    public BigDecimal getOrderDiscount() {
        if (orderDiscount == null) {
            orderDiscount = new BigDecimal("0");
        }
        return orderDiscount;
    }

    public void setOrderDiscount(BigDecimal orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public BigDecimal getAutoDiscount() {
        if (autoDiscount == null) {
            autoDiscount = new BigDecimal("0");
        }
        return autoDiscount;
    }

    public void setAutoDiscount(BigDecimal autoDiscount) {
        this.autoDiscount = autoDiscount;
    }

    public BigDecimal getIntegral() {
        if (integral == null) {
            integral = new BigDecimal("0");
        }
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public BigDecimal getPromoDiscount() {
        if (promoDiscount == null) {
            promoDiscount = new BigDecimal("0");
        }
        return promoDiscount;
    }

    public void setPromoDiscount(BigDecimal promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public BigDecimal getPromoVoucher() {
        if (promoVoucher == null) {
            promoVoucher = new BigDecimal("0");
        }
        return promoVoucher;
    }

    public void setPromoVoucher(BigDecimal promoVoucher) {
        this.promoVoucher = promoVoucher;
    }

    public BigDecimal getPromoDebt() {
        if (promoDebt == null) {
            promoDebt = new BigDecimal("0");
        }
        return promoDebt;
    }

    public void setPromoDebt(BigDecimal promoDebt) {
        this.promoDebt = promoDebt;
    }

    public BigDecimal getPromoGift() {
        if (promoGift == null) {
            promoGift = new BigDecimal("0");
        }
        return promoGift;
    }

    public void setPromoGift(BigDecimal promoGift) {
        this.promoGift = promoGift;
    }

    public BigDecimal getActiveConsume() {
        if (activeConsume == null) {
            activeConsume = new BigDecimal("0");
        }
        return activeConsume;
    }

    public void setActiveConsume(BigDecimal activeConsume) {
        this.activeConsume = activeConsume;
    }

    public BigDecimal getPrivilegeThirdPrty() {
        if (privilegeThirdPrty == null) {
            privilegeThirdPrty = new BigDecimal("0");
        }
        return privilegeThirdPrty;
    }

    public void setPrivilegeThirdPrty(BigDecimal privilegeThirdPrty) {
        this.privilegeThirdPrty = privilegeThirdPrty;
    }

    public BigDecimal getWeixinCardPrivilege() {
        return weixinCardPrivilege;
    }

    public void setWeixinCardPrivilege(BigDecimal weixinCardPrivilege) {
        this.weixinCardPrivilege = weixinCardPrivilege;
    }

    public void setDishMarketing(BigDecimal dishMarketing) {
        this.dishMarketing = dishMarketing;
    }

    public void setShukePrivilege(BigDecimal shukePrivilege) {
        this.shukePrivilege = shukePrivilege;
    }

    public BigDecimal getEntertainPrivilege() {
        if (entertainPrivilege == null) {
            entertainPrivilege = new BigDecimal("0");
        }
        return entertainPrivilege;
    }

    public void setEntertainPrivilege(BigDecimal entertainPrivilege) {
        this.entertainPrivilege = entertainPrivilege;
    }


    public Map getGoodsMap() {
        return goodsMap;
    }

    public void setGoodsMap(Map goodsMap) {
        this.goodsMap = goodsMap;
    }

    public Map getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map orderMap) {
        this.orderMap = orderMap;
    }
}
