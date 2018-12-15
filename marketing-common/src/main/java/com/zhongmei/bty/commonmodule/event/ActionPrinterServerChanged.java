package com.zhongmei.bty.commonmodule.event;

/**
 * Created by demo on 2018/12/15
 */
public class ActionPrinterServerChanged {
    private String ip;
    private String deviceIdentity;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }
}
