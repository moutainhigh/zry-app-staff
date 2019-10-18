package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

import java.math.BigDecimal;


@DatabaseTable(tableName = "market_activity_rule")
public class MarketActivityRule extends CrmBasicEntityBase implements ICreator, IUpdator {


    private static final long serialVersionUID = 1L;

    public interface $ extends CrmBasicEntityBase.$ {
        public static final String planId = "plan_id";

        public static final String name = "name";

        public static final String limitPeriod = "limit_period";

        public static final String periodStart = "period_start";

        public static final String periodEnd = "period_end";
                public static final String limitWeek = "limit_week";

        public static final String allDish = "all_dish";

        public static final String discount = "discount";

        public static final String menuId = "menu_id";

        public static final String payment = "payment";

        public static final String reduce = "reduce";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


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
