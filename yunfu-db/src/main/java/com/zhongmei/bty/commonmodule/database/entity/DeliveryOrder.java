package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderOperateSource;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderType;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

/**
 * @Date： 2017/7/25
 * @Description:配送记录
 * @Version: 1.0
 */
@DatabaseTable(tableName = "delivery_order")
public class DeliveryOrder extends IdEntityBase {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "delivery_order"
     */
    public interface $ extends IdEntityBase.$ {
        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * trade_id
         */
        String tradeId = "trade_id";

        /**
         * trade_uuid
         */
        String tradeUuid = "trade_uuid";

        /**
         * deliverer_id
         */
        String delivererId = "deliverer_id";

        /**
         * deliverer_name
         */
        String delivererName = "deliverer_name";

        /**
         * deliverer_phone
         */
        String delivererPhone = "deliverer_phone";

        /**
         * delivery_status
         */
        String delivererStatus = "delivery_status";

        /**
         * status_flag
         */
        String statusFlag = "status_flag";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";

        /**
         * cancel_flag
         */
        String cancelFlag = "cancel_flag";

        /**
         * opreate_source
         */
        String operateSource = "operate_source";

        /**
         * delivery_platform
         */
        String deliveryPlatform = "delivery_platform";

        /**
         * enable_flag
         */
        String enableFlag = "enable_flag";

        /**
         * sub_delivery_status
         */
        String subDeliveryStatus = "sub_delivery_status";

        /**
         * order_type
         */
        String orderType = "order_type";
    }


    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "deliverer_id")
    private Long delivererId;

    @DatabaseField(columnName = "deliverer_name")
    private String delivererName;

    @DatabaseField(columnName = "deliverer_phone")
    private String delivererPhone;

    @DatabaseField(columnName = "delivery_status", canBeNull = false)
    private Integer deliveryStatus;

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

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
     * 品牌Identy
     */
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    /**
     * 是否可以取消（1-可以，0-不可以）
     */
    @DatabaseField(columnName = "cancel_flag")
    private Integer cancelFlag;

    @DatabaseField(columnName = "operate_source")
    private Integer operateSource;

    @DatabaseField(columnName = "delivery_platform")
    private Integer deliveryPlatform;

    @DatabaseField(columnName = "enable_flag")
    private Integer enableFlag;

    @DatabaseField(columnName = "sub_delivery_status")
    private Integer subDeliveryStatus;

    @DatabaseField(columnName = "order_type")
    private Integer orderType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public Long getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(Long delivererId) {
        this.delivererId = delivererId;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public String getDelivererPhone() {
        return delivererPhone;
    }

    public void setDelivererPhone(String delivererPhone) {
        this.delivererPhone = delivererPhone;
    }

    public DeliveryOrderStatus getDeliveryStatus() {
        return ValueEnums.toEnum(DeliveryOrderStatus.class, deliveryStatus);
    }

    public void setDeliveryStatus(DeliveryOrderStatus deliveryStatus) {
        this.deliveryStatus = ValueEnums.toValue(deliveryStatus);
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

    public Bool getStatusFlag() {
        return ValueEnums.toEnum(Bool.class, statusFlag);
    }

    public void setStatusFlag(Bool statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Bool getCancelFlag() {
        return ValueEnums.toEnum(Bool.class, cancelFlag);
    }

    public void setCancelFlag(Bool cancelFlag) {
        this.cancelFlag = ValueEnums.toValue(cancelFlag);
    }

    public DeliveryOrderOperateSource getOperateSource() {
        return ValueEnums.toEnum(DeliveryOrderOperateSource.class, operateSource);
    }

    public void setOperateSource(DeliveryOrderOperateSource operateSource) {
        this.operateSource = ValueEnums.toValue(operateSource);
    }

    public DeliveryPlatform getDeliveryPlatform() {
        return ValueEnums.toEnum(DeliveryPlatform.class, deliveryPlatform);
    }

    public void setDeliveryPlatform(DeliveryPlatform deliveryPlatform) {
        this.deliveryPlatform = ValueEnums.toValue(deliveryPlatform);
    }

    public YesOrNo getEnableFlag() {
        return ValueEnums.toEnum(YesOrNo.class, enableFlag);
    }

    public void setEnableFlag(YesOrNo enableFlag) {
        this.enableFlag = ValueEnums.toValue(enableFlag);
    }

    public DeliveryOrderSubStatus getSubDeliveryStatus() {
        return ValueEnums.toEnum(DeliveryOrderSubStatus.class, subDeliveryStatus);
    }

    public void setSubDeliveryStatus(DeliveryOrderSubStatus subDeliveryStatus) {
        this.subDeliveryStatus = ValueEnums.toValue(subDeliveryStatus);
    }

    public DeliveryOrderType getOrderType() {
        return ValueEnums.toEnum(DeliveryOrderType.class, orderType);
    }

    public void setOrderType(DeliveryOrderType orderType) {
        this.orderType = ValueEnums.toValue(orderType);
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(YesOrNo.YES, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
