package com.zhongmei.bty.basemodule.pay.message;

import java.math.BigDecimal;


public class WechatPayReq {
    private BigDecimal usefulAmount;
    private BigDecimal exemptAmount;
    private BigDecimal noDiscountAmount;    private Long payModeId;
    private Long tradeId;
    private String authCode;
    private String deviceIdenty;
    private Long updatorId;
    private Long customerId;    private String entityCardNo;    private BigDecimal sendValue;    private String updatorName;
    private String payModeName;
    private Long tradeUpdateTime;
    public Long getTradeUpdateTime() {
        return tradeUpdateTime;
    }

    public void setTradeUpdateTime(Long tradeUpdateTime) {
        this.tradeUpdateTime = tradeUpdateTime;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public String getEntityCardNo() {
        return entityCardNo;

    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

    public Long getCustomerId() {

        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getNoDiscountAmount() {
        return noDiscountAmount;
    }

    public void setNoDiscountAmount(BigDecimal unDiscountAmount) {
        noDiscountAmount = unDiscountAmount;
    }


    public BigDecimal getUsefulAmount() {
        return usefulAmount;
    }

    public void setUsefulAmount(BigDecimal usefulAmount) {
        this.usefulAmount = usefulAmount;
    }

    public BigDecimal getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }
}
