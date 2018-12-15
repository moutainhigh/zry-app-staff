package com.zhongmei.bty.queue.vo;

import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class NewQueueAreaVo {

    private CommercialQueueLine queueLine;

    private boolean isSelected;

    private List<NewQueueBeanVo> queueingBeanVoList;

    private List<NewQueueBeanVo> queuedBeanVoList;

    public CommercialQueueLine getQueueLine() {
        return queueLine;
    }

    public void setQueueLine(CommercialQueueLine queueLine) {
        this.queueLine = queueLine;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<NewQueueBeanVo> getQueueingBeanVoList() {
        return queueingBeanVoList;
    }

    public void setQueueingBeanVoList(List<NewQueueBeanVo> queueingBeanVoList) {
        this.queueingBeanVoList = queueingBeanVoList;
    }

    public List<NewQueueBeanVo> getQueuedBeanVoList() {
        return queuedBeanVoList;
    }

    public void setQueuedBeanVoList(List<NewQueueBeanVo> queuedBeanVoList) {
        this.queuedBeanVoList = queuedBeanVoList;
    }

    public int getQueueingCount() {
        return Utils.isNotEmpty(queueingBeanVoList) ? queueingBeanVoList.size() : 0;
    }

    public int getQueueHistoryCount() {
        return Utils.isNotEmpty(queuedBeanVoList) ? queuedBeanVoList.size() : 0;
    }

    public boolean isQueueTypeAll() {
        return queueLine != null && queueLine.getId() == 0L;
    }
}
