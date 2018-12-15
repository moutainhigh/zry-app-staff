package com.zhongmei.bty.basemodule.trade.message;

public class TradePayStateReq {
    private Long tradeId;

    private Long paymentItemId;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }
}
