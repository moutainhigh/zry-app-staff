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

/**
 * Created by demo on 2018/12/15
 */

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
            case HeaderType.HEADER_TYPE_SYNC://同步组请求头
                defaultHeaderMap.put(HttpConstant.YF_API_MSG_ID, UUID.randomUUID().toString());
                defaultHeaderMap.put(HttpConstant.YF_API_DEVICE_ID, SystemUtils.getMacAddress());
                defaultHeaderMap.put(HttpConstant.YF_API_SHOP_ID, ShopInfoManager.getInstance().getShopInfo().getShopId() + "");
                defaultHeaderMap.put(HttpConstant.YF_API_BRAND_ID, ShopInfoManager.getInstance().getShopInfo().getBrandId() + "");
                defaultHeaderMap.put(HttpConstant.YF_API_TIMESTAMP, Calendar.getInstance().getTimeInMillis() + "");
//                defaultHeaderMap.put(HttpConstant.YF_API_TOKEN, HttpConstant.getYfApiToken());

                //defaultHeaderMap.put(HttpConstant.ZONE_VERSION, "1");
                //添加语言
                //defaultHeaderMap.put(HttpConstant.KRY_SYNC_LOCALE, SharedPreferenceUtil.getSpUtil().getString(Constant.DEFAULTLANGUAGE, ""));
                defaultHeaderMap.put("Accept-Encoding", "gzip");

                //添加api安全认证信息
                /*String shopId = ShopInfoCfg.getInstance().shopId;
                Map<String, String> apiSignMap = HttpConstant.tokenEncrypt(shopId);
                if (apiSignMap != null && apiSignMap.size() > 0) {
                    defaultHeaderMap.put(HttpConstant.YF_API_SIGN, apiSignMap.get("token"));
                    defaultHeaderMap.put(HttpConstant.YF_API_TIMESTAMP, apiSignMap.get("time"));
                }*/
                break;
            case HeaderType.HEADER_TYPE_ERP://erp请求头
                defaultHeaderMap.put(HttpConstant.YF_API_MSG_ID, UUID.randomUUID().toString());
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
