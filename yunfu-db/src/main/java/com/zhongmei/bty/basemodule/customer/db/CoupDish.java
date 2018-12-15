package com.zhongmei.bty.basemodule.customer.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * @Date： 16/8/8
 * @Description:
 * @Version: 1.0
 */

@DatabaseTable(tableName = "coup_dish")
public class CoupDish extends EntityBase<Long> {

    private static final long serialVersionUID = 1L;

    public interface $ {

        /**
         * id
         */
        String id = "id";

        /**
         * coupon_id
         */
        String couponId = "coupon_id";

        /**
         * dish_id
         */
        String dishId = "dish_id";

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

        /**
         * is_delete
         */
        String isDelete = "is_delete";

        /**
         * dish_type_id
         */
        String dishTypeId = "dish_type_id";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    @DatabaseField(columnName = "coupon_id")
    private Long couponId;

    @DatabaseField(columnName = "dish_id")
    private Long dishId;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "create_time")
    private Long createTime;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time")
    private Long updateTime;

    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;

    @DatabaseField(columnName = "dish_type_id")
    private Long dishTypeId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Long getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Long dishTypeId) {
        this.dishTypeId = dishTypeId;
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
