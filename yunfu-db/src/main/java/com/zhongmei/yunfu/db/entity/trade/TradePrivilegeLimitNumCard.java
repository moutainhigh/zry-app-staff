package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 优惠和次卡的关联
 * Title:TradePrivilegeLimitNumCard.java
 * Description:
 * Copyright: Copyright (c) 2012
 *
 * @version 1.0 2018年06月14日 16:00
 */
@DatabaseTable(tableName = "trade_privilege_limitnumcard")
public class TradePrivilegeLimitNumCard extends ServerEntityBase {

    public interface $ extends BasicEntityBase.$ {
        String tradeId = "trade_id";
        String tradeUuid = "trade_uuid";
        String tradePrivilegeId = "trade_privilege_id";
    }

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;
    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;
    @DatabaseField(columnName = "trade_privilege_id")
    private Long tradePrivilegeId;
    @DatabaseField(columnName = "trade_privilege_uuid")
    private String tradePrivilegeUuid;
    @DatabaseField(columnName = "card_no")
    private String cardNo;
    @DatabaseField(columnName = "customer_id")
    private Long customerId;

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


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setInVaild() {
        setStatusFlag(StatusFlag.INVALID);
        validateUpdate();
    }
}
