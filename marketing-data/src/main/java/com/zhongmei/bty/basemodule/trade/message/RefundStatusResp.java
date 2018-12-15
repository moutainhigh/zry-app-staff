package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.entity.trade.Trade;

public class RefundStatusResp {

    private List<Trade> trades;

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<PaymentItemUnionpay> paymentItemUnionpays;

    private RefundStatusResp(List<Trade> trades, List<PaymentItem> paymentItems) {
        this.trades = trades;
        this.paymentItems = paymentItems;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PaymentItemUnionpay> getPaymentItemUnionpays() {
        return paymentItemUnionpays;
    }

    public void setPaymentItemUnionpays(List<PaymentItemUnionpay> paymentItemUnionpays) {
        this.paymentItemUnionpays = paymentItemUnionpays;
    }
}
