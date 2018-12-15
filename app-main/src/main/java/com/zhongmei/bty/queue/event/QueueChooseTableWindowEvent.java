package com.zhongmei.bty.queue.event;

/**
 * Author：LiuYang
 * Date：2016/7/4 18:16
 * E-mail：liuy0
 */
public class QueueChooseTableWindowEvent {
    private boolean isOpen;

    public QueueChooseTableWindowEvent(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}
