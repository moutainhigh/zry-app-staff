package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

/**
 * Model class of 营销方案基本信息表,由于遗留问题，此表的 status_flag无用
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "market_plan")
public class MarketPlan extends IdEntityBase {

    /**
     * @date：2016年5月19日 下午1:39:26
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    public static interface $ extends IdEntityBase.$ {

        public static final String planName = "plan_name";

        public static final String panTypeId = "pan_type_id";

        public static final String planDesc = "plan_desc";

        public static final String isShare = "is_share";

        public static final String planStartDay = "plan_start_day";

        public static final String planEndDay = "plan_end_day";

        public static final String planType = "plan_type";

        public static final String planStatus = "plan_status";

        public static final String planNum = "plan_num";

        public static final String couponSentAmount = "coupon_sent_amount";

        public static final String forecastPersonAmount = "forecast_person_amount";

        public static final String isDelete = "is_delete";

        public static final String isHandSend = "is_hand_send";

        public static final String marketTemplateType = "market_template_type";

        public static final String specialMarketType = "special_market_type";

        public static final String lotteryAmount = "lottery_amount";

        public static final String brandId = "brand_id";

        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

        /**
         * server_create_time
         */
        public static final String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        public static final String serverUpdateTime = "server_update_time";

    }

    /**
     * 品牌id.
     */
    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 方案名称.
     */
    @DatabaseField(columnName = "plan_name")
    private String planName;

    /**
     * 方案类别ID.
     */
    @DatabaseField(columnName = "pan_type_id")
    private Long panTypeId;

    /**
     * 方案描述.
     */
    @DatabaseField(columnName = "plan_desc")
    private String planDesc;

    /**
     * 是否共用.
     */
    @DatabaseField(columnName = "is_share")
    private Integer isShare;

    /**
     * 营销起始日.
     */
    @DatabaseField(columnName = "plan_start_day")
    private String planStartDay;

    /**
     * 营销终止日.
     */
    @DatabaseField(columnName = "plan_end_day")
    private String planEndDay;

    /**
     * 营销方式.
     */
    @DatabaseField(columnName = "plan_type")
    private Integer planType;

    /**
     * 方案状态.
     */
    @DatabaseField(columnName = "plan_status")
    private Integer planStatus;

    /**
     * 活动号.
     */
    @DatabaseField(columnName = "plan_num")
    private Integer planNum;

    /**
     * 券已发数量.
     */
    @DatabaseField(columnName = "coupon_sent_amount")
    private Integer couponSentAmount;

    /**
     * 预计参与人数.
     */
    @DatabaseField(columnName = "forecast_person_amount")
    private Integer forecastPersonAmount;

    /**
     * 是否删除.
     */
    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    /**
     * 是否人工发放.
     */
    @DatabaseField(columnName = "is_hand_send")
    private Integer isHandSend;

    /**
     * 营销模板类型.
     */
    @DatabaseField(columnName = "market_template_type")
    private Integer marketTemplateType;

    /**
     * 专项营销类型.
     */
    @DatabaseField(columnName = "special_market_type")
    private Integer specialMarketType;

    /**
     * 已抽奖次数.
     */
    @DatabaseField(columnName = "lottery_amount")
    private Integer lotteryAmount;

    /**
     * 创建者id.
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者名称.
     */
    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 更新者id.
     */
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    /**
     * 更新人名称.
     */
    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    /**
     * Set the 方案名称.
     *
     * @param planName 方案名称
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * Get the 方案名称.
     *
     * @return 方案名称
     */
    public String getPlanName() {
        return this.planName;
    }

    /**
     * Set the 方案类别ID.
     *
     * @param panTypeId 方案类别ID
     */
    public void setPanTypeId(Long panTypeId) {
        this.panTypeId = panTypeId;
    }

    /**
     * Get the 方案类别ID.
     *
     * @return 方案类别ID
     */
    public Long getPanTypeId() {
        return this.panTypeId;
    }

    /**
     * Set the 方案描述.
     *
     * @param planDesc 方案描述
     */
    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }

    /**
     * Get the 方案描述.
     *
     * @return 方案描述
     */
    public String getPlanDesc() {
        return this.planDesc;
    }

    /**
     * Set the 活动号.
     *
     * @param planNum 活动号
     */
    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    /**
     * Get the 活动号.
     *
     * @return 活动号
     */
    public Integer getPlanNum() {
        return this.planNum;
    }

    /**
     * Set the 券已发数量.
     *
     * @param couponSentAmount 券已发数量
     */
    public void setCouponSentAmount(Integer couponSentAmount) {
        this.couponSentAmount = couponSentAmount;
    }

    /**
     * Get the 券已发数量.
     *
     * @return 券已发数量
     */
    public Integer getCouponSentAmount() {
        return this.couponSentAmount;
    }

    /**
     * Set the 预计参与人数.
     *
     * @param forecastPersonAmount 预计参与人数
     */
    public void setForecastPersonAmount(Integer forecastPersonAmount) {
        this.forecastPersonAmount = forecastPersonAmount;
    }

    /**
     * Get the 预计参与人数.
     *
     * @return 预计参与人数
     */
    public Integer getForecastPersonAmount() {
        return this.forecastPersonAmount;
    }

    /**
     * Set the 已抽奖次数.
     *
     * @param lotteryAmount 已抽奖次数
     */
    public void setLotteryAmount(Integer lotteryAmount) {
        this.lotteryAmount = lotteryAmount;
    }

    /**
     * Get the 已抽奖次数.
     *
     * @return 已抽奖次数
     */
    public Integer getLotteryAmount() {
        return this.lotteryAmount;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public Integer getIsShare() {
        return isShare;
    }

    public void setIsShare(Integer isShare) {
        this.isShare = isShare;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsHandSend() {
        return isHandSend;
    }

    public void setIsHandSend(Integer isHandSend) {
        this.isHandSend = isHandSend;
    }

    public Integer getMarketTemplateType() {
        return marketTemplateType;
    }

    public void setMarketTemplateType(Integer marketTemplateType) {
        this.marketTemplateType = marketTemplateType;
    }

    public Integer getSpecialMarketType() {
        return specialMarketType;
    }

    public void setSpecialMarketType(Integer specialMarketType) {
        this.specialMarketType = specialMarketType;
    }

    public String getPlanStartDay() {
        return planStartDay;
    }

    public void setPlanStartDay(String planStartDay) {
        this.planStartDay = planStartDay;
    }

    public String getPlanEndDay() {
        return planEndDay;
    }

    public void setPlanEndDay(String planEndDay) {
        this.planEndDay = planEndDay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MarketPlan other = (MarketPlan) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        if (isDelete == null)
            return false;
        return isDelete == 0;
    }

    @Override
    public Long verValue() {
        // TODO Auto-generated method stub
        return serverUpdateTime;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

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

}
