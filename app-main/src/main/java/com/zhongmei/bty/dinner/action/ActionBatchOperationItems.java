package com.zhongmei.bty.dinner.action;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;

import java.util.List;



public class ActionBatchOperationItems {

    public boolean isCategory = false;

    public List<DishDataItem> items;

    public ActionBatchOperationItems(List<DishDataItem> items, boolean isCategory) {
        this.items = items;
        this.isCategory = isCategory;
    }
}
