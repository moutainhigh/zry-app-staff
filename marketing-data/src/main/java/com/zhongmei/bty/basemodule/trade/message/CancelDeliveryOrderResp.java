package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;

import java.io.Serializable;



public class CancelDeliveryOrderResp implements Serializable {
    DeliveryOrder deliveryOrder;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
}
