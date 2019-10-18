package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import java.io.Serializable;



public class CusomerPayInfo implements Serializable {
    private long customerId;
    private double memberCardBalance;
    private long memberIntegral;
    private CustomerResp customer;
    public CustomerResp cardCustomer;
    private EcCard ecCard;

    private String memberPassword;
    private CustomerLoginResp memberResp;
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public double getMemberCardBalance() {
        return memberCardBalance;
    }

    public void setMemberCardBalance(double memberCardBalance) {
        this.memberCardBalance = memberCardBalance;
    }

    public long getMemberIntegral() {
        return memberIntegral;
    }

    public void setMemberIntegral(long memberIntegral) {
        this.memberIntegral = memberIntegral;
    }

    public CustomerResp getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResp customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerId = customer.customerId;
        } else {
            this.customerId = 0;
        }
    }

    public EcCard getEcCard() {
        return ecCard;
    }

    public void setEcCard(EcCard ecCard) {
        this.ecCard = ecCard;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public CustomerLoginResp getMemberResp() {
        return memberResp;
    }

    public void setMemberResp(CustomerLoginResp memberResp) {
        this.memberResp = memberResp;
    }
}
