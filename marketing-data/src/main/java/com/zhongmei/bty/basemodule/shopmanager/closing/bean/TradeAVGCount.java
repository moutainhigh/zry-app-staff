package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;

/**
 * 核数 统计
 * Created by demo on 2018/12/15
 */
public class TradeAVGCount implements Serializable {
    private String peopleCount;//来客数
    private Integer tradeType;//订单类型
    private Long tradeCount;//账单数据量
    private String name;
    private String tradeAmount;//金额
    private String unitName;
    private String unitDisplayName;

    public String getUnitDisplayName() {
        return unitDisplayName;
    }

    public void setUnitDisplayName(String unitDisplayName) {
        this.unitDisplayName = unitDisplayName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
}
