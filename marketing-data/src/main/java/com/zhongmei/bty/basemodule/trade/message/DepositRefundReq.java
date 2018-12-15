package com.zhongmei.bty.basemodule.trade.message;

import java.math.BigDecimal;

/**
 * @Date： 16/5/19
 * @Description:
 * @Version: 1.0
 */
public class DepositRefundReq {

    private Long tradeId;
    private BigDecimal depositRefund;
    private Long payModeId;//-3
    private String payModeName;//现金
    private Long reasonId;
    private String reasonContent;
    private Long clientUpdateTime;
    private Long updatorId;
    private String updatorName;

    public DepositRefundReq(Long tradeId, BigDecimal depositRefund) {
        this.tradeId = tradeId;
        this.depositRefund = depositRefund;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
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

    public BigDecimal getDepositRefund() {
        return depositRefund;
    }

    public void setDepositRefund(BigDecimal depositRefund) {
        this.depositRefund = depositRefund;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
}
