package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;



public interface ICouponDishRelate {
    String getTradeItemUuid();

    Long getDishId();

    BigDecimal getDishNum();
}
