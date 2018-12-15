package com.zhongmei.bty.basemodule.devices.mispos.data.message;


import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;

/**
 * 单卡查询 透传参数
 */
public class CardSingleSearchByTransReq extends BaseRequest {

//	/**
//	 * 品牌id
//	 */
//	private Long brandId;
//	/**
//	 * 门店ID
//	 */
//	private Long commercialId;

    private int pageSize;

    private Long userId;

    private String cardNum;

    private Long cardKindId;

//	private String clientType = "pos";

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

//	public Long getBrandId() {
//		return brandId;
//	}
//
//	public void setBrandId(Long brandId) {
//		this.brandId = brandId;
//	}
//
//	public Long getCommercialId() {
//		return commercialId;
//	}
//
//	public void setCommercialId(Long commercialId) {
//		this.commercialId = commercialId;
//	}

//	public String getClientType() {
//		return clientType;
//	}
//
//	public void setClientType(String clientType) {
//		this.clientType = clientType;
//	}

    /**
     * 传 cardtypes
     *
     * @return
     */
    public Integer[] getCardTypes() {
        if (cardTypes == null) {
            cardTypes = new Integer[]{EntityCardType.CUSTOMER_ENTITY_CARD.value(), EntityCardType.GENERAL_CUSTOMER_CARD.value()};
        }
        return cardTypes;
    }

    /**
     * 设置 cardtypes
     *
     * @return
     */
    public void setCardTypes(Integer... params) {
        if (params.length > 0) {
            cardTypes = params;
        }
    }


}