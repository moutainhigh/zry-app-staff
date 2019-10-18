package com.zhongmei.bty.basemodule.commonbusiness.message;


public class CommercialCustomSettingsReq {
    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private Long brandIdenty;
    private Long shopIdenty;
    private String key;
    private String value;
}
