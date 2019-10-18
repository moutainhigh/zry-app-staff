package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;


public class DiscountShopVo {
        public static final int NORMAL = 0;
        public static final int FREE = 1;
        public static final int DIVDISCOUNT = 2;
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
