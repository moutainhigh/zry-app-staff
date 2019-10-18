package com.zhongmei.bty.data.operates.message.content;



public class CheckVersionsAreaReq {


    private Long versionCode;

    private Integer appType;

    private Long brandId;

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public Integer getAppType() {
        return appType;
    }

    public Long getBrandId() {
        return brandId;
    }
}
