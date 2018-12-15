package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerResp;

/**
 * 顾客明细刷新
 */
public class DetailRefreshEvent {
    public final CustomerResp customer;

    public DetailRefreshEvent(CustomerResp customer) {
        this.customer = customer;
    }

}
