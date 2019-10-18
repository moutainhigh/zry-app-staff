package com.zhongmei.bty.basemodule.customer.bean.coupon;

import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;


public class CouponVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isSelected;
    private boolean enabled = true;
    private CustomerCouponResp couponInfo;
    private Coupon coupon;
    private Long dishId;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public CustomerCouponResp getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CustomerCouponResp couponInfo) {
        this.couponInfo = couponInfo;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((couponInfo == null) ? 0 : couponInfo.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CouponVo other = (CouponVo) obj;
        if (couponInfo == null) {
            if (other.getCouponInfo() != null)
                return false;
        } else if (!couponInfo.equals(other.getCouponInfo()))
            return false;
        return true;
    }
}
