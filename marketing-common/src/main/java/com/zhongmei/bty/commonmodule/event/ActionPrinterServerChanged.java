package com.zhongmei.bty.commonmodule.event;


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
