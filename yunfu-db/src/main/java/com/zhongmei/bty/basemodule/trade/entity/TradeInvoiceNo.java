package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "trade_invoice_no")
public class TradeInvoiceNo extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        String shopIdenty = "shop_identy";


        String tradeId = "trade_id";


        String tradeUuid = "trade_uuid";
    }


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;


    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "code")
    private String code;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public String getCode() {
        return code;
    }
}
