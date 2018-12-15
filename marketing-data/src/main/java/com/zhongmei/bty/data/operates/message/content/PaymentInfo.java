package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayVoReq;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 会员虚拟卡充值收银信息上行数据
 */
public class PaymentInfo {
    private String uuid;//支付UUid
    private BigDecimal receivableAmount;
    private BigDecimal exemptAmount;
    private BigDecimal actualAmount;
    private Long clientCreateTime;
    private Long clientUpdateTime;
    private List<PaymentItem> paymentItems;
    private List<PaymentItemUnionpayVoReq> paymentCards;

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<PaymentItemUnionpayVoReq> getPaymentCards() {
        return paymentCards;
    }

    public void setPaymentCards(List<PaymentItemUnionpayVoReq> paymentCards) {
        this.paymentCards = paymentCards;
    }


}
