package com.zhongmei.bty.basemodule.devices.phone.event;

public class EventNotifyCenter {

    public EventNotifyCenter(boolean ishow) {
        this.isShow = ishow;
    }

    private Boolean isShow;

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }


}
