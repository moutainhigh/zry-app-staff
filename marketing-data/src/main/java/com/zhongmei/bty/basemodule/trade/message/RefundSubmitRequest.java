package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;

/**
 * 根据手机号获取顾客信息
 * Created by demo on 2018/12/15
 */
public class RefundSubmitRequest {

    public Long tradeId;
    public Long paymentItemId;
    public BigDecimal refundFee;

    public RefundSubmitRequest(Long tradeId, Long paymentItemId, BigDecimal refundFee) {
        this.tradeId = tradeId;
        this.paymentItemId = paymentItemId;
        this.refundFee = refundFee;
    }
}
