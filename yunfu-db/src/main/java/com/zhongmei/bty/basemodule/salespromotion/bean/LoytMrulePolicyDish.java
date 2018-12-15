package com.zhongmei.bty.basemodule.salespromotion.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

@DatabaseTable(tableName = "loyt_mrule_policy_dish")
public class LoytMrulePolicyDish extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "loyt_mrule_policy_dish"
     */
    public interface $ extends IdEntityBase.$ {

        public static final String brandId = "brand_id";
        public static final String planId = "plan_id";
        public static final String dishType = "dish_type";
        public static final String midDishTypeId = "mid_dish_type_id";
        public static final String unitName = "unit_name";
        public static final String price = "price";
        public static final String dishId = "dish_id";
        public static final String dishName = "dish_name";
        public static final String validFlag = "valid_flag";

        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";
        public static final String creatorId = "creator_id";
        public static final String updaterId = "updater_id";

    }

    @DatabaseField(columnName = "brand_id")
    private Long brandId;//商户Id

    @DatabaseField(columnName = "plan_id")
    private Long planId;//方案Id

    @DatabaseField(columnName = "dish_type")
    private int dishType; //1-子菜品，2-中类菜品

    @DatabaseField(columnName = "mid_dish_type_id")
    private Long midDishTypeId;

    @DatabaseField(columnName = "unit_name")
    private String unitName;

    @DatabaseField(columnName = "price")
    private BigDecimal price;

    @DatabaseField(columnName = "dish_id")
    private Long dishId;//菜品id

    @DatabaseField(columnName = "dish_name")
    private String dishName;//是否有效 0无效，1有效

    @DatabaseField(columnName = "valid_flag")
    private int validFlag;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updater_id")
    private Long updaterId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public int getDishType() {
        return dishType;
    }

    public void setDishType(int dishType) {
        this.dishType = dishType;
    }

    public Long getMidDishTypeId() {
        return midDishTypeId;
    }

    public void setMidDishTypeId(Long midDishTypeId) {
        this.midDishTypeId = midDishTypeId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public int getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(int validFlag) {
        this.validFlag = validFlag;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    public boolean isValid() {
        return validFlag == 1;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
