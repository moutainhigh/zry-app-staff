package com.zhongmei.bty.customer.event;

/**
 * 顾客明细刷新
 */
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
