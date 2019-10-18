package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import java.io.Serializable;
import java.math.BigDecimal;


public class CustomerSaleCardInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private long tradeId;
    private long tradeItemId;
    private long cardId;    private String cardNo;    private long cardKindId;    private String cardKind;    private BigDecimal cardCost;    private long saleTime;    private long createId;    private String createName;    private long serverCreateTime;
    private long serverUpdateTime;
    private int cardStatus;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public String getCardKind() {
        return cardKind;
    }

    public void setCardKind(String cardKind) {
        this.cardKind = cardKind;
    }

    public BigDecimal getCardCost() {
        return cardCost;
    }

    public void setCardCost(BigDecimal cardCost) {
        this.cardCost = cardCost;
    }

    public long getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(long saleTime) {
        this.saleTime = saleTime;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

}
