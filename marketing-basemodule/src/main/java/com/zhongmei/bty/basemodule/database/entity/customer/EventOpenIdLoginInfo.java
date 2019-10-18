package com.zhongmei.bty.basemodule.database.entity.customer;


public class EventOpenIdLoginInfo {

    OpenIdLoginInfo mOpenIdLoginInfo;

    public EventOpenIdLoginInfo(OpenIdLoginInfo openIdLoginInfo) {
        this.mOpenIdLoginInfo = openIdLoginInfo;
    }

    public OpenIdLoginInfo getmOpenIdLoginInfo() {
        return mOpenIdLoginInfo;
    }

    public void setmOpenIdLoginInfo(OpenIdLoginInfo mOpenIdLoginInfo) {
        this.mOpenIdLoginInfo = mOpenIdLoginInfo;
    }

}
