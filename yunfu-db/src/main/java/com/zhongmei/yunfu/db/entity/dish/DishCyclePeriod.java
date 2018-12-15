package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.DayNumber;

/**
 * DishCyclePeriod is a ORMLite bean type. Corresponds to the database table
 * "dish_cycle_period"
 */
@DatabaseTable(tableName = "dish_cycle_period")
public class DishCyclePeriod extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "auth_user"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * dish_id
         */
        public static final String dishId = "dish_id";

        /**
         * day_number
         */
        public static final String dayNumber = "day_number";

        /**
         * validity_start
         */
        public static final String validityStart = "validity_start";

        /**
         * validity_end
         */
        public static final String validityEnd = "validity_end";

        /**
         * period_start
         */
        public static final String periodStart = "period_start";

        /**
         * period_end
         */
        public static final String periodEnd = "period_end";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "dish_id", canBeNull = false)
    private Long dishId;

    @DatabaseField(columnName = "day_number", canBeNull = false)
    private Integer dayNumber;

    @DatabaseField(columnName = "validity_start", canBeNull = false)
    private String validityStart;

    @DatabaseField(columnName = "validity_end", canBeNull = false)
    private String validityEnd;

    @DatabaseField(columnName = "period_start")
    private String periodStart;

    @DatabaseField(columnName = "period_end")
    private String periodEnd;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

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

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public DayNumber getDayNumber() {
        return ValueEnums.toEnum(DayNumber.class, dayNumber);
    }

    public void setDayNumber(DayNumber dayNumber) {
        this.dayNumber = ValueEnums.toValue(dayNumber);
    }

    public String getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(String validityStart) {
        this.validityStart = validityStart;
    }

    public String getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(String validityEnd) {
        this.validityEnd = validityEnd;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(dishId, dayNumber, validityStart, validityEnd);
    }
}
