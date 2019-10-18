package com.zhongmei.yunfu.db.entity.dish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;


@DatabaseTable(tableName = "dish_shop")
public class DishShop extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;



    public interface $ extends BasicEntityBase.$ {


        public static final String aliasName = "alias_name";


        public static final String barcode = "barcode";


        public static final String brandDishId = "brand_dish_id";


        public static final String brandDishUuid = "brand_dish_uuid";


        public static final String clearStatus = "clear_status";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String dishCode = "dish_code";


        public static final String dishDesc = "dish_desc";


        public static final String dishIncreaseUnit = "dish_increase_unit";


        public static final String dishNameIndex = "dish_name_index";


        public static final String dishTypeId = "dish_type_id";


        public static final String enabledFlag = "enabled_flag";


        public static final String hasStandard = "has_standard";


        public static final String isChangePrice = "is_change_price";


        public static final String isDiscountAll = "is_discount_all";


        public static final String isSendOutside = "is_send_outside";


        public static final String isSingle = "is_single";


        public static final String marketPrice = "market_price";


        public static final String maxNum = "max_num";


        public static final String minNum = "min_num";


        public static final String name = "name";


        public static final String residueTotal = "residue_total";


        public static final String residueTotalWechat = "residue_total_wechat";


        public static final String saleTotal = "sale_total";


        public static final String saleTotalWechat = "sale_total_wechat";


        public static final String saleType = "sale_type";


        public static final String scene = "scene";


        public static final String shopIdenty = "shop_identy";


        public static final String skuKey = "sku_key";


        public static final String sort = "sort";


        public static final String stepNum = "step_num";


        public static final String type = "type";


        public static final String unitId = "unit_id";


        public static final String unvalidTime = "unvalid_time";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String uuid = "uuid";


        public static final String validTime = "valid_time";


        public static final String videoUrl = "video_url";


        public static final String wmType = "wm_type";


        public static final String shortName = "short_name";


        public static final String aliasShortName = "alias_short_name";


        public static final String dishQty = "dish_qty";


        public static final String boxQty = "box_qty";


        public static final String weight = "weight";


        public static final String unitName = "unit_name";
    }

    @DatabaseField(columnName = "unit_name")
    private String unitName;

    @DatabaseField(columnName = "alias_name")
    private String aliasName;

    @DatabaseField(columnName = "barcode")
    private String barcode;

    @DatabaseField(columnName = "brand_dish_id", canBeNull = false)
    private Long brandDishId;

    @DatabaseField(columnName = "brand_dish_uuid", canBeNull = false)
    private String brandDishUuid;

    @DatabaseField(columnName = "clear_status", canBeNull = false)
    private Integer clearStatus;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "dish_code")
    private String dishCode;

    @DatabaseField(columnName = "dish_desc")
    private String dishDesc;

    @DatabaseField(columnName = "dish_increase_unit", canBeNull = false)
    private java.math.BigDecimal dishIncreaseUnit = BigDecimal.ONE;

    @DatabaseField(columnName = "dish_name_index")
    private String dishNameIndex;

    @DatabaseField(columnName = "dish_type_id")
    private Long dishTypeId;

    @DatabaseField(columnName = "enabled_flag")
    private Integer enabledFlag;

    @DatabaseField(columnName = "has_standard")
    private Integer hasStandard;

    @DatabaseField(columnName = "is_change_price")
    private Integer isChangePrice;

    @DatabaseField(columnName = "is_discount_all", canBeNull = false)
    private Integer isDiscountAll;

    @DatabaseField(columnName = "is_send_outside", canBeNull = false)
    private Integer isSendOutside;

    @DatabaseField(columnName = "is_single", canBeNull = false)
    private Integer isSingle;

    @DatabaseField(columnName = "market_price", canBeNull = false)
    private java.math.BigDecimal marketPrice;

    @DatabaseField(columnName = "max_num")
    private java.math.BigDecimal maxNum;

    @DatabaseField(columnName = "min_num", canBeNull = false)
    private java.math.BigDecimal minNum;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "residue_total", canBeNull = false)
    private java.math.BigDecimal residueTotal;

    @DatabaseField(columnName = "residue_total_wechat", canBeNull = false)
    private java.math.BigDecimal residueTotalWechat;

    @DatabaseField(columnName = "sale_total", canBeNull = false)
    private java.math.BigDecimal saleTotal;

    @DatabaseField(columnName = "sale_total_wechat", canBeNull = false)
    private java.math.BigDecimal saleTotalWechat;

    @DatabaseField(columnName = "sale_type")
    private Integer saleType;

    @DatabaseField(columnName = "scene", canBeNull = false)
    private String scene;

    @DatabaseField(columnName = "sku_key")
    private String skuKey;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;

    @DatabaseField(columnName = "step_num", canBeNull = false)
    private java.math.BigDecimal stepNum;

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "unit_id")
    private Long unitId;

    @DatabaseField(columnName = "unvalid_time", canBeNull = false)
    private String unvalidTime;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "valid_time", canBeNull = false)
    private String validTime;

    @DatabaseField(columnName = "video_url")
    private String videoUrl;

    @DatabaseField(columnName = "wm_type")
    private Integer wmType;

    @DatabaseField(columnName = "short_name")
    private String shortName;

    @DatabaseField(columnName = "alias_short_name")
    private String aliasShortName;

    @DatabaseField(columnName = "dish_qty")
    private BigDecimal dishQty;

    @DatabaseField(columnName = "box_qty")
    private BigDecimal boxQty;

    @DatabaseField(columnName = "weight")
    private BigDecimal weight;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getBrandDishId() {
        return id;
    }

    public void setBrandDishId(Long brandDishId) {
        this.brandDishId = brandDishId;
    }

    public String getBrandDishUuid() {
        return brandDishUuid;
    }

    public void setBrandDishUuid(String brandDishUuid) {
        this.brandDishUuid = brandDishUuid;
    }

    public ClearStatus getClearStatus() {
        return ValueEnums.toEnum(ClearStatus.class, clearStatus);
    }

    public void setClearStatus(ClearStatus clearStatus) {
        this.clearStatus = ValueEnums.toValue(clearStatus);
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

    public String getDishCode() {
        return dishCode;
    }

    public void setDishCode(String dishCode) {
        this.dishCode = dishCode;
    }

    public String getDishDesc() {
        return dishDesc;
    }

    public void setDishDesc(String dishDesc) {
        this.dishDesc = dishDesc;
    }

    public java.math.BigDecimal getDishIncreaseUnit() {
        return dishIncreaseUnit;
    }

    public void setDishIncreaseUnit(java.math.BigDecimal dishIncreaseUnit) {
        this.dishIncreaseUnit = dishIncreaseUnit;
    }

    public String getDishNameIndex() {
        return dishNameIndex;
    }

    public void setDishNameIndex(String dishNameIndex) {
        this.dishNameIndex = dishNameIndex;
    }

    public Long getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Long dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public Bool getHasStandard() {
        return ValueEnums.toEnum(Bool.class, hasStandard);
    }

    public void setHasStandard(Bool hasStandard) {
        this.hasStandard = ValueEnums.toValue(hasStandard);
    }

    public Bool getIsChangePrice() {
        return ValueEnums.toEnum(Bool.class, isChangePrice);
    }

    public void setIsChangePrice(Bool isChangePrice) {
        this.isChangePrice = ValueEnums.toValue(isChangePrice);
    }

    public Bool getIsDiscountAll() {
        return ValueEnums.toEnum(Bool.class, isDiscountAll);
    }

    public void setIsDiscountAll(Bool isDiscountAll) {
        this.isDiscountAll = ValueEnums.toValue(isDiscountAll);
    }

    public Bool getIsSendOutside() {
        return ValueEnums.toEnum(Bool.class, isSendOutside);
    }

    public void setIsSendOutside(Bool isSendOutside) {
        this.isSendOutside = ValueEnums.toValue(isSendOutside);
    }

    public Bool getIsSingle() {
        return ValueEnums.toEnum(Bool.class, isSingle);
    }

    public void setIsSingle(Bool isSingle) {
        this.isSingle = ValueEnums.toValue(isSingle);
    }

    public java.math.BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(java.math.BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public java.math.BigDecimal getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(java.math.BigDecimal maxNum) {
        this.maxNum = maxNum;
    }

    public java.math.BigDecimal getMinNum() {
        return minNum;
    }

    public void setMinNum(java.math.BigDecimal minNum) {
        this.minNum = minNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.math.BigDecimal getResidueTotal() {
        return residueTotal;
    }

    public void setResidueTotal(java.math.BigDecimal residueTotal) {
        this.residueTotal = residueTotal;
    }

    public java.math.BigDecimal getResidueTotalWechat() {
        return residueTotalWechat;
    }

    public void setResidueTotalWechat(java.math.BigDecimal residueTotalWechat) {
        this.residueTotalWechat = residueTotalWechat;
    }

    public java.math.BigDecimal getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(java.math.BigDecimal saleTotal) {
        this.saleTotal = saleTotal;
    }

    public java.math.BigDecimal getSaleTotalWechat() {
        return saleTotalWechat;
    }

    public void setSaleTotalWechat(java.math.BigDecimal saleTotalWechat) {
        this.saleTotalWechat = saleTotalWechat;
    }

    public SaleType getSaleType() {
        return ValueEnums.toEnum(SaleType.class, saleType);
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = ValueEnums.toValue(saleType);
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public java.math.BigDecimal getStepNum() {
        return stepNum;
    }

    public void setStepNum(java.math.BigDecimal stepNum) {
        this.stepNum = stepNum;
    }

    public DishType getType() {
        return ValueEnums.toEnum(DishType.class, type);
    }

    public void setType(DishType type) {
        this.type = ValueEnums.toValue(type);
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnvalidTime() {
        return unvalidTime;
    }

    public void setUnvalidTime(String unvalidTime) {
        this.unvalidTime = unvalidTime;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAliasShortName() {
        return aliasShortName;
    }

    public void setAliasShortName(String aliasShortName) {
        this.aliasShortName = aliasShortName;
    }

    public BigDecimal getDishQty() {
        return dishQty;
    }

    public void setDishQty(BigDecimal dishQty) {
        this.dishQty = dishQty;
    }

    public BigDecimal getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(BigDecimal boxQty) {
        this.boxQty = boxQty;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public boolean isTempDish(String tempDishCode) {
        if (this.dishCode != null && tempDishCode.equalsIgnoreCase(dishCode)) {
            return true;
        }
        return false;
    }


    public boolean isCombo() {
        return getType() == DishType.COMBO;
    }

    @Override
    public String toString() {
        return "DishShop{" +
                "id=" + id +
                "& name=" + name +
                '}';
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(brandDishId,
                brandDishUuid,
                clearStatus,
                dishIncreaseUnit,
                isDiscountAll,
                isSendOutside,
                isSingle,
                marketPrice,
                maxNum,
                minNum,
                name,
                residueTotal,
                residueTotalWechat,
                saleTotal,
                saleTotalWechat,
                saleType,
                scene,
                sort,
                stepNum,
                type,
                unvalidTime,
                uuid,
                validTime);
    }
}

