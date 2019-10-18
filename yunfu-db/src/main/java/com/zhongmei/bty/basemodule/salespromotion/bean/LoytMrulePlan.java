package com.zhongmei.bty.basemodule.salespromotion.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;



@DatabaseTable(tableName = "loyt_mrule_plan")
public class LoytMrulePlan extends IdEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {

        public static final String name = "name";
        public static final String brandId = "brand_id";
        public static final String planType = "plan_type";
        public static final String planState = "plan_state";
        public static final String startTime = "start_time";
        public static final String endTime = "end_time";
        public static final String limitCommercial = "limit_commercial";
        public static final String couponShared = "coupon_shared";
        public static final String limitPeriod = "limit_period";
        public static final String startPeriod = "start_period";
        public static final String endPeriod = "end_period";
        public static final String limitWeek = "limit_week";
        public static final String weekDay = "week_day";
        public static final String stackRule = "stack_rule";
        public static final String applyCrowd = "apply_crowd";
        public static final String crowdTagId = "crowd_tag_id";
        public static final String description = "description";
        public static final String deletedFlag = "deleted_flag";

        public static final String creatorId = "creator_id";
        public static final String updaterId = "updater_id";

        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";

    }

    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "brand_id")
    private Long brandId;
    @DatabaseField(columnName = "plan_type")
    private int planType;
    @DatabaseField(columnName = "plan_state")
    private int planState;
    ;
    @DatabaseField(columnName = "start_time")
    private String startTime;
    ;
    @DatabaseField(columnName = "end_time")
    private String endTime;
    ;
    @DatabaseField(columnName = "limit_commercial")
    private int limitCommercial;
    @DatabaseField(columnName = "coupon_shared")
    private int couponShared;
    @DatabaseField(columnName = "limit_period")
    private int limitPeriod;
    @DatabaseField(columnName = "start_period")
    private String startPeriod;
    @DatabaseField(columnName = "end_period")
    private String endPeriod;
    @DatabaseField(columnName = "limit_week")
    private int limitWeek;
    @DatabaseField(columnName = "week_day")
    private String weekDay;
    @DatabaseField(columnName = "stack_rule")
    private int stackRule;
    @DatabaseField(columnName = "apply_crowd")
    private int applyCrowd;
    @DatabaseField(columnName = "crowd_tag_id")
    private int crowdTagId;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "deleted_flag")
    private int deletedFlag;
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
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

    public int getLimitCommercial() {
        return limitCommercial;
    }

    public void setLimitCommercial(int limitCommercial) {
        this.limitCommercial = limitCommercial;
    }

    public int getCouponShared() {
        return couponShared;
    }

    public void setCouponShared(int couponShared) {
        this.couponShared = couponShared;
    }

    public int getLimitPeriod() {
        return limitPeriod;
    }

    public void setLimitPeriod(int limitPeriod) {
        this.limitPeriod = limitPeriod;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public int getLimitWeek() {
        return limitWeek;
    }

    public void setLimitWeek(int limitWeek) {
        this.limitWeek = limitWeek;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public int getStackRule() {
        return stackRule;
    }

    public void setStackRule(int stackRule) {
        this.stackRule = stackRule;
    }

    public int getApplyCrowd() {
        return applyCrowd;
    }

    public void setApplyCrowd(int applyCrowd) {
        this.applyCrowd = applyCrowd;
    }

    public int getCrowdTagId() {
        return crowdTagId;
    }

    public void setCrowdTagId(int crowdTagId) {
        this.crowdTagId = crowdTagId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(int deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return deletedFlag == 0;    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
