package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.db.ICreator;

/**
 * @Date：2016-2-18 上午11:14:39
 * @Description: pos刷卡记录提交数据
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class PaymentItemUnionpayVoReq implements java.io.Serializable, ICreator {

    private static final long serialVersionUID = 1L;

    private String cardNumber;// 银行卡号

    private String cardName;// 发卡行名称

    private String expireDate;// 有效期

    private String issNumber;// 发卡行号

    private String issName;// 发卡行名称

    private Long creatorId;// 操作员id

    private String creatorName;// 操作员名称

    private PaymentItemUnionpayReq paymentItemUnionpay;// 刷卡记录详情

    private PaymentDeviceReq paymentDevice;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getIssNumber() {
        return issNumber;
    }

    public void setIssNumber(String issNumber) {
        this.issNumber = issNumber;
    }

    public String getIssName() {
        return issName;
    }

    public void setIssName(String issName) {
        this.issName = issName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public PaymentItemUnionpayReq getPaymentItemUnionpay() {
        return paymentItemUnionpay;
    }

    public void setPaymentItemUnionpay(PaymentItemUnionpayReq paymentItemUnionpay) {
        this.paymentItemUnionpay = paymentItemUnionpay;
    }

    public PaymentDeviceReq getPaymentDevice() {
        return paymentDevice;
    }

    public void setPaymentDevice(PaymentDeviceReq paymentDevice) {
        this.paymentDevice = paymentDevice;
    }
}
