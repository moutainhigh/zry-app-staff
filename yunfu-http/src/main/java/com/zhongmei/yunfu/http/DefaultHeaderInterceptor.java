package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.net.HttpConstant;
import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.Constant;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;



public class DefaultHeaderInterceptor implements NetworkRequest.RequestInterceptor {

    private String url;

    public DefaultHeaderInterceptor(String url) {
        this.url = url;
    }

    @Override
    public NetworkRequest intercept(NetworkRequest request) {
        request.setHeader(getDefaultHeader());
        return request;
    }

    public HashMap<String, String> getDefaultHeader() {
        return getHeaders(HeaderType.HEADER_TYPE_SYNC);
    }

    public HashMap<String, String> getHeaders(int headerType) {
        HashMap<String, String> defaultHeaderMap = new LinkedHashMap<String, String>();
        switch (headerType) {
            case HeaderType.HEADER_TYPE_SYNC:                defaultHeaderMap.put(HttpConstant.YF_API_MSG_ID, UUID.randomUUID().toString());
                defaultHeaderMap.put(HttpConstant.YF_API_DEVICE_ID, SystemUtils.getMacAddress());
                defaultHeaderMap.put(HttpConstant.YF_API_SHOP_ID, ShopInfoManager.getInstance().getShopInfo().getShopId() + "");
                defaultHeaderMap.put(HttpConstant.YF_API_BRAND_ID, ShopInfoManager.getInstance().getShopInfo().getBrandId() + "");
                defaultHeaderMap.put(HttpConstant.YF_API_TIMESTAMP, Calendar.getInstance().getTimeInMillis() + "");

                                                                defaultHeaderMap.put("Accept-Encoding", "gzip");


                break;
            case HeaderType.HEADER_TYPE_ERP:                defaultHeaderMap.put(HttpConstant.YF_API_MSG_ID, UUID.randomUUID().toString());
                defaultHeaderMap.put(HttpConstant.YF_API_DEVICE_ID, SystemUtils.getMacAddress());
                defaultHeaderMap.put(HttpConstant.YF_API_SHOP_ID, "1");
                defaultHeaderMap.put(HttpConstant.YF_API_BRAND_ID, "1");
                break;
            default:
                break;
        }

        return defaultHeaderMap;
    }
}
