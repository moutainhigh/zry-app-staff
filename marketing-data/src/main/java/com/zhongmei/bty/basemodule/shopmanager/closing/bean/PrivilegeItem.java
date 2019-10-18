package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;
import java.util.List;



public class PrivilegeItem {
    private String privileTypeName;    private BigDecimal privileTypeAmount;    private String percent;    private List<PrivilegeItemDetails> privilegeItemses;

    public String getPrivileTypeName() {
        return privileTypeName;
    }

    public void setPrivileTypeName(String privileTypeName) {
        this.privileTypeName = privileTypeName;
    }

    public BigDecimal getPrivileTypeAmount() {
        return privileTypeAmount;
    }

    public void setPrivileTypeAmount(BigDecimal privileTypeAmount) {
        this.privileTypeAmount = privileTypeAmount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public List<PrivilegeItemDetails> getPrivilegeItemses() {
        return privilegeItemses;
    }

    public void setPrivilegeItemses(List<PrivilegeItemDetails> privilegeItemses) {
        this.privilegeItemses = privilegeItemses;
    }
}
