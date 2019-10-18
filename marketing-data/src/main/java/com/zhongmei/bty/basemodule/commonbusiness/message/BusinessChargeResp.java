package com.zhongmei.bty.basemodule.commonbusiness.message;

import java.math.BigDecimal;
import java.util.List;


public class BusinessChargeResp {

    private BigDecimal businessAmount;


    private BigDecimal unGetAmount;


    private BigDecimal tableAvg;


    private BigDecimal personAvg;


    public BigDecimal getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(BigDecimal businessAmount) {
        this.businessAmount = businessAmount;
    }

    public BigDecimal getUnGetAmount() {
        return unGetAmount;
    }

    public void setUnGetAmount(BigDecimal unGetAmount) {
        this.unGetAmount = unGetAmount;
    }

    public BigDecimal getTableAvg() {
        return tableAvg;
    }

    public void setTableAvg(BigDecimal tableAvg) {
        this.tableAvg = tableAvg;
    }

    public BigDecimal getPersonAvg() {
        return personAvg;
    }

    public void setPersonAvg(BigDecimal personAvg) {
        this.personAvg = personAvg;
    }
}

