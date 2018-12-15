package com.zhongmei.bty.basemodule.pay.message;

import java.io.Serializable;

public class PaymentDeviceReq implements Serializable {
    private Long posChannelId;

    private String deviceNumber;

    public Long getPosChannelId() {
        return posChannelId;
    }

    public void setPosChannelId(Long posChannelId) {
        this.posChannelId = posChannelId;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
