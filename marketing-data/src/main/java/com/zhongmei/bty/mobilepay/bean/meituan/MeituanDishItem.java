package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;



public class MeituanDishItem implements ICouponDishRelate {
    public Long skuId;
    public String skuUuid;
    public Long tradeItemId;
    public String tradeItemUuid;
    public String dishName;
    public BigDecimal price;
        public BigDecimal num;
        public boolean isNeedRelate = true;

    public int deductionType;
    public BigDecimal deductionAmount;
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