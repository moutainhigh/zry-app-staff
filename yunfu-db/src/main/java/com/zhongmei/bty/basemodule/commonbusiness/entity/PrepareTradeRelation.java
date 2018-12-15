package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.commonbusiness.enums.TradeRelatedType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 排队预订和单据关联表
 */
@DatabaseTable(tableName = "prepare_trade_relation")
public class PrepareTradeRelation extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "commercial_area"
     */
    public interface $ extends IdEntityBase.$ {

        /**
         * type
         */
        public static final String type = "type";

        /**
         * related_id
         */
        public static final String relatedId = "related_id";

        /**
         * trade_id
         */
        public static final String tradeId = "trade_id";

        /**
         * brand_identy
         */
        public static final String brandIdenty = "brand_identy";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * shop_identy
         */
        public static final String deviceIdenty = "device_identy";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * status_flag
         */
        String statusFlag = "status_flag";

    }

    /**
     * 预点菜关联业务类型 1为预订 2为排队
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 关联id 预订或排队id
     */
    @DatabaseField(columnName = "related_id")
    private Long relatedId;

    /**
     * 订单id
     */
    @DatabaseField(columnName = "tradeId")
    private Long tradeId;

    /**
     * 品牌标识
     */
    @DatabaseField(columnName = "brand_identy")
    private Long brandIdenty;

    /**
     * 门店标识
     */
    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;

    /**
     * 设备标识
     */
    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    public TradeRelatedType getType() {
        return ValueEnums.toEnum(TradeRelatedType.class, type);
    }

    public void setType(TradeRelatedType type) {
        this.type = ValueEnums.toValue(type);
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag);
    }
}
