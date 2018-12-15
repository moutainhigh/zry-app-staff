package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

public class PaymentItemExt extends PaymentItem {

    //private PaymentItemExtra paymentItemExtra;

    private PaymentItemUnionCardReq paymentItemUnionPay;//add 20161129 for pos cards

    /*private PaymentItemGroupon paymentItemGroupon;//美团券信息 add 20160926

    private String authCode;//付款码，支付上行数据

    private String consumePassword;//会员付款密码

    private Integer type;//add 20170612 for customer password type

    private Integer isDeposit;//add 20170706 for deposit 1 支付押金 2不是支付押金  默认2*/

    /*public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(Integer isDeposit) {
        this.isDeposit = isDeposit;
    }


    public PaymentItemExtra getPaymentItemExtra() {
        return paymentItemExtra;
    }

    public void setPaymentItemExtra(PaymentItemExtra paymentItemExtra) {
        this.paymentItemExtra = paymentItemExtra;
    }*/

    public void setPaymentItemUnionPay(PaymentItemUnionCardReq paymentItemUnionPay) {
        this.paymentItemUnionPay = paymentItemUnionPay;
    }

    public PaymentItemUnionCardReq getPaymentItemUnionPay() {
        return paymentItemUnionPay;
    }

    /*public PaymentItemGroupon getPaymentItemGroupon() {
        return paymentItemGroupon;
    }

    public void setPaymentItemGroupon(PaymentItemGroupon paymentItemGroupon) {
        this.paymentItemGroupon = paymentItemGroupon;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getConsumePassword() {
        return consumePassword;
    }

    public void setConsumePassword(String consumePassword) {
        this.consumePassword = consumePassword;
    }*/
}
