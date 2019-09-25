package com.zhongmei.yunfu.bean.req;

import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerCouponResp implements Serializable {

    private Integer applyDish;//菜品使用规则，1部分可用 2部分不可用
    private String content;
    private Integer couponState;//优惠券状态
    private Integer couponType;//优惠券类型  1,折扣 2。代金  3礼品
    private BigDecimal discountValue;
    private Long endTime;//使用结束时间
    private BigDecimal fullValue;//满额
    private Long id;//券id
    private String name;
    private String remark;
    private Long customerCouponId;//记录id

    private Long dishId;
    private String dishName;


    public Integer getApplyDish() {
        return applyDish;
    }

    public void setApplyDish(Integer applyDish) {
        this.applyDish = applyDish;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCouponState() {
        return couponState;
    }

    public void setCouponState(Integer couponState) {
        this.couponState = couponState;
    }

    public CouponType getCouponType() {
        return ValueEnums.toEnum(CouponType.class, couponType);
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = ValueEnums.toValue(couponType);
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getFullValue() {
        return fullValue;
    }

    public void setFullValue(BigDecimal fullValue) {
        this.fullValue = fullValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Long getCustomerCouponId() {
        return customerCouponId;
    }

    public void setCustomerCouponId(Long customerCouponId) {
        this.customerCouponId = customerCouponId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : customerCouponId.hashCode());
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
        CustomerCouponResp other = (CustomerCouponResp) obj;
        if (this.id == null) {
            return false;
        } else {
            return this.id.equals(other.id);
        }
    }
}
