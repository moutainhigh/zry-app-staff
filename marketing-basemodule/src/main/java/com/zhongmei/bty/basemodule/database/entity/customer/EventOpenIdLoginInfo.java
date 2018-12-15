package com.zhongmei.bty.basemodule.database.entity.customer;

/**
 * @Date： 2016/12/1
 * @Description:OpenId登录返回信息
 * @Version: 1.0
 */
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
