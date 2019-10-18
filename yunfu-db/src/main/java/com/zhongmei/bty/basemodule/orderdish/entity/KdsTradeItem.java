package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;


@DatabaseTable(tableName = "kds_trade_item")
public class KdsTradeItem extends IdEntityBase {

    @DatabaseField
    public long dishServerChangeTime;

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long verValue() {
        return dishServerChangeTime;
    }
}
