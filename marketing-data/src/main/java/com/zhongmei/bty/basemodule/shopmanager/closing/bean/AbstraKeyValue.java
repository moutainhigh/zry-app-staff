package com.zhongmei.bty.basemodule.shopmanager.closing.bean;


public abstract class AbstraKeyValue {
    public String payName;
    public String payAmount;

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }
}
