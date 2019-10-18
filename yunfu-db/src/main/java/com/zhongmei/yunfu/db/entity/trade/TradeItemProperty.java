package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.PropertyKind;


@DatabaseTable(tableName = "trade_item_property")
public class TradeItemProperty extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String amount = "amount";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String price = "price";


        public static final String propertyName = "property_name";


        public static final String propertyType = "property_type";


        public static final String propertyUuid = "property_uuid";


        public static final String quantity = "quantity";


        public static final String tradeItemId = "trade_item_id";


        public static final String tradeItemUuid = "trade_item_uuid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "amount")
    private java.math.BigDecimal amount;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "price")
    private java.math.BigDecimal price;

    @DatabaseField(columnName = "property_name", canBeNull = true)
    private String propertyName;

    @DatabaseField(columnName = "property_type", canBeNull = false)
    private Integer propertyType;

    @DatabaseField(columnName = "property_uuid", canBeNull = false)
    private String propertyUuid;

    @DatabaseField(columnName = "quantity", canBeNull = false)
    private java.math.BigDecimal quantity;

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid", canBeNull = false)
    private String tradeItemUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    private Long batchId;

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
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

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
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

    public java.math.BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
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

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public TradeItemProperty clone() {
        TradeItemProperty itemProperty = new TradeItemProperty();
        itemProperty.setId(getId());
        itemProperty.setUuid(getUuid());
        itemProperty.setPrice(getPrice());
        itemProperty.setAmount(getAmount());
        itemProperty.setPropertyName(getPropertyName());
        itemProperty.setPropertyType(getPropertyType());
        itemProperty.setPropertyUuid(getPropertyUuid());
        itemProperty.setTradeItemId(getTradeItemId());
        itemProperty.setTradeItemUuid(getTradeItemUuid());
        itemProperty.setQuantity(getQuantity());
        itemProperty.setBrandIdenty(getBrandIdenty());
        itemProperty.setShopIdenty(getShopIdenty());
        itemProperty.setDeviceIdenty(getDeviceIdenty());
        itemProperty.setStatusFlag(getStatusFlag());
        itemProperty.setUpdatorId(updatorId);
        itemProperty.setUpdatorName(updatorName);
        itemProperty.setCreatorName(creatorName);
        itemProperty.setCreatorId(creatorId);
        itemProperty.setClientCreateTime(getClientCreateTime());
        itemProperty.setClientUpdateTime(getClientUpdateTime());
        itemProperty.setServerCreateTime(getServerCreateTime());
        itemProperty.setServerUpdateTime(getServerUpdateTime());
        itemProperty.setChanged(isChanged());
        itemProperty.setBatchId(getBatchId());
        return itemProperty;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(propertyType, propertyUuid, quantity, tradeItemUuid);
    }
}

