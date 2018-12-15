package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;

public class TradePayStateResp {
    public Integer payState;

    public String message;

    public int status;

    private Trade trade;

    private List<Trade> trades;

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<PaymentItemExtra> paymentItemExtras;

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getPayState() {
        return this.payState;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
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

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<PaymentItemExtra> getPaymentItemExtras() {
        return paymentItemExtras;
    }

    public void setPaymentItemExtras(List<PaymentItemExtra> paymentItemExtras) {
        this.paymentItemExtras = paymentItemExtras;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}
