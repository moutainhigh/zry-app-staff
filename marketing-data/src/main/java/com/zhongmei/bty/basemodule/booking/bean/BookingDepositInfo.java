package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BookingDepositInfo implements Serializable {

    private Trade trade;

    //private Payment payment;
    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    /*public Payment getPayment() {
        return payment;
    }*/

    /*public void setPayments(Payment payment) {
        this.payment = payment;
    }*/

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    /**
     * 设置预订金金额
     *
     * @param tradeAmount
     */
    public void setTradeAmount(BigDecimal tradeAmount) {
        if (trade == null) trade = new Trade();
        trade.setTradeAmount(tradeAmount);
    }

    /**
     * 获取预订金金额
     *
     * @return
     */
    public BigDecimal getTradeAmount() {
        return trade != null && trade.getTradeAmount() != null ? trade.getTradeAmount() : BigDecimal.ZERO;
    }

}
