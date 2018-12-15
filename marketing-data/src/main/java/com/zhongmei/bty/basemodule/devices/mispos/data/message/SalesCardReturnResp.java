package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.util.List;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

public class SalesCardReturnResp extends TradeResp {

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<CustomerSaleCardInfo> cardSaleInfos;

    public List<CustomerSaleCardInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }

    public void setCardSaleInfos(List<CustomerSaleCardInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
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

}
