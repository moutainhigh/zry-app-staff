package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.trade.message.RecisionDinnerReq;

import java.util.List;



public class BeautyTradeDeleteReq {
    private RecisionDinnerReq obsoleteRequest;
    List<InventoryItemReq> returnInventoryItems;
    private boolean reviseStock = true;
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
