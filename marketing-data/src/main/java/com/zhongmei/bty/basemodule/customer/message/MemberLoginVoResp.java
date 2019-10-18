package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;



public class MemberLoginVoResp extends LoyaltyTransferResp<CustomerLoginResp> {

    private CustomerLoginType customerLoginType;
    public CustomerLoginType getCustomerLoginType() {
        return customerLoginType;
    }

    public void setCustomerLoginType(CustomerLoginType customerLoginType) {
        this.customerLoginType = customerLoginType;
    }

}
