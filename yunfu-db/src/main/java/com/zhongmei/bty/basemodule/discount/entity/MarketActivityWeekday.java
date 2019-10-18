package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.database.entity.base.ServerBrandIdEntityBase;


@DatabaseTable(tableName = "market_activity_weekday")
public class MarketActivityWeekday extends ServerBrandIdEntityBase {

    public static interface $ extends ServerBrandIdEntityBase.$ {

        public static final String planId = "plan_id";

        public static final String ruleId = "rule_id";

        public static final String weekday = "weekday";
    }


    @DatabaseField(columnName = "plan_id", canBeNull = false)
    private Long planId;


    @DatabaseField(columnName = "rule_id")
    private Long ruleId;


    @DatabaseField(columnName = "weekday")
    private Integer weekday;


    public MarketActivityWeekday() {
    }


    public void setPlanId(Long planId) {
        this.planId = planId;
    }


    public Long getPlanId() {
        return this.planId;
    }


    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }


    public Long getRuleId() {
        return this.ruleId;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MarketActivityWeekday other = (MarketActivityWeekday) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
