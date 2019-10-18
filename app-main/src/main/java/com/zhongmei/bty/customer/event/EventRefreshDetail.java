package com.zhongmei.bty.customer.event;


public class EventRefreshDetail {
    public Long customerId;

    public String moblie;

    public EventRefreshDetail(Long customerId) {
        this.customerId = customerId;
    }

    public EventRefreshDetail(Long customerId, String moblie) {
        this.customerId = customerId;
        this.moblie = moblie;
    }

}
