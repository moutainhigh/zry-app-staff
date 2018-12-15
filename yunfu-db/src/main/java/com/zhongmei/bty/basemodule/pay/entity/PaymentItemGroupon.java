package com.zhongmei.bty.basemodule.pay.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ServerEntityBase;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "payment_item_groupon")
public class PaymentItemGroupon extends ServerEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String grouponId = "groupon_id";

        public static final String tradeId = "trade_id";

        public static final String paymentItemId = "payment_item_id";

        public static final String usedSerialNo = "used_serial_no";

    }


    @DatabaseField(columnName = "groupon_id", canBeNull = false)
    private Long grouponId;

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "payment_item_id", canBeNull = false)
    private Long paymentItemId;

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    @DatabaseField(columnName = "deal_title ")
    private String dealTitle;

    @DatabaseField(columnName = "market_price")
    private BigDecimal marketPrice;

    @DatabaseField(columnName = "price")
    private BigDecimal price;

    @DatabaseField(columnName = "use_count")
    private int useCount;

    @DatabaseField(columnName = "used_serial_no")
    private String usedSerialNo;

    private String serialNo; // 团购券码(用于上行接口)

    public String getUsedSerialNo() {
        return usedSerialNo;
    }

    public void setUsedSerialNo(String usedSerialNo) {
        this.usedSerialNo = usedSerialNo;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getGrouponId() {
        return grouponId;
    }

    public void setGrouponId(Long grouponId) {
        this.grouponId = grouponId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(grouponId, tradeId, paymentItemId);
    }
}
