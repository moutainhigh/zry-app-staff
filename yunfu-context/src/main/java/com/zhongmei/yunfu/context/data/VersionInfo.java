package com.zhongmei.yunfu.context.data;

import com.google.gson.Gson;
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
    private String updateDesc;
    private String createDateTime;
    private int updateType;
    private String updateUrl;
    private String syncUrl;
    private String shopId;
    private String versionName;

    public static VersionInfo create(String json) {
        return getVersionInfo(json);
    }

    private static VersionInfo getVersionInfo(String json) {
        try {
            return new Gson().fromJson(json, VersionInfo.class);
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

    public String getUpdateUrl() {
        return updateUrl;
    }

    public boolean isForce() {
        //强制更新
        return updateType == 1;
//        return false;
    }

    public boolean hasUpdate() {
        if (updateType != 0) {
            long curVersion = Utils.toLong(SystemUtils.getVersionCode(mPackageName));
            long updateVersion = Utils.toLong(versionCode);
            if (curVersion < updateVersion) {
                return true;
            }
        }
        return false;
    }
}
