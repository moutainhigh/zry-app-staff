package com.zhongmei.bty.customer.event;

/**
 * 显示编辑按钮
 */
public class EventShowEditIcon {
    /*是否显示编辑图标*/
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
