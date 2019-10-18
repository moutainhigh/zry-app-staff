package com.zhongmei.bty.cashier.shoppingcart.vo;


public class ChooseVo<T> {

    private T property;

    private boolean selected;

    public ChooseVo(T property, boolean selected) {
        this.property = property;
        this.selected = selected;
    }

    public T getProperty() {
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
        ChooseVo other = (ChooseVo) obj;
        if (property == null) {
            if (other.property != null)
                return false;
        } else if (!property.equals(other.property))
            return false;
        return true;
    }

}
