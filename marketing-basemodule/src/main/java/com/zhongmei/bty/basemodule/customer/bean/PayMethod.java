package com.zhongmei.bty.basemodule.customer.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayMethod implements Serializable {
    /**
     * @date：2015-6-11 上午11:19:08
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private BigDecimal value;    //应收
    private BigDecimal faceAmount; //实收（找零钱收到的）
    private BigDecimal changeAmount; //找零
    private BigDecimal usefulAmount;//找零后实收
    private Long paymentType; //类型


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public PayMethod(String name, BigDecimal value) {
        super();
        this.name = name;
        this.value = value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getFaceAmount() {
        return faceAmount;
    }

    public void setFaceAmount(BigDecimal faceAmount) {
        this.faceAmount = faceAmount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Long paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getUsefulAmount() {
        return usefulAmount;
    }

    public void setUsefulAmount(BigDecimal usefulAmount) {
        this.usefulAmount = usefulAmount;
    }
}
