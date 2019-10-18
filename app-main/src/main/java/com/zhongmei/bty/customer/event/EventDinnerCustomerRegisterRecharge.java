package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerResp;



public class EventDinnerCustomerRegisterRecharge {
    private CustomerResp mCustomer;

    public EventDinnerCustomerRegisterRecharge(CustomerResp mCustomer) {
        this.mCustomer = mCustomer;
    }

    public CustomerResp getmCustomer() {
        return mCustomer;
    }
}
