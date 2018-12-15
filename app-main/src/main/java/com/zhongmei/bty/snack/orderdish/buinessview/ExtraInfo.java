package com.zhongmei.bty.snack.orderdish.buinessview;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class ExtraInfo {
    public final OrderExtra orderExtra;
    public BigDecimal qty;

    public ExtraInfo(OrderExtra orderExtra, boolean needCount) {
        this.orderExtra = orderExtra;
        if (needCount) {
            if (orderExtra != null && orderExtra.getSingleQty() != null) {
                qty = orderExtra.getSingleQty();
            } else {
                qty = BigDecimal.ZERO;
            }
        }
    }

    public String getName() {
        return orderExtra.getSkuName();
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return orderExtra.getDishShop().getMarketPrice();
    }

    public BigDecimal getAmount() {
        return qty.multiply(getPrice());
    }
}
