package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.enums.CommercialType;


@DatabaseTable(tableName = "market_plan_commercial_rel")
public class MarketPlanCommercialRel extends CrmBasicEntityBase {
    private static final long serialVersionUID = 1L;



    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String commercialId = "commercial_id";

        public static final String commercialType = "commercial_type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";

    }

    @DatabaseField(columnName = "plan_id", canBeNull = false)
    private Long planId;

    @DatabaseField(columnName = "commercial_id", canBeNull = false)
    private Long commercialId;

    @DatabaseField(columnName = "commercial_type")
    private Integer commercialType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

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

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public CommercialType getCommercialType() {

        return ValueEnums.toEnum(CommercialType.class, commercialType);
    }

    public void setCommercialType(CommercialType commercialType) {
        this.commercialType = ValueEnums.toValue(commercialType);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(planId, commercialId);
    }
}
