package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "dish_setmeal_group")
public class DishSetmealGroup extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String aliasName = "alias_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String name = "name";


        public static final String orderMax = "order_max";


        public static final String orderMin = "order_min";


        public static final String setmealDishId = "setmeal_dish_id";


        public static final String sort = "sort";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "alias_name")
    private String aliasName;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "order_max", canBeNull = false)
    private java.math.BigDecimal orderMax;

    @DatabaseField(columnName = "order_min", canBeNull = false)
    private java.math.BigDecimal orderMin;

    @DatabaseField(columnName = "setmeal_dish_id", canBeNull = false)
    private Long setmealDishId;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.math.BigDecimal getOrderMax() {
        return orderMax;
    }

    public void setOrderMax(java.math.BigDecimal orderMax) {
        this.orderMax = orderMax;
    }

    public java.math.BigDecimal getOrderMin() {
        return orderMin;
    }

    public void setOrderMin(java.math.BigDecimal orderMin) {
        this.orderMin = orderMin;
    }

    public Long getSetmealDishId() {
        return setmealDishId;
    }

    public void setSetmealDishId(Long setmealDishId) {
        this.setmealDishId = setmealDishId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(name, orderMax, orderMin, setmealDishId, sort);
    }
}

