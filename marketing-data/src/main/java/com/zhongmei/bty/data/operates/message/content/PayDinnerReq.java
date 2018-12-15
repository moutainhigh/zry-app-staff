package com.zhongmei.bty.data.operates.message.content;


import com.zhongmei.bty.basemodule.trade.message.TradeReq;

/**
 * @date:2015年9月28日下午6:19:53
 */
public class PayDinnerReq {

    private TradeReq trade;
    private PaymentDinnerTo payment;

    public TradeReq getTrade() {
        return trade;
    }

    public void setTrade(TradeReq trade) {
        this.trade = trade;
    }

    public PaymentDinnerTo getPayment() {
        return payment;
    }

    public void setPayment(PaymentDinnerTo payment) {
        this.payment = payment;
    }


}
