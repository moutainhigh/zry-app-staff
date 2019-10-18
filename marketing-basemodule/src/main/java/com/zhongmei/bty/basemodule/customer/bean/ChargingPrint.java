package com.zhongmei.bty.basemodule.customer.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class ChargingPrint implements Serializable {

    private static final long serialVersionUID = 1L;

        private String customerName;

        private String customerSex;

        private int chargingType;

        private Long chargingTime;

        private BigDecimal beforeValuecard;

        private BigDecimal trueIncomeValuecard;

        private BigDecimal chargeValuecard;

        private BigDecimal endValuecard;

        private BigDecimal capitalStart;
        private BigDecimal capitalEnd;
        private BigDecimal presentStart;
        private BigDecimal presentEnd;

        private String userId;

        private String commercialName;

        private String phoneNo;

        private List<PayMethod> payMethods;

        private String customerIntegral;

        private String cardNum;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(String customerSex) {
        this.customerSex = customerSex;
    }

    public BigDecimal getBeforeValuecard() {
        return beforeValuecard;
    }

    public void setBeforeValuecard(BigDecimal beforeValuecard) {
        this.beforeValuecard = beforeValuecard;
    }

    public BigDecimal getEndValuecard() {
        return endValuecard;
    }

    public void setEndValuecard(BigDecimal endValuecard) {
        this.endValuecard = endValuecard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<PayMethod> getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(List<PayMethod> payMethods) {
        this.payMethods = payMethods;
    }

    public String getCustomerIntegral() {
        return customerIntegral;
    }

    public void setCustomerIntegral(String customerIntegral) {
        this.customerIntegral = customerIntegral;
    }

    public BigDecimal getTrueIncomeValuecard() {
        return trueIncomeValuecard;
    }

    public void setTrueIncomeValuecard(BigDecimal trueIncomeValuecard) {
        this.trueIncomeValuecard = trueIncomeValuecard;
    }

    public BigDecimal getChargeValuecard() {
        return chargeValuecard;
    }

    public void setChargeValuecard(BigDecimal chargeValuecard) {
        this.chargeValuecard = chargeValuecard;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getChargingType() {
        return chargingType;
    }

    public void setChargingType(int chargingType) {
        this.chargingType = chargingType;
    }

    public BigDecimal getCapitalStart() {
        return capitalStart;
    }

    public void setCapitalStart(BigDecimal capitalStart) {
        this.capitalStart = capitalStart;
    }

    public BigDecimal getCapitalEnd() {
        return capitalEnd;
    }

    public void setCapitalEnd(BigDecimal capitalEnd) {
        this.capitalEnd = capitalEnd;
    }

    public BigDecimal getPresentStart() {
        return presentStart;
    }

    public void setPresentStart(BigDecimal presentStart) {
        this.presentStart = presentStart;
    }

    public BigDecimal getPresentEnd() {
        return presentEnd;
    }

    public void setPresentEnd(BigDecimal presentEnd) {
        this.presentEnd = presentEnd;
    }

    public Long getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Long chargingTime) {
        this.chargingTime = chargingTime;
    }
}
