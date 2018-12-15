package com.zhongmei.bty.basemodule.trade.message;

import com.j256.ormlite.stmt.query.In;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class TradeUnionRequest {
    private Long id;
    private String uuid;
    private Long memberId;
    private BigDecimal privilegeAmount;
    private BigDecimal saleAmount;
    private Long clientUpdateTime;
    private Long serverUpdateTime;
    private Integer skuKindCount;
    private BigDecimal tradeAmount;
    private BigDecimal tradeAmountBefore;
    private String tradeMemo;
    private Integer tradePayForm;
    private Integer tradePeopleCount;
    private String deviceIdenty;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getPrivilegeAmount() {
        return privilegeAmount;
    }

    public void setPrivilegeAmount(BigDecimal privilegeAmount) {
        this.privilegeAmount = privilegeAmount;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Integer getSkuKindCount() {
        return skuKindCount;
    }

    public void setSkuKindCount(Integer skuKindCount) {
        this.skuKindCount = skuKindCount;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getTradeAmountBefore() {
        return tradeAmountBefore;
    }

    public void setTradeAmountBefore(BigDecimal tradeAmountBefore) {
        this.tradeAmountBefore = tradeAmountBefore;
    }

    public String getTradeMemo() {
        return tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public Integer getTradePayForm() {
        return tradePayForm;
    }

    public void setTradePayForm(Integer tradePayForm) {
        this.tradePayForm = tradePayForm;
    }

    public Integer getTradePeopleCount() {
        return tradePeopleCount;
    }

    public void setTradePeopleCount(Integer tradePeopleCount) {
        this.tradePeopleCount = tradePeopleCount;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }
}
