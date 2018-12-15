package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.enums.CommercialType;

/**
 * @Date：2016-4-21 下午4:05:42
 * @Description:
 * @Version: 1.0
 */
@DatabaseTable(tableName = "market_plan_commercial_rel")
public class MarketPlanCommercialRel extends CrmBasicEntityBase {
    private static final long serialVersionUID = 1L;
	 /*`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	  `plan_id` bigint(20) NOT NULL COMMENT '营销方案id',
	  `commercial_id` bigint(20) NOT NULL COMMENT '商户id',
	  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
	  `commercial_type` tinyint(10) DEFAULT NULL COMMENT '营销方案与商户关联类型（1、用券门店 2、消费门店 3、升级会员门店、4、会员加入门店）',
	  `status_flag` tinyint(4) DEFAULT NULL COMMENT '状态标识 1:有效 2:无效',
	  `creator_id` bigint(20) NOT NULL COMMENT '创建者id',
	  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建者名称',
	  `updator_id` bigint(20) NOT NULL COMMENT '更新者id',
	  `updator_name` varchar(50) DEFAULT NULL COMMENT '最后修改者姓名',
	  `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务器创建时间',
	  `server_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '服务器更新时间',
*/

    /**
     * The columns of table "market_plan_commercial_rel"
     */
    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String commercialId = "commercial_id";

        public static final String commercialType = "commercial_type";

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
