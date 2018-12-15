package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * 菜品与美团券关系接口
 */

public interface ICouponDishRelate {
    String getTradeItemUuid();

    Long getDishId();

    BigDecimal getDishNum();
}
