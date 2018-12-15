package com.zhongmei.bty.basemodule.customer.message;


public class MemberIntegralInfoReq {

    private String customerId;

    public MemberIntegralInfoReq(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


}
