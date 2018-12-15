package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;

import java.util.List;

/**
 * v3支付请求专用
 * Created by demo on 2018/12/15
 */

public class NPaymentItemReq extends PaymentItem {

    private int isRefund = 1; //是否支持退款 1：是；2：否

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }


    /* private PaymentItemExtra paymentItemExtra;

            private PaymentItemUnionCardReq paymentItemUnionPay;//add 20161129 for pos cards

            private PaymentItemGroupon paymentItemGroupon;//美团券信息 add 20160926

            private String authCode;//付款码，支付上行数据

            private String consumePassword;//会员付款密码

            private Integer type;//add 20170612 for customer password type

            private Integer isDeposit;//add 20170706 for deposit 1 支付押金 2不是支付押金  默认2

            @Override
            public PaymentItemExtra getPaymentItemExtra() {
                return paymentItemExtra;
            }

            @Override
            public void setPaymentItemExtra(PaymentItemExtra paymentItemExtra) {
                this.paymentItemExtra = paymentItemExtra;
            }

            @Override
            public PaymentItemUnionCardReq getPaymentItemUnionPay() {
                return paymentItemUnionPay;
            }

            @Override
            public void setPaymentItemUnionPay(PaymentItemUnionCardReq paymentItemUnionPay) {
                this.paymentItemUnionPay = paymentItemUnionPay;
            }

            @Override
            public PaymentItemGroupon getPaymentItemGroupon() {
                return paymentItemGroupon;
            }

            @Override
            public void setPaymentItemGroupon(PaymentItemGroupon paymentItemGroupon) {
                this.paymentItemGroupon = paymentItemGroupon;
            }

            @Override
            public String getAuthCode() {
                return authCode;
            }

            @Override
            public void setAuthCode(String authCode) {
                this.authCode = authCode;
            }

            @Override
            public String getConsumePassword() {
                return consumePassword;
            }

            @Override
            public void setConsumePassword(String consumePassword) {
                this.consumePassword = consumePassword;
            }

            @Override
            public Integer getType() {
                return type;
            }

            @Override
            public void setType(Integer type) {
                this.type = type;
            }

            @Override
            public Integer getIsDeposit() {
                return isDeposit;
            }

            @Override
            public void setIsDeposit(Integer isDeposit) {
                this.isDeposit = isDeposit;
            }*/
    //add v8.3美团券与菜品关联关系 begin
    List<PaymentItemGrouponDish> paymentItemGrouponDish;

    public List<PaymentItemGrouponDish> getPaymentItemGrouponDish() {
        return paymentItemGrouponDish;
    }

    public void setPaymentItemGrouponDish(List<PaymentItemGrouponDish> paymentItemGrouponDish) {
        this.paymentItemGrouponDish = paymentItemGrouponDish;
    }
    //add v8.3美团券与菜品关联关系 end
}
