package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.ChildDishType;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "dish_setmeal")
public class DishSetmeal extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String childDishId = "child_dish_id";


        public static final String childDishType = "child_dish_type";


        public static final String comboDishTypeId = "combo_dish_type_id";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String dishId = "dish_id";


        public static final String isDefault = "is_default";


        public static final String isMulti = "is_multi";


        public static final String isReplace = "is_replace";


        public static final String leastCellNum = "least_cell_num";


        public static final String price = "price";


        public static final String sort = "sort";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "child_dish_id")
    private Long childDishId;

    @DatabaseField(columnName = "child_dish_type")
    private Integer childDishType;

    @DatabaseField(columnName = "combo_dish_type_id")
    private Long comboDishTypeId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "dish_id")
    private Long dishId;

    @DatabaseField(columnName = "is_default")
    private Integer isDefault;

    @DatabaseField(columnName = "is_multi")
    private Integer isMulti;

    @DatabaseField(columnName = "is_replace")
    private Integer isReplace;

    @DatabaseField(columnName = "least_cell_num")
    private java.math.BigDecimal leastCellNum;

    @DatabaseField(columnName = "price")
    private java.math.BigDecimal price;

    @DatabaseField(columnName = "sort")
    private Integer sort;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getChildDishId() {
        return childDishId;
    }

    public void setChildDishId(Long childDishId) {
        this.childDishId = childDishId;
    }

    public ChildDishType getChildDishType() {
        return ValueEnums.toEnum(ChildDishType.class, childDishType);
    }

    public void setChildDishType(ChildDishType childDishType) {
        this.childDishType = ValueEnums.toValue(childDishType);
    }

    public Long getComboDishTypeId() {
        return comboDishTypeId;
    }

    public void setComboDishTypeId(Long comboDishTypeId) {
        this.comboDishTypeId = comboDishTypeId;
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

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Bool getIsDefault() {
        return ValueEnums.toEnum(Bool.class, isDefault);
    }

    public void setIsDefault(Bool isDefault) {
        this.isDefault = ValueEnums.toValue(isDefault);
    }

    public Bool getIsMulti() {
        return ValueEnums.toEnum(Bool.class, isMulti);
    }

    public void setIsMulti(Bool isMulti) {
        this.isMulti = ValueEnums.toValue(isMulti);
    }

    public Bool getIsReplace() {
        return ValueEnums.toEnum(Bool.class, isReplace);
    }

    public void setIsReplace(Bool isReplace) {
        this.isReplace = ValueEnums.toValue(isReplace);
    }

    public java.math.BigDecimal getLeastCellNum() {
        return leastCellNum;
    }

    public void setLeastCellNum(java.math.BigDecimal leastCellNum) {
        this.leastCellNum = leastCellNum;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
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

}

