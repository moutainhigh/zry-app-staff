package com.zhongmei.bty.basemodule.booking.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;



public class BookingDepositInfo implements Serializable {

    private Trade trade;

        private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }





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


    public void setTradeAmount(BigDecimal tradeAmount) {
        if (trade == null) trade = new Trade();
        trade.setTradeAmount(tradeAmount);
    }


    public BigDecimal getTradeAmount() {
        return trade != null && trade.getTradeAmount() != null ? trade.getTradeAmount() : BigDecimal.ZERO;
    }

}
