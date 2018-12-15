package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.PrivilegeUseStatus;

/**
 * 优惠扩展表，判断优惠是否被核销
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "trade_privilege_extra")
public class TradePrivilegeExtra extends BasicEntityBase {

    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        String tradeId = "trade_id";
        String tradeUuid = "trade_uuid";
        String tradePrivilegeId = "trade_privilege_id";
        String tradePrivilegeUuid = "trade_privilege_uuid";
        String customerId = "customer_id";
        String useStatus = "use_status";
    }

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;

    @DatabaseField(columnName = "trade_privilege_id", canBeNull = false)
    private Long tradePrivilegeId;

    @DatabaseField(columnName = "trade_privilege_uuid", canBeNull = false)
    private String tradePrivilegeUuid;

    @DatabaseField(columnName = "customer_id")
    private Long customerId;

    @DatabaseField(columnName = "use_status", canBeNull = false)
    private Integer useStatus;

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

    public Long getTradePrivilegeId() {
        return tradePrivilegeId;
    }

    public void setTradePrivilegeId(Long tradePrivilegeId) {
        this.tradePrivilegeId = tradePrivilegeId;
    }

    public String getTradePrivilegeUuid() {
        return tradePrivilegeUuid;
    }

    public void setTradePrivilegeUuid(String tradePrivilegeUuid) {
        this.tradePrivilegeUuid = tradePrivilegeUuid;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public PrivilegeUseStatus getUseStatus() {
        return ValueEnums.toEnum(PrivilegeUseStatus.class, useStatus);
    }

    public void setUseStatus(PrivilegeUseStatus useStatus) {
        this.useStatus = ValueEnums.toValue(useStatus);
    }
}
