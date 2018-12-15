package com.zhongmei.bty.entity.bean.print.handover;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交接明细条目
 */
public class CashHandoverItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付方式
     */
    private String payModeName;

    /**
     * 实时金额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 营业金额
     */
    private BigDecimal orderAmount = BigDecimal.ZERO;

    /**
     * 储值金额
     */
    private BigDecimal valuecardAmount = BigDecimal.ZERO;

    /**
     * 交接金额
     */
    private BigDecimal actualAmount = BigDecimal.ZERO;

    /**
     * 差额
     */
    private BigDecimal diffAmount = BigDecimal.ZERO;

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getValuecardAmount() {
        return valuecardAmount;
    }

    public void setValuecardAmount(BigDecimal valuecardAmount) {
        this.valuecardAmount = valuecardAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
