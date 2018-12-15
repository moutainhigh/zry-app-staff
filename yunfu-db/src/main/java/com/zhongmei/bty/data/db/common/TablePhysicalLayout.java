package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.IsDelete;
import com.zhongmei.bty.entity.enums.LayoutType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * TablePhysicalLayout is a ORMLite bean type. Corresponds to the database table "table_physical_layout"
 */
@DatabaseTable(tableName = "table_physical_layout")
public class TablePhysicalLayout extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "table_physical_layout"
     */
    public interface $ extends IdEntityBase.$ {

        /**
         * area_id
         */
        public static final String areaId = "area_id";

        /**
         * brand_id
         */
        public static final String brandId = "brand_id";

        /**
         * commercial_id
         */
        public static final String commercialId = "commercial_id";

        /**
         * create_time
         */
        public static final String createTime = "create_time";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * floor
         */
        public static final String floor = "floor";

        /**
         * image_id
         */
        public static final String imageId = "image_id";

        /**
         * is_delete
         */
        public static final String isDelete = "is_delete";

        /**
         * layout_type
         */
        public static final String layoutType = "layout_type";

        /**
         * physical_layout_name
         */
        public static final String physicalLayoutName = "physical_layout_name";

        /**
         * publish_status
         */
        public static final String publishStatus = "publish_status";

        /**
         * updater_id
         */
        public static final String updaterId = "updater_id";

        /**
         * update_time
         */
        public static final String updateTime = "update_time";

    }

    @DatabaseField(columnName = "area_id")
    private Long areaId;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "create_time", canBeNull = false)
    private Long createTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "floor")
    private Integer floor;

    @DatabaseField(columnName = "image_id")
    private Long imageId;

    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    @DatabaseField(columnName = "layout_type")
    private Integer layoutType;

    @DatabaseField(columnName = "physical_layout_name", canBeNull = false)
    private String physicalLayoutName;

    @DatabaseField(columnName = "publish_status")
    private Byte publishStatus;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public IsDelete getIsDelete() {
        return ValueEnums.toEnum(IsDelete.class, isDelete);
    }

    public void setIsDelete(IsDelete isDelete) {
        this.isDelete = ValueEnums.toValue(isDelete);
    }

    public LayoutType getLayoutType() {
        return ValueEnums.toEnum(LayoutType.class, layoutType);
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = ValueEnums.toValue(layoutType);
    }

    public String getPhysicalLayoutName() {
        return physicalLayoutName;
    }

    public void setPhysicalLayoutName(String physicalLayoutName) {
        this.physicalLayoutName = physicalLayoutName;
    }

    public Byte getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Byte publishStatus) {
        this.publishStatus = publishStatus;
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
        return ValueEnums.equalsValue(IsDelete.VALID, isDelete);
    }

    @Override
    public Long verValue() {
        return updateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(createTime, physicalLayoutName, updateTime);
    }
}

