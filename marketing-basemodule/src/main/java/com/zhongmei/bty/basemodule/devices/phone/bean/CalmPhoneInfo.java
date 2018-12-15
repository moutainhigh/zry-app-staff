package com.zhongmei.bty.basemodule.devices.phone.bean;

import com.zhongmei.bty.basemodule.devices.phone.bean.AdapterData;

/**
 * Clam支持的外设电话的基本属性
 *
 * @date 2014-8-6
 */
public class CalmPhoneInfo implements AdapterData {
    /**
     * 设备的MAC地址
     */
    private String mac;

    /**
     * 是否是上次绑定过的设备
     */
    private boolean isLastConnect;

    public CalmPhoneInfo(String mac, boolean isLastConnect) {
        super();
        this.mac = mac;
        this.isLastConnect = isLastConnect;
    }

    public CalmPhoneInfo() {
        super();
    }

    public String getMac() {
        return mac;
    }

    public boolean isLastConect() {
        return isLastConnect;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setLastConect(boolean isLastConect) {
        this.isLastConnect = isLastConect;
    }

}   
