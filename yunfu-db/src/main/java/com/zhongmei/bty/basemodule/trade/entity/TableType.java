package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.trade.enums.TableTypeZone;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;

/**
 * Trade is a ORMLite bean type. Corresponds to the database table "table_type"
 */
@DatabaseTable(tableName = "table_type")
public class TableType extends OldEntityBase {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "table_type"
     */
    public interface $ extends OldEntityBase.$ {

        /**
         * table_type_name
         */
        String tableTypeName = "table_type_name";

        /**
         * table_type_zone
         */
        String tablePersonCount = "tableType";

        /**
         * sort
         */
        String sort = "sort";

        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * commercial_id
         */
        String commercialID = "commercial_id";

        /**
         * create_date_time
         */
        String createDateTime = "create_date_time";

        /**
         * modify_date_time
         */
        String modifyDateTime = "modify_date_time";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;

    @DatabaseField(columnName = "table_type_name")
    private String tableTypeName;

    @DatabaseField(columnName = "table_type_zone")
    private Integer tableType;

    /**
     * 排序
     */
    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;

    @DatabaseField(columnName = "create_date_time", canBeNull = false)
    private Long createDateTime;

    @DatabaseField(columnName = "modify_date_time", canBeNull = false)
    private Long modifyDateTime;

    public String getTableTypeName() {
        return tableTypeName;
    }

    public void setTableTypeName(String tableTypeName) {
        this.tableTypeName = tableTypeName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public TableTypeZone getTableType() {
        return ValueEnums.toEnum(TableTypeZone.class, tableType);
    }

    public void setTableType(TableTypeZone tableType) {
        this.tableType = ValueEnums.toValue(tableType);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(createDateTime, modifyDateTime);
    }
}
