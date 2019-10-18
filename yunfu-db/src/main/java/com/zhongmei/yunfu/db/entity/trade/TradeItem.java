package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;

import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_item")
public class TradeItem extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String actualAmount = "actual_amount";


        public static final String amount = "amount";


        public static final String batchNo = "batch_no";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String dishSetmealGroupId = "dish_setmeal_group_id";


        public static final String enableWholePrivilege = "enable_whole_privilege";


        public static final String feedsAmount = "feeds_amount";


        public static final String invalidType = "invalid_type";


        public static final String isChangePrice = "is_change_price";


        public static final String issueStatus = "issue_status";


        public static final String parentId = "parent_id";


        public static final String parentUuid = "parent_uuid";


        public static final String price = "price";


        public static final String propertyAmount = "property_amount";


        public static final String quantity = "quantity";


        public static final String relateTradeItemId = "relate_trade_Item_id";


        public static final String relateTradeItemUuid = "relate_trade_Item_uuid";


        public static final String returnQuantity = "return_quantity";


        public static final String servingStatus = "serving_status";


        public static final String skuName = "sku_name";


        public static final String skuId = "sku_id";


        public static final String skuUuid = "sku_uuid";


        public static final String sort = "sort";


        public static final String tradeId = "trade_id";


        public static final String tradeMemo = "trade_memo";


        public static final String tradeTableId = "trade_table_id";


        public static final String tradeTableUuid = "trade_table_uuid";


        public static final String tradeUuid = "trade_uuid";


        public static final String type = "type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String saleType = "sale_type";


        public static final String unitName = "unit_name";


        public static final String guestPrinted = "guest_printed";
    }

    @DatabaseField(columnName = "actual_amount", canBeNull = false)
    private java.math.BigDecimal actualAmount;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private java.math.BigDecimal amount;

    @DatabaseField(columnName = "batch_no")
    private String batchNo;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "dish_setmeal_group_id")
    private Long dishSetmealGroupId;

    @DatabaseField(columnName = "enable_whole_privilege")
    private Integer enableWholePrivilege;

    @DatabaseField(columnName = "feeds_amount")
    private java.math.BigDecimal feedsAmount;

    @DatabaseField(columnName = "invalid_type")
    private Integer invalidType;

    @DatabaseField(columnName = "is_change_price")
    private Integer isChangePrice;

    @DatabaseField(columnName = "issue_status")
    private Integer issueStatus;

    @DatabaseField(columnName = "parent_id")
    private Long parentId;

    @DatabaseField(columnName = "parent_uuid")
    private String parentUuid;

    @DatabaseField(columnName = "price", canBeNull = false)
    private java.math.BigDecimal price;

    @DatabaseField(columnName = "property_amount", canBeNull = false)
    private java.math.BigDecimal propertyAmount;

    @DatabaseField(columnName = "quantity", canBeNull = false)
    private java.math.BigDecimal quantity;

    @DatabaseField(columnName = "relate_trade_Item_id")
    private Long relateTradeItemId;

    @DatabaseField(columnName = "relate_trade_Item_uuid")
    private String relateTradeItemUuid;

    @DatabaseField(columnName = "return_quantity")
    private BigDecimal returnQuantity;

    @DatabaseField(columnName = "serving_status")
    private Integer servingStatus;

    @DatabaseField(columnName = "sku_name", canBeNull = false)
    private String dishName;

    @DatabaseField(columnName = "type")
    private Integer type = DishType.SINGLE.value();

    @DatabaseField(columnName = "sku_id")
    private Long dishId;

    @DatabaseField(columnName = "sku_uuid")
    private String skuUuid;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;

    @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

    @DatabaseField(columnName = "trade_memo")
    private String tradeMemo;

    @DatabaseField(columnName = "trade_table_id")
    private Long tradeTableId;

    @DatabaseField(columnName = "trade_table_uuid")
    private String tradeTableUuid;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "sale_type")
    private Integer saleType;

    @DatabaseField(columnName = "unit_name")
    private String unitName;

    @DatabaseField(columnName = "guest_printed")
    private Integer guestPrinted = GuestPrinted.UNPRINT.value();

    private int itemSource = 1;        private Long batchId;

    public java.math.BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(java.math.BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Bool getIsChangePrice() {
        return ValueEnums.toEnum(Bool.class, isChangePrice);
    }

    public void setIsChangePrice(Bool isChangePrice) {
        this.isChangePrice = ValueEnums.toValue(isChangePrice);
    }

    public Long getDishSetmealGroupId() {
        return dishSetmealGroupId;
    }

    public void setDishSetmealGroupId(Long dishSetmealGroupId) {
        this.dishSetmealGroupId = dishSetmealGroupId;
    }

    public Bool getEnableWholePrivilege() {
        return ValueEnums.toEnum(Bool.class, enableWholePrivilege);
    }

    public void setEnableWholePrivilege(Bool enableWholePrivilege) {
        this.enableWholePrivilege = ValueEnums.toValue(enableWholePrivilege);
    }

    public java.math.BigDecimal getFeedsAmount() {
        return feedsAmount;
    }

    public void setFeedsAmount(java.math.BigDecimal feedsAmount) {
        this.feedsAmount = feedsAmount;
    }

    public InvalidType getInvalidType() {
        return ValueEnums.toEnum(InvalidType.class, invalidType);
    }

    public void setInvalidType(InvalidType invalidType) {
        this.invalidType = ValueEnums.toValue(invalidType);
    }

    public IssueStatus getIssueStatus() {
        return ValueEnums.toEnum(IssueStatus.class, issueStatus);
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = ValueEnums.toValue(issueStatus);
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public java.math.BigDecimal getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(java.math.BigDecimal propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public java.math.BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }

    public ServingStatus getServingStatus() {
        return ValueEnums.toEnum(ServingStatus.class, servingStatus);
    }

    public void setServingStatus(ServingStatus servingStatus) {
        this.servingStatus = ValueEnums.toValue(servingStatus);
    }

    public Long getRelateTradeItemId() {
        return relateTradeItemId;
    }

    public void setRelateTradeItemId(Long relateTradeItemId) {
        this.relateTradeItemId = relateTradeItemId;
    }

    public String getRelateTradeItemUuid() {
        return relateTradeItemUuid;
    }

    public void setRelateTradeItemUuid(String relateTradeItemUuid) {
        this.relateTradeItemUuid = relateTradeItemUuid;
    }

    public BigDecimal getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String skuName) {
        this.dishName = skuName;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long skuId) {
        this.dishId = skuId;
    }

    public String getSkuUuid() {
        return skuUuid;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeMemo() {
        return tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public Long getTradeTableId() {
        return tradeTableId;
    }

    public void setTradeTableId(Long tradeTableId) {
        this.tradeTableId = tradeTableId;
    }

    public String getTradeTableUuid() {
        return tradeTableUuid;
    }

    public void setTradeTableUuid(String tradeTableUuid) {
        this.tradeTableUuid = tradeTableUuid;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public DishType getType() {
        return ValueEnums.toEnum(DishType.class, type);
    }

    public void setType(DishType type) {
        this.type = ValueEnums.toValue(type);
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

    public SaleType getSaleType() {
        return ValueEnums.toEnum(SaleType.class, saleType);
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = ValueEnums.toValue(saleType);
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public GuestPrinted getGuestPrinted() {
        return ValueEnums.toEnum(GuestPrinted.class, guestPrinted);
    }

    public void setGuestPrinted(GuestPrinted guestPrinted) {
        this.guestPrinted = ValueEnums.toValue(guestPrinted);
    }

    public int getItemSource() {
        return itemSource;
    }

    public void setItemSource(int itemSource) {
        this.itemSource = itemSource;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public TradeItem clone() {
        TradeItem tradeItem = new TradeItem();
        tradeItem.setId(getId());
        tradeItem.setTradeId(tradeId);
        tradeItem.setTradeUuid(tradeUuid);
        tradeItem.setUuid(getUuid());
        tradeItem.setParentId(parentId);
        tradeItem.setParentUuid(parentUuid);
        tradeItem.setSkuUuid(getSkuUuid());
        tradeItem.setDishSetmealGroupId(dishSetmealGroupId);
        tradeItem.setDishName(getDishName());
        tradeItem.setType(getType());
        tradeItem.setSort(getSort());
        tradeItem.setPrice(getPrice());
        tradeItem.setQuantity(getQuantity());
        tradeItem.setAmount(amount);
        tradeItem.setPropertyAmount(getPropertyAmount());
        tradeItem.setActualAmount(actualAmount);
        tradeItem.setTradeMemo(getTradeMemo());
        tradeItem.setBrandIdenty(getBrandIdenty());
        tradeItem.setShopIdenty(getShopIdenty());
        tradeItem.setDeviceIdenty(getDeviceIdenty());
        tradeItem.setStatusFlag(getStatusFlag());
        tradeItem.setUpdatorId(updatorId);
        tradeItem.setUpdatorName(updatorName);
        tradeItem.setCreatorName(creatorName);
        tradeItem.setCreatorId(creatorId);
        tradeItem.setClientCreateTime(getClientCreateTime());
        tradeItem.setClientUpdateTime(getClientUpdateTime());
        tradeItem.setServerCreateTime(getServerCreateTime());
        tradeItem.setServerUpdateTime(getServerUpdateTime());
        tradeItem.setIssueStatus(getIssueStatus());
        tradeItem.setTradeTableUuid(tradeTableUuid);
        tradeItem.setBatchNo(batchNo);
        tradeItem.setEnableWholePrivilege(getEnableWholePrivilege());
        tradeItem.setUnitName(getUnitName());
        tradeItem.setSaleType(getSaleType());
        tradeItem.setTradeTableId(tradeTableId);
        tradeItem.setRelateTradeItemId(getRelateTradeItemId());
        tradeItem.setRelateTradeItemUuid(getRelateTradeItemUuid());
        tradeItem.setFeedsAmount(feedsAmount);
        tradeItem.setInvalidType(getInvalidType());
        tradeItem.setIsChangePrice(getIsChangePrice());
        tradeItem.setServingStatus(getServingStatus());
        tradeItem.setReturnQuantity(getReturnQuantity());
        tradeItem.setGuestPrinted(getGuestPrinted());
        tradeItem.setDishId(getDishId());
        tradeItem.setChanged(isChanged());
        tradeItem.setBatchId(getBatchId());
        return tradeItem;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(actualAmount, amount, price, propertyAmount, dishName, skuUuid, sort, tradeUuid, saleType);
    }

    public void modifyQty(BigDecimal quantity) {
        BigDecimal newAmount = MathDecimal.divDown(getAmount().multiply(quantity), getQuantity(), 2);
        setAmount(newAmount);
        BigDecimal newActualAmount = MathDecimal.divDown(getActualAmount().multiply(quantity), getQuantity(), 2);
        setActualAmount(newActualAmount);
        BigDecimal newPropertyAmount = MathDecimal.divDown(getPropertyAmount().multiply(quantity), getQuantity(), 2);
        setPropertyAmount(newPropertyAmount);
        BigDecimal newFeedAmount = MathDecimal.divDown(getFeedsAmount().multiply(quantity), getQuantity(), 2);
        setFeedsAmount(newFeedAmount);
        setQuantity(quantity);
        setChanged(true);
    }
}
