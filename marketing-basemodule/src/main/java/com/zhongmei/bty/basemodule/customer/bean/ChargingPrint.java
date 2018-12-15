package com.zhongmei.bty.basemodule.customer.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 实体卡和会员储值信息
 */
public class ChargingPrint implements Serializable {
    /**
     * @date：2015-6-11 上午11:18:43
     */
    private static final long serialVersionUID = 1L;

    // 客户名字
    private String customerName;

    // 客户性别 0-》女 1-》男
    private String customerSex;

    // 判断是储值还是退货  0-》储值 1-》退货 2-》储值消费
    private int chargingType;

    //交易时间
    private Long chargingTime;

    // 充值或退货前金额
    private BigDecimal beforeValuecard;

    // 实收金额（给的钱）
    private BigDecimal trueIncomeValuecard;

    // 实充金额
    private BigDecimal chargeValuecard;

    // 充值或退货后的余额
    private BigDecimal endValuecard;

    //充值前本金
    private BigDecimal capitalStart;
    //充值后本金
    private BigDecimal capitalEnd;
    //充值前赠送
    private BigDecimal presentStart;
    //充值后赠送
    private BigDecimal presentEnd;

    // 收银员name
    private String userId;

    // 商店名称
    private String commercialName;

    // 顾客电话号码
    private String phoneNo;

    // 支付方式
    private List<PayMethod> payMethods;

    // 会员积分
    private String customerIntegral;

    //实体卡号
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
