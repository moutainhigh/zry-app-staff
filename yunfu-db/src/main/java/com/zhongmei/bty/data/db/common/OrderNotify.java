package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;
import com.zhongmei.bty.entity.enums.NotifyOrderType;
import com.zhongmei.bty.entity.enums.NotifyType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * Trade is a ORMLite bean type. Corresponds to the database
 * table "tables"
 */
@DatabaseTable(tableName = "order_notify")
public class OrderNotify extends OldEntityBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "tables"
     */
    public interface $ extends OldEntityBase.$ {

        /**
         * order_type
         */
        String orderType = "order_type";

        /**
         * type
         */
        String type = "type";

        /**
         * order_uuid
         */
        String orderUuid = "order_uuid";

        /**
         * notify_datetime
         */
        String notifyDatetime = "notify_datetime";

        /**
         * notify_count
         */
        String notifyCount = "notify_count";

        /**
         * brand_id
         */
        String brandId = "brand_id";

        /**
         * commercial_id
         */
        String commercialID = "commercial_id";

        /**
         * create_time
         */
        String createTime = "create_time";

        /**
         * update_time
         */
        String updateTime = "update_time";

        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * mac_address
         */
        String macAddress = "mac_address";

    }

    /**
     * 通知单据类型
     * 1=booking,2=queue,3=takeaway,4=orderall,5=trade
     */
    @DatabaseField(columnName = "order_type")
    private Integer orderType;

    /**
     * uuid
     */
    @DatabaseField(columnName = "uuid")
    private String uuid;

    /**
     * 通知类型
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 关联订单uuid
     */
    @DatabaseField(columnName = "order_uuid")
    private String orderUuid;

    /**
     * 通知时间
     */
    @DatabaseField(columnName = "notify_datetime")
    private Long notifyDatetime;

    /**
     * 通知次数
     */
    @DatabaseField(columnName = "notify_count")
    private Integer notifyCount;

    /**
     * 品牌
     */
    @DatabaseField(columnName = "brand_id")
    private Long brandId;

    /**
     * 门店
     */
    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;

    /**
     * 创建时间
     */
    @DatabaseField(columnName = "create_time", canBeNull = false)
    private Long createTime;

    /**
     * 更新时间
     */
    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;

    /**
     * mac地址
     */
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
