package com.zhongmei.bty.data.operates.message.content;

public class CardSingleSearchReq {
    public int pageSize;

    public Long userId;

    public String cardNo;

    //public Integer cardStatus;

    public Long cardKindId;

    private Integer commercialType;

    private Integer cardType = 1;

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

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return this.cardNo;
    }

//	public CardStatus getCardStatus() {
//		return ValueEnums.toEnum(CardStatus.class, cardStatus);
//	}
//	
//	public void setCardStatus(CardStatus cardStatus) {
//		this.cardStatus = ValueEnums.toValue(cardStatus);
//	}

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

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
}