package com.zhongmei.bty.customer.event;

import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.OrderCategory;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.WindowToken;
import com.zhongmei.bty.basemodule.customer.bean.CustomerSellOrderBean;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;


public class EventClickOrdercenterListItem {
    public OrderCategory orderCategory;

    public WindowToken windowToken;

    public EntityCardType entityCardType;

    public long tradeId = -1;

    public CustomerOrderBean orderBean;

    public boolean isEmpty = false;

    public EventClickOrdercenterListItem(WindowToken windowToken, OrderCategory orderCategory) {
        this.orderCategory = orderCategory;
        this.windowToken = windowToken;
    }

    public EventClickOrdercenterListItem(WindowToken windowToken, OrderCategory orderCategory, long tradeId) {
        this.tradeId = tradeId;
        this.windowToken = windowToken;
        this.orderCategory = orderCategory;
    }

    public EventClickOrdercenterListItem(WindowToken windowToken, OrderCategory orderCategory,
                                         CustomerOrderBean orderBean) {
        this.windowToken = windowToken;
        this.orderBean = orderBean;
        this.orderCategory = orderCategory;
    }

    public EventClickOrdercenterListItem(WindowToken windowToken, OrderCategory orderCategory, boolean isEmpty) {
        this.windowToken = windowToken;
        this.orderCategory = orderCategory;
        this.isEmpty = isEmpty;
    }

    public EntityCardType getEntityCardType() {
        return entityCardType;
    }

    public void setEntityCardType(EntityCardType entityCardType) {
        this.entityCardType = entityCardType;
    }
}
