package com.zhongmei.yunfu.db.entity.booking;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.LimitBookingUnit;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "booking_setting")
public class BookingSetting extends EntityBase<Long> {


    private static final long serialVersionUID = 1L;

    public interface $ {


        String id = "id";


        String commercialId = "commercial_id";


        String brandId = "brand_id";


        String limitBookingNumber = "limit_booking_number";


        String limitBookingUnit = "limit_booking_unit";


        String keepTime = "keep_time";


        String limitServiceTime = "limit_service_time";


        String orderMenu = "order_menu";


        String isDelete = "is_delete";


        String creatorId = "creator_id";


        String updateTime = "update_ime";


        String createTime = "create_time";


        String updaterId = "updater_d";

    }

    @DatabaseField(columnName = "id", id = true, canBeNull = false)
    private Long id;


    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "is_delete", canBeNull = false)
    private Integer isDelete;


    @DatabaseField(columnName = "limit_booking_number")
    private Integer limitBookingNumber;


    @DatabaseField(columnName = "limit_booking_unit")
    private Integer limitBookingUnit;


    @DatabaseField(columnName = "keep_time")
    private String keepTime;


    @DatabaseField(columnName = "limit_service_time")
    private String limitServiceTime;


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
