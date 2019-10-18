package com.zhongmei.bty.basemodule.devices.display.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;


@DatabaseTable(tableName = "market_activity_sendinfo")
public class MarketActivitySendinfo extends UserEntityBase implements Serializable {


    private static final long serialVersionUID = 1L;

    public interface $ extends UserEntityBase.$ {
        public static final String brandId = "brand_id";

        public static final String planId = "plan_id";

        public static final String target = "target";

        public static final String startDay = "startDay";

        public static final String endDay = "endDay";

        public static final String url = "url";

        public static final String sysFileId = "sys_file_id";

        public static final String priority = "priority";

        public static final String isShow = "is_show";

        public static final String marketPlanType = "market_plan_type";

                                            }


    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "plan_id")
    private Long planId;


    @DatabaseField(columnName = "target")
    private Integer target;


    @DatabaseField(columnName = "startDay")
    private Long startDay;


    @DatabaseField(columnName = "endDay")
    private Long endDay;


    @DatabaseField(columnName = "url")
    private String url;


    @DatabaseField(columnName = "sys_file_id")
    private Long sysFileId;

        @DatabaseField(columnName = "priority")
    private Integer priority;

        @DatabaseField(columnName = "is_show")
    private Integer isShow;

    @DatabaseField(columnName = "market_plan_type")
    private Integer marketPlanType;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Long getStartDay() {
        return startDay;
    }

    public void setStartDay(Long startDay) {
        this.startDay = startDay;
    }

    public Long getEndDay() {
        return endDay;
    }

    public void setEndDay(Long endDay) {
        this.endDay = endDay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSysFileId() {
        return sysFileId;
    }

    public void setSysFileId(Long sysFileId) {
        this.sysFileId = sysFileId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getMarketPlanType() {
        return marketPlanType;
    }

    public void setMarketPlanType(Integer marketPlanType) {
        this.marketPlanType = marketPlanType;
    }

}
