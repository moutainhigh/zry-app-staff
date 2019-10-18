package com.zhongmei.bty.customer.event;


public class EventShowEditIcon {

    boolean isShow;


    public EventShowEditIcon(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

}
