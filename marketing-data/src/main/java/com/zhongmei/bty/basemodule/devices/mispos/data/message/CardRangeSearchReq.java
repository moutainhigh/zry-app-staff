package com.zhongmei.bty.basemodule.devices.mispos.data.message;

public class CardRangeSearchReq {

    private Integer source = 1;

    private Long userId;

    private Long cardKindId;

    private String beginCardNum;

    private String endCardNum;


    private Integer commercialType;

    private int pageSize = 200;


    private String cardTypes;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public CardRangeSearchReq() {
    }

    public CardRangeSearchReq(int pagesize) {

        this.setPageSize(pagesize);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public String getBeginCardNum() {
        return beginCardNum;
    }

    public void setBeginCardNum(String beginCardNum) {
        this.beginCardNum = beginCardNum;
    }

    public String getEndCardNum() {
        return endCardNum;
    }

    public void setEndCardNum(String endCardNum) {
        this.endCardNum = endCardNum;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(Integer commercialType) {
        this.commercialType = commercialType;
    }

    public String getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(String cardTypes) {
        this.cardTypes = cardTypes;
    }
}
