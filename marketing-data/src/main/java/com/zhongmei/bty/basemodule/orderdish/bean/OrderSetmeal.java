package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;

import java.math.BigDecimal;
import java.util.Set;


public class OrderSetmeal extends OrderDish {

    protected DishSetmeal setmeal;

    public OrderSetmeal(DishAndStandards dish, DishSetmeal setmeal) {
        super(dish);
        this.setmeal = setmeal;
    }

    public OrderSetmeal(DishShop dishShop, Set<DishProperty> standards, DishSetmeal setmeal, DishUnitDictionary unit) {
        super(dishShop, standards, unit);
        this.setmeal = setmeal;
    }

    public DishSetmeal getSetmeal() {
        return setmeal;
    }

    public void setSetmeal(DishSetmeal setmeal) {
        this.setmeal = setmeal;
    }

    public Long getSetmealId() {
        if (setmeal != null) {
            return setmeal.getId();
        } else {
            return -1L;
        }
    }

    public Long getSetmealGroupId() {
        if (setmeal != null) {
            return setmeal.getComboDishTypeId();
        } else {
            return -1L;
        }
    }

    @Override
    public BigDecimal getPrice() {
        if (getDefinePrice() != null) {
            return getDefinePrice();
        }
        if (setmeal != null) {
            return setmeal.getPrice();
        } else {
            return getPrice();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((setmeal == null) ? 0 : setmeal.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderSetmeal other = (OrderSetmeal) obj;
        if (setmeal == null) {
            if (other.setmeal != null)
                return false;
        } else if (!setmeal.equals(other.setmeal))
            return false;
        return true;
    }


}
