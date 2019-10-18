package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerListResp;


public class EventCreateOrEditCustomer {
    public int type;

    public CustomerListResp bean;



    public EventCreateOrEditCustomer(int type, CustomerListResp bean) {
        this.type = type;
        this.bean = bean;
    }

}
