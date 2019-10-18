package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.UserDeliveryStatus;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "auth_user_shop")
public class AuthUserShop extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String deliveryStatus = "delivery_status";


        public static final String isLogin = "is_login";


        public static final String shopIdenty = "shop_identy";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String userId = "user_id";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "delivery_status")
    private Integer deliveryStatus;

    @DatabaseField(columnName = "is_login")
    private Integer isLogin;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "user_id", canBeNull = false)
    private Long userId;

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

    public UserDeliveryStatus getDeliveryStatus() {
        return ValueEnums.toEnum(UserDeliveryStatus.class, deliveryStatus);
    }

    public void setDeliveryStatus(UserDeliveryStatus deliveryStatus) {
        this.deliveryStatus = ValueEnums.toValue(deliveryStatus);
    }

    public Bool getIsLogin() {
        return ValueEnums.toEnum(Bool.class, isLogin);
    }

    public void setIsLogin(Bool isLogin) {
        this.isLogin = ValueEnums.toValue(isLogin);
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, userId);
    }
}

