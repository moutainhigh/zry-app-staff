package com.zhongmei.bty.basemodule.inventory.bean;

import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class InventoryVo implements Serializable, NoProGuard {
    private static final long serialVersionUID = 1L;

    private HashMap<String, InventoryItem> returnInventoryItemMap;
    private List<TradeItem> newAddDishList;
    public HashMap<String, InventoryItem> getReturnInventoryItemMap() {
        return returnInventoryItemMap;
    }

    public void setReturnInventoryItemMap(HashMap<String, InventoryItem> returnInventoryItemMap) {
        this.returnInventoryItemMap = returnInventoryItemMap;
    }

    public List<TradeItem> getNewAddDishList() {
        return newAddDishList;
    }

    public void addNewAddDishList(List<TradeItem> newAddDishList) {
        if (Utils.isEmpty(this.newAddDishList)) {
            this.newAddDishList = new ArrayList<>();
        }
        this.newAddDishList.removeAll(newAddDishList);
        this.newAddDishList.addAll(newAddDishList);
    }
}
