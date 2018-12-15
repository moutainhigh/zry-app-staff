package com.zhongmei.bty.dinner.action;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;

public class ActionDinnerChangePage {

    private DishDataItem item;

    public ActionDinnerChangePage(DishDataItem item) {
        this.item = item;
    }

    public DishDataItem getItem() {
        return item;
    }

    public void setItem(DishDataItem item) {
        this.item = item;
    }

}
