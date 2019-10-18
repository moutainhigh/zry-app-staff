package com.zhongmei.bty.basemodule.booking.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


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


    @DatabaseField(columnName = "uuid")
    private String uuid;


    @DatabaseField(columnName = "booking_trade_item_id")
    private Long bookingTradeItemId;


    @DatabaseField(columnName = "booking_trade_item_uuid")
    private String bookingTradeItemUuid;


    @DatabaseField(columnName = "property_type")
    private Integer propertyType;


    @DatabaseField(columnName = "property_uuid")
    private String propertyUuid;


    @DatabaseField(columnName = "property_name")
    private String propertyName;


    @DatabaseField(columnName = "price")
    private BigDecimal price;


    @DatabaseField(columnName = "quantity")
    private BigDecimal quantity;


    @DatabaseField(columnName = "amount")
    private BigDecimal amount;



    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;


    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;



    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


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
