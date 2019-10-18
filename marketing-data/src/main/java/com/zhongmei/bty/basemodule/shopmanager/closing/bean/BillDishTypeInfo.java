package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;
import java.util.List;


public class BillDishTypeInfo implements Serializable {

    private Long dishTypeId;

    private String typeName;

    private List<BillDishInfo> billDishInfoList;


    private String quantity;

    private String actualAmount;


    public Long getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Long dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<BillDishInfo> getBillDishInfoList() {
        return billDishInfoList;
    }

    public void setBillDishInfoList(List<BillDishInfo> billDishInfoList) {
        this.billDishInfoList = billDishInfoList;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }
}
