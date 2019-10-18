package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "discount_shop")
public class DiscountShop extends BasicEntityBase implements ICreator, IUpdator {


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



    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;


    @DatabaseField(columnName = "discount_id", canBeNull = false)
    private Long discountId;


    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;


    @DatabaseField(columnName = "name_two")
    private String nameTwo;


    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;


    @DatabaseField(columnName = "content", canBeNull = false)
    private Double content;


    @DatabaseField(columnName = "unit", canBeNull = false)
    private String unit;


    @DatabaseField(columnName = "enabled_flag", canBeNull = false)
    private Integer enabledFlag;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }


    public Long getShopIdenty() {
        return this.shopIdenty;
    }


    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }


    public Long getDiscountId() {
        return this.discountId;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }


    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }


    public String getNameTwo() {
        return this.nameTwo;
    }


    public DiscountType getType() {
        return ValueEnums.toEnum(DiscountType.class, type);
    }


    public void setType(DiscountType type) {
        this.type = ValueEnums.toValue(type);
    }



    public void setContent(Double content) {
        this.content = content;
    }


    public Double getContent() {
        return this.content;
    }


    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getUnit() {
        return this.unit;
    }



    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }


    public Integer getEnabledFlag() {
        return this.enabledFlag;
    }


    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public Long getCreatorId() {
        return this.creatorId;
    }


    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public String getCreatorName() {
        return this.creatorName;
    }


    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }


    public Long getUpdatorId() {
        return this.updatorId;
    }


    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }


    public String getUpdatorName() {
        return this.updatorName;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, discountId, name, type, content, unit, enabledFlag);
    }
}
