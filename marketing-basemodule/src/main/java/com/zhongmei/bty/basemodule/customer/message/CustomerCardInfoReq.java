package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

public class CustomerCardInfoReq extends BaseRequest {
//
//    private Long brandId;//品牌ID
//
//    private Long commercialId;//门店ID

    private Long commercialType;//标志 1:售卡,2:使用（会员卡），3:储值，4：消费 （3和4是临时卡）	非必填

//    private String clientType;//客户端请求来源

    private Long userId;//用户ID

    private Long cardKindId;//卡种Id 不能与cardNum同时为空

    private String cardNum;//卡号 不能与cardKindId同时为空

    private int[] cardStatus;//卡状态(实体卡状态 0：未制卡，1:未出售，2：未激活，3：已激活，4：已停用，5：已废除) 非必填

    private int[] cardTypes;//实体卡类型 1:权益实体卡 2:匿名实体卡 3:会员实体卡	非必填（不填则为全部）

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
