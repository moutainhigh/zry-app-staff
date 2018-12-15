package com.zhongmei.bty.queue.event;

/**
 * 用来桌台气泡显示的时候区域不可点击
 */
public class EventIsCanClick {
    //是否可以点击
    private boolean canClick;

    public EventIsCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public boolean getCanClick() {
        return canClick;
    }

}
