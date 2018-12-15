package com.zhongmei.bty.queue.event;

/**
 * Author：LiuYang
 * Date：2016/6/29 08:37
 * E-mail：liuy0
 */
public class IdleTableCountChangeEvent {
    public final int count;

    public IdleTableCountChangeEvent(int count) {
        this.count = count;
    }
}
