package com.zhongmei.bty.basemodule.inventory.bean;

import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class InventoryVo implements Serializable, NoProGuard {
    private static final long serialVersionUID = 1L;

    private HashMap<String, InventoryItem> returnInventoryItemMap;//扣库存的数据，包含套餐外壳未携带子菜

    private List<TradeItem> newAddDishList;//新增的菜品

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
//        Set<TradeItem> tradeItemSet = new HashSet<>();
//        tradeItemSet.addAll(this.newAddDishList);
//        this.newAddDishList.clear();
//        this.newAddDishList.addAll(tradeItemSet);
    }
}
