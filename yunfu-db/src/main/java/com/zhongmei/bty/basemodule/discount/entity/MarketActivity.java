package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.ActivityType;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;


@DatabaseTable(tableName = "market_activity")
public class MarketActivity extends CrmBasicEntityBase {


    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String promotionType = "promotion_type";

        public static final String activityType = "activity_type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


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
