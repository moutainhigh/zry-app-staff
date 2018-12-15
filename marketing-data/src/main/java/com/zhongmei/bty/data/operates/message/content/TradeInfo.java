package com.zhongmei.bty.data.operates.message.content;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
public class TradeInfo {
    private String uuid;//充值订单uuid
    private BigDecimal totalValueCard;//充值订单金额
    private String tradeNo;
    private Long creatorId;
    private String creatorName;
    private Long updatorId;
    private String updatorName;
    private Long clientCreateTime;
    private Long clientUpdateTime;
    private Integer source;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getTotalValueCard() {
        return totalValueCard;
    }

    public void setTotalValueCard(BigDecimal totalValueCard) {
        this.totalValueCard = totalValueCard;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

}
