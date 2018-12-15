package com.zhongmei.bty.basemodule.devices.mispos.event;

public class EventReadKeyboard {
    private boolean issuccess;
    public String errorinfo;

    public EventReadKeyboard(boolean success, String merrorinfo) {
        issuccess = success;
        errorinfo = merrorinfo;
    }

    public boolean isIssuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

}
