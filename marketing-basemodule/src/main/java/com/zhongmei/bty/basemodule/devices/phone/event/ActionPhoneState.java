package com.zhongmei.bty.basemodule.devices.phone.event;

public class ActionPhoneState {
    private boolean isCall = false;

    public ActionPhoneState(boolean isCall) {
        super();
        this.isCall = isCall;
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall(boolean isCall) {
        this.isCall = isCall;
    }


}
