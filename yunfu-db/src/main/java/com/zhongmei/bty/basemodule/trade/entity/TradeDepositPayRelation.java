package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "trade_deposit_pay_relation")
public class TradeDepositPayRelation extends BasicEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {



        public static final String tradeId = "trade_id";


        public static final String tradeUuid = "trade_uuid";

        public static final String paymentItemId = "payment_item_id";

        public static final String paymentId = "payment_id";

        public static final String tradeDepositId = "trade_deposit_id";

        public static final String depositType = "deposit_type";


    }


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


    @DatabaseField(columnName = "deposit_type", canBeNull = false)
    private Integer depositType;



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
