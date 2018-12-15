package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

import java.math.BigDecimal;

/**
 * @Description:
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@DatabaseTable(tableName = "market_activity_rule")
public class MarketActivityRule extends CrmBasicEntityBase implements ICreator, IUpdator {
    /*
     * `id` bigint(20) NOT NULL AUTO_INCREMENT, `brand_id`
     * bigint(20) DEFAULT NULL COMMENT '品牌ID', `plan_id`
     * bigint(20) DEFAULT NULL COMMENT '营销计划ID', `name`
     * varchar(100) DEFAULT NULL COMMENT '优惠条件',
     * `limit_period` tinyint(4) DEFAULT NULL COMMENT
     * '是否限制时段', `period_start` time DEFAULT NULL COMMENT
     * '开始时段', `period_end` time DEFAULT NULL COMMENT
     * '结束时段', `limit_week` tinyint(4) DEFAULT NULL COMMENT
     * '是否限制星期', `week_day` varchar(50) DEFAULT NULL COMMENT
     * '星期1，2，3，4，5，6，7', `content` varchar(200) DEFAULT
     * NULL COMMENT '优惠内容', `all_dish` tinyint(4) DEFAULT
     * NULL COMMENT '是否所有菜品', `discount` decimal(4,1)
     * DEFAULT NULL COMMENT '折扣', `menu_id` bigint(20)
     * DEFAULT NULL COMMENT '特价菜单ID', `payment`
     * decimal(20,0) DEFAULT NULL COMMENT '消费金额', `reduce`
     * decimal(10,2) DEFAULT NULL COMMENT '立减金额',
     * `server_create_time` timestamp NULL DEFAULT NULL ON
     * UPDATE CURRENT_TIMESTAMP COMMENT ' 创建时间',
     * `server_update_time` timestamp NULL DEFAULT NULL ON
     * UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', `creator_id`
     * bigint(20) DEFAULT NULL COMMENT ' 创建者Id',
     * `creator_name` varchar(100) DEFAULT NULL,
     * `updator_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
     * `updator_name` varchar(100) DEFAULT NULL,
     * `status_flag` tinyint(4) DEFAULT '1' COMMENT '
     * dish_num BigDecimal  营销方式为单菜品时存放菜品数量
     * 是否有效标识',
     */

    private static final long serialVersionUID = 1L;

    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String name = "name";

        public static final String limitPeriod = "limit_period";

        public static final String periodStart = "period_start";

        public static final String periodEnd = "period_end";
        //此字段已废
        public static final String limitWeek = "limit_week";

        public static final String allDish = "all_dish";

        public static final String discount = "discount";

        public static final String menuId = "menu_id";

        public static final String payment = "payment";

        public static final String reduce = "reduce";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        public static final String dishNum = "dish_num";

    }

    @DatabaseField(columnName = "plan_id", canBeNull = false)
    private Long planId;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "limit_period")
    private Integer limitPeriod;

    @DatabaseField(columnName = "period_start")
    private String periodStart;

    @DatabaseField(columnName = "period_end")
    private String periodEnd;

    @DatabaseField(columnName = "limit_week")
    private Integer limitWeek;

    @DatabaseField(columnName = "week_day")
    private String weekDay;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "all_dish")
    private Integer allDish;

    @DatabaseField(columnName = "discount")
    private BigDecimal discount;

    @DatabaseField(columnName = "menu_id")
    private Long menuId;

    @DatabaseField(columnName = "payment")
    private BigDecimal payment;

    @DatabaseField(columnName = "reduce")
    private BigDecimal reduce;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "dish_num")
    private BigDecimal dishNum;

    @DatabaseField(foreign = true, foreignColumnName = MarketPlan.$.id)
    private MarketPlan marketPlan;

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {

        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLimitPeriod() {
        return limitPeriod;
    }

    public void setLimitPeriod(Integer limitPeriod) {
        this.limitPeriod = limitPeriod;
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

    public Integer getLimitWeek() {
        return limitWeek;
    }

    public void setLimitWeek(Integer limitWeek) {
        this.limitWeek = limitWeek;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAllDish() {
        return allDish;
    }

    public void setAllDish(Integer allDish) {
        this.allDish = allDish;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }

    public MarketPlan getMarketPlan() {
        return marketPlan;
    }

    public void setMarketPlan(MarketPlan marketPlan) {
        this.marketPlan = marketPlan;
    }

    public BigDecimal getDishNum() {
        return dishNum;
    }

    public void setDishNum(BigDecimal dishNum) {
        this.dishNum = dishNum;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(planId);
    }
}
