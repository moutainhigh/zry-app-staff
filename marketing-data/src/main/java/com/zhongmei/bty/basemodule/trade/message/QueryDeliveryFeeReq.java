package com.zhongmei.bty.basemodule.trade.message;



public class QueryDeliveryFeeReq {
        private Long brandId;

        private Long shopId;

        private Long tradeId;

        private String tradeNo;

        private String thirdTranNo;

        private Integer deliveryPlatform;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getThirdTranNo() {
        return thirdTranNo;
    }

    public void setThirdTranNo(String thirdTranNo) {
        this.thirdTranNo = thirdTranNo;
    }

    public Integer getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(Integer deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }
}
