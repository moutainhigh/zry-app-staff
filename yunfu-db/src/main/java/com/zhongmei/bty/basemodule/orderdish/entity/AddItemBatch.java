package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.AddItemBatchStatus;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.SourceId;



@DatabaseTable(tableName = "add_item_batch")
public class AddItemBatch extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {
        public static final String tradeId = "trade_id";

        public static final String tableId = "table_id";

        public static final String tradeNo = "trade_no";

        public static final String tableName = "table_name";

        public static final String contactsName = "contacts_name";

        public static final String contactsSex = "contacts_sex";

        public static final String shopIdenty = "shop_identy";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

        public static final String handleStatus = "handle_status";

    }


        @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

        @DatabaseField(columnName = "table_id")
    private Long tableId;

        @DatabaseField(columnName = "trade_no")
    private String tradeNo;

        @DatabaseField(columnName = "table_name")
    private String tableName;

        @DatabaseField(columnName = "contacts_name")
    private String contactsName;

        @DatabaseField(columnName = "contacts_sex")
    private Integer contactsSex;

        @DatabaseField(columnName = "handle_status")
    private Integer handleStatus;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Integer sourceId;


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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public Sex getContactsSex() {
        return ValueEnums.toEnum(Sex.class, contactsSex);
    }

    public void setContactsSex(Sex sex) {

        this.contactsSex = ValueEnums.toValue(sex);
    }

    public AddItemBatchStatus getHandleStatus() {
        return ValueEnums.toEnum(AddItemBatchStatus.class, handleStatus);
    }

    public void setHandleStatus(AddItemBatchStatus addItemBatchStatus) {
        this.handleStatus = ValueEnums.toValue(addItemBatchStatus);
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

    public SourceId getSource() {
        return ValueEnums.toEnum(SourceId.class, sourceId);
    }

    public void setSource(SourceId sourceId) {
        this.sourceId = ValueEnums.toValue(sourceId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        return result;
    }
}