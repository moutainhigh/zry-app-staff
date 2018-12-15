package com.zhongmei.bty.basemodule.inventory.bean;

import java.math.BigDecimal;

/**
 * @Date： 2017/2/28
 * @Description:库存数据对象
 * @Version: 1.0
 */
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
