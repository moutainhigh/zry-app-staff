package com.zhongmei.bty.queue.event;

import com.zhongmei.bty.queue.vo.NewQueueBeanVo;

/**
 * Author：LiuYang
 * Date：2016/6/30 15:11
 * E-mail：liuy0
 */
public class QueueShowChooseTableEvent {

    private boolean isShow;
    private NewQueueBeanVo queueVo;

    public QueueShowChooseTableEvent(boolean isShow, NewQueueBeanVo queueVo) {
        this.isShow = isShow;
        this.queueVo = queueVo;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public NewQueueBeanVo getQueueVo() {
        return this.queueVo;
    }
}
