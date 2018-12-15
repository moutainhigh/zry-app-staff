package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;

/**
 * Created by demo on 2018/12/15
 */

public class MemberLoginVoResp extends LoyaltyTransferResp<CustomerLoginResp> {

    private CustomerLoginType customerLoginType; // 本地loginType

    public CustomerLoginType getCustomerLoginType() {
        return customerLoginType;
    }

    public void setCustomerLoginType(CustomerLoginType customerLoginType) {
        this.customerLoginType = customerLoginType;
    }

}
