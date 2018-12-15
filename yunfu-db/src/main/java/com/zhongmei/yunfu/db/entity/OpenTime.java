package com.zhongmei.yunfu.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.BusinessTimeType;
import com.zhongmei.yunfu.db.enums.IsNextDay;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * Trade is a ORMLite bean type. Corresponds to the database table "tables"
 */
@DatabaseTable(tableName = "open_time")
public class OpenTime extends OldEntityBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "tables"
     */
    public interface $ extends OldEntityBase.$ {

        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * business_time_type
         */
        String businessTimeType = "business_time_type";

        /**
         * closing_time
         */
        String closingTime = "closing_time";

        /**
         * is_next_day
         */
        String isNextDay = "is_next_day";

        String endTime = "end_time";

        String stopTime = "start_time";

        String type = "type";

        /**
         * week
         */
        String week = "week";

        /**
         * create_time
         */
        String createTime = "create_time";

        /**
         * modify_time
         */
        String modifyTime = "modify_time";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;

    @DatabaseField(columnName = "business_time_type")
    private Integer businessTimeType;

    @DatabaseField(columnName = "closing_time")
    private String closingTime;

    @DatabaseField(columnName = "is_next_day")
    private Integer isNextDay;

    @DatabaseField(columnName = "week")
    private Integer week;

    @DatabaseField(columnName = "create_time")
    private Long createTime;

    @DatabaseField(columnName = "start_time")
    private String startTime;

    @DatabaseField(columnName = "end_time")
    private String endTime;

    @DatabaseField(columnName = "type")
    private Integer type;

    @DatabaseField(columnName = "modify_time")
    private Long modifyTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BusinessTimeType getBusinessTimeType() {
        return ValueEnums.toEnum(BusinessTimeType.class, businessTimeType);
    }

    public void setBusinessTimeType(BusinessTimeType businessTimeType) {
        this.businessTimeType = ValueEnums.toValue(businessTimeType);
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public IsNextDay getIsNextDay() {
        return ValueEnums.toEnum(IsNextDay.class, isNextDay);
    }

    public void setIsNextDay(IsNextDay isNextDay) {
        this.isNextDay = ValueEnums.toValue(isNextDay);
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Long verValue() {
        return modifyTime;
    }

}
