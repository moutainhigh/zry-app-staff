package com.zhongmei.bty.basemodule.devices.phone.event;

/**
 *
 */
public class EventHangResult {

    private boolean mOn;
    private String msg;

    public EventHangResult(boolean on) {
        super();
        this.mOn = on;
    }

    public EventHangResult(boolean on, String msg) {
        super();
        this.mOn = on;
        this.msg = msg;
    }

    public boolean isHangOn() {
        return mOn;
    }

    public String getMsg() {
        return msg;
    }

}
