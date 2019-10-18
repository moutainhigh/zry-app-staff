package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;

import java.util.List;



public class NPaymentItemReq extends PaymentItem {

    private int isRefund = 1;
    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }



        List<PaymentItemGrouponDish> paymentItemGrouponDish;

    public List<PaymentItemGrouponDish> getPaymentItemGrouponDish() {
        return paymentItemGrouponDish;
    }

    public void setPaymentItemGrouponDish(List<PaymentItemGrouponDish> paymentItemGrouponDish) {
        this.paymentItemGrouponDish = paymentItemGrouponDish;
    }
    }
