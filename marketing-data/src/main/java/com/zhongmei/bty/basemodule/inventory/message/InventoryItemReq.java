package com.zhongmei.bty.basemodule.inventory.message;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class InventoryItemReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long dishId;

    private String dishUuid;

    private BigDecimal quantity;    //退库存数量

    private String dishName;

    private BigDecimal amount;

    private BigDecimal price;

    private BigDecimal returnQuantity; //退货数量

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
