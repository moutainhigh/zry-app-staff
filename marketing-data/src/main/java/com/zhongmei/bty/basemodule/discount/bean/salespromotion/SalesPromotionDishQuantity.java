package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.io.Serializable;
import java.math.BigDecimal;


public class SalesPromotionDishQuantity implements Serializable {


    public Long dishTypeId;


    public Long brandDishId;

    public BigDecimal singleQty;


    public BigDecimal price;


    public IShopcartItem shopcartItem;
}
