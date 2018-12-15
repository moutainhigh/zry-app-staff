package com.zhongmei.bty.basemodule.pay.message;

import java.math.BigDecimal;

/**
 * 获取微信支付URL的请求
 *
 * @version: 1.0
 * @date 2015年6月4日
 */
public class WechatPayUrlReq {
    private BigDecimal usefulAmount;
    private BigDecimal exemptAmount;
    private BigDecimal noDiscountAmount;//不参与优惠金额
    private Long customerId;//会员Id
    private String entityCardNo;//匿名卡号
    private BigDecimal sendValue;//储值赠送金额
    private Long payModeId;

    private Long tradeId;

    private String deviceIdenty;

    private Long updatorId;

    private String updatorName;

    private String payModeName;

    private Long tradeUpdateTime;//订单最新时间戳

    public Long getTradeUpdateTime() {
        return tradeUpdateTime;
    }

    public void setTradeUpdateTime(Long tradeUpdateTime) {
        this.tradeUpdateTime = tradeUpdateTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEntityCardNo() {
        return entityCardNo;
    }

    public void setEntityCardNo(String entityCardNo) {
        this.entityCardNo = entityCardNo;
    }

    public BigDecimal getSendValue() {
        return sendValue;
    }

    public void setSendValue(BigDecimal sendValue) {
        this.sendValue = sendValue;
    }

    public BigDecimal getNoDiscountAmount() {
        return noDiscountAmount;
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

    public void setNoDiscountAmount(BigDecimal unDiscountAmount) {
        noDiscountAmount = unDiscountAmount;
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
