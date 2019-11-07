package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.bty.basemodule.commonbusiness.constants.Configure;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;


public class ServerAddressUtil {

    private static ServerAddressUtil instance;

    public static ServerAddressUtil getInstance() {
        if (instance == null) {
            instance = new ServerAddressUtil();
        }
        return instance;
    }


    public static String getRemoteHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            case 5://新加坡
                return "http://demo.com";
            default:
                return Configure.REMOTE_SERVER_HOST;
        }
    }


    public String getErpApi() {
        return ShopInfoManager.REMOTE_SERVER_HOST + String.format("/pos/api/sync/shop/info/%s", SystemUtils.getMacAddress());
//                return ShopInfoManager.MARKETING_SERVER_HOST + String.format("/pos/shop/info/%s", SystemUtils.getMacAddress());
    }


    public String getOwns() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/sync/data";
    }


    public String customerLogin() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/login";
    }


    public String queryCustomerList() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer";
    }


    public String getCustomerDetailById() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/info";
    }


    public String memberCoupons() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/coups";
    }


    public String memberValuecardHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/balance";
    }


    public String createMemberByMobile() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/save";
    }


    public String customerBindCard() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/ecard/bind";
    }


    public String getEntityCardStoreHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/trade/card_time";
    }



    public String getMemberStoreHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/trade/member_store";
    }


    public String refreshStateUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/pos/api/pay/syncTradeStatus";
    }


    public String getPayStatusOfThird() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/syncTradeStatus";
    }

        public String getPayStateUrl() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/syncTradeStatus";
    }


    public String getPayStatus() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/queryTradeStatus";
    }


    public String getRefundPayment(Long paymentItemId) {
        return ShopInfoManager.REMOTE_SERVER_HOST
                + String.format("/pos/api/pay/refund/%d", paymentItemId);
    }


    public String newQueryIntegralDetail() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/integral";
    }


    public String getUpdateCheckApi() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/systemVersion/checkVersion?shopId="
                + ShopInfoManager.getInstance().getShopInfo().getShopId() + "&brandId="+
                ShopInfoManager.getInstance().getShopInfo().getBrandId()
                +"&versionCode="
                + SystemUtils.getVersionCode();
    }


    public String getOpenIdUrl() {
        return "/pos/pay/login/code?shopId="
                + ShopInfoCfg.getInstance().shopId + "&deviceId="
                + SystemUtils.getMacAddress() + "&appType="
                + SystemUtils.getAppType() + "&uuid=";
    }

}
