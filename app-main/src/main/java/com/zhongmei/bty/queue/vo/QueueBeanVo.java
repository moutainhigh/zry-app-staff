package com.zhongmei.bty.queue.vo;

import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.queue.manager.QueueManager;

/**
 * Created by demo on 2018/12/15
 */

public class QueueBeanVo {

    private Queue queue;
    private CommercialQueueLine queueLine;
    private TradeVo tradeVo;

    public QueueBeanVo(Queue queue, CommercialQueueLine queueLine) {
        this.queue = queue;
        this.queueLine = queueLine;
    }

    /**
     * 是否预点菜
     *
     * @return
     */
    public boolean isOrderDish() {
        return tradeVo != null && tradeVo.getTrade().getId() != null;
    }

    public Queue getQueue() {
        return queue;
    }

    public CommercialQueueLine getQueueLine() {
        return queueLine;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    public String getNum() {
        return QueueManager.formatNum(queue, queueLine);
    }
}
