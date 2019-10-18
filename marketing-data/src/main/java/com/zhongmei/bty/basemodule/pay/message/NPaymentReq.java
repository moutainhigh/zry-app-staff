package com.zhongmei.bty.basemodule.pay.message;

import java.math.BigDecimal;
import java.util.List;



public class NPaymentReq {
    private Integer paymentType;
    private String uuid;
    private BigDecimal receivableAmount;
    private BigDecimal exemptAmount;
    private BigDecimal actualAmount;
    private BigDecimal shopActualAmount;
    private int recycleStatus = 1;     List<NPaymentItemReq> paymentItems;

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }


    public String getUuid() {
        return uuid;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }


    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getShopActualAmount() {
        return shopActualAmount;
    }

    public void setShopActualAmount(BigDecimal shopActualAmount) {
        this.shopActualAmount = shopActualAmount;
    }

    public BigDecimal getExemptAmount() {
        return exemptAmount;
    }


    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }


    public BigDecimal getActualAmount() {
        return actualAmount;
    }


    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public List<NPaymentItemReq> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<NPaymentItemReq> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<NPaymentItemReq> getPaymentItemList() {
        return paymentItems;
    }

    public void setPaymentItemList(List<NPaymentItemReq> paymentItemList) {
        this.paymentItems = paymentItemList;
    }
}
