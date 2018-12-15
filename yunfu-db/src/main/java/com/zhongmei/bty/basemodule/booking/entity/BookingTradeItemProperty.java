package com.zhongmei.bty.basemodule.booking.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "booking_trade_item_property")
public class BookingTradeItemProperty extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {
        String uuid = "uuid";
        String bookingTradeItemId = "booking_trade_item_id";

        String bookingTradeItemUuid = "booking_trade_item_uuid";

        String propertyType = "property_type";

        String propertyUuid = "property_uuid";

        String propertyName = "property_name";

        String price = "price";

        String quantity = "quantity";

        String amount = "amount";

        String clientCreateTime = "client_create_time";

        String clientUpdateTime = "client_update_time";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updatorId = "updator_id";

        String updatorName = "updator_name";

    }

    /**
     * 自动创建的uuid
     */
    @DatabaseField(columnName = "uuid")
    private String uuid;

    /**
     * 服务端自增ID
     */
    @DatabaseField(columnName = "booking_trade_item_id")
    private Long bookingTradeItemId;

    /**
     * 关联BOOKING_TRADE_ITEM的UUID
     */
    @DatabaseField(columnName = "booking_trade_item_uuid")
    private String bookingTradeItemUuid;

    /**
     * 属性类别：(待口味、做法等统一后可以考虑取消此字段)  1:TASTE:口味  2:RECIPE:做法
     */
    @DatabaseField(columnName = "property_type")
    private Integer propertyType;

    /**
     * 属性UUID，对应口味或做法的主键id（uuid）
     */
    @DatabaseField(columnName = "property_uuid")
    private String propertyUuid;

    /**
     * 属性名称
     */
    @DatabaseField(columnName = "property_name")
    private String propertyName;

    /**
     * 单价
     */
    @DatabaseField(columnName = "price")
    private BigDecimal price;

    /**
     * 数量
     */
    @DatabaseField(columnName = "quantity")
    private BigDecimal quantity;

    /**
     * '金额，等于 PRICE * QTY'
     */
    @DatabaseField(columnName = "amount")
    private BigDecimal amount;


    /**
     * PAD本地创建时间
     */
    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    /**
     * PAD本地最后修改时间
     */
    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;


    /**
     * 创建者，创建此记录的系统用户
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者，创建者姓名
     */
    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 最后修改此记录的用户
     */
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    /**
     * 最后修改者姓名
     */
    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    private Long bookingId;

    public void setPropertyType(Integer propertyType) {
        this.propertyType = propertyType;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBookingTradeItemUuid() {
        return bookingTradeItemUuid;
    }

    public void setBookingTradeItemUuid(String bookingTradeItemUuid) {
        this.bookingTradeItemUuid = bookingTradeItemUuid;
    }

    public PropertyKind getPropertyType() {
        return ValueEnums.toEnum(PropertyKind.class, propertyType);
    }

    public void setPropertyType(PropertyKind propertyType) {
        this.propertyType = ValueEnums.toValue(propertyType);
    }

    public String getPropertyUuid() {
        return propertyUuid;
    }

    public void setPropertyUuid(String propertyUuid) {
        this.propertyUuid = propertyUuid;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }


    public Long getBookingTradeItemId() {
        return bookingTradeItemId;
    }

    public void setBookingTradeItemId(Long bookingTradeItemId) {
        this.bookingTradeItemId = bookingTradeItemId;
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

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag) == StatusFlag.VALID;
    }

    @Override
    public void validateUpdate() {
        super.validateUpdate();
        setClientUpdateTime(System.currentTimeMillis());
    }

    @Override
    public Long verValue() {
        return clientUpdateTime;
    }
}
