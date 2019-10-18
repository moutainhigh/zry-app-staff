package com.zhongmei.bty.data.operates.message.content;



public class CheckAuthReq {

    private Long brandId;
    private Long commercialId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }
}
