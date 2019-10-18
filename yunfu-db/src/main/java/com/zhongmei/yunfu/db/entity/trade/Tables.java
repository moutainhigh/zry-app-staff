package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.AbsBasicEntityBase;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;


@DatabaseTable(tableName = "tables")
public class Tables extends AbsBasicEntityBase {


    private static final long serialVersionUID = 1L;


    public interface $ extends AbsBasicEntityBase.$ {


        String canBooking = "can_booking";


        String areaId = "area_id";


        String tableName = "table_name";


        String minConsum = "min_consum";


        String tableTypeID = "table_type_id";


        String tablePersonCount = "table_person_count";


        String tableStatus = "table_status";


        String sort = "sort";


        String uuid = "uuid";


        String commercialID = "commercial_id";


        String createDateTime = "create_date_time";


        String modifyDateTime = "modify_date_time";

    }

    @DatabaseField(columnName = "area_id")
    private Long areaId;

    @DatabaseField(columnName = "uuid")
    private String uuid;

    @DatabaseField(columnName = "table_name")
    private String tableName;

    @DatabaseField(columnName = "min_consum")
    private BigDecimal minConsum;


    @DatabaseField(columnName = "can_booking")
    private Integer canBooking;

    @DatabaseField(columnName = "table_type_id")
    private Long tableTypeID;

    @DatabaseField(columnName = "table_person_count")
    private Integer tablePersonCount;

    @DatabaseField(columnName = "table_status")
    private Integer tableStatus;

    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;

    @DatabaseField(columnName = "create_date_time")
    private Long createDateTime;

    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public BigDecimal getMinConsum() {
        return minConsum;
    }

    public void setMinConsum(BigDecimal minConsum) {
        this.minConsum = minConsum;
    }

    public Long getTableTypeID() {
        return tableTypeID;
    }

    public void setTableTypeID(Long tableTypeID) {
        this.tableTypeID = tableTypeID;
    }

    public Integer getTablePersonCount() {
        return tablePersonCount;
    }

    public void setTablePersonCount(Integer tablePersonCount) {
        this.tablePersonCount = tablePersonCount;
    }

    public TableStatus getTableStatus() {
        return ValueEnums.toEnum(TableStatus.class, tableStatus);
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = ValueEnums.toValue(tableStatus);
    }

    public Integer getCanBooking() {
        return canBooking;
    }

    public void setCanBooking(Integer canBooking) {
        this.canBooking = canBooking;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        return super.checkNonNull() && EntityBase.checkNonNull(createDateTime, modifyDateTime);
    }
}
