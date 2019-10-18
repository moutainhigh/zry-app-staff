package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "printer_dish_document")
public class PrinterDishDocument extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String dishId = "dish_id";


        public static final String dishUuid = "dish_uuid";


        public static final String documentId = "document_id";


        public static final String documentUuid = "document_uuid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

        public static final String cashierTicketId = "cashier_ticket_id";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "dish_id", canBeNull = false)
    private Long dishId;

    @DatabaseField(columnName = "dish_uuid", canBeNull = false)
    private String dishUuid;

    @DatabaseField(columnName = "document_id", canBeNull = false)
    private Long documentId;

    @DatabaseField(columnName = "document_uuid", canBeNull = false)
    private String documentUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

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

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getDishUuid() {
        return dishUuid;
    }

    public void setDishUuid(String dishUuid) {
        this.dishUuid = dishUuid;
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

    public Long getCashierTicketId() {
        return cashierTicketId;
    }

    public void setCashierTicketId(Long cashierTicketId) {
        this.cashierTicketId = cashierTicketId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(dishId, dishUuid, documentId, documentUuid);
    }
}

