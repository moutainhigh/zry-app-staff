package com.zhongmei.analysis;

import java.util.HashMap;

/**
 * Created by demo on 2018/12/15
 */
public class PerformanceActionData {
    private String shopId;
    private String macAddress;
    private String code;
    private HashMap<String, String> userActionMap = new HashMap<>();

    public PerformanceActionData() {
    }

    public PerformanceActionData(String shopId, String macAddress, HashMap<String, String> userActionMap) {
        this.shopId = shopId;
        this.macAddress = macAddress;
        this.userActionMap = userActionMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public HashMap<String, String> getUserActionMap() {
        return userActionMap;
    }

    public void setUserActionMap(HashMap<String, String> userActionMap) {
        this.userActionMap = userActionMap;
    }
}
