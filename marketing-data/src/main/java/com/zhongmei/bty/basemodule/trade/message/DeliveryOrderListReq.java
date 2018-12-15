package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

/**
 * 配送订单批量查询请求体
 */

public class DeliveryOrderListReq {
    //品牌编号
    private Long brandId;

    //门店编号
    private Long shopId;

    //订单Id列表
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
