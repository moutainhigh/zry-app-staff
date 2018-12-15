package com.zhongmei.bty.basemodule.database.entity.customer;

/**
 * @Date： 2016/12/1
 * @Description:OpenId登录返回信息
 * @Version: 1.0
 */
public class OpenIdLoginInfo {

    private String uuid;

    private boolean result;

    private CommCustomer customer;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public CommCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(CommCustomer customer) {
        this.customer = customer;
    }
}
