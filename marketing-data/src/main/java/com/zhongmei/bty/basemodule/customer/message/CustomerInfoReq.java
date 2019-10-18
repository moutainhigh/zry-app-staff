package com.zhongmei.bty.basemodule.customer.message;

public class CustomerInfoReq {
    private String mobile;

    private Long customerId;

    private String password;

    private int needPassword = 2;
    private int isCustomer = 2;

    private int ignoreDisable = 2;

    private String wxNo;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(int needPassword) {
        this.needPassword = needPassword;
    }

    public int getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(int isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getWxNo() {
        return wxNo;
    }

    public void setWxNo(String wxNo) {
        this.wxNo = wxNo;
    }

    public int getIgnoreDisable() {
        return ignoreDisable;
    }

    public void setIgnoreDisable(int ignoreDisable) {
        this.ignoreDisable = ignoreDisable;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
