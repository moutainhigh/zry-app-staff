package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.MainApplication;

/**
 * Created by demo on 2018/12/15
 */
public class CalmRouterRequest<T> {

    public Long brandID;
    public Long shopID;
    public String deviceID;
    public T content;

    public CalmRouterRequest() {
        brandID = MainApplication.getInstance().getBrandIdenty();
        shopID = MainApplication.getInstance().getShopIdenty();
        deviceID = MainApplication.getInstance().getDeviceIdenty();
    }

    public void setContent(T content) {
        this.content = content;
    }
}
