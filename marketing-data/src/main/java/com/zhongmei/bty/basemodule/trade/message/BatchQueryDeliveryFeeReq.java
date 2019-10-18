package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;



public class BatchQueryDeliveryFeeReq {
        private Long brandId;

        private Long shopId;
        private Integer deliveryPlatform;

    private List<DeliverFeeOrder> orders;

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

    public Integer getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(Integer deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public List<DeliverFeeOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliverFeeOrder> orders) {
        this.orders = orders;
    }

    public static class DeliverFeeOrder {
                private Long tradeId;

                private String tradeNo;

                private String thirdTranNo;

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

    }
}
