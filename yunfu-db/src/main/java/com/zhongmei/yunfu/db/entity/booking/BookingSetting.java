package com.zhongmei.yunfu.db.entity.booking;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.LimitBookingUnit;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;

/**
 * 预订设置
 */
@DatabaseTable(tableName = "booking_setting")
public class BookingSetting extends EntityBase<Long> {

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
         * limit_booking_number
         */
        String limitBookingNumber = "limit_booking_number";

        /**
         * limit_booking_unit
         */
        String limitBookingUnit = "limit_booking_unit";

        /**
         * keep_time
         */
        String keepTime = "keep_time";

        /**
         * limit_service_time
         */
        String limitServiceTime = "limit_service_time";

        /**
         * order_menu
         */
        String orderMenu = "order_menu";

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

    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 是否删除(0 否; 1 是)
     */
    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;

    /**
     * 预订限制数量
     */
    @DatabaseField(columnName = "limit_booking_number")
    private Integer limitBookingNumber;

    /**
     * 预订限制单位
     */
    @DatabaseField(columnName = "limit_booking_unit")
    private Integer limitBookingUnit;

    /**
     * 预定保留时长
     */
    @DatabaseField(columnName = "keep_time")
    private String keepTime;

    /**
     * 平均就餐时间
     */
    @DatabaseField(columnName = "limit_service_time")
    private String limitServiceTime;

    /**
     * 是否预点菜（0开启，1关闭）
     */
    @DatabaseField(columnName = "order_menu")
    private Integer orderMenu;

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

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
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

    public Integer getLimitBookingNumber() {
        return limitBookingNumber;
    }

    public void setLimitBookingNumber(Integer limitBookingNumber) {
        this.limitBookingNumber = limitBookingNumber;
    }

    public LimitBookingUnit getLimitBookingUnit() {
        return ValueEnums.toEnum(LimitBookingUnit.class, limitBookingUnit);
    }

    public void setLimitBookingUnit(LimitBookingUnit limitBookingUnit) {
        this.limitBookingUnit = ValueEnums.toValue(limitBookingUnit);
    }

    public String getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(String keepTime) {
        this.keepTime = keepTime;
    }

    public String getLimitServiceTime() {
        return limitServiceTime;
    }

    public void setLimitServiceTime(String limitServiceTime) {
        this.limitServiceTime = limitServiceTime;
    }

    public Integer getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(Integer orderMenu) {
        this.orderMenu = orderMenu;
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
