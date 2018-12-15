package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * TradeStatusLog is a ORMLite bean type. Corresponds to the database table "trade_status_log"
 */
@DatabaseTable(tableName = "trade_status_log")
public class TradeStatusLog extends DataEntityBase implements ICreator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_status_log"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * reason_content
         */
        public static final String reasonContent = "reason_content";

        /**
         * reason_id
         */
        public static final String reasonId = "reason_id";

        /**
         * trade_id
         */
        public static final String tradeId = "trade_id";

        /**
         * trade_status
         */
        public static final String tradeStatus = "trade_status";

        /**
         * trade_uuid
         */
        public static final String tradeUuid = "trade_uuid";

        /**
         * trade_pay_status
         */
        public static final String tradePayStatus = "trade_pay_status";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "reason_content")
    private String reasonContent;

    @DatabaseField(columnName = "reason_id")
    private Long reasonId;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "trade_status", canBeNull = false)
    private Integer tradeStatus;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "trade_pay_status")
    private Integer tradePayStatus;


    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = ValueEnums.toValue(tradeStatus);
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public void setTradePayStatus(TradePayStatus tradePayStatus) {
        this.tradePayStatus = ValueEnums.toValue(tradePayStatus);
    }


    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeStatus, tradeUuid);
    }
}

