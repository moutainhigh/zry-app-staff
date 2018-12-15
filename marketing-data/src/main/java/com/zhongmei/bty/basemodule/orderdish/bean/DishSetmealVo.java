package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;

/**
 * @version: 1.0
 * @date 2015年7月8日
 */
public class DishSetmealVo extends DishVo {

    protected final DishSetmeal setmeal;
    private BigDecimal selectedQty;

    public DishSetmealVo(DishShop dish, DishSetmeal setmeal, DishUnitDictionary unit) {
        this(dish, setmeal, new LinkedHashSet<DishProperty>(), unit);
    }

    public DishSetmealVo(DishShop dish, DishSetmeal setmeal, Set<DishProperty> standards, DishUnitDictionary unit) {
        super(dish, standards, unit);
        this.setmeal = setmeal;
        selectedQty = BigDecimal.ZERO;
    }

    public DishSetmeal getSetmeal() {
        return setmeal;
    }

    @Override
    public BigDecimal getPrice() {
        if (setmeal != null) {
            return setmeal.getPrice();
        } else {
            return getPrice();
        }
    }

    public BigDecimal getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(BigDecimal selectedQty) {
        this.selectedQty = selectedQty;
    }

    public OrderSetmeal toOrderSetmeal() {
        return new OrderSetmeal(dish, setmeal);
    }

    @Override
    public OrderDish toOrderDish() {
        return toOrderSetmeal();
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
        DishSetmealVo other = (DishSetmealVo) obj;
        if (setmeal == null) {
            if (other.setmeal != null)
                return false;
        } else if (!setmeal.equals(other.setmeal))
            return false;
        return true;
    }

}
