package com.zhongmei.bty.mobilepay.event;


public class ExemptEvent {

    private int eraseType;

    public ExemptEvent(int eraseType) {
        this.eraseType = eraseType;
    }

    public int getEraseType() {
        return eraseType;
    }
}
