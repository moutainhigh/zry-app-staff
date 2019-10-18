package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.AddItemRecordStatus;


@DatabaseTable(tableName = "add_item_record")
public class AddItemRecord extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {
        public static final String tradeId = "trade_id";

        public static final String tableId = "table_id";

        public static final String batchId = "batch_id";

        public static final String mockTradeItemUuid = "mock_trade_item_uuid";

        public static final String mockTradeItemData = "mock_trade_item_data";

        public static final String handleStatus = "handle_status";

        public static final String customerId = "customer_id";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";
    }

        @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

        @DatabaseField(columnName = "table_id")
    private Long tableId;

        @DatabaseField(columnName = "batch_id")
    private Long batchId;

        @DatabaseField(columnName = "mock_trade_item_uuid")
    private String mockTradeItemUuid;

        @DatabaseField(columnName = "mock_trade_item_data")
    private String mockTradeItemData;

        @DatabaseField(columnName = "handle_status")
    private Integer handleStatus;

        @DatabaseField(columnName = "customer_id")
    private Long customerId;


        @DatabaseField(columnName = "updator_id")
    private Long updatorId;

        @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getMockTradeItemUuid() {
        return mockTradeItemUuid;
    }

    public void setMockTradeItemUuid(String mockTradeItemUuid) {
        this.mockTradeItemUuid = mockTradeItemUuid;
    }

    public String getMockTradeItemData() {
        return mockTradeItemData;
    }

    public void setMockTradeItemData(String mockTradeItemData) {
        this.mockTradeItemData = mockTradeItemData;
    }

    public AddItemRecordStatus getHandleStatus() {
        return ValueEnums.toEnum(AddItemRecordStatus.class, handleStatus);
    }

    public void setHandleStatus(AddItemRecordStatus addItemRecordStatus) {
        this.handleStatus = ValueEnums.toValue(addItemRecordStatus);
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

}
