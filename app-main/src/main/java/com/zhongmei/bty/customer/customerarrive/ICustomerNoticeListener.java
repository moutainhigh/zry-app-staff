package com.zhongmei.bty.customer.customerarrive;

import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;



public interface ICustomerNoticeListener {
    public void onCustomerChanged(CustomerArrivalShop customerArrivalShop);
}
