package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

public class CustomerCardInfoReq extends BaseRequest {

    private Long commercialType;

    private Long userId;
    private Long cardKindId;
    private String cardNum;
    private int[] cardStatus;
    private int[] cardTypes;
    public Long getBrandId() {
        return brandIdenty;
    }

    public void setBrandId(Long brandId) {
        this.brandIdenty = brandId;
    }

    public Long getCommercialId() {
        return shopIdenty;
    }

    public void setCommercialId(Long commercialId) {
        this.shopIdenty = commercialId;
    }

    public Long getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(Long commercialType) {
        this.commercialType = commercialType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int[] getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int[] cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int[] getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(int[] cardTypes) {
        this.cardTypes = cardTypes;
    }
}
