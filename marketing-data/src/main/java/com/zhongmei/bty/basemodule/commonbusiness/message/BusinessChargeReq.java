package com.zhongmei.bty.basemodule.commonbusiness.message;


public class BusinessChargeReq {


    private Long brandId;


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
