package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

/**
 * Created by demo on 2018/12/15
 * 该数据由服务器生成
 */
@DatabaseTable(tableName = "trade_invoice_no")
public class TradeInvoiceNo extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {
        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * trade_id
         */
        String tradeId = "trade_id";

        /**
         * trade_uuid
         */
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
