package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class PrivilegeItem {
    private String privileTypeName;//优惠类型名称
    private BigDecimal privileTypeAmount;//优惠金额
    private String percent;//优惠占比
    private List<PrivilegeItemDetails> privilegeItemses;

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
