package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;



public class DeliveryOrderListReq {
        private Long brandId;

        private Long shopId;

        private List<Long> orderIds;

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

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }
}
