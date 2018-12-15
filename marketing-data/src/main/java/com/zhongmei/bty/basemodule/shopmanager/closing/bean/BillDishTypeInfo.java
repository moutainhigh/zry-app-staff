package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 菜品类别信息表
 * Created by demo on 2018/12/15
 */
public class BillDishTypeInfo implements Serializable {
    /**
     * 菜品类别Id
     */
    private Long dishTypeId;
    /**
     * 菜品类别名称
     */
    private String typeName;
    /**
     * 菜品类别下的菜品明细
     */
    private List<BillDishInfo> billDishInfoList;

    /**
     * 数量
     */
    private String quantity;
    /**
     * 总价
     */
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
