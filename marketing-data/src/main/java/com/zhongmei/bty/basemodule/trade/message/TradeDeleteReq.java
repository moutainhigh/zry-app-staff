package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;

import java.util.List;



public class TradeDeleteReq {
    private RecisionDinnerReq tradeDinnerDeleteRequest;
    List<InventoryItemReq> returnInventoryItems;

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

    public RecisionDinnerReq getTradeDinnerDeleteRequest() {
        return tradeDinnerDeleteRequest;
    }

    public void setTradeDinnerDeleteRequest(RecisionDinnerReq tradeDinnerDeleteRequest) {
        this.tradeDinnerDeleteRequest = tradeDinnerDeleteRequest;
    }
}
