package com.zhongmei.bty.snack.event;

import java.util.ArrayList;
import java.util.List;

import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;


public class EventCouponVoResult {

    private List<CouponVo> rebateCoupons;
    private List<CouponVo> discountCoupons;
    private List<CouponVo> giftCoupons;
    private List<CouponVo> cashCoupons;
    private CouponVo selectedCouponVo;

    public List<CouponVo> getSelectedCoupons() {
        return selectedCoupons;
    }

    private List<CouponVo> selectedCoupons = new ArrayList<CouponVo>();
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
