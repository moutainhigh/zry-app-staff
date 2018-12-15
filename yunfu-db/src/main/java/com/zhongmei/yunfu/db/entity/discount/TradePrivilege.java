package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * TradePrivilege is a ORMLite bean type. Corresponds to the database table "trade_privilege"
 */
@DatabaseTable(tableName = "trade_privilege")
public class TradePrivilege extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_privilege"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * coupon_id
         */
        public static final String couponId = "coupon_id";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * privilege_amount
         */
        public static final String privilegeAmount = "privilege_amount";

        /**
         * privilege_type
         */
        public static final String privilegeType = "privilege_type";

        /**
         * privilege_value
         */
        public static final String privilegeValue = "privilege_value";

        /**
         * surcharge_name
         */
        public static final String surchargeName = "surcharge_name";

        /**
         * privilege_name
         */
        public static final String privilegeName = "privilege_name";

        /**
         * promo_id
         */
        public static final String promoId = "promo_id";

        /**
         * trade_id
         */
        public static final String tradeId = "trade_id";

        /**
         * trade_item_id
         */
        public static final String tradeItemId = "trade_item_id";

        /**
         * trade_item_uuid
         */
        public static final String tradeItemUuid = "trade_item_uuid";

        /**
         * trade_uuid
         */
        public static final String tradeUuid = "trade_uuid";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "coupon_id")
    private Long couponId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "privilege_amount", canBeNull = false)
    private java.math.BigDecimal privilegeAmount;

    @DatabaseField(columnName = "privilege_type", canBeNull = false)
    private Integer privilegeType;

    @DatabaseField(columnName = "privilege_value", canBeNull = false)
    private java.math.BigDecimal privilegeValue;

    @DatabaseField(columnName = "surcharge_name")
    private String surchargeName;

    @DatabaseField(columnName = "privilege_name")
    private String privilegeName;

    @DatabaseField(columnName = "promo_id")
    private Long promoId;

    @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid")
    private String tradeItemUuid;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public java.math.BigDecimal getPrivilegeAmount() {
        return privilegeAmount;
    }

    public void setPrivilegeAmount(java.math.BigDecimal privilegeAmount) {
        this.privilegeAmount = privilegeAmount;
    }

    public PrivilegeType getPrivilegeType() {
        return ValueEnums.toEnum(PrivilegeType.class, privilegeType);
    }

    public void setPrivilegeType(PrivilegeType privilegeType) {
        this.privilegeType = ValueEnums.toValue(privilegeType);
    }

    public java.math.BigDecimal getPrivilegeValue() {
        return privilegeValue;
    }

    public void setPrivilegeValue(java.math.BigDecimal privilegeValue) {
        this.privilegeValue = privilegeValue;
    }

    public String getSurchargeName() {
        return surchargeName;
    }

    public void setSurchargeName(String surchargeName) {
        this.surchargeName = surchargeName;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
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

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public TradePrivilege clone() {
        TradePrivilege tradePrivilege = new TradePrivilege();
        tradePrivilege.setId(getId());
        tradePrivilege.setUuid(getUuid());
        tradePrivilege.setPrivilegeValue(getPrivilegeValue());
        tradePrivilege.setPrivilegeAmount(getPrivilegeAmount());
        tradePrivilege.setTradeItemUuid(getTradeItemUuid());
        tradePrivilege.setTradeItemId(getTradeItemId());
        tradePrivilege.setCouponId(getCouponId());
        tradePrivilege.setPrivilegeName(getPrivilegeName());
        tradePrivilege.setPrivilegeType(getPrivilegeType());
        tradePrivilege.setPromoId(getPromoId());
        tradePrivilege.setTradeId(getTradeId());
        tradePrivilege.setTradeUuid(getTradeUuid());
        tradePrivilege.setSurchargeName(getSurchargeName());
        tradePrivilege.setBrandIdenty(getBrandIdenty());
        tradePrivilege.setShopIdenty(getShopIdenty());
        tradePrivilege.setDeviceIdenty(getDeviceIdenty());
        tradePrivilege.setStatusFlag(getStatusFlag());
        tradePrivilege.setUpdatorId(updatorId);
        tradePrivilege.setUpdatorName(updatorName);
        tradePrivilege.setCreatorName(creatorName);
        tradePrivilege.setCreatorId(creatorId);
        tradePrivilege.setClientCreateTime(getClientCreateTime());
        tradePrivilege.setClientUpdateTime(getClientUpdateTime());
        tradePrivilege.setServerCreateTime(getServerCreateTime());
        tradePrivilege.setServerUpdateTime(getServerUpdateTime());
        tradePrivilege.setChanged(isChanged());
        return tradePrivilege;
    }

    //是否保存过服务器
    public boolean isSaveServer() {
        if (getId() != null) {
            return true;
        }
        return false;
    }

    /**
     * 设置为无效
     */
    public void setInValid() {
        this.setStatusFlag(StatusFlag.INVALID);
        this.validateUpdate();
    }

    public boolean isCommonPrivilege() {
        if (getPrivilegeType() == PrivilegeType.DISCOUNT
                || getPrivilegeType() == PrivilegeType.MEMBER_PRICE
                || getPrivilegeType() == PrivilegeType.REBATE
                || getPrivilegeType() == PrivilegeType.FREE
                || getPrivilegeType() == PrivilegeType.GIVE
                || getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT) {
            return true;
        }
        return false;
    }

    /**
     * 是否是小程序优惠
     *
     * @return
     */
    public boolean isAppletPrivilege() {
        if (getPrivilegeType() == PrivilegeType.COLLAGE ||
                getPrivilegeType() == PrivilegeType.SECKILL
                || getPrivilegeType() == PrivilegeType.BARGAIN) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(privilegeAmount, privilegeType, privilegeValue, tradeUuid);
    }
}

