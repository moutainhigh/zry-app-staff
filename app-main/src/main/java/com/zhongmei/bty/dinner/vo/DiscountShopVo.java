package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;

/**
 * @date:2016年2月16日上午10:42:28
 */
public class DiscountShopVo {
    // 普通
    public static final int NORMAL = 0;
    // 是否是赠送
    public static final int FREE = 1;
    // 自定义折扣
    public static final int DIVDISCOUNT = 2;
    // 移除优惠
    public static final int REMOVEPRIVILEGE = 3;

    private DiscountShop discountShop;
    private boolean isSelected = false;
    private int discountType;

    public DiscountShop getDiscountShop() {
        return discountShop;
    }

    public void setDiscountShop(DiscountShop discountShop) {
        this.discountShop = discountShop;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getDiscountType() {
        return discountType;
    }
}
