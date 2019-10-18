package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerResp;


public class DetailRefreshEvent {
    public final CustomerResp customer;

    public DetailRefreshEvent(CustomerResp customer) {
        this.customer = customer;
    }

}
