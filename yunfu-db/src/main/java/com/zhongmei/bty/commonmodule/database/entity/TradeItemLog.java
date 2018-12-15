package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;

/**
 * TradeItemLog is a ORMLite bean type. Corresponds to the database table "trade_item_log"
 */
@DatabaseTable(tableName = "trade_item_log")
public class TradeItemLog extends DataEntityBase implements ICreator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_item_log"
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
         * quantity
         */
        public static final String quantity = "quantity";

        /**
         * reason_content
         */
        public static final String reasonContent = "reason_content";

        /**
         * trade_item_id
         */
        public static final String tradeItemId = "trade_item_id";

        /**
         * trade_item_uuid
         */
        public static final String tradeItemUuid = "trade_item_uuid";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "quantity", canBeNull = false)
    private java.math.BigDecimal quantity;

    @DatabaseField(columnName = "reason_content")
    private String reasonContent;

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid", canBeNull = false)
    private String tradeItemUuid;

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

    public java.math.BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(quantity, tradeItemUuid);
    }
}

