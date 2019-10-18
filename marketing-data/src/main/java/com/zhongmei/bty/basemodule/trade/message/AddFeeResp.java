package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;



public class AddFeeResp {

    private DeliveryOrderRecord deliveryOrderRecord;

    public DeliveryOrderRecord getDeliveryOrderRecord() {
        return deliveryOrderRecord;
    }

    public void setDeliveryOrderRecord(DeliveryOrderRecord deliveryOrderRecord) {
        this.deliveryOrderRecord = deliveryOrderRecord;
    }
}
