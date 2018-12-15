package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */

public class CheckVersionsAreaReq {

//    private String version_name;

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
