package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;

/**
 * Created by demo on 2018/12/15
 */

public class AddFeeResp {

    private DeliveryOrderRecord deliveryOrderRecord;

    public DeliveryOrderRecord getDeliveryOrderRecord() {
        return deliveryOrderRecord;
    }

    public void setDeliveryOrderRecord(DeliveryOrderRecord deliveryOrderRecord) {
        this.deliveryOrderRecord = deliveryOrderRecord;
    }
}
