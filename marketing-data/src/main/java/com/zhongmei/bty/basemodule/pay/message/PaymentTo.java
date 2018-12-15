package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.Payment;

import java.util.List;

/**
 * v1 payment上行列表数据
 * Created by demo on 2018/12/15
 */

public class PaymentTo extends Payment {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<PaymentItemTo> paymentItems;
    private List<PaymentItemUnionpayVoReq> paymentCards;// 银联刷卡记录  add 20160218

    public List<PaymentItemTo> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItemTo> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<PaymentItemUnionpayVoReq> getPaymentCards() {
        return paymentCards;
    }

    public void setPaymentCards(List<PaymentItemUnionpayVoReq> paymentCards) {
        this.paymentCards = paymentCards;
    }
}
