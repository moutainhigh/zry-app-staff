package com.zhongmei.bty.mobilepay.event;

/**
 * 发送抹零信息
 */
public class ExemptEvent {

    private int eraseType;

    public ExemptEvent(int eraseType) {
        this.eraseType = eraseType;
    }

    public int getEraseType() {
        return eraseType;
    }
}
