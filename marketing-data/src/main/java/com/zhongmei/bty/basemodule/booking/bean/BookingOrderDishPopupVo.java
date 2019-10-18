package com.zhongmei.bty.basemodule.booking.bean;



public class BookingOrderDishPopupVo {
    private boolean isSelected;

    public static final int ORDER_DISH = 1;
    public static final int NOT_ORDER_DISH = 2;
    public static final int ALL = 0;


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
