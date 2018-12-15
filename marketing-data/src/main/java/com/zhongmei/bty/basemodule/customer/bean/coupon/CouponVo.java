package com.zhongmei.bty.basemodule.customer.bean.coupon;

import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;

/**
 * @Date：2015-8-11 下午12:01:27
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class CouponVo implements java.io.Serializable {
    /**
     * @date：2015-8-11 下午3:19:07
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    private boolean isSelected;// ui使用，是否已经被选中

    private boolean enabled = true;// 购物车根据条件判断它是否可以生效

    private CustomerCouponResp couponInfo;// 券的id等基本信息

    private Coupon coupon;// 券的模板数据

    private Long dishId;//关联菜品id


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

    /*
     * id相等即相等
     */
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
