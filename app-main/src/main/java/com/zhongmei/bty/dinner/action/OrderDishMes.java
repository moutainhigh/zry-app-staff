package com.zhongmei.bty.dinner.action;

import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;



public class OrderDishMes {
    boolean isClick;
    ShopcartItem item;
    SetmealShopcartItem setmealShopcartItem;

    public OrderDishMes(boolean isClick, ShopcartItem item, SetmealShopcartItem setmealShopcartItem) {
        this.isClick = isClick;
        this.item = item;
        this.setmealShopcartItem = setmealShopcartItem;
    }

    public SetmealShopcartItem getSetmealShopcartItem() {
        return setmealShopcartItem;
    }

    public void setSetmealShopcartItem(SetmealShopcartItem setmealShopcartItem) {
        this.setmealShopcartItem = setmealShopcartItem;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isClick() {
        return isClick;
    }


    public ShopcartItem getItem() {
        return item;
    }

    public void setItem(ShopcartItem item) {
        this.item = item;
    }


}
