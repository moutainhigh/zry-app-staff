package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.bty.commonmodule.database.enums.PeriodType;


@DatabaseTable(tableName = "period")
public class Period extends EntityBase<String> {

    private static final long serialVersionUID = 1L;

    public interface $ {

        String commercialID = "commercial_id";

        String id = "id";

        String periodName = "period_name";

        String periodType = "period_type";

        String periodStartTime = "period_starttime";

        String periodEndTime = "period_endtime";

        String status = "status";

        String createDateTime = "crate_date_time";

        String modifyDateTime = "modify_date_time";

        String uuid = "uuid";
    }


    @DatabaseField(columnName = "uuid", id = true, canBeNull = false)
    private String uuid;


    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;


    @DatabaseField(columnName = "id")
    private Long id;


    @DatabaseField(columnName = "period_name")
    private String periodName;


    @DatabaseField(columnName = "period_type")
    private Integer periodType;


    @DatabaseField(columnName = "period_starttime")
    private String periodStarttime;


    @DatabaseField(columnName = "period_endtime")
    private String periodEndtime;


    @DatabaseField(columnName = "status")
    private Integer status;


    @DatabaseField(columnName = "crate_date_time")
    private Long createDateTime;


    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public PeriodType getPeriodType() {
        return ValueEnums.toEnum(PeriodType.class, periodType);
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = ValueEnums.toValue(periodType);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getUuid() {
        return uuid;
    }

    public String getPeriodStarttime() {
        return periodStarttime;
    }

    public void setPeriodStarttime(String periodStarttime) {
        this.periodStarttime = periodStarttime;
    }

    public String getPeriodEndtime() {
        return periodEndtime;
    }

    public void setPeriodEndtime(String periodEndtime) {
        this.periodEndtime = periodEndtime;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean isValid() {
        return status == 0;
    }

    @Override
    public String pkValue() {
        return uuid;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(uuid);
    }
}
