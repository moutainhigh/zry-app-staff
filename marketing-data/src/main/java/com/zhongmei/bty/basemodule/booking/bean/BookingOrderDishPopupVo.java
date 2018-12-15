package com.zhongmei.bty.basemodule.booking.bean;

/**
 * Created by demo on 2018/12/15
 */

public class BookingOrderDishPopupVo {
    private boolean isSelected;

    public static final int ORDER_DISH = 1;
    public static final int NOT_ORDER_DISH = 2;
    public static final int ALL = 0;

    /**
     * 0——否（没有点餐）
     * 1——是（已点餐）
     */
    private int value;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
