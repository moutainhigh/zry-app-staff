package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.CreditType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.ServerEntityBase;

import java.math.BigDecimal;

/**
 * 挂账记录表 Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "trade_credit_log")
public class TradeCreditLog extends ServerEntityBase implements ICreator {

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "credit_type", canBeNull = false)
    private Integer creditType;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private BigDecimal amount;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "customer_id", canBeNull = false)
    private Long customerId;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "customer_name")
    private String customerName;

    @DatabaseField(columnName = "customer_phone")
    private String customerPhone;

    /**
     * The columns of table "trade_reason_rel"
     */
    public interface $ extends BasicEntityBase.$ {
        String tradeId = "trade_id";

        String creditType = "credit_type";

        String amount = "amount";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String customerId = "customer_id";

        String uuid = "uuid";

        String customer_name = "customer_name";

        String customer_phone = "customer_phone";
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public CreditType getCreditType() {
        return ValueEnums.toEnum(CreditType.class, creditType);
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = ValueEnums.toValue(creditType);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeId, creditType, amount, customerId, uuid);
    }
}
