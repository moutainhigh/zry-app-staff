package com.zhongmei.bty.basemodule.customer.message;

import java.math.BigDecimal;


public class MemberRechargeReq extends RechargeReq {


    private BigDecimal bankValueCard;


    private BigDecimal cashValueCard;


    private BigDecimal totalValueCard;


    private Integer source;


    private Long paymentTime;

    public BigDecimal getBankValueCard() {
        return bankValueCard;
    }

    public void setBankValueCard(BigDecimal bankValueCard) {
        this.bankValueCard = bankValueCard;
    }

    public BigDecimal getCashValueCard() {
        return cashValueCard;
    }

    public void setCashValueCard(BigDecimal cashValueCard) {
        this.cashValueCard = cashValueCard;
    }

    public BigDecimal getTotalValueCard() {
        return totalValueCard;
    }

    public void setTotalValueCard(BigDecimal totalValueCard) {
        this.totalValueCard = totalValueCard;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
    }

}
