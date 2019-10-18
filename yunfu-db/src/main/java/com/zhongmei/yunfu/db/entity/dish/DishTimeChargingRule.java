package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.DataEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "dish_time_charging_rule")
public class DishTimeChargingRule extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {
        public static final String dishId = "dish_id";

        public static final String startChargingTimes = "start_charging_times";

        public static final String startChargingPrice = "start_charging_price";

        public static final String chargingUnit = "charging_unit";

        public static final String chargingPrice = "charging_price";

        public static final String fullUnit = "full_unit";

        public static final String fullUnitCharging = "full_unit_charging";

        public static final String no_full_unit = "no_full_unit";

        public static final String noFullUnitCharging = "no_full_unit_charging";

        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "dish_id")
    private Long dishId;
    @DatabaseField(columnName = "start_charging_times")
    private BigDecimal startChargingTimes;     @DatabaseField(columnName = "start_charging_price")
    private BigDecimal startChargingPrice;
    @DatabaseField(columnName = "charging_unit")
    private BigDecimal chargingUnit;     @DatabaseField(columnName = "charging_price")
    private BigDecimal chargingPrice;
    @DatabaseField(columnName = "full_unit")
    private BigDecimal fullUnit;    @DatabaseField(columnName = "full_unit_charging")
    private BigDecimal fullUnitCharging;
    @DatabaseField(columnName = "no_full_unit")
    private BigDecimal noFullUnit;    @DatabaseField(columnName = "no_full_unit_charging")
    private BigDecimal noFullUnitCharging;
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;
    @DatabaseField(columnName = "creator_name")
    private String creatorName;
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;
    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public BigDecimal getStartChargingTimes() {
        return startChargingTimes;
    }

    public void setStartChargingTimes(BigDecimal startChargingTimes) {
        this.startChargingTimes = startChargingTimes;
    }

    public BigDecimal getStartChargingPrice() {
        return startChargingPrice;
    }

    public void setStartChargingPrice(BigDecimal startChargingPrice) {
        this.startChargingPrice = startChargingPrice;
    }

    public BigDecimal getChargingUnit() {
        return chargingUnit;
    }

    public void setChargingUnit(BigDecimal chargingUnit) {
        this.chargingUnit = chargingUnit;
    }

    public BigDecimal getChargingPrice() {
        return chargingPrice;
    }

    public void setChargingPrice(BigDecimal chargingPrice) {
        this.chargingPrice = chargingPrice;
    }

    public BigDecimal getFullUnit() {
        return fullUnit;
    }

    public void setFullUnit(BigDecimal fullUnit) {
        this.fullUnit = fullUnit;
    }

    public BigDecimal getFullUnitCharging() {
        return fullUnitCharging;
    }

    public void setFullUnitCharging(BigDecimal fullUnitCharging) {
        this.fullUnitCharging = fullUnitCharging;
    }

    public BigDecimal getNoFullUnit() {
        return noFullUnit;
    }

    public void setNoFullUnit(BigDecimal noFullUnit) {
        this.noFullUnit = noFullUnit;
    }

    public BigDecimal getNoFullUnitCharging() {
        return noFullUnitCharging;
    }

    public void setNoFullUnitCharging(BigDecimal noFullUnitCharging) {
        this.noFullUnitCharging = noFullUnitCharging;
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
}
