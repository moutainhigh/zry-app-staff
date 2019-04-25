package com.zhongmei.yunfu.context.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhongmei.yunfu.context.util.JsonUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

/**
 * Created by demo on 2018/12/15
 */

public class VersionInfo {

    private String mPackageName;

    private int status;
    private String message;
    private String versionCode;
    private String versionDescribe;
    private String createDate;
    private int upgradeModel;
    private String downloadUrl;
    private String syncUrl;
    private String shopId;
    private String versionName;

    public static VersionInfo create(String json) {
        return getVersionInfo(json);
    }

    private static VersionInfo getVersionInfo(String json) {
        try {
            return new Gson().fromJson(JsonUtil.getString(json,"content"), VersionInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getVersionDes(){
        return versionDescribe;
    }

    public boolean isForce() {
        //强制更新
        return upgradeModel == 1;
//        return false;
    }

    public boolean hasUpdate() {
        if (upgradeModel != 0) {
            long curVersion = Utils.toLong(SystemUtils.getVersionCode(mPackageName));
            long updateVersion = Utils.toLong(versionCode);
            if (curVersion < updateVersion) {
                return true;
            }
        }
        return false;
    }
}
