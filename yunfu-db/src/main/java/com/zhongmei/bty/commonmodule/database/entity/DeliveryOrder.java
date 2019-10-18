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


@DatabaseTable(tableName = "delivery_order")
public class DeliveryOrder extends IdEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {

        String uuid = "uuid";


        String tradeId = "trade_id";


        String tradeUuid = "trade_uuid";


        String delivererId = "deliverer_id";


        String delivererName = "deliverer_name";


        String delivererPhone = "deliverer_phone";


        String delivererStatus = "delivery_status";


        String statusFlag = "status_flag";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String brandIdenty = "brand_identy";


        String cancelFlag = "cancel_flag";


        String operateSource = "operate_source";


        String deliveryPlatform = "delivery_platform";


        String enableFlag = "enable_flag";


        String subDeliveryStatus = "sub_delivery_status";


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


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


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
