package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.math.BigDecimal;
import java.util.List;


public class AnonymousCardStoreResp {

    private Trade trade;
    private Payment payment;
    private List<PaymentItem> paymentItems;
    private BigDecimal valueCard = BigDecimal.ZERO;
    private List<PrintOperation> printOperations;
                                            private BigDecimal beforeRealValue = BigDecimal.ZERO;
    private BigDecimal beforeSendValue = BigDecimal.ZERO;
    private BigDecimal beforePrepareValue = BigDecimal.ZERO;
    private BigDecimal currentRealValue = BigDecimal.ZERO;
    private BigDecimal currentSendValue = BigDecimal.ZERO;
    private BigDecimal currentPrepareValue = BigDecimal.ZERO;
    private BigDecimal endRealValue = BigDecimal.ZERO;
    private BigDecimal endSendValue = BigDecimal.ZERO;
    private BigDecimal endPrepareValue = BigDecimal.ZERO;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public BigDecimal getValueCard() {
        return valueCard;
    }

    public void setValueCard(BigDecimal valueCard) {
        this.valueCard = valueCard;
    }

    public List<PrintOperation> getPrintOperations() {
        return printOperations;
    }

    public void setPrintOperations(List<PrintOperation> printOperations) {
        this.printOperations = printOperations;
    }

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public void setBeforeRealValue(BigDecimal beforeRealValue) {
        this.beforeRealValue = beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public void setBeforeSendValue(BigDecimal beforeSendValue) {
        this.beforeSendValue = beforeSendValue;
    }

    public BigDecimal getBeforePrepareValue() {
        return beforePrepareValue;
    }

    public void setBeforePrepareValue(BigDecimal beforePrepareValue) {
        this.beforePrepareValue = beforePrepareValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public void setCurrentRealValue(BigDecimal currentRealValue) {
        this.currentRealValue = currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public void setCurrentSendValue(BigDecimal currentSendValue) {
        this.currentSendValue = currentSendValue;
    }

    public BigDecimal getCurrentPrepareValue() {
        return currentPrepareValue;
    }

    public void setCurrentPrepareValue(BigDecimal currentPrepareValue) {
        this.currentPrepareValue = currentPrepareValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public void setEndRealValue(BigDecimal endRealValue) {
        this.endRealValue = endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public void setEndSendValue(BigDecimal endSendValue) {
        this.endSendValue = endSendValue;
    }

    public BigDecimal getEndPrepareValue() {
        return endPrepareValue;
    }

    public void setEndPrepareValue(BigDecimal endPrepareValue) {
        this.endPrepareValue = endPrepareValue;
    }
}
