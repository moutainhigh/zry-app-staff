package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "trade_invoice")
public class TradeInvoice extends BasicEntityBase {


    public interface $ extends BasicEntityBase.$ {

        String shopIdenty = "shop_identy";


        String tradeId = "trade_id";


        String invoiceTitle = "invoice_title";


        String taxpayerId = "taxpayer_id";
    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "invoice_title", canBeNull = false)
    private String invoiceTitle;

    @DatabaseField(columnName = "taxpayer_id")
    private String taxpayerId;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }
}
