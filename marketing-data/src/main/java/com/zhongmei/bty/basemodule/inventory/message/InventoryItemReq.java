package com.zhongmei.bty.basemodule.inventory.message;

import java.io.Serializable;
import java.math.BigDecimal;



public class InventoryItemReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long dishId;

    private String dishUuid;

    private BigDecimal quantity;
    private String dishName;

    private BigDecimal amount;

    private BigDecimal price;

    private BigDecimal returnQuantity;
    public String getSkuUuid() {
        return dishUuid;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getDishName() {
        return dishName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setSkuUuid(String skuUuid) {
        this.dishUuid = skuUuid;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setSkuName(String skuName) {
        this.dishName = skuName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getReturnQuantity() {
        return returnQuantity;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }
}
