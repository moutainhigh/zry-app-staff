package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.trade.entity.MemberValueCard;
import com.zhongmei.yunfu.db.entity.trade.Payment;

import java.util.List;


public class TradePaymentResp extends TradeResp {

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<PaymentItemExtra> paymentItemExtras;

    private List<PaymentItemUnionpay> paymentItemUnionpays;

    private List<PaymentDevice> paymentDevices;

    private List<CardSaleInfo> cardSaleInfos;

    private List<PaymentItemGroupon> paymentItemGroupons;

        private List<MemberValueCard> memberValuecard;

    public List<Payment> getPayments() {
        return payments;
    }

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

    public List<CardSaleInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }

    public void setCardSaleInfos(List<CardSaleInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
    }

    public void setPaymentItemGroupons(List<PaymentItemGroupon> paymentItemGroupons) {
        this.paymentItemGroupons = paymentItemGroupons;
    }

    public List<MemberValueCard> getMemberValuecard() {
        return memberValuecard;
    }

    public void setMemberValuecard(List<MemberValueCard> memberValuecard) {
        this.memberValuecard = memberValuecard;
    }
}
