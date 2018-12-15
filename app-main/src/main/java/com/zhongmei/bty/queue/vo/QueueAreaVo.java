package com.zhongmei.bty.queue.vo;

import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：LiuYang
 * Date：2016/6/28 17:03
 * E-mail：liuy0
 */
public class QueueAreaVo {
    private CommercialArea tableArea;
    private boolean isSelected;
    private List<Tables> tablesList;

    public QueueAreaVo() {
        tablesList = new ArrayList<Tables>();
    }

    public CommercialArea getArea() {
        return tableArea;
    }

    public void setArea(CommercialArea tableArea) {
        this.tableArea = tableArea;
    }

    public List<Tables> getTablesList() {
        return tablesList;
    }

    public void setTablesList(List<Tables> tablesList) {
        this.tablesList = tablesList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
