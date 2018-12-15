package com.zhongmei.yunfu;

import android.text.TextUtils;

/**
 * Created by demo on 2018/12/15
 * 门店配置信息
 */
public class ShopInfo {

    public String syncUrl = ShopInfoManager.REMOTE_SERVER_HOST;

    private String deviceId;
    private Long shopId;
    private String shopName;
    private Long brandId;
    private String brandName;
    private String shopPhone;
    private String shopAddress;
    private String shopLogo;
    private String latitude;
    private String longitude;
    private String openTime;  //9:00-18:00

    public String getDeviceId() {
        return deviceId;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSyncUrl() {
        return syncUrl;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }


    public String getStartTime() {
        if (TextUtils.isEmpty(openTime)) {
            return "00:00";
        }

        return openTime.substring(0, openTime.indexOf("-"));
    }

    public String getEndTime() {
        if (TextUtils.isEmpty(openTime)) {
            return "23:30";
        }

        return openTime.substring(openTime.indexOf("-") + 1, openTime.length());
    }
}
