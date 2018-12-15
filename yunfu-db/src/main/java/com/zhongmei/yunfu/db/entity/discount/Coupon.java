package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;

/**
 * Coupon is a ORMLite bean type. Corresponds to the database table "coupon"
 */
@DatabaseTable(tableName = "coupon")
public class Coupon extends IdEntityBase {


    public interface $ {
        public final String applyDish = "apply_dish";
        public final String content = "content";
        public final String couponState = "coupon_state";
        public final String couponType = "coupon_type";
        public final String discountValue = "discount_value";
        public final String endTime = "end_time";
        public final String fullValue = "full_value";
        public final String name = "name";
        public final String remark = "remark";
        public final String dishId = "dish_id";
        public final String dishName = "dish_name";
        /**
         * status_flag
         */
        String statusFlag = "status_flag";

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";
        /**
         * id
         */
        String id = "id";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        String deviceIdenty = "device_identy";
    }

    @DatabaseField(columnName = "apply_dish")
    private Integer applyDish;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "coupon_state")
    private Integer couponState;

    @DatabaseField(columnName = "coupon_type")
    private Integer couponType;

    @DatabaseField(columnName = "discount_value")
    private BigDecimal discountValue;

    @DatabaseField(columnName = "end_time")
    private Long endTime;

    @DatabaseField(columnName = "full_value")
    private BigDecimal fullValue;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "remark")
    private String remark;

    @DatabaseField(columnName = "dish_id")
    private Long dishId;

    @DatabaseField(columnName = "dish_name")
    private String dishName;

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    /**
     * 品牌Identy
     */
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    /**
     * 门店Identy
     */
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    /**
     * 设备Identy
     */
    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    public Integer getApplyDish() {
        return applyDish;
    }

    public void setApplyDish(Integer applyDish) {
        this.applyDish = applyDish;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCouponState() {
        return couponState;
    }

    public void setCouponState(Integer couponState) {
        this.couponState = couponState;
    }

    public CouponType getCouponType() {
        return ValueEnums.toEnum(CouponType.class, couponType);
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = ValueEnums.toValue(couponType);
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getFullValue() {
        return fullValue;
    }

    public void setFullValue(BigDecimal fullValue) {
        this.fullValue = fullValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

}
