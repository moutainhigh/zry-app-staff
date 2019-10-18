package com.zhongmei.bty.mobilepay.event;



public class SeparateEvent {
    public static final int EVENT_SEPARATE_SAVE = 1;    public static final int EVENT_SEPARATE_PAYED = 2;    public static final int EVENT_SEPARATE_PAYING = 3;    public static final int EVENT_RESOURCE_PAYING = 4;
    public SeparateEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    int status = 0;
}
