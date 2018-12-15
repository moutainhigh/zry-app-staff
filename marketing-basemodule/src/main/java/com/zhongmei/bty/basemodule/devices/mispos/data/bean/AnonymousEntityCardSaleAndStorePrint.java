package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 匿名实体卡售卡、充值、售卡及充值打印信息
 */
public class AnonymousEntityCardSaleAndStorePrint implements Serializable {
    private String cardNumber;//卡号
    private BigDecimal cardCost;//卡费
    private BigDecimal addValue;//储值金额
    private BigDecimal valueCard;//卡内余额
    private BigDecimal refundAfterCardAmount; // 退卡后余额，一般为0
    private BigDecimal totalAmount;//应付合计或者退款合计
    private List<PaymentItem> paymentItems;//详细的支付方式
    private Long operatorId;// 收银员id
    private String operatorName;//收银员name
    private Long operatorTime;//操作时间,即充值时间，退卡时间
    private Integer type; // 第一次购卡充值0，充值1，退卡2

    public AnonymousEntityCardSaleAndStorePrint() {

    }

    public AnonymousEntityCardSaleAndStorePrint(String cardNumber, Integer type) {
        this.cardNumber = cardNumber;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getRefundAfterCardAmount() {
        return refundAfterCardAmount;
    }

    public void setRefundAfterCardAmount(BigDecimal refundAfterCardAmount) {
        this.refundAfterCardAmount = refundAfterCardAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public BigDecimal getAddValue() {
        return addValue;
    }

    public void setAddValue(BigDecimal addValue) {
        this.addValue = addValue;
    }

    public BigDecimal getValueCard() {
        return valueCard;
    }

    public void setValueCard(BigDecimal valueCard) {
        this.valueCard = valueCard;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Long operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public BigDecimal getCardCost() {
        return cardCost;
    }

    public void setCardCost(BigDecimal cardCost) {
        this.cardCost = cardCost;
    }
}
