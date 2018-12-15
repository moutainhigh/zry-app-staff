package com.zhongmei.bty.basemodule.customer.message;

import java.math.BigDecimal;

/**
 * 会员充值请求数据封装
 *
 * @version: 1.0
 * @date 2015年5月8日
 */
public class MemberRechargeReq extends RechargeReq {

    /**
     * 银行卡冲入的钱
     */
    private BigDecimal bankValueCard;

    /**
     * 现金冲入的钱
     */
    private BigDecimal cashValueCard;

    /**
     * 储值
     */
    private BigDecimal totalValueCard;

    /**
     * 来源，1为Calm，2为手机app，3为其他系统导入，4为微信，5支付宝，6商家官网，7百度
     */
    private Integer source;

    /**
     * 付款时间
     */
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
