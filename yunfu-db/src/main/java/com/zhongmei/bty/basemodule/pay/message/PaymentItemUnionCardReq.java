package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.bty.basemodule.pay.bean.PaymentCard;

import java.io.Serializable;

/**
 * 银行卡交易相关信息.
 */

public class PaymentItemUnionCardReq implements Serializable {
    private PaymentItemUnionpayReq record;//交易明细
    private PaymentCard paymentCard;//交易卡信息
    private PaymentDeviceReq paymentDevice;//交易终端信息

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
