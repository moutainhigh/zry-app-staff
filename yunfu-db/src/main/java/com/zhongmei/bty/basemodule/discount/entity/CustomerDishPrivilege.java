package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.enums.MemberPrivilegeType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;


@DatabaseTable(tableName = "customer_dish_privilege")
public class CustomerDishPrivilege extends CrmBasicEntityBase {

    public interface $ extends BasicEntityBase.$ {

        public static final String levelId="level_id";

        public static final String dishId="dish_id";

        public static final String privilegeType="privilege_type";

        public static final String privilegeValue="privilege_value";

        public static final String shopIdenty = "shop_identy";

        public static final String creatorId = "creator_id";

        public static final String creatorName = "creator_name";

        public static final String updatorId = "updator_id";

        public static final String updatorName = "updator_name";

    }


    @DatabaseField(columnName = "level_id")
    private int levelId;


    @DatabaseField(columnName = "dish_id")
    private Long dishId;


    @DatabaseField(columnName = "privilege_type")
    private Integer privilegeType;


    @DatabaseField(columnName = "privilege_value")
    private BigDecimal privilegeValue;

    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public MemberPrivilegeType getPrivilegeType() {
        return ValueEnums.toEnum(MemberPrivilegeType.class, privilegeType);
    }

    public void setPrivilegeType(MemberPrivilegeType privilegeType) {
        this.privilegeType = ValueEnums.toValue(privilegeType);
    }

    public BigDecimal getPrivilegeValue() {
        return privilegeValue;
    }

    public void setPrivilegeValue(BigDecimal privilegeValue) {
        this.privilegeValue = privilegeValue;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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
        return super.checkNonNull() && EntityBase.checkNonNull(levelId,dishId,privilegeType,privilegeValue);
    }

}
