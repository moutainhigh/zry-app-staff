package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.pay.message.PaymentReq;


public class TradePaymentReq {

    private TradeReq trade;

    private PaymentReq payment;

    public TradeReq getTrade() {
        return trade;
    }

    public void setTrade(TradeReq trade) {
        this.trade = trade;
    }

    public PaymentReq getPayment() {
        return payment;
    }

    public void setPayment(PaymentReq payment) {
        this.payment = payment;
    }

}
