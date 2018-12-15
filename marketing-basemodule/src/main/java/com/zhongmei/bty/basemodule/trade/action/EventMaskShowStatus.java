package com.zhongmei.bty.basemodule.trade.action;

/**
 * Created by demo on 2018/12/15
 */

public class EventMaskShowStatus {
    private boolean isVisiable;

    public boolean isVisiable() {
        return isVisiable;
    }

    public void setVisiable(boolean visiable) {
        isVisiable = visiable;
    }

    public EventMaskShowStatus(boolean isVisiable) {
        this.isVisiable = isVisiable;
    }

    public EventMaskShowStatus() {
    }
}
