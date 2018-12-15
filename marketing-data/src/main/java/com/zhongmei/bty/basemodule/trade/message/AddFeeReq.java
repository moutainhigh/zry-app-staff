package com.zhongmei.bty.basemodule.trade.message;

/**
 * Created by demo on 2018/12/15
 */

public class AddFeeReq {

    private Long shopIdenty;
    private Long deliveryOrderId;
    private int deliveryPlatform;
    private double amount;
    private String operaterName;
    private Long operaterNo;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public int getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(int deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
    }

    public Long getOperaterNo() {
        return operaterNo;
    }

    public void setOperaterNo(Long operaterNo) {
        this.operaterNo = operaterNo;
    }
}
