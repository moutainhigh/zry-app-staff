package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.database.entity.base.ServerBrandIdEntityBase;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Model class of 商品类营销菜品表.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "market_activity_dish")
public class MarketActivityDish extends ServerBrandIdEntityBase implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public final static int MARKET_ACTIVITY_DISH = 1;//菜品
    public final static int MARKET_ACTIVITY_DISH_TYPE = 2;//中类

    public static interface $ extends ServerBrandIdEntityBase.$ {

        public static final String planId = "plan_id";

        public static final String ruleId = "rule_id";

        public static final String type = "type";

        public static final String dishId = "dish_id";

        public static final String dishName = "dish_name";

        public static final String dishNum = "dish_num";

    }

    /**
     * 方案id.
     */
    @DatabaseField(columnName = "plan_id")
    private Long planId;

    /**
     * 规则id.
     */
    @DatabaseField(columnName = "rule_id")
    private Long ruleId;

    /**
     * 类型（1-菜品，2-中类）.
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 品牌菜品id.
     */
    @DatabaseField(columnName = "dish_id")
    private Long dishId;

    /**
     * 菜品名称.
     */
    @DatabaseField(columnName = "dish_name")
    private String dishName;

    /**
     * 菜品数量.
     */
    @DatabaseField(columnName = "dish_num")
    private BigDecimal dishNum;

    /**
     * Constructor.
     */
    public MarketActivityDish() {
    }

    /**
     * Set the 方案id.
     *
     * @param planId 方案id
     */
    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    /**
     * Get the 方案id.
     *
     * @return 方案id
     */
    public Long getPlanId() {
        return this.planId;
    }

    /**
     * Set the 规则id.
     *
     * @param ruleId 规则id
     */
    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * Get the 规则id.
     *
     * @return 规则id
     */
    public Long getRuleId() {
        return this.ruleId;
    }

    /**
     * Set the 品牌菜品id.
     *
     * @param dishId 品牌菜品id
     */
    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    /**
     * Get the 品牌菜品id.
     *
     * @return 品牌菜品id
     */
    public Long getDishId() {
        return this.dishId;
    }

    /**
     * Set the 菜品名称.
     *
     * @param dishName 菜品名称
     */
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    /**
     * Get the 菜品名称.
     *
     * @return 菜品名称
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
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
