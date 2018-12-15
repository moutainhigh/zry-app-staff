package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.IsDelete;
import com.zhongmei.bty.entity.enums.LayoutType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * TablePosition is a ORMLite bean type. Corresponds to the database table "table_position"
 */
@DatabaseTable(tableName = "table_position")
public class TablePosition extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "table_position"
     */
    public interface $ extends IdEntityBase.$ {

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
         * height
         */
        public static final String height = "height";

        /**
         * is_delete
         */
        public static final String isDelete = "is_delete";

        /**
         * layout_id
         */
        public static final String layoutId = "layout_id";

        /**
         * layout_type
         */
        public static final String layoutType = "layout_type";

        /**
         * table_id
         */
        public static final String tableId = "table_id";

        /**
         * updater_id
         */
        public static final String updaterId = "updater_id";

        /**
         * update_time
         */
        public static final String updateTime = "update_time";

        /**
         * width
         */
        public static final String width = "width";

        /**
         * x_axis
         */
        public static final String xAxis = "x_axis";

        /**
         * y_axis
         */
        public static final String yAxis = "y_axis";

    }

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "create_time", canBeNull = false)
    private Long createTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "height")
    private Integer height;

    @DatabaseField(columnName = "is_delete")
    private Integer isDelete;

    @DatabaseField(columnName = "layout_id")
    private Long layoutId;

    @DatabaseField(columnName = "layout_type")
    private Integer layoutType;

    @DatabaseField(columnName = "table_id")
    private Long tableId;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;

    @DatabaseField(columnName = "width")
    private Integer width;

    @DatabaseField(columnName = "x_axis")
    private Integer xAxis;

    @DatabaseField(columnName = "y_axis")
    private Integer yAxis;

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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public IsDelete getIsDelete() {
        return ValueEnums.toEnum(IsDelete.class, isDelete);
    }

    public void setIsDelete(IsDelete isDelete) {
        this.isDelete = ValueEnums.toValue(isDelete);
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public LayoutType getLayoutType() {
        return ValueEnums.toEnum(LayoutType.class, layoutType);
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = ValueEnums.toValue(layoutType);
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getyAxis() {
        return yAxis;
    }

    public void setyAxis(Integer yAxis) {
        this.yAxis = yAxis;
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
        return super.checkNonNull() && checkNonNull(createTime, layoutId, tableId, updateTime);
    }
}

