package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.trade.message.RecisionDinnerReq;

import java.util.List;

/**
 * 作废订单请求V2
 * Created by demo on 2018/12/15
 */

public class BeautyTradeDeleteReq {
    private RecisionDinnerReq obsoleteRequest;
    List<InventoryItemReq> returnInventoryItems;
    private boolean reviseStock = true;//退换库存

    public List<InventoryItemReq> getReturnInventoryItems() {
        return returnInventoryItems;
    }

    public void setReturnInventoryItems(List<InventoryItemReq> returnInventoryItems) {
        if (this.returnInventoryItems == null) {
            this.returnInventoryItems = returnInventoryItems;
        } else {
            this.returnInventoryItems.addAll(returnInventoryItems);
        }
    }

    public RecisionDinnerReq getObsoleteRequest() {
        return obsoleteRequest;
    }

    public void setObsoleteRequest(RecisionDinnerReq obsoleteRequest) {
        this.obsoleteRequest = obsoleteRequest;
    }

    public boolean isReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(boolean reviseStock) {
        this.reviseStock = reviseStock;
    }
}
