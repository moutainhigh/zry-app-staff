package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

public class PaymentItemExt extends PaymentItem {


    private PaymentItemUnionCardReq paymentItemUnionPay;




    public void setPaymentItemUnionPay(PaymentItemUnionCardReq paymentItemUnionPay) {
        this.paymentItemUnionPay = paymentItemUnionPay;
    }

    public PaymentItemUnionCardReq getPaymentItemUnionPay() {
        return paymentItemUnionPay;
    }


}
