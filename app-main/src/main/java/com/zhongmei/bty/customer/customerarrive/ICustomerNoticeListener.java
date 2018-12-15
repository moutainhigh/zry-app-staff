package com.zhongmei.bty.customer.customerarrive;

import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;

/**
 * Created by demo on 2018/12/15
 */

public interface ICustomerNoticeListener {
    public void onCustomerChanged(CustomerArrivalShop customerArrivalShop);
}
