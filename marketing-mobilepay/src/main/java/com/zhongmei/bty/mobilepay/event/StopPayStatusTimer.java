package com.zhongmei.bty.mobilepay.event;

/**
 * @Date：2015-7-31 上午11:44:38
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class StopPayStatusTimer {
    private boolean isStop;// true stop

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
