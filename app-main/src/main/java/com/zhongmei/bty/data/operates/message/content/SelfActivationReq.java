package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */

public class SelfActivationReq {

    private String commercialId;//商户id

    private String mac;//mac地址

    private String goodsNumber;//实物编号

    private String appType;//应用类型

    private String osType;//系统类型

    private String oldVersion;//版本号

    private String versionName;//版本名称

    private String padNo;//pad编号

    private Integer deviceType;//设置类型（1：pad ,2:ipad）

    private Integer isMainPos;//是否为主收银pos机 1 否 0 是

    private Integer isBindPos;//是否绑定银联Pos 1:是 2:否

    private Integer autobind;//是否绑定蓝牙;1绑定电话盒,2-蓝牙;

    private Integer bindDeviceType;//绑定设备类型 1:商用设备 2:行政设备

    private String syncType;//同步方式

    private String backupSyncType;//备用同步方式

    private Integer updateType;//更新类型 0-检测不到更新 1-强制更新 2-可选更新


    public String getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(String commercialId) {
        this.commercialId = commercialId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPadNo() {
        return padNo;
    }

    public void setPadNo(String padNo) {
        this.padNo = padNo;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getIsMainPos() {
        return isMainPos;
    }

    public void setIsMainPos(Integer isMainPos) {
        this.isMainPos = isMainPos;
    }

    public Integer getIsBindPos() {
        return isBindPos;
    }

    public void setIsBindPos(Integer isBindPos) {
        this.isBindPos = isBindPos;
    }

    public Integer getAutobind() {
        return autobind;
    }

    public void setAutobind(Integer autobind) {
        this.autobind = autobind;
    }

    public Integer getBindDeviceType() {
        return bindDeviceType;
    }

    public void setBindDeviceType(Integer bindDeviceType) {
        this.bindDeviceType = bindDeviceType;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getBackupSyncType() {
        return backupSyncType;
    }

    public void setBackupSyncType(String backupSyncType) {
        this.backupSyncType = backupSyncType;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }
}
