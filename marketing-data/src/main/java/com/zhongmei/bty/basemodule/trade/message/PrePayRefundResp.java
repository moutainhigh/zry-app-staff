package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.List;



public class PrePayRefundResp {

    private Trade trade;

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    public Trade getTrade() {
        return trade;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }
}
