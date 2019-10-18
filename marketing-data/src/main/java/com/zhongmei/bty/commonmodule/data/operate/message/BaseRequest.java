package com.zhongmei.bty.commonmodule.data.operate.message;

import com.zhongmei.yunfu.context.base.BaseApplication;


public class BaseRequest {

    public String clientType = "pos";
    public Long brandIdenty;     public Long shopIdenty;
    public BaseRequest() {
        clientType = "pos";
        brandIdenty = BaseApplication.sInstance.getBrandIdenty();
        shopIdenty = BaseApplication.sInstance.getShopIdenty();
    }



}
