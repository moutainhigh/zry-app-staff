package com.zhongmei.yunfu.db.entity.booking;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "booking_trade_item")
public class BookingTradeItem extends IdEntityBase {


    public interface $ extends BasicEntityBase.$ {

        String bookingId = "booking_id";

        String bookingUuid = "booking_uuid";

        String parentId = "parent_id";

        String parentUuid = "parent_uuid";

        String skuUuid = "sku_uuid";
        /**
         * sku_id
         */
        public static final String skuId = "sku_id";


        String dishSetmealGroupId = "dish_setmeal_group_id";

        String skuName = "sku_name";

        String type = "type";

        String sort = "sort";

        String price = "price";

        String quantity = "quantity";

        String amount = "amount";

        String propertyAmount = "property_amount";

        String actualAmount = "actual_amount";

        String memo = "memo";

        String uuid = "uuid";

        String statusFlag = "status_flag";

        String clientCreateTime = "client_create_time";

        String clientUpdateTime = "client_update_time";

        String serverCreateTime = "server_create_time";

        String serverUpdateTime = "server_update_time";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updatorId = "updator_id";

        String updatorName = "updator_name";

        String tradeTableUuid = "trade_table_uuid";

        String enableWholePrivilege = "enable_whole_privilege";

        String unitName = "unit_name";

        String saleType = "sale_type";

        String tradeTableId = "trade_table_id";

        String relateTradeItemId = "relate_trade_item_id";

        String relateTradeItemUuid = "relate_trade_item_uuid";

        String feedsAmount = "feeds_amount";

        String invalidType = "invalid_type";

        String recycleStatus = "recycle_status";

        String isChangePrice = "is_change_price";

        String returnQuantity = "return_quantity";


    }

    /**
     * 预定id
     */
    @DatabaseField(columnName = "booking_id")
    private Long bookingId;

    /**
     * 预定uuid
     */
    @DatabaseField(columnName = "booking_uuid")
    private String bookingUuid;

    /**
     * 预定id
     */
    @DatabaseField(columnName = "parent_id")
    private Long parentId;

    /**
     * 指向父记录的uuid，如果是子菜才有值，单菜此字段为空
     */
    @DatabaseField(columnName = "parent_uuid")
    private String parentUuid;

    /**
     * 商品UUID
     */
    @DatabaseField(columnName = "dish_uuid")
    private String dishUuid;

    /**
     * 套餐内菜品分组 id
     */
    @DatabaseField(columnName = "dish_setmeal_group_id")
    private Long dishSetmealGroupId;

    /**
     * 商品名称
     */
    @DatabaseField(columnName = "sku_name")
    private String dishName;

    @DatabaseField(columnName = "dish_id")
    private Long dishId;

    /**
     * 菜品类型
     * 菜品种类 0:单菜 1:套餐 2:加料 10:临时卡售卡 11:临时卡储值 12: 团餐餐标 13：自助餐菜单
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 排序位
     */
    @DatabaseField(columnName = "sort")
    private Integer sort;

    /**
     * 单价
     */
    @DatabaseField(columnName = "price")
    private BigDecimal price;

    /**
     * 数量
     */
    @DatabaseField(columnName = "quantity")
    private BigDecimal quantity = BigDecimal.ONE;

    /**
     * 金额，等于 PRICE * QTY
     */
    @DatabaseField(columnName = "amount")
    private BigDecimal amount;

    /**
     * 各种特征的金额合计
     */
    @DatabaseField(columnName = "property_amount")
    private BigDecimal propertyAmount;

    /**
     * 售价，等于 AMOUNT + FEATURE_AMOUNT'
     */
    @DatabaseField(columnName = "actual_amount")
    private BigDecimal actualAmount;

    /**
     * 备注
     */
    @DatabaseField(columnName = "memo")
    private String memo;

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
     * UUID，本笔记录唯一值
     */
    @DatabaseField(columnName = "uuid")
    private String uuid;

    /**
     * 1:VALID:有效的\r\n 2:INVALID:无效的'
     */
    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;

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
     * 服务端创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务端最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    /**
     * 创建者，创建此记录的系统用户
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者姓名
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

    /**
     * 桌台UUID
     */
    @DatabaseField(columnName = "booking_trade_table_uuid")
    private String bookingTradeTableUuid;

    /**
     * 是否参与整单折扣 1 是 2 否
     */
    @DatabaseField(columnName = "enable_whole_privilege")
    private Integer enableWholePrivilege;

    /**
     * 单位名称
     */
    @DatabaseField(columnName = "unit_name")
    private String unitName;

    /**
     * 销售类型: 1 称重 2非称重
     */
    @DatabaseField(columnName = "sale_type")
    private Integer saleType;

    /**
     * 桌台id
     */
    @DatabaseField(columnName = "booking_trade_table_id")
    private Long bookingTradeTableId;

    /**
     * 当此记录是修改其他品项而来时记录被修改的品项ID
     */
    @DatabaseField(columnName = "relate_trade_item_id")
    private Long relateTradeItemId;

    /**
     * 当此记录是修改其他品项而来时记录被修改的品项UUID
     */
    @DatabaseField(columnName = "relate_trade_item_uuid")
    private String relateTradeItemUuid;

    /**
     * 0.00
     */
    @DatabaseField(columnName = "feeds_amount")
    private BigDecimal feedsAmount;

    /**
     * 无效类型 1:退菜，2:被拆单，3：被删除 , 4:改菜,5:退菜被撤回，6:改菜被撤回,7移菜
     */
    @DatabaseField(columnName = "invalid_type")
    private Integer invalidType;

    /**
     * 1=正常  2=在回收站
     */
    @DatabaseField(columnName = "recycle_status")
    private Integer recycleStatus;

    /**
     * 是否允许变价: 1 允许 2不允许
     */
    @DatabaseField(columnName = "is_change_price")
    private Integer isChangePrice;

    /**
     * 退菜数量
     */
    @DatabaseField(columnName = "return_quantity")
    private BigDecimal returnQuantity;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingUuid() {
        return bookingUuid;
    }

    public void setBookingUuid(String bookingUuid) {
        this.bookingUuid = bookingUuid;
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

    public String getDishUuid() {
        return dishUuid;
    }

    public void setDishUuid(String skuUuid) {
        this.dishUuid = skuUuid;
    }

    public Long getDishSetmealGroupId() {
        return dishSetmealGroupId;
    }

    public void setDishSetmealGroupId(Long dishSetmealGroupId) {
        this.dishSetmealGroupId = dishSetmealGroupId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String skuName) {
        this.dishName = skuName;
    }

    public DishType getType() {
        return ValueEnums.toEnum(DishType.class, type);
    }

    public void setType(DishType type) {
        this.type = ValueEnums.toValue(type);
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public BigDecimal getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(BigDecimal propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
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

    public String getBookingTradeTableUuid() {
        return bookingTradeTableUuid;
    }

    public void setBookingTradeTableUuid(String bookingTradeTableUuid) {
        this.bookingTradeTableUuid = bookingTradeTableUuid;
    }

    public Bool getEnableWholePrivilege() {
        return ValueEnums.toEnum(Bool.class, enableWholePrivilege);
    }

    public void setEnableWholePrivilege(Bool enableWholePrivilege) {
        this.enableWholePrivilege = ValueEnums.toValue(enableWholePrivilege);
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public SaleType getSaleType() {
        return ValueEnums.toEnum(SaleType.class, saleType);
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = ValueEnums.toValue(saleType);
    }

    public Long getBookingTradeTableId() {
        return bookingTradeTableId;
    }

    public void setBookingTradeTableId(Long bookingTradeTableId) {
        this.bookingTradeTableId = bookingTradeTableId;
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

    public BigDecimal getFeedsAmount() {
        return feedsAmount;
    }

    public void setFeedsAmount(BigDecimal feedsAmount) {
        this.feedsAmount = feedsAmount;
    }

    public InvalidType getInvalidType() {
        return ValueEnums.toEnum(InvalidType.class, invalidType);
    }

    public void setInvalidType(InvalidType invalidType) {
        this.invalidType = ValueEnums.toValue(invalidType);
    }

    public ServingStatus getServingStatus() {
        return ValueEnums.toEnum(ServingStatus.class, recycleStatus);
    }

    public void setServingStatus(ServingStatus servingStatus) {
        this.recycleStatus = ValueEnums.toValue(servingStatus);
    }

    public Bool getIsChangePrice() {
        return ValueEnums.toEnum(Bool.class, isChangePrice);
    }

    public void setIsChangePrice(Bool isChangePrice) {
        this.isChangePrice = ValueEnums.toValue(isChangePrice);
    }

    public BigDecimal getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(BigDecimal returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long skuId) {
        this.dishId = skuId;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag) == StatusFlag.VALID;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }


    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setClientCreateTime(System.currentTimeMillis());
        setClientUpdateTime(System.currentTimeMillis());
        setChanged(true);
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
    }

    public void validateUpdate() {
        setChanged(true);
        setClientUpdateTime(System.currentTimeMillis());
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

}
