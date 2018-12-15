package com.zhongmei.bty.data.operates.message.content;

/**
 * 封装查询卡券详情请求对象
 * Created by demo on 2018/12/15
 */
public class WxCouponsInfoReq {
    private Long brandId;
    private Long commercialId;
    private String codeNumber;

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

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }
}
