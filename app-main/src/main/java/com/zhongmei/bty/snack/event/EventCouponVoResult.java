package com.zhongmei.bty.snack.event;

import java.util.ArrayList;
import java.util.List;

import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;


public class EventCouponVoResult {

    private List<CouponVo> rebateCoupons;// 满减券

    private List<CouponVo> discountCoupons;// 折扣券

    private List<CouponVo> giftCoupons;// 礼品券

    private List<CouponVo> cashCoupons;// 现金券

    private CouponVo selectedCouponVo;

    public List<CouponVo> getSelectedCoupons() {
        return selectedCoupons;
    }

    private List<CouponVo> selectedCoupons = new ArrayList<CouponVo>();//支持多种券时使用

    public List<CouponVo> getRebateCoupons() {
        return rebateCoupons;
    }

    public void setRebateCoupons(List<CouponVo> rebateCoupons) {
        this.rebateCoupons = rebateCoupons;
    }

    public List<CouponVo> getDiscountCoupons() {
        return discountCoupons;
    }

    public void setDiscountCoupons(List<CouponVo> discountCoupons) {
        this.discountCoupons = discountCoupons;
    }

    public List<CouponVo> getGiftCoupons() {
        return giftCoupons;
    }

    public void setGiftCoupons(List<CouponVo> giftCoupons) {
        this.giftCoupons = giftCoupons;
    }

    public List<CouponVo> getCashCoupons() {
        return cashCoupons;
    }

    public void setCashCoupons(List<CouponVo> cashCoupons) {
        this.cashCoupons = cashCoupons;
    }

    public CouponVo getSelectedCouponVo() {
        return selectedCouponVo;
    }

    public void setSelectedCouponVo(CouponVo selectedCouponVo) {
        this.selectedCouponVo = selectedCouponVo;
        this.selectedCoupons.add(selectedCouponVo);
    }
}
