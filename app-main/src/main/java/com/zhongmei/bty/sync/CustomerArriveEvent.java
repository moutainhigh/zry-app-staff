package com.zhongmei.bty.sync;

import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;

/**
 * 顾客到店事件
 *
 * @created 2017/7/25
 */
public class CustomerArriveEvent {

    public CustomerArrivalShop getCustomerArrivalShop() {
        return customerArrivalShop;
    }

    CustomerArrivalShop customerArrivalShop;

    public CustomerArriveEvent(CustomerArrivalShop customerArrivalShop) {
        this.customerArrivalShop = customerArrivalShop;
    }
}
