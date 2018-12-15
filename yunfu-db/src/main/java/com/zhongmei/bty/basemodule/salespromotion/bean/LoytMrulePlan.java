package com.zhongmei.bty.basemodule.salespromotion.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

/**
 * Created by demo on 2018/12/15
 */

@DatabaseTable(tableName = "loyt_mrule_plan")
public class LoytMrulePlan extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "loyt_mrule_plan"
     */
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
    private String name;//方案名称

    @DatabaseField(columnName = "brand_id")
    private Long brandId;//商户Id

    @DatabaseField(columnName = "plan_type")
    private int planType;//1,新会员招募 2,升级会员赠券 3,精准营销 4,消费满额赠券 5,储值赠券 6,生日营销 7,抽奖营销 8,预订营销 9,排队营销 10,单商品营销 11,多商品营销 12,关怀信息推送 13,分享营销 14,进店领券 15,评分赠券 16,支付有礼17,消费频次激励18,消费累计满额赠券  19满量促销,20满额促销21, 买赠活动 22, 单一价

    @DatabaseField(columnName = "plan_state")
    private int planState;
    ;//1, 新建2, 进行中3, 暂停4, 结束

    @DatabaseField(columnName = "start_time")
    private String startTime;
    ;//营销周期开始时间

    @DatabaseField(columnName = "end_time")
    private String endTime;
    ;//营销周期结束时间

    @DatabaseField(columnName = "limit_commercial")
    private int limitCommercial;//是否限制参与门店

    @DatabaseField(columnName = "coupon_shared")
    private int couponShared;//多商品营销中的配置，未来可能还会有其他的同享类型，到时再增加相应的标识字段

    @DatabaseField(columnName = "limit_period")
    private int limitPeriod;//是否限制时段

    @DatabaseField(columnName = "start_period")
    private String startPeriod;//控制到分钟，格式：HH:mm

    @DatabaseField(columnName = "end_period")
    private String endPeriod;//控制到分钟，格式：HH:mm

    @DatabaseField(columnName = "limit_week")
    private int limitWeek;//是否限制星期

    @DatabaseField(columnName = "week_day")
    private String weekDay;//星期1，2，3，4，5，6，7

    @DatabaseField(columnName = "stack_rule")
    private int stackRule;//是否叠加

    @DatabaseField(columnName = "apply_crowd")
    private int applyCrowd;//适用人群：1.会员，2.非会员，3.不限，4.人群标签

    @DatabaseField(columnName = "crowd_tag_id")
    private int crowdTagId;//人群标签Id

    @DatabaseField(columnName = "description")
    private String description;//方案描述

    @DatabaseField(columnName = "deleted_flag")
    private int deletedFlag;//0未删除，1已删除

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
        return deletedFlag == 0;//0未删除，1已删除
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
