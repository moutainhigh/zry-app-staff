package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;

/**
 * Created by demo on 2018/12/15
 */

public class DispatchOrderResp extends TradeResp {
    private DeliveryOrder deliveryOrder;

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
}
