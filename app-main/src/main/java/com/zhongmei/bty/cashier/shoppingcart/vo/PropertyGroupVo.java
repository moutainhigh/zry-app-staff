package com.zhongmei.bty.cashier.shoppingcart.vo;

import java.util.List;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;


public class PropertyGroupVo<T extends DishPropertyVo> {


    private DishPropertyType propertyType;

    private List<T> propertyList;

    public PropertyGroupVo(DishPropertyType propertyType, List<T> propertyList) {
        this.propertyType = propertyType;
        this.propertyList = propertyList;
    }

    public DishPropertyType getPropertyType() {
        return propertyType;
    }

    public List<T> getPropertyList() {
        return propertyList;
    }

    public DishProperty getProperty(int index) {
        return propertyList.get(index).getProperty();
    }

    @Override
    public String toString() {
        return "PropertyGroupVo [name=" + propertyType.getName() + "]";
    }

}
