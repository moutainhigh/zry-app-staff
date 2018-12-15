package com.zhongmei.bty.basemodule.trade.message;

import java.io.Serializable;

/**
 * Created by demo on 2018/12/15
 */

public class CancelDeliveryOrderReq implements Serializable {
    private long shopIdenty;
    private int deliveryPlatform;
    private long orderId;
    private String orderNo;
    private long brandIdenty;

    public long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public int getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(int deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }
}
