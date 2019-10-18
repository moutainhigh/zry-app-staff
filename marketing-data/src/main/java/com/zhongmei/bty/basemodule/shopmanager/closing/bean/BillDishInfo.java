package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;
import java.util.List;


public class BillDishInfo implements Serializable {
    private String typeName;

    private Long typeId;

    private String skuName;

    private Long tradeItemId;

    private Double quantity;

    private Double actualAmount;

    private String serverUpdateTime;


    private String uuid;
    private String dishCode;


    private List<BillDishInfo> subList;

    public String getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(String serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }
	


    public List<BillDishInfo> getSubList() {
        return subList;
    }

    public void setSubList(List<BillDishInfo> subList) {
        this.subList = subList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDishCode() {
        return dishCode;
    }

    public void setDishCode(String dishCode) {
        this.dishCode = dishCode;
    }
}
