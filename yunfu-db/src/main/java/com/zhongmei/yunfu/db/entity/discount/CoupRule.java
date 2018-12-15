package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * CoupRule is a ORMLite bean type. Corresponds to the database table "coup_rule"
 */
@DatabaseTable(tableName = "coup_rule")
public class CoupRule extends EntityBase<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {

        /**
         * id
         */
        String id = "id";

        /**
         * is_delete
         */
        String isDelete = "is_delete";

        /**
         * coupon_id
         */
        String couponId = "coupon_id";

        /**
         * rule_name
         */
        String ruleName = "rule_name";

        /**
         * rule_value
         */
        String ruleValue = "rule_value";

        /**
         * brand_id
         */
        String brandId = "brand_id";

        /**
         * create_id
         */
        String createId = "create_id";

        /**
         * create_time
         */
        String createTime = "create_time";

        /**
         * updator_id
         */
        String updatorId = "updator_id";

        /**
         * update_time
         */
        String updateTime = "update_time";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;

    @DatabaseField(columnName = "coupon_id")
    private Long couponId;

    @DatabaseField(columnName = "rule_name")
    private String ruleName;

    @DatabaseField(columnName = "rule_value")
    private String ruleValue;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "create_id")
    private Long creatorId;

    @DatabaseField(columnName = "create_time")
    private Long createTime;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time")
    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean isValid() {
        return isDelete == 0;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return updateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id, isDelete);
    }
}
