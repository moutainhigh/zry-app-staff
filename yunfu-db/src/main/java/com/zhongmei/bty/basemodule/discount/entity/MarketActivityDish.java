package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.database.entity.base.ServerBrandIdEntityBase;

import java.io.Serializable;
import java.math.BigDecimal;


@DatabaseTable(tableName = "market_activity_dish")
public class MarketActivityDish extends ServerBrandIdEntityBase implements Serializable {


    private static final long serialVersionUID = 1L;

    public final static int MARKET_ACTIVITY_DISH = 1;    public final static int MARKET_ACTIVITY_DISH_TYPE = 2;
    public static interface $ extends ServerBrandIdEntityBase.$ {

        public static final String planId = "plan_id";

        public static final String ruleId = "rule_id";

        public static final String type = "type";

        public static final String dishId = "dish_id";

        public static final String dishName = "dish_name";

        public static final String dishNum = "dish_num";

    }


    @DatabaseField(columnName = "plan_id")
    private Long planId;


    @DatabaseField(columnName = "rule_id")
    private Long ruleId;


    @DatabaseField(columnName = "type")
    private Integer type;


    @DatabaseField(columnName = "dish_id")
    private Long dishId;


    @DatabaseField(columnName = "dish_name")
    private String dishName;


    @DatabaseField(columnName = "dish_num")
    private BigDecimal dishNum;


    public MarketActivityDish() {
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


    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }


    public Long getDishId() {
        return this.dishId;
    }


    public void setDishName(String dishName) {
        this.dishName = dishName;
    }


    public String getDishName() {
        return this.dishName;
    }

    public BigDecimal getDishNum() {
        return dishNum;
    }

    public void setDishNum(BigDecimal dishNum) {
        this.dishNum = dishNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        MarketActivityDish other = (MarketActivityDish) obj;
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
