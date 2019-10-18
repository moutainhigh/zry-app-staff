package com.zhongmei.bty.basemodule.trade.message;


public class RefundCheckRequest {

    public Long tradeId;
    public Long paymentItemId;

    public RefundCheckRequest(Long tradeId, Long paymentItemId) {
        this.tradeId = tradeId;
        this.paymentItemId = paymentItemId;
    }
}
