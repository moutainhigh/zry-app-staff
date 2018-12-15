package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营业概况
 * Created by demo on 2018/12/15
 */
public class BizAmountInfo implements Serializable {
    private Double tradeAmount = 0.00;//销售总额
    private Double privilegeAmount = 0.00;//优惠金额
    private Double exemptAmount = 0.00;//抹零
    private Double redundant = 0.00;//溢收
    private BigDecimal totalTradeAmount;//应收总额（尾数处理后金额）
    private BigDecimal tradeAmountBefore;//应收总额（尾数处理前金额）
    private BigDecimal promotionAmount;
    private BigDecimal depositAmount;
    private BigDecimal roundOffAmount; //尾数处理
    private BigDecimal serviceChargeAmount;

    /**
     * 尾数处理总金额
     */
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
