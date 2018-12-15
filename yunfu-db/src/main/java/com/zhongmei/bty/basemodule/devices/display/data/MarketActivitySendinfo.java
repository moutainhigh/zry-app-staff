package com.zhongmei.bty.basemodule.devices.display.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.UserEntityBase;

/**
 * Model class of 第二屏营销图片表.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "market_activity_sendinfo")
public class MarketActivitySendinfo extends UserEntityBase implements Serializable {

    /**
     * serialVersionUID.
     */
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

        // `market_plan_type` tinyint(4) DEFAULT NULL
        // COMMENT '1, 预订时间营销, 2, 预订渠道营销, 3, 预订频次营销,\r\n 4,
        // 排队渠道营销,5, 排队频次营销, 6, 排队时间营销, 7, 新会员招募,\r\n8,
        // 生日营销, 9,
        // 外卖渠道营销,10,消费满额赠券,11,消费满额立减,\r\n12,储值赠券活动,13,商品类营销,14,关怀短信,15,"抽奖活动"),',
    }

    /**
     * 品牌id.
     */
    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 营销方案id.
     */
    @DatabaseField(columnName = "plan_id")
    private Long planId;

    /**
     * pos/自助点餐.1、pos 2、自助
     */
    @DatabaseField(columnName = "target")
    private Integer target;

    /**
     * 开始时间.
     */
    @DatabaseField(columnName = "startDay")
    private Long startDay;

    /**
     * 截止日期.
     */
    @DatabaseField(columnName = "endDay")
    private Long endDay;

    /**
     * 图片url.
     */
    @DatabaseField(columnName = "url")
    private String url;

    /**
     * sys_file表Id.
     */
    @DatabaseField(columnName = "sys_file_id")
    private Long sysFileId;

    // 图片优先顺序
    @DatabaseField(columnName = "priority")
    private Integer priority;

    // 图片是否暂时 1、展示，2、不展示
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
