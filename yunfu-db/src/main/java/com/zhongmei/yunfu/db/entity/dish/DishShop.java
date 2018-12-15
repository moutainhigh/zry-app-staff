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

/**
 * DishShop is a ORMLite bean type. Corresponds to the database table "dish_shop"
 */
@DatabaseTable(tableName = "dish_shop")
public class DishShop extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

//    CREATE TABLE `dish_shop` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id : 自增id',
//            `uuid` varchar(50) NOT NULL COMMENT '门店菜品uuid : 唯一标识',
//            `brand_dish_id` bigint(20) NOT NULL COMMENT '品牌菜品id : 品牌菜品id',
//            `brand_dish_uuid` varchar(50) NOT NULL COMMENT '品牌菜品uuid : 品牌菜品uuid',
//            `dish_type_id` bigint(20) DEFAULT NULL COMMENT '菜品类型id : 菜品类型id',
//            `dish_code` varchar(100) DEFAULT NULL COMMENT '菜品编码 : 菜品编码',
//            `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '菜品类型 : 菜品种类 0:单菜 1:套餐 2:加料 3:实体卡',
//            `name` varchar(100) NOT NULL COMMENT '菜品名称 : 菜品名称',
//            `alias_name` varchar(100) DEFAULT NULL COMMENT '别名 : 别名',
//            `short_name` varchar(100) DEFAULT NULL COMMENT '短名称',
//            `alias_short_name` varchar(100) DEFAULT NULL COMMENT '别名：短名称',
//            `dish_name_index` varchar(100) DEFAULT NULL COMMENT '菜品名称索引(首字母) : 菜品名称索引(首字母)',
//            `barcode` varchar(120) DEFAULT NULL COMMENT '条形码 : 条形码',
//            `unit_id` bigint(20) DEFAULT NULL COMMENT '单位id : 单位id',
//            `weight` decimal(10,2) DEFAULT '0.00' COMMENT '单位换算称重',
//            `market_price` decimal(10,2) NOT NULL COMMENT '原价 : 原价',
//            `sort` int(11) NOT NULL COMMENT '排序 : 排序',
//            `dish_desc` varchar(500) DEFAULT NULL COMMENT '菜品描述 : 菜品描述',
//            `video_url` varchar(300) DEFAULT NULL COMMENT '视频地址 : 视频地址',
//            `wm_type` tinyint(4) DEFAULT NULL COMMENT '库存类型 : 1、预制商品2、现制商品3、外购商品4、原物料5、半成品',
//            `sale_type` tinyint(4) DEFAULT '1' COMMENT '销售类型 : 1 称重销售',
//            `dish_increase_unit` decimal(6,2) NOT NULL DEFAULT '1.00' COMMENT '起卖份数 : 起卖份数',
//            `is_single` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否允许单点 : 1 允许 2不允许',
//            `is_discount_all` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否允许整单打折 : 1 允许 2不允许',
//            `source` tinyint(4) NOT NULL DEFAULT '1' COMMENT '来源：1  on_mind，2 on_mobile',
//            `is_send_outside` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否允许外送 : 1 允许 2不允许',
//            `is_order` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否允许堂食1.允许2.不允许',
//            `def_property` tinyint(4) NOT NULL DEFAULT '1' COMMENT '商品自定义属性：1.普通商品；2.自定义商品',
//            `step_num` decimal(6,2) NOT NULL DEFAULT '1.00' COMMENT '增量设置',
//            `min_num` int(11) NOT NULL DEFAULT '0' COMMENT '适合人群（小） : 适合人群（小）',
//            `max_num` int(11) NOT NULL DEFAULT '0' COMMENT '适合人群（大） : 适合人群（大）',
//            `clear_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '估清 : 1：在售 ，2：卖光',
//            `is_manual` tinyint(1) DEFAULT NULL COMMENT '是否手动操作在售或者估清',
//            `sale_total` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每日售卖总数 : 每日售卖总数',
//            `residue_total` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '剩余总数 : 剩余总数',
//            `sale_total_wechat` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '外卖可售数量 : 外卖可售数量',
//            `residue_total_wechat` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '外卖剩余数量',
//            `valid_time` time NOT NULL COMMENT '时间段起始时间 : 时间段起始时间',
//            `unvalid_time` time NOT NULL COMMENT '时间段截止时间 : 时间段截止时间',
//            `scene` varchar(50) NOT NULL COMMENT '销售场景 : 商户终端、微信、自助点餐。（3位二进制组合，“1”为可售，“0”为不可售，如“110”表示“商户终端-可售、微信-可售、自助点餐-不可售”）',
//            `shop_identy` bigint(20) NOT NULL COMMENT '商户id : 商户id',
//            `brand_identy` bigint(20) NOT NULL COMMENT '品牌id : 品牌id',
//            `enabled_flag` tinyint(4) DEFAULT '1' COMMENT '启用停用标识 : 区别与StatusFlag，启用停用的作用是该数据是有效数据，但是被停用。 1:启用;2:停用',
//            `sku_key` varchar(100) DEFAULT NULL COMMENT '商品唯一编号 : 商品唯一编号，用算法生成',
//            `product_id` bigint(20) DEFAULT NULL COMMENT '所属SPU id',
//            `has_standard` tinyint(4) NOT NULL DEFAULT '2' COMMENT '是否有规格 : 是否有规格 1 是  2  不是',
//            `dish_qty` decimal(6,3) NOT NULL DEFAULT '1.000' COMMENT '商品数量',
//            `box_qty` tinyint(4) NOT NULL DEFAULT '1' COMMENT '餐盒数量',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态标识 : 状态标识 1:启用 2:禁用',
//            `creator_name` varchar(32) DEFAULT NULL COMMENT '创建者名称 : 创建者名称',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者id : 创建者id',
//            `updator_name` varchar(32) DEFAULT NULL COMMENT '最后修改者姓名 : 最后修改者姓名',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者id : 更新者id',
//            `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务器创建时间 : 服务器创建时间',
//            `server_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务器更新时间',
//            `curr_remain_total` bigint(20) DEFAULT NULL COMMENT '当前可销售数量，NULL代表没有限制',
//            `is_change_price` tinyint(4) DEFAULT '2' COMMENT '是否允许变价: 1 允许 2不允许',
//    PRIMARY KEY (`id`),
//    UNIQUE KEY `uuid` (`uuid`),
//    KEY `idx_shop_identy_server_update_time` (`shop_identy`,`server_update_time`),
//    KEY `idx_brand_dish_id` (`brand_dish_id`),
//    KEY `idx_brand_identy` (`brand_identy`),
//    KEY `idx_server_update_time` (`server_update_time`),
//    KEY `idx_shop_identy_dish_code` (`shop_identy`,`dish_code`(5)),
//    KEY `idx_product_id` (`product_id`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=116584307522459649 DEFAULT CHARSET=utf8 COMMENT='门店菜品'

    /**
     * The columns of table "dish_shop"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * alias_name
         */
        public static final String aliasName = "alias_name";

        /**
         * barcode
         */
        public static final String barcode = "barcode";

        /**
         * brand_dish_id
         */
        public static final String brandDishId = "brand_dish_id";

        /**
         * brand_dish_uuid
         */
        public static final String brandDishUuid = "brand_dish_uuid";

        /**
         * clear_status
         */
        public static final String clearStatus = "clear_status";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * dish_code
         */
        public static final String dishCode = "dish_code";

        /**
         * dish_desc
         */
        public static final String dishDesc = "dish_desc";

        /**
         * dish_increase_unit
         */
        public static final String dishIncreaseUnit = "dish_increase_unit";

        /**
         * dish_name_index
         */
        public static final String dishNameIndex = "dish_name_index";

        /**
         * dish_type_id
         */
        public static final String dishTypeId = "dish_type_id";

        /**
         * enabled_flag
         */
        public static final String enabledFlag = "enabled_flag";

        /**
         * has_standard
         */
        public static final String hasStandard = "has_standard";

        /**
         * is_change_price
         */
        public static final String isChangePrice = "is_change_price";

        /**
         * is_discount_all
         */
        public static final String isDiscountAll = "is_discount_all";

        /**
         * is_send_outside
         */
        public static final String isSendOutside = "is_send_outside";

        /**
         * is_single
         */
        public static final String isSingle = "is_single";

        /**
         * market_price
         */
        public static final String marketPrice = "market_price";

        /**
         * max_num
         */
        public static final String maxNum = "max_num";

        /**
         * min_num
         */
        public static final String minNum = "min_num";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * residue_total
         */
        public static final String residueTotal = "residue_total";

        /**
         * residue_total_wechat
         */
        public static final String residueTotalWechat = "residue_total_wechat";

        /**
         * sale_total
         */
        public static final String saleTotal = "sale_total";

        /**
         * sale_total_wechat
         */
        public static final String saleTotalWechat = "sale_total_wechat";

        /**
         * sale_type
         */
        public static final String saleType = "sale_type";

        /**
         * scene
         */
        public static final String scene = "scene";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * sku_key
         */
        public static final String skuKey = "sku_key";

        /**
         * sort
         */
        public static final String sort = "sort";

        /**
         * step_num
         */
        public static final String stepNum = "step_num";

        /**
         * type
         */
        public static final String type = "type";

        /**
         * unit_id
         */
        public static final String unitId = "unit_id";

        /**
         * unvalid_time
         */
        public static final String unvalidTime = "unvalid_time";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * uuid
         */
        public static final String uuid = "uuid";

        /**
         * valid_time
         */
        public static final String validTime = "valid_time";

        /**
         * video_url
         */
        public static final String videoUrl = "video_url";

        /**
         * wm_type
         */
        public static final String wmType = "wm_type";

        /**
         * short_name
         */
        public static final String shortName = "short_name";

        /**
         * alias_short_name
         */
        public static final String aliasShortName = "alias_short_name";

        /**
         * dish_qty
         */
        public static final String dishQty = "dish_qty";

        /**
         * box_qty
         */
        public static final String boxQty = "box_qty";

        /**
         * weight
         */
        public static final String weight = "weight";

        /**
         * 单位
         */
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

    /**
     * 是否是临时菜
     *
     * @param tempDishCode
     * @return
     */
    public boolean isTempDish(String tempDishCode) {
        if (this.dishCode != null && tempDishCode.equalsIgnoreCase(dishCode)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是套餐
     *
     * @return
     */
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

