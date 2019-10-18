package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;


public class UnHandoverItem implements Serializable {


    private static final long serialVersionUID = 1L;
    String deviceId;
        Integer padNo;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPadNo() {
        return padNo;
    }

    public void setPadNo(Integer padNo) {
        this.padNo = padNo;
    }

}
