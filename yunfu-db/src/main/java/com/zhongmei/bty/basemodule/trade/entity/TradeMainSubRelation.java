package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;



@DatabaseTable(tableName = "trade_main_sub_relation")
public class TradeMainSubRelation extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String mainTradeId = "main_trade_id";
        public static final String subTradeId = "sub_trade_id";
        public static final String shopIdenty = "shop_identy";
    }

    @DatabaseField(columnName = "main_trade_id")
    private Long mainTradeId;


    @DatabaseField(columnName = "sub_trade_id")
    private Long subTradeId;


    public Long getMainTradeId() {
        return mainTradeId;
    }

    public void setMainTradeId(Long mainTradeId) {
        this.mainTradeId = mainTradeId;
    }

    public Long getSubTradeId() {
        return subTradeId;
    }

    public void setSubTradeId(Long subTradeId) {
        this.subTradeId = subTradeId;
    }

}
