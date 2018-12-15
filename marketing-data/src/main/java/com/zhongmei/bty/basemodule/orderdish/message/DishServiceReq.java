package com.zhongmei.bty.basemodule.orderdish.message;

import java.util.List;

import com.zhongmei.bty.basemodule.orderdish.bean.DishServiceStatusItem;

public class DishServiceReq {
    List<DishServiceStatusItem> tradeItems;

    public List<DishServiceStatusItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<DishServiceStatusItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

}
