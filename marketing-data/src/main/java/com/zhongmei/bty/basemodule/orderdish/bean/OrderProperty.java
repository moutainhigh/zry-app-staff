package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.enums.PropertyKind;

import java.math.BigDecimal;


public class OrderProperty implements IOrderProperty {

    private final DishPropertyType propertyType;
    private final DishProperty property;

    public OrderProperty(DishPropertyType propertyType, DishProperty property) {
        this.propertyType = propertyType;
        this.property = property;
    }

    public DishPropertyType getPropertyType() {
        return propertyType;
    }

    public DishProperty getProperty() {
        return property;
    }

    @Override
    public String getPropertyUuid() {
        return property.getUuid();
    }

    @Override
    public String getPropertyName() {
        return property.getName();
    }

    @Override
    public PropertyKind getPropertyKind() {
        return property.getPropertyKind();
    }

    @Override
    public BigDecimal getPropertyPrice() {
        return property.getReprice();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderProperty other = (OrderProperty) obj;
        if (property == null) {
            if (other.property != null)
                return false;
        } else if (!property.equals(other.property))
            return false;
        return true;
    }

}
