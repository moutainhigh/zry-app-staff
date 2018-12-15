package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;

/**
 * @Date：2016-4-21 下午4:56:35
 * @Description:
 * @Version: 1.0
 */
@DatabaseTable(tableName = "market_activity")
public class MarketActivity extends CrmBasicEntityBase {
	 /*`id` bigint(20) NOT NULL AUTO_INCREMENT,
	  `plan_id` bigint(20) DEFAULT NULL COMMENT '营销计划Id',
	  `promotion_type` tinyint(4) DEFAULT NULL COMMENT '优惠类型',  (立减、满减等方式)
	  `activity_type` tinyint(4) DEFAULT NULL COMMENT '营销类型',  1单商品，2多商品
	  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌Id',
	  `status_flag` tinyint(4) DEFAULT '1' COMMENT '是否有效',
	  `server_create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
	  `server_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
	  `creator_name` varchar(100) DEFAULT NULL,
	  `updator_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
	  `updator_name` varchar(100) DEFAULT NULL,*/

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "market_plan_commercial_rel"
     */
    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String promotionType = "promotion_type";

        public static final String activityType = "activity_type";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

    }

    @DatabaseField(columnName = "plan_id")
    private Long planId;

    @DatabaseField(columnName = "promotion_type")
    private Integer promotionType;

    @DatabaseField(columnName = "activity_type")
    private Integer activityType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
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

    public PromotionType getPromotionType() {
        return ValueEnums.toEnum(PromotionType.class, promotionType);
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = ValueEnums.toValue(promotionType);
    }

    public ActivityType getActivityType() {
        return ValueEnums.toEnum(ActivityType.class, activityType);
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = ValueEnums.toValue(activityType);
    }
}
