package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class MeituanDishItem implements ICouponDishRelate {
    public Long skuId;
    public String skuUuid;
    public Long tradeItemId;
    public String tradeItemUuid;
    public String dishName;
    public BigDecimal price;
    //          匹配的菜品数量
    public BigDecimal num;
    //是否建立关联表数据
    public boolean isNeedRelate = true;

    public int deductionType;//add v8.14 抵扣类型 0 抵数量 ，1 抵金额

    public BigDecimal deductionAmount;//add v8.14 抵扣金额

    @Override
    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    @Override
    public Long getDishId() {
        return skuId;
    }

    @Override
    public BigDecimal getDishNum() {
        return num;
    }
}