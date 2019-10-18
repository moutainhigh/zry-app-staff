package com.zhongmei.bty.basemodule.database.entity.customer;


public class OpenIdLoginInfo {

    private String uuid;

    private boolean result;

    private CommCustomer customer;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public CommCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(CommCustomer customer) {
        this.customer = customer;
    }
}
