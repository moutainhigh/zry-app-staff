package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;



public class PrivilegeItemDetails {
    private String privileName;
    private BigDecimal privileAmount;
    private String percent;

    public String getPrivileName() {
        return privileName;
    }

    public void setPrivileName(String privileName) {
        this.privileName = privileName;
    }

    public BigDecimal getPrivileAmount() {
        return privileAmount;
    }

    public void setPrivileAmount(BigDecimal privileAmount) {
        this.privileAmount = privileAmount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
