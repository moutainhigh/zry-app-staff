package com.zhongmei.bty.queue.event;

import com.zhongmei.bty.queue.vo.NewQueueBeanVo;

/**
 * Created by demo on 2018/12/15
 */

public class QueueShowDetailEvent {

    private NewQueueBeanVo queueBeanVo;

    public QueueShowDetailEvent(NewQueueBeanVo queueBeanVo) {
        this.queueBeanVo = queueBeanVo;
    }

    public NewQueueBeanVo getQueueBeanVo() {
        return queueBeanVo;
    }

}
