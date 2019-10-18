package com.zhongmei.bty.mobilepay.event;


public class StopPayStatusTimer {
    private boolean isStop;
    public StopPayStatusTimer(boolean is) {
        isStop = is;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
