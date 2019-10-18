package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.trade.entity.MemberValueCard;

import java.util.List;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;


public class CustomerSellcardDetailResp {
    List<Trade> trades;
    List<TradeItem> tradeItems;
    List<Payment> payments;
    List<PaymentItem> paymentItems;
    List<CustomerSaleCardInfo> cardSaleInfos;
    List<CardInfo> cardInfos;

        private List<MemberValueCard> memberValuecard;

    public List<MemberValueCard> getMemberValuecard() {
        return memberValuecard;
    }

    public void setMemberValuecard(List<MemberValueCard> memberValuecard) {
        this.memberValuecard = memberValuecard;
    }

    public List<Trade> getTrades() {
        return trades;
    }


    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }


    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }


    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
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


    public List<CustomerSaleCardInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }


    public void setCardSaleInfos(List<CustomerSaleCardInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
    }

    public List<CardInfo> getCardInfos() {
        return cardInfos;
    }


    public void setCardInfos(List<CardInfo> cardInfos) {
        this.cardInfos = cardInfos;
    }
}
