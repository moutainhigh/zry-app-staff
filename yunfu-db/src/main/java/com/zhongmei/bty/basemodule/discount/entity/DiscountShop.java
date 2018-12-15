package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * Model class of 折扣门店表.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "discount_shop")
public class DiscountShop extends BasicEntityBase implements ICreator, IUpdator {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {
        public static final String shopIdenty = "shop_identy";
        public static final String discountId = "discount_id";

        public static final String name = "name";

        public static final String nameTwo = "name_two";

        public static final String type = "type";

        public static final String content = "content";

        public static final String unit = "unit";

        public static final String enabledFlag = "enabled_flag";

        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

    }


    /**
     * 门店ID.
     */
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    /**
     * 折扣ID.
     */
    @DatabaseField(columnName = "discount_id", canBeNull = false)
    private Long discountId;

    /**
     * 折扣名称.
     */
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    /**
     * 折扣名称二.
     */
    @DatabaseField(columnName = "name_two")
    private String nameTwo;

    /**
     * 折扣方式.
     */
    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    /**
     * 折扣内容.
     */
    @DatabaseField(columnName = "content", canBeNull = false)
    private Double content;

    /**
     * 折扣单位.
     */
    @DatabaseField(columnName = "unit", canBeNull = false)
    private String unit;

    /**
     * 启用标识.
     */
    @DatabaseField(columnName = "enabled_flag", canBeNull = false)
    private Integer enabledFlag;

    /**
     * 创建者id.
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者名称.
     */
    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 更新者id.
     */
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    /**
     * 更新人名称.
     */
    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * Set the 门店ID.
     *
     * @param shopIdenty 门店ID
     */
    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    /**
     * Get the 门店ID.
     *
     * @return 门店ID
     */
    public Long getShopIdenty() {
        return this.shopIdenty;
    }

    /**
     * Set the 折扣ID.
     *
     * @param discountId 折扣ID
     */
    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    /**
     * Get the 折扣ID.
     *
     * @return 折扣ID
     */
    public Long getDiscountId() {
        return this.discountId;
    }

    /**
     * Set the 折扣名称.
     *
     * @param name 折扣名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the 折扣名称.
     *
     * @return 折扣名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the 折扣名称二.
     *
     * @param nameTwo 折扣名称二
     */
    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    /**
     * Get the 折扣名称二.
     *
     * @return 折扣名称二
     */
    public String getNameTwo() {
        return this.nameTwo;
    }


    public DiscountType getType() {
        return ValueEnums.toEnum(DiscountType.class, type);
    }


    public void setType(DiscountType type) {
        this.type = ValueEnums.toValue(type);
    }


    /**
     * Set the 折扣内容.
     *
     * @param content 折扣内容
     */
    public void setContent(Double content) {
        this.content = content;
    }

    /**
     * Get the 折扣内容.
     *
     * @return 折扣内容
     */
    public Double getContent() {
        return this.content;
    }

    /**
     * Set the 折扣单位.
     *
     * @param unit 折扣单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Get the 折扣单位.
     *
     * @return 折扣单位
     */
    public String getUnit() {
        return this.unit;
    }


    /**
     * Set the 启用标识.
     *
     * @param enabledFlag 启用标识
     */
    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * Get the 启用标识.
     *
     * @return 启用标识
     */
    public Integer getEnabledFlag() {
        return this.enabledFlag;
    }

    /**
     * Set the 创建者id.
     *
     * @param creatorId 创建者id
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Get the 创建者id.
     *
     * @return 创建者id
     */
    public Long getCreatorId() {
        return this.creatorId;
    }

    /**
     * Set the 创建者名称.
     *
     * @param creatorName 创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * Get the 创建者名称.
     *
     * @return 创建者名称
     */
    public String getCreatorName() {
        return this.creatorName;
    }

    /**
     * Set the 更新者id.
     *
     * @param updatorId 更新者id
     */
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    /**
     * Get the 更新者id.
     *
     * @return 更新者id
     */
    public Long getUpdatorId() {
        return this.updatorId;
    }

    /**
     * Set the 更新人名称.
     *
     * @param updatorName 更新人名称
     */
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    /**
     * Get the 更新人名称.
     *
     * @return 更新人名称
     */
    public String getUpdatorName() {
        return this.updatorName;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, discountId, name, type, content, unit, enabledFlag);
    }
}
