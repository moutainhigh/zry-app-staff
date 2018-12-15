package com.zhongmei.bty.cashier.shoppingcart.vo;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;

/**
 * 对属性的封装
 *
 * @version: 1.0
 * @date 2015年7月9日
 */
public class DishPropertyVo {

    private DishProperty property;
    /**
     * 是否选中
     */
    private boolean selected;

    public DishPropertyVo(DishProperty property, boolean selected) {
        this.property = property;
        this.selected = selected;
    }

    public DishProperty getProperty() {
        return property;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
        DishPropertyVo other = (DishPropertyVo) obj;
        if (property == null) {
            if (other.property != null)
                return false;
        } else if (!property.equals(other.property))
            return false;
        return true;
    }

}
