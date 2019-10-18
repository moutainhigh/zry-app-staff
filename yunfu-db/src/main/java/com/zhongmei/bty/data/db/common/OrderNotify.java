package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;
import com.zhongmei.bty.entity.enums.NotifyOrderType;
import com.zhongmei.bty.entity.enums.NotifyType;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "order_notify")
public class OrderNotify extends OldEntityBase {


    private static final long serialVersionUID = 1L;


    public interface $ extends OldEntityBase.$ {


        String orderType = "order_type";


        String type = "type";


        String orderUuid = "order_uuid";


        String notifyDatetime = "notify_datetime";


        String notifyCount = "notify_count";


        String brandId = "brand_id";


        String commercialID = "commercial_id";


        String createTime = "create_time";


        String updateTime = "update_time";


        String uuid = "uuid";


        String macAddress = "mac_address";

    }


    @DatabaseField(columnName = "order_type")
    private Integer orderType;


    @DatabaseField(columnName = "uuid")
    private String uuid;


    @DatabaseField(columnName = "type")
    private Integer type;


    @DatabaseField(columnName = "order_uuid")
    private String orderUuid;


    @DatabaseField(columnName = "notify_datetime")
    private Long notifyDatetime;


    @DatabaseField(columnName = "notify_count")
    private Integer notifyCount;


    @DatabaseField(columnName = "brand_id")
    private Long brandId;


    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;


    @DatabaseField(columnName = "create_time", canBeNull = false)
    private Long createTime;


    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;


    @DatabaseField(columnName = "mac_address")
    private String macAddress;

    public NotifyOrderType getOrderType() {
        return ValueEnums.toEnum(NotifyOrderType.class, orderType);
    }

    public void setOrderType(NotifyOrderType orderType) {
        this.orderType = ValueEnums.toValue(orderType);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public NotifyType getType() {
        return ValueEnums.toEnum(NotifyType.class, type);
    }

    public void setType(NotifyType type) {
        this.type = ValueEnums.toValue(type);
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Long getNotifyDatetime() {
        return notifyDatetime;
    }

    public void setNotifyDatetime(Long notifyDatetime) {
        this.notifyDatetime = notifyDatetime;
    }

    public Integer getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdate_time() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public Long verValue() {
        return updateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(createTime, updateTime);
    }
}
