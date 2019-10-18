package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.data.operates.message.content.InventoryReq;



public class BeautyModifyReq {
    BeautyTradeReq tradeRequest;
    InventoryChangeReq inventoryRequest;

    public BeautyTradeReq getTradeRequest() {
        return tradeRequest;
    }

    public void setTradeRequest(BeautyTradeReq tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public InventoryChangeReq getInventoryRequest() {
        return inventoryRequest;
    }

    public void setInventoryRequest(InventoryChangeReq inventoryRequest) {
        this.inventoryRequest = inventoryRequest;
    }
}
