package com.zhongmei.bty.basemodule.devices.mispos.data;

import java.math.BigDecimal;

public class CardSaleInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private long cardKindId;    private int cardStatus;    private String cardNum;     private String cardKindName;    private BigDecimal cardCost;
        private long tradeId;
    private long tradeItemId;
    private long cardId;
    private long saleTime;
    private long createId;
    private String createName;
    private long serverCreateTime;
    private long serverUpdateTime;


        String needPassword;    String cardKind;
        private String password;
    private Long cardType;
    private String identyCardNo;

    public String getCardKind() {
        return cardKind;
    }

    public void setCardKind(String cardKind) {
        this.cardKind = cardKind;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public BigDecimal getCardCost() {
        return cardCost;
    }

    public void setCardCost(BigDecimal cardCost) {
        this.cardCost = cardCost;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardKindName() {
        return cardKindName;
    }

    public void setCardKindName(String cardKindName) {
        this.cardKindName = cardKindName;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCardType() {
        return cardType;
    }

    public void setCardType(Long cardType) {
        this.cardType = cardType;
    }

    public String getIdentyCardNo() {
        return identyCardNo;
    }

    public void setIdentyCardNo(String identyCardNo) {
        this.identyCardNo = identyCardNo;
    }
}
