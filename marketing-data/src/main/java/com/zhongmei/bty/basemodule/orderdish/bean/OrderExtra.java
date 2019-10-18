package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;


public class OrderExtra extends OrderSetmeal {

    public OrderExtra(DishShop dishShop, DishSetmeal setmeal) {
        super(new DishAndStandards(dishShop, null), setmeal);
    }

    @Override
    public BigDecimal getPrice() {
        return dish.getPrice();
    }

    @Override
    public OrderExtra clone() throws CloneNotSupportedException {
        OrderExtra orderExtra = new OrderExtra(dish.getDishShop(), setmeal);
        orderExtra.setQty(getSingleQty() == null ? BigDecimal.ZERO : getSingleQty(),
                getTotalQty() == null ? BigDecimal.ZERO : getTotalQty());
        orderExtra.setDefineName(getDefineName());

        return orderExtra;
    }
}
