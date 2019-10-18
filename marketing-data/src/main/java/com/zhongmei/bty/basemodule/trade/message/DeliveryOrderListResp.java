package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;

import java.util.List;



public class DeliveryOrderListResp {
    List<DeliveryOrder> orders;
    List<DeliveryOrderRecord> orderRecords;

    public List<DeliveryOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryOrder> orders) {
        this.orders = orders;
    }

    public List<DeliveryOrderRecord> getOrderRecords() {
        return orderRecords;
    }

    public void setOrderRecords(List<DeliveryOrderRecord> orderRecords) {
        this.orderRecords = orderRecords;
    }
}
