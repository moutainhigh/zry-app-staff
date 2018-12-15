package com.zhongmei.bty.basemodule.commonbusiness.message;

/**
 * Created by demo on 2018/12/15
 */
public class BusinessChargeReq {

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 门店id
     */
    private Long shopId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
