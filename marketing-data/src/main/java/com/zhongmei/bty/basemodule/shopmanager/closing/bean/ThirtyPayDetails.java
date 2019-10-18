package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;



public class ThirtyPayDetails {
    private String payName;    private BigDecimal payAmount;    private BigDecimal thirdUsefulAmount;    private int itemCount;
    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getThirdUsefulAmount() {
        return thirdUsefulAmount;
    }

    public void setThirdUsefulAmount(BigDecimal thirdUsefulAmount) {
        this.thirdUsefulAmount = thirdUsefulAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
