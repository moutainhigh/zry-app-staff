package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;

/**
 * ThirdReturnInfo is a ORMLite bean type. Corresponds to the database table "trade_return_info"
 */
@DatabaseTable(tableName = "trade_return_info")
public class TradeReturnInfo extends BasicEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_return_info"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * trade_id
         */
        public static final String tradeId = "trade_id";

        /**
         * return_status
         */
        public static final String returnStatus = "return_status";

        /**
         * uuid
         */
        public static final String uuid = "uuid";

        /**
         * reason
         */
        public static final String reason = "reason";

        /**
         * sequence_no
         */
        public static final String sequence_no = "sequence_no";
    }

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "return_status", canBeNull = false)
    private Integer returnStatus;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "reason")
    private String reason;

    @DatabaseField(columnName = "sequence_no")
    private Integer sequenceNo;


    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public TradeReturnInfoReturnStatus getReturnStatus() {
        return ValueEnums.toEnum(TradeReturnInfoReturnStatus.class, returnStatus);
    }

    public void setReturnStatus(TradeReturnInfoReturnStatus returnStatus) {
        this.returnStatus = ValueEnums.toValue(returnStatus);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeId, returnStatus, uuid);
    }
}

