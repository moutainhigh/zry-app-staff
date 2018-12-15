package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CardChangeInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CardInfo;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class EntityCardChangeDetailResp {
    List<Trade> trades;
    List<Payment> payments;
    List<PaymentItem> paymentItems;
    List<CardChangeInfo> cardChangeInfos;
    List<CardInfo> cardInfos;

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

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

    public List<CardChangeInfo> getCardChangeInfos() {
        return cardChangeInfos;
    }

    public void setCardChangeInfos(List<CardChangeInfo> cardChangeInfos) {
        this.cardChangeInfos = cardChangeInfos;
    }

    public List<CardInfo> getCardInfos() {
        return cardInfos;
    }

    public void setCardInfos(List<CardInfo> cardInfos) {
        this.cardInfos = cardInfos;
    }
}
