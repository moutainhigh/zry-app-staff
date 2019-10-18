package com.zhongmei.bty.basemodule.inventory.bean;

import java.math.BigDecimal;


public class InventoryInfo {

    private String uuid;

    private BigDecimal inventoryQty;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(BigDecimal inventoryQty) {
        this.inventoryQty = inventoryQty;
    }
}
