package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "dish_property")
public class DishProperty extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String aliasName = "alias_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String enabledFlag = "enabled_flag";


        public static final String name = "name";


        public static final String propertyKind = "property_kind";


        public static final String propertyTypeId = "property_type_id";


        public static final String reprice = "reprice";


        public static final String sort = "sort";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String uuid = "uuid";


        public static final String dishShopId = "dish_shop_id";

    }

    @DatabaseField(columnName = "alias_name")
    private String aliasName;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "enabled_flag")
    private Integer enabledFlag = Bool.YES.value();

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "property_kind", canBeNull = false)
    private Integer propertyKind;

    @DatabaseField(columnName = "property_type_id")
    private Long propertyTypeId;

    @DatabaseField(columnName = "reprice", canBeNull = false)
    private java.math.BigDecimal reprice;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "dish_shop_id")
    private Long dishShopId;

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

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyKind getPropertyKind() {
        return ValueEnums.toEnum(PropertyKind.class, propertyKind);
    }

    public void setPropertyKind(PropertyKind propertyKind) {
        this.propertyKind = ValueEnums.toValue(propertyKind);
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public java.math.BigDecimal getReprice() {
        return reprice;
    }

    public void setReprice(java.math.BigDecimal reprice) {
        this.reprice = reprice;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getDishShopId() {
        return dishShopId;
    }

    public void setDishShopId(Long dishShopId) {
        this.dishShopId = dishShopId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(name, propertyKind, reprice, sort, uuid);
    }
}

