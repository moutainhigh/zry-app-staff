package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.TableStatus;


@DatabaseTable(tableName = "trade_table")
public class TradeTable extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String memo = "memo";


        public static final String tableId = "table_id";


        public static final String tableName = "table_name";


        public static final String tablePeopleCount = "table_people_count";


        public static final String tradeId = "trade_id";


        public static final String tradeUuid = "trade_uuid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String waiterId = "waiter_id";


        public static final String waiterName = "waiter_name";


        public static final String selfTableStatus = "self_table_status";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "memo")
    private String memo;

    @DatabaseField(columnName = "table_id", index = true)
    private Long tableId;

    @DatabaseField(columnName = "table_name")
    private String tableName;

    @DatabaseField(columnName = "table_people_count")
    private Integer tablePeopleCount;

    @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "waiter_id")
    private Long waiterId;

    @DatabaseField(columnName = "waiter_name")
    private String waiterName;

    @DatabaseField(columnName = "self_table_status")
    private Integer selfTableStatus = TableStatus.OCCUPIED.value();

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getTablePeopleCount() {
        return tablePeopleCount == null ? 0 : tablePeopleCount;
    }

    public void setTablePeopleCount(Integer tablePeopleCount) {
        this.tablePeopleCount = tablePeopleCount;
    }

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

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public TableStatus getSelfTableStatus() {
        return ValueEnums.toEnum(TableStatus.class, selfTableStatus);
    }

    public void setSelfTableStatus(TableStatus selfTableStatus) {
        this.selfTableStatus = ValueEnums.toValue(selfTableStatus);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeUuid);
    }
}

