package com.zhongmei.bty.basemodule.commonbusiness.message;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Date 2016/7/1
 * @Description:打印小票数据响应
 */
public class BusinessChargeResp {
    /**
     * 已结账金额
     */
    private BigDecimal businessAmount;

    /**
     * 未结账金额
     */
    private BigDecimal unGetAmount;

    /**
     * 桌均价
     */
    private BigDecimal tableAvg;

    /**
     * 人均价
     */
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

