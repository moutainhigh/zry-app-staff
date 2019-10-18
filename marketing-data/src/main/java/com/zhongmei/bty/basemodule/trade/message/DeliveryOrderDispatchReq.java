package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;
import java.util.List;



public class DeliveryOrderDispatchReq {
        private Long brandId;
        private Long shopId;
        private Integer deliveryPlatform;
        private List<DispatchOrder> orders;

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

    public List<DispatchOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DispatchOrder> orders) {
        this.orders = orders;
    }

    public static class DispatchOrder {
                private Long orderId;
                private String orderNo;
                private Integer isResend;
                private BigDecimal deliveryFee;
                private Integer orderType;
                private Integer lastDeliveryPlatform;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Integer getIsResend() {
            return isResend;
        }

        public void setIsResend(Integer isResend) {
            this.isResend = isResend;
        }

        public BigDecimal getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeliveryFee(BigDecimal deliveryFee) {
            this.deliveryFee = deliveryFee;
        }

        public Integer getOrderType() {
            return orderType;
        }

        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }

        public Integer getLastDeliveryPlatform() {
            return lastDeliveryPlatform;
        }

        public void setLastDeliveryPlatform(Integer lastDeliveryPlatform) {
            this.lastDeliveryPlatform = lastDeliveryPlatform;
        }
    }
}
