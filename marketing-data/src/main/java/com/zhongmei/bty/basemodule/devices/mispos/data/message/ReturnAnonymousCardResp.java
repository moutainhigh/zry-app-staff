package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.trade.entity.MemberValueCard;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.db.entity.trade.Payment;

import java.util.List;

/**
 * @Date： 2016/6/30
 * @Description:匿名卡退卡返回对象
 * @Version: 1.0
 */
public class ReturnAnonymousCardResp extends TradeResp {

    private List<Payment> payments;

    private List<PaymentItem> paymentItems;

    private List<CustomerSaleCardInfo> cardSaleInfos;

    private List<MemberValueCard> memberValuecard;

    public List<MemberValueCard> getMemberValuecard() {
        return memberValuecard;
    }

    public void setMemberValuecard(List<MemberValueCard> memberValuecard) {
        this.memberValuecard = memberValuecard;
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
}
