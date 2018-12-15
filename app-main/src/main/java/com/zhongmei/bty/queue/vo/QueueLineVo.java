package com.zhongmei.bty.queue.vo;

import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;

/**
 * 队列
 */
public class QueueLineVo {

    private boolean isQueueTypeAll = false;
    private CommercialQueueLine queueLine;
    private boolean isSelected;
    private int queueCount;

    public static QueueLineVo createQueueTypeAll() {
        QueueLineVo queueLineVo = new QueueLineVo();
        queueLineVo.isQueueTypeAll = true;
        return queueLineVo;
    }

    public CommercialQueueLine getQueueLine() {
        return queueLine;
    }

    public void setQueueLine(CommercialQueueLine queueLine) {
        this.queueLine = queueLine;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    public boolean isQueueTypeAll() {
        return isQueueTypeAll;
    }
}
