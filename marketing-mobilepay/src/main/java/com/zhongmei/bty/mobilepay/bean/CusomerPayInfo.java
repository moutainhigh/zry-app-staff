package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import java.io.Serializable;

/**
 * 保存储值支付相关信息(会员相关信息和匿名卡相关信息)
 */

public class CusomerPayInfo implements Serializable {
    private long customerId;//会员服务器id

    private double memberCardBalance;// 会员余额

    private long memberIntegral;// 会员积分

    private CustomerResp customer;// 会员姓名

    public CustomerResp cardCustomer; //临时添加: 实体卡储值需要

    private EcCard ecCard;

    private String memberPassword;// 会员支付密码

    private CustomerLoginResp memberResp;// 会员储值信息

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
