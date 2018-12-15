package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.ExtraChargeCalcWay;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;
import com.zhongmei.yunfu.context.util.GsonUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;

/**
 * ExtraCharge is a ORMLite bean type. Corresponds to the database table "extra_charge"
 * <p>
 * CREATE TABLE `extra_charge` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 * `name` varchar(50) NOT NULL COMMENT '名称',
 * `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '编码',
 * `calc_way` tinyint(4) NOT NULL DEFAULT '1' COMMENT '计算方式:1,按例比;2,按人数;3,固定金额;4、最低消费;5,按人数/时间 计费',
 * `content` decimal(10,2) NOT NULL,
 * `min_consume` decimal(10,2) DEFAULT NULL COMMENT '最低消费',
 * `content_by_time` decimal(10,2) DEFAULT '0.00' COMMENT '每多少时间产生一次计费, 单位为分钟',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌ID',
 * `shop_identy` bigint(20) DEFAULT NULL COMMENT '门店ID',
 * `order_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否加入订单;1:是;2:否',
 * `discount_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否参与折扣;1:是;2:否',
 * `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '逻辑删除标志;1:否;2:是',
 * `enabled_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识;1:是;2:否',
 * `delete_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可删除标识;1:是;2:否',
 * `creator_name` varchar(32) DEFAULT NULL COMMENT '创建人名称',
 * `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
 * `updator_name` varchar(32) DEFAULT NULL COMMENT '更新人名称',
 * `updator_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
 * `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务器创建时间',
 * `server_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '服务器更新时间',
 * `business_type` tinyint(4) DEFAULT NULL COMMENT '门店的业态,例如 正餐 80,快餐 81',
 * `tax_code` varchar(20) DEFAULT NULL COMMENT '附加费关联税费的code',
 * `privilege_flag` tinyint(4) DEFAULT NULL COMMENT '服务费是在优惠前计算还是优惠后计算,1:优惠前计算 2:优惠后计算',
 * `enable_table_area` tinyint(4) DEFAULT '2' COMMENT '是否启用桌台区域关联，1启用 2不启用',
 * PRIMARY KEY (`id`),
 * KEY `idx_brand_shop_name` (`brand_identy`,`shop_identy`,`name`) USING BTREE,
 * KEY `idx_shop_identy_server_update_time` (`brand_identy`,`shop_identy`,`server_update_time`) USING BTREE
 * ) ENGINE=InnoDB AUTO_INCREMENT=87958597611257857 DEFAULT CHARSET=utf8 COMMENT='附加费配置表';
 */
@DatabaseTable(tableName = "extra_charge")
public class ExtraCharge extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "extra_charge"
     */
    public interface $ extends BasicEntityBase.$ {


        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * calc_way
         */
        public static final String calcWay = "calc_way";

        /**
         * content
         */
        public static final String content = "content";

        /**
         * order_flag
         */
        public static final String orderFlag = "order_flag";

        /**
         * discount_flag
         */
        public static final String discountFlag = "discount_flag";

        /**
         * enabled_flag
         */
        public static final String enabledFlag = "enabled_flag";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * code
         */
        public static final String code = "code";

        /**
         * content_by_time
         */
        public static final String contentByTime = "content_by_time";

        String businessType = "business_type";

        String taxCode = "tax_code";

        String privilegeFlag = "privilege_flag";

        String enableTableArea = "enable_table_area";
    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "code")
    private String code;

    @DatabaseField(columnName = "calc_way", canBeNull = false)
    private Integer calcWay;

    @DatabaseField(columnName = "order_flag", canBeNull = false)
    private Integer orderFlag;

    @DatabaseField(columnName = "discount_flag", canBeNull = false)
    private Integer discountFlag;

    @DatabaseField(columnName = "enabled_flag", canBeNull = false)
    private Integer enabledFlag;

    @DatabaseField(columnName = "content", canBeNull = false)
    private String content;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "content_by_time")
    private BigDecimal contentByTime;

    @DatabaseField(columnName = $.businessType)
    private Integer businessType;

    @DatabaseField(columnName = $.taxCode)
    private String taxCode;

    @DatabaseField(columnName = $.privilegeFlag)
    private Integer privilegeFlag;

    @DatabaseField(columnName = $.enableTableArea)
    private Integer enableTableArea;

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

    public ExtraChargeCalcWay getCalcWay() {
        return ValueEnums.toEnum(ExtraChargeCalcWay.class, calcWay);
    }

    public void setCalcWay(ExtraChargeCalcWay calcWay) {
        this.calcWay = ValueEnums.toValue(calcWay);
    }

    public Bool getOrderFlag() {
        return ValueEnums.toEnum(Bool.class, orderFlag);
    }

    public void setOrderFlag(Bool orderFlag) {
        this.orderFlag = ValueEnums.toValue(orderFlag);
    }

    public Bool getDiscountFlag() {
        return ValueEnums.toEnum(Bool.class, discountFlag);
    }

    public void setDiscountFlag(Bool discountFlag) {
        this.discountFlag = ValueEnums.toValue(discountFlag);
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getContentByTime() {
        return contentByTime;
    }

    public void setContentByTime(BigDecimal contentByTime) {
        this.contentByTime = contentByTime;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getPrivilegeFlag() {
        return privilegeFlag;
    }

    public void setPrivilegeFlag(Integer privilegeFlag) {
        this.privilegeFlag = privilegeFlag;
    }

    /**
     * 是否自动加入订单
     *
     * @return
     */
    public boolean isAutoJoinTrade() {
        return getOrderFlag() == Bool.YES;
    }

    public boolean isDiscountAfter() {
        return Utils.equals(privilegeFlag, 2);
    }

    public Integer getEnableTableArea() {
        return enableTableArea;
    }

    public void setEnableTableArea(Integer enableTableArea) {
        this.enableTableArea = enableTableArea;
    }

    /**
     * 是否启用桌台区域关联
     *
     * @return
     */
    public boolean isTableAreaEnable() {
        return Utils.equals(enableTableArea, 1);
    }

    public static ExtraCharge create(ExtraCharge extraCharge) {
        ExtraCharge newExtraCharge = new ExtraCharge();
        newExtraCharge.validateCreate();
        newExtraCharge.creatorId = extraCharge.creatorId;
        newExtraCharge.creatorName = extraCharge.creatorName;
        newExtraCharge.code = extraCharge.code;
        newExtraCharge.calcWay = extraCharge.calcWay;
        newExtraCharge.orderFlag = extraCharge.orderFlag;
        newExtraCharge.discountFlag = extraCharge.discountFlag;
        newExtraCharge.enabledFlag = extraCharge.enabledFlag;
        newExtraCharge.content = extraCharge.content;
        newExtraCharge.name = extraCharge.name;
        newExtraCharge.shopIdenty = extraCharge.shopIdenty;
        newExtraCharge.updatorId = extraCharge.updatorId;
        newExtraCharge.updatorName = extraCharge.updatorName;
        newExtraCharge.contentByTime = extraCharge.contentByTime;
        newExtraCharge.businessType = extraCharge.businessType;
        newExtraCharge.taxCode = extraCharge.taxCode;
        newExtraCharge.privilegeFlag = extraCharge.privilegeFlag;
        newExtraCharge.enableTableArea = extraCharge.enableTableArea;
        return newExtraCharge;
    }

    public TradeInitConfig toTradeInitConfig() {
        TradeInitConfig initConfig = new TradeInitConfig();
        initConfig.validateCreate();
        initConfig.setKeyId(TradeInitConfigKeyId.SERVICE_CHARGE_RATE);
        ServiceCharge mapValue = new ServiceCharge();
        mapValue.id = id;
        mapValue.name = name;
        mapValue.calcWay = calcWay;
        mapValue.feeRate = content;
        mapValue.discountType = isDiscountAfter() ? 1 : 2;
        initConfig.setValue(GsonUtil.objectToJson(mapValue));
        return initConfig;
    }

    static public class ServiceCharge {
        public Long id; //主键
        public String name; //名称
        public Integer calcWay; //计算方式:1,按例比;2,按人数;3,固定金额;4、最低消费;5,按人数/香蕉费',
        public String feeRate; //计算值
        public Integer discountType; //折扣类型，1：after discount-折扣后, 2, before discount-折扣前

        public Integer getPrivilegeFlag() {
            return Utils.equals(discountType, 2) ? 1 : 2;
        }
    }
}
