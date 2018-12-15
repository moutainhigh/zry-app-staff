package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerResp;

/**
 * Created by demo on 2018/12/15
 */

public class EventDinnerCustomerRegisterRecharge {
    private CustomerResp mCustomer;

    public EventDinnerCustomerRegisterRecharge(CustomerResp mCustomer) {
        this.mCustomer = mCustomer;
    }

    public CustomerResp getmCustomer() {
        return mCustomer;
    }
}
