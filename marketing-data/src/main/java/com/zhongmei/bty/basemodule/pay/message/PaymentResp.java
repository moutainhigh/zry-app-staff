package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;

import java.util.List;

/**
 * 封装Payment相关的实时请求回复的数据
 *
 * @version: 1.0
 * @date 2015年4月15日
 */
public class PaymentResp extends TradeResp {

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<PaymentItemExtra> paymentItemExtras;

    private List<PaymentItemUnionpay> paymentItemUnionpays;

    private List<PaymentDevice> paymentDevices;

    private List<PaymentItemGroupon> paymentItemGroupons;

    public List<Payment> getPayments() {
        return payments;
    }

    private List<TradePrivilegeExtra> tradePrivilegeExtras;

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<PaymentItemExtra> getPaymentItemExtras() {
        return paymentItemExtras;
    }

    public void setPaymentItemExtras(List<PaymentItemExtra> paymentItemExtras) {
        this.paymentItemExtras = paymentItemExtras;
    }

    public List<PaymentItemGroupon> getPaymentItemGroupons() {
        return paymentItemGroupons;
    }

    public void setPaymentItemGroupons(List<PaymentItemGroupon> paymentItemGroupons) {
        this.paymentItemGroupons = paymentItemGroupons;
    }

    public List<PaymentItemUnionpay> getPaymentItemUnionpays() {
        return paymentItemUnionpays;
    }

    public void setPaymentItemUnionpays(List<PaymentItemUnionpay> paymentItemUnionpays) {
        this.paymentItemUnionpays = paymentItemUnionpays;
    }

    public List<PaymentDevice> getPaymentDevices() {
        return paymentDevices;
    }

    public void setPaymentDevices(List<PaymentDevice> paymentDevices) {
        this.paymentDevices = paymentDevices;
    }

    public List<TradePrivilegeExtra> getTradePrivilegeExtras() {
        return tradePrivilegeExtras;
    }

    public void setTradePrivilegeExtras(List<TradePrivilegeExtra> tradePrivilegeExtras) {
        this.tradePrivilegeExtras = tradePrivilegeExtras;
    }
}
