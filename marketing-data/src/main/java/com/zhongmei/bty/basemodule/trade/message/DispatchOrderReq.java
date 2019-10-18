package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;



public class DispatchOrderReq {
    private Integer deliveryPlatform;
    private List<Long> tradeIds;

    public void setDeliveryPlatform(Integer deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public void setTradeIds(List<Long> tradeIds) {
        this.tradeIds = tradeIds;
    }
}
