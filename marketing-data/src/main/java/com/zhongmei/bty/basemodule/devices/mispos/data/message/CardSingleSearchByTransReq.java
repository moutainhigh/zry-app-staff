package com.zhongmei.bty.basemodule.devices.mispos.data.message;


import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;


public class CardSingleSearchByTransReq extends BaseRequest {


    private int pageSize;

    private Long userId;

    private String cardNum;

    private Long cardKindId;


    private Integer commercialType;

    private Integer[] cardTypes;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public Long getCardKindId() {
        return this.cardKindId;
    }

    public Integer getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(Integer commercialType) {
        this.commercialType = commercialType;
    }




    public Integer[] getCardTypes() {
        if (cardTypes == null) {
            cardTypes = new Integer[]{EntityCardType.CUSTOMER_ENTITY_CARD.value(), EntityCardType.GENERAL_CUSTOMER_CARD.value()};
        }
        return cardTypes;
    }


    public void setCardTypes(Integer... params) {
        if (params.length > 0) {
            cardTypes = params;
        }
    }


}