package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * PrinterTableDocument is a ORMLite bean type. Corresponds to the database table "printer_table_document"
 */
@DatabaseTable(tableName = "printer_table_document")
public class PrinterTableDocument extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "printer_dish_document"
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
         * table_id
         */
        public static final String tableId = "table_id";

        /**
         * table_uuid
         */
        public static final String tableUuid = "table_uuid";

        /**
         * document_id
         */
        public static final String documentId = "document_id";

        /**
         * document_uuid
         */
        public static final String documentUuid = "document_uuid";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        public static final String cashierTicketId = "cashier_ticket_id";


    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "table_id", canBeNull = false)
    private Long tableId;

    @DatabaseField(columnName = "table_uuid", canBeNull = false)
    private String tableUuid;

    @DatabaseField(columnName = "document_id", canBeNull = false)
    private Long documentId;

    @DatabaseField(columnName = "document_uuid", canBeNull = false)
    private String documentUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getCashierTicketId() {
        return cashierTicketId;
    }

    public void setCashierTicketId(Long cashierTicketId) {
        this.cashierTicketId = cashierTicketId;
    }

    @DatabaseField(columnName = "cashier_ticket_id", canBeNull = true)
    private Long cashierTicketId;

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

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableUuid() {
        return tableUuid;
    }

    public void setTableUuid(String tableUuid) {
        this.tableUuid = tableUuid;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(String documentUuid) {
        this.documentUuid = documentUuid;
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tableId, tableUuid, documentId, documentUuid);
    }
}

