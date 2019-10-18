package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.trade.message.TradePaymentReq;


public class PaymentOfRepeatReq {

    private PayDinnerReq actual;

    private TradePaymentReq repay4Sell;

    private TradePaymentReq repay4Refund;

    public PayDinnerReq getActual() {
        return actual;
    }

    public void setActual(PayDinnerReq actual) {
        this.actual = actual;
    }

    public TradePaymentReq getRepay4Sell() {
        return repay4Sell;
    }

    public void setRepay4Sell(TradePaymentReq repay4Sell) {
        this.repay4Sell = repay4Sell;
    }

    public TradePaymentReq getRepay4Refund() {
        return repay4Refund;
    }

    public void setRepay4Refund(TradePaymentReq repay4Refund) {
        this.repay4Refund = repay4Refund;
    }

}
