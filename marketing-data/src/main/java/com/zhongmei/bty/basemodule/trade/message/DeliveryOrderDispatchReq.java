package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单下发配送平台请求体
 */

public class DeliveryOrderDispatchReq {
    //品牌ID
    private Long brandId;
    //门店ID
    private Long shopId;
    //目标配送平台
    private Integer deliveryPlatform;
    //下发配送订单信息
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
        //订单ID
        private Long orderId;
        //订单号
        private String orderNo;
        //是否重发配送单(正整数，0-否，>1-是,0标识首次下发，>1标识第n次下发，如3标识第三次下发请求)默认传0标识未知
        private Integer isResend;
        //配送费金额
        private BigDecimal deliveryFee;
        //配送订单类型（1-派送，2-取件,默认传1）
        private Integer orderType;
        //前配送平台编码
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
