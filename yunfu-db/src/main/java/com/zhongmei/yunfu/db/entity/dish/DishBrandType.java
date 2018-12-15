package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;

/**
 * DishBrandType is a ORMLite bean type. Corresponds to the database table "dish_brand_type"
 */
@DatabaseTable(tableName = "dish_brand_type")
public class DishBrandType extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "dish_brand_type"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * alias_name
         */
        public static final String aliasName = "alias_name";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * dish_type_desc
         */
        public static final String dishTypeDesc = "dish_type_desc";

        /**
         * enabled_flag
         */
        public static final String enabledFlag = "enabled_flag";

        /**
         * is_order
         */
        public static final String isOrder = "is_order";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * parent_id
         */
        public static final String parentId = "parent_id";

        /**
         * sort
         */
        public static final String sort = "sort";

        /**
         * type_code
         */
        public static final String typeCode = "type_code";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * uuid
         */
        public static final String uuid = "uuid";

    }

    @DatabaseField(columnName = "alias_name")
    private String aliasName;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "dish_type_desc")
    private String dishTypeDesc;

    @DatabaseField(columnName = "enabled_flag")
    private Integer enabledFlag = Bool.YES.value();

    @DatabaseField(columnName = "is_order", canBeNull = false)
    private Byte isOrder;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "parent_id")
    private Long parentId;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;//菜品品类的sort排序值

    @DatabaseField(columnName = "type_code")
    private String typeCode;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "is_show")
    private Integer isShow = Bool.YES.value();

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

    public String getDishTypeDesc() {
        return dishTypeDesc;
    }

    public void setDishTypeDesc(String dishTypeDesc) {
        this.dishTypeDesc = dishTypeDesc;
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public Byte getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(Byte isOrder) {
        this.isOrder = isOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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

    public Integer isShow() {
        return isShow;
    }

    public void setShow(Integer isShow) {
        this.isShow = isShow;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(isOrder, name, sort, uuid);
    }

    @Override
    public String toString() {
        return "DishBrandType{" +
                "id=" + id +
                "& name=" + name +
                '}';
    }
}

