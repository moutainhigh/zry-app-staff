package com.zhongmei.beauty.events;



public class OrderDishMaskingEvent {
    private boolean isShowMasking = false;

    public OrderDishMaskingEvent(boolean isShow) {
        this.isShowMasking = isShow;
    }

    public boolean isShowMasking() {
        return isShowMasking;
    }

}
