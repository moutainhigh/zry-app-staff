package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class ThirtyPayDetails {
    private String payName;//支付方式名称
    private BigDecimal payAmount;//第三方实收金额
    private BigDecimal thirdUsefulAmount;//第三方预计实付金额
    private int itemCount;//笔数

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
