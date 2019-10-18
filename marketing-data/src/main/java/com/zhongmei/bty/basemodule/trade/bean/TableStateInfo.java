package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;



public class TableStateInfo {
    public final Long id;

    public final TableStatus tableStatus;

    public final Long serverUpdateTime;

    public final BusinessType tradeBusinessType;
    public TableStateInfo(Tables table, BusinessType businessType) {
        this.id = table.getId();
        this.tableStatus = table.getTableStatus();
        this.serverUpdateTime = table.verValue();
        this.tradeBusinessType = businessType;
    }
}
