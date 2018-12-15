package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * 订单来源
 */
@DatabaseTable(tableName = "commercial_order_source")
public class CommercialOrderSource extends EntityBase<Long> {

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
         * commercial_id
         */
        String commercialId = "commercial_id";

        /**
         * brand_id
         */
        String brandId = "brand_id";

        /**
         * is_delete
         */
        String source = "source";

        /**
         * is_delete
         */
        String isDelete = "is_delete";

        /**
         * creator_id
         */
        String creatorId = "creator_id";

        /**
         * updateTime
         */
        String updateTime = "update_ime";

        /**
         * create_time
         */
        String createTime = "create_time";

        /**
         * updater_d
         */
        String updaterId = "updater_d";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;

    /**
     * 商户id
     */
    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 是否删除(0 否; -1 是)
     */
    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;

    /**
     * 来源
     */
    @DatabaseField(columnName = "source")
    private Integer source;

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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
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
