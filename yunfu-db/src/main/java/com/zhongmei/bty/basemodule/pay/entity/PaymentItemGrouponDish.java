package com.zhongmei.bty.basemodule.pay.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ServerEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "payment_item_groupon_dish")
public class PaymentItemGrouponDish extends ServerEntityBase {
    public interface $ extends ServerEntityBase.$ {

        public static final String tradeId = "trade_id";

        public static final String paymentItemId = "payment_item_id";

        public static final String serialNo = "serial_no";

    }

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "trade_item_uuid")
    private String tradeItemUuid;
    @DatabaseField(columnName = "payment_item_id")
    private Long paymentItemId;
    @DatabaseField(columnName = "payment_item_uuid")
    private String paymentItemUuid;
    @DatabaseField(columnName = "serial_no")
    private String serialNo;
    @DatabaseField(columnName = "dish_id")
    private Long dishId;
    @DatabaseField(columnName = "dish_num")
    private BigDecimal dishNum;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public BigDecimal getDishNum() {
        return dishNum;
    }

    public void setDishNum(BigDecimal dishNum) {
        this.dishNum = dishNum;
    }


    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public String getPaymentItemUuid() {
        return paymentItemUuid;
    }

    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
