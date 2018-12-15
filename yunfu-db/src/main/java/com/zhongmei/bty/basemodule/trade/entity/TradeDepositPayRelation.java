package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

/**
 * TradeDeposit is a ORMLite bean type. Corresponds to the database table "trade_deposit"
 */
@DatabaseTable(tableName = "trade_deposit_pay_relation")
public class TradeDepositPayRelation extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_deposit"
     */
    public interface $ extends BasicEntityBase.$ {
        /**
         * shop_identy
         */
        //public static final String shopIdenty = "shop_identy";

        /**
         * trade_id
         */
        public static final String tradeId = "trade_id";

        /**
         * trade_uuid
         */
        public static final String tradeUuid = "trade_uuid";

        public static final String paymentItemId = "payment_item_id";

        public static final String paymentId = "payment_id";

        public static final String tradeDepositId = "trade_deposit_id";

        public static final String depositType = "deposit_type";


    }

    //@DatabaseField(columnName = "shop_identy", canBeNull = false)
    //private Long shopIdenty;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "payment_id", canBeNull = false)
    private Long paymentId;

    @DatabaseField(columnName = "payment_item_id", canBeNull = false)
    private Long paymentItemId;


    @DatabaseField(columnName = "trade_deposit_id", canBeNull = false)
    private Long tradeDepositId;

    /**
     * 1 付押金  2 退押金',
     */
    @DatabaseField(columnName = "deposit_type", canBeNull = false)
    private Integer depositType;

    /*public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }*/

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public Long getTradeDepositId() {
        return tradeDepositId;
    }

    public void setTradeDepositId(Long tradeDepositId) {
        this.tradeDepositId = tradeDepositId;
    }

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }
}
