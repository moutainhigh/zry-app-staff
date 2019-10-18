package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;


@DatabaseTable(tableName = "market_plan")
public class MarketPlan extends IdEntityBase {


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


        public static final String serverCreateTime = "server_create_time";


        public static final String serverUpdateTime = "server_update_time";

    }


    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "plan_name")
    private String planName;


    @DatabaseField(columnName = "pan_type_id")
    private Long panTypeId;


    @DatabaseField(columnName = "plan_desc")
    private String planDesc;


    @DatabaseField(columnName = "is_share")
    private Integer isShare;


    @DatabaseField(columnName = "plan_start_day")
    private String planStartDay;


    @DatabaseField(columnName = "plan_end_day")
    private String planEndDay;


    @DatabaseField(columnName = "plan_type")
    private Integer planType;


    @DatabaseField(columnName = "plan_status")
    private Integer planStatus;


    @DatabaseField(columnName = "plan_num")
    private Integer planNum;


    @DatabaseField(columnName = "coupon_sent_amount")
    private Integer couponSentAmount;


    @DatabaseField(columnName = "forecast_person_amount")
    private Integer forecastPersonAmount;


    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;


    @DatabaseField(columnName = "is_hand_send")
    private Integer isHandSend;


    @DatabaseField(columnName = "market_template_type")
    private Integer marketTemplateType;


    @DatabaseField(columnName = "special_market_type")
    private Integer specialMarketType;


    @DatabaseField(columnName = "lottery_amount")
    private Integer lotteryAmount;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    public void setPlanName(String planName) {
        this.planName = planName;
    }


    public String getPlanName() {
        return this.planName;
    }


    public void setPanTypeId(Long panTypeId) {
        this.panTypeId = panTypeId;
    }


    public Long getPanTypeId() {
        return this.panTypeId;
    }


    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }


    public String getPlanDesc() {
        return this.planDesc;
    }


    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }


    public Integer getPlanNum() {
        return this.planNum;
    }


    public void setCouponSentAmount(Integer couponSentAmount) {
        this.couponSentAmount = couponSentAmount;
    }


    public Integer getCouponSentAmount() {
        return this.couponSentAmount;
    }


    public void setForecastPersonAmount(Integer forecastPersonAmount) {
        this.forecastPersonAmount = forecastPersonAmount;
    }


    public Integer getForecastPersonAmount() {
        return this.forecastPersonAmount;
    }


    public void setLotteryAmount(Integer lotteryAmount) {
        this.lotteryAmount = lotteryAmount;
    }


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


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


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
                if (isDelete == null)
            return false;
        return isDelete == 0;
    }

    @Override
    public Long verValue() {
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
