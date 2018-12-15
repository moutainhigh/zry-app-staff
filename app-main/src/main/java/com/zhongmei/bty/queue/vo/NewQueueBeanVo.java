package com.zhongmei.bty.queue.vo;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.Locale;

/**
 * Created by demo on 2018/12/15
 */

public class NewQueueBeanVo {

    private Queue queue;

    private QueueExtra queueExtra;

    private CommercialQueueLine queueLine;

    private PrepareTradeRelation tradeRelation;

    private TradeVo tradeVo;

    private boolean isSelected;

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public QueueExtra getQueueExtra() {
        return queueExtra;
    }

    public void setQueueExtra(QueueExtra queueExtra) {
        this.queueExtra = queueExtra;
    }

    public CommercialQueueLine getQueueLine() {
        return queueLine;
    }

    public void setQueueLine(CommercialQueueLine queueLine) {
        this.queueLine = queueLine;
    }

    public PrepareTradeRelation getTradeRelation() {
        return tradeRelation;
    }

    public void setTradeRelation(PrepareTradeRelation tradeRelation) {
        this.tradeRelation = tradeRelation;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getQueueNumber() {
        if (queue == null || queueLine == null) {
            return "";
        }
        String queueChar = queueLine.getQueueChar();
        if (queue.getQueueSource() != null && queue.getQueueSource() != QueueOrderSource.DaoDian && queue.getQueueSource() != QueueOrderSource.MeiDaQueue) {
            queueChar += "N";
        }
        String num = String.format(Locale.getDefault(), "%03d", queue.getQueueNumber());
        return queueChar + num;
    }

    public boolean isOrderDish() {
        return tradeRelation != null && tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeItemList());
    }
}
