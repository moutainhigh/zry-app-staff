package com.zhongmei.beauty.events;

/**
 * Created by demo on 2018/12/15
 */

public class OrderDishMaskingEvent {
    private boolean isShowMasking = false;

    public OrderDishMaskingEvent(boolean isShow) {
        this.isShowMasking = isShow;
    }

    public boolean isShowMasking() {
        return isShowMasking;
    }

}
