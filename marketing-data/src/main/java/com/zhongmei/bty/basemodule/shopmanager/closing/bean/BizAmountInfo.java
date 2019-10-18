package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;
import java.math.BigDecimal;


public class BizAmountInfo implements Serializable {
    private Double tradeAmount = 0.00;    private Double privilegeAmount = 0.00;    private Double exemptAmount = 0.00;    private Double redundant = 0.00;    private BigDecimal totalTradeAmount;    private BigDecimal tradeAmountBefore;    private BigDecimal promotionAmount;
    private BigDecimal depositAmount;
    private BigDecimal roundOffAmount;     private BigDecimal serviceChargeAmount;


    public BigDecimal getMantissaAmount() {
        BigDecimal mantissaAmount = BigDecimal.ZERO;
        if (totalTradeAmount != null && tradeAmountBefore != null) {
            mantissaAmount = tradeAmountBefore.subtract(totalTradeAmount);
        }
        return mantissaAmount;

    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(BigDecimal promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Double getPrivilegeAmount() {
        return privilegeAmount;
    }

    public void setPrivilegeAmount(Double privilegeAmount) {
        this.privilegeAmount = privilegeAmount;
    }

    public Double getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(Double exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public Double getRedundant() {
        return redundant;
    }

    public void setRedundant(Double redundant) {
        this.redundant = redundant;
    }

    public BigDecimal getTotalTradeAmount() {
        return totalTradeAmount;
    }

    public void setTotalTradeAmount(BigDecimal totalTradeAmount) {
        this.totalTradeAmount = totalTradeAmount;
    }

    public BigDecimal getTradeAmountBefore() {
        return tradeAmountBefore;
    }

    public void setTradeAmountBefore(BigDecimal tradeAmountBefore) {
        this.tradeAmountBefore = tradeAmountBefore;
    }

    public BigDecimal getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(BigDecimal roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }
}
