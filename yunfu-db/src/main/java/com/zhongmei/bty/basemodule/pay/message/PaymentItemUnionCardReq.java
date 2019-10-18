package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.bty.basemodule.pay.bean.PaymentCard;

import java.io.Serializable;



public class PaymentItemUnionCardReq implements Serializable {
    private PaymentItemUnionpayReq record;    private PaymentCard paymentCard;    private PaymentDeviceReq paymentDevice;
    public PaymentItemUnionpayReq getRecord() {
        return record;
    }

    public void setRecord(PaymentItemUnionpayReq record) {
        this.record = record;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public PaymentDeviceReq getPaymentDevice() {
        return paymentDevice;
    }

    public void setPaymentDevice(PaymentDeviceReq paymentDevice) {
        this.paymentDevice = paymentDevice;
    }
}
