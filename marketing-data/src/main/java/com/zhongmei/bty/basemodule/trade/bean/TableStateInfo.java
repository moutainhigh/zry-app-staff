package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;

/**
 * Created by demo on 2018/12/15
 */

public class TableStateInfo {
    public final Long id;

    public final TableStatus tableStatus;

    public final Long serverUpdateTime;

    public final BusinessType tradeBusinessType;//当前桌子上订单的行业类型（自助餐，团餐，正餐等）

    public TableStateInfo(Tables table, BusinessType businessType) {
        this.id = table.getId();
        this.tableStatus = table.getTableStatus();
        this.serverUpdateTime = table.verValue();
        this.tradeBusinessType = businessType;
    }
}
