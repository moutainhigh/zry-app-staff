package com.zhongmei.bty.basemodule.inventory.message;

import java.util.List;


public class InventoryChangeReq {

    List<InventoryItemReq> returnInventoryItems;

    List<InventoryItemReq> deductInventoryItems;

    public InventoryChangeReq() {
    }

    public InventoryChangeReq(List<InventoryItemReq> returnInventoryItems, List<InventoryItemReq> deductInventoryItems) {
        this.returnInventoryItems = returnInventoryItems;
        this.deductInventoryItems = deductInventoryItems;
    }

    public List<InventoryItemReq> getReturnInventoryItems() {
        return returnInventoryItems;
    }

    public void setReturnInventoryItems(List<InventoryItemReq> returnInventoryItems) {
        this.returnInventoryItems = returnInventoryItems;
    }

    public List<InventoryItemReq> getDeductInventoryItems() {
        return deductInventoryItems;
    }

    public void setDeductInventoryItems(List<InventoryItemReq> deductInventoryItems) {
        this.deductInventoryItems = deductInventoryItems;
    }
}
