package com.zhongmei.yunfu.init.sync;

import com.zhongmei.yunfu.init.sync.bean.SyncRequest;
import com.zhongmei.yunfu.init.sync.bean.SyncResponse;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.util.Gsons;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.net.HttpConstant;

import java.lang.reflect.Type;
import java.util.UUID;


public class SyncHttpCaller extends JsonHttpCaller<SyncRequest, SyncResponse> {

    public SyncHttpCaller(String url) {
        super(url, Type.class.cast(SyncResponse.class));
        setDataIntoHeader();
        setGsonBuilder(Gsons.gsonBuilder());
        setConnectTimeout(20 * 1000);
        setReadTimeout(60 * 1000);
    }

    public void setDataIntoHeader() {
        setHttpProperty(HttpConstant.YF_API_MSG_ID, UUID.randomUUID().toString());
        setHttpProperty(HttpConstant.YF_API_DEVICE_ID, SystemUtils.getMacAddress());
        setHttpProperty(HttpConstant.YF_API_SHOP_ID, ShopInfoManager.getInstance().getShopInfo().getShopId() + "");
        setHttpProperty(HttpConstant.YF_API_BRAND_ID, ShopInfoManager.getInstance().getShopInfo().getBrandId() + "");
        setHttpProperty(HttpConstant.YF_API_TOKEN, "token");
    }
}
