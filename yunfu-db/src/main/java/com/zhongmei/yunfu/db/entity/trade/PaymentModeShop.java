package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;

import java.math.BigDecimal;

/**
 * 收款类型
 */
@DatabaseTable(tableName = "payment_mode_shop")
public class PaymentModeShop extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "payment_mode_shop"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * brand_mode_id
         */
        public static final String brandModeId = "brand_mode_id";

        /**
         * erp_mode_id
         */
        public static final String erpModeId = "erp_mode_id";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * alias_name
         */
        public static final String aliasName = "alias_name";

        /**
         * face_value
         */
        public static final String faceValue = "face_value";

        /**
         * payment_mode_type
         */
        public static final String paymentModeType = "payment_mode_type";

        /**
         * is_change
         */
        public static final String isChange = "is_change";

        /**
         * is_discount
         */
        public static final String isDiscount = "is_discount";

        /**
         * is_invoice
         */
        public static final String isInvoice = "is_invoice";

        /**
         * is_refund
         */
        public static final String isRefund = "is_refund";

        /**
         * is_cure
         */
        public static final String isCurd = "is_cure";

        /**
         * sort
         */
        public static final String sort = "sort";

        /**
         * enabled_flag
         */
        public static final String enabledFlag = "enabled_flag";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * shop_identy
         */
        public static final String shopIdenty = "shop_identy";

    }

    @DatabaseField(columnName = "brand_mode_id")
    private Long brandModeId;

    @DatabaseField(columnName = "erp_mode_id", canBeNull = false)
    private Long erpModeId;

    /**
     * 支付方式名称
     */
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    /**
     * 别名
     */
    @DatabaseField(columnName = "alias_name")
    private String aliasName;

    /**
     * 面值
     */
    @DatabaseField(columnName = "face_value")
    private BigDecimal faceValue;

    /**
     * 支付类型
     */
    @DatabaseField(columnName = "payment_mode_type")
    private Integer paymentModeType;

    /**
     * 是否找零
     */
    @DatabaseField(columnName = "is_change")
    private Integer isChange;

    /**
     * 是否打折
     */
    @DatabaseField(columnName = "is_discount")
    private Integer isDiscount;

    /**
     * 是否发票
     */
    @DatabaseField(columnName = "is_invoice")
    private Integer isInvoice;

    /**
     * 是否支持退货
     */
    @DatabaseField(columnName = "is_refund")
    private Integer isRefund;

    /**
     * 是否被固化
     */
    @DatabaseField(columnName = "is_cure")
    private Integer isCure;

    /**
     * 排序
     */
    @DatabaseField(columnName = "sort")
    private Integer sort;

    /**
     * 是否被启用
     */
    @DatabaseField(columnName = "enabled_flag")
    private Integer enabledFlag;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getBrandModeId() {
        return brandModeId;
    }

    public void setBrandModeId(Long brandModeId) {
        this.brandModeId = brandModeId;
    }

    public Long getErpModeId() {
        return erpModeId;
    }

    public void setErpModeId(Long erpModeId) {
        this.erpModeId = erpModeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public PayModelGroup getPaymentModeType() {
        return ValueEnums.toEnum(PayModelGroup.class, paymentModeType);
    }

    public void setPaymentModeType(PayModelGroup paymentModeType) {
        this.paymentModeType = ValueEnums.toValue(paymentModeType);
    }

    public Bool getIsChange() {
        return ValueEnums.toEnum(Bool.class, isChange);
    }

    public void setIsChange(Bool isChange) {
        this.isChange = ValueEnums.toValue(isChange);
    }

    public Bool getIsDiscount() {
        return ValueEnums.toEnum(Bool.class, isDiscount);
    }

    public void setIsDiscount(Bool isDiscount) {
        this.isDiscount = ValueEnums.toValue(isDiscount);
    }

    public Bool getIsInvoice() {
        return ValueEnums.toEnum(Bool.class, isInvoice);
    }

    public void setIsInvoice(Bool isInvoice) {
        this.isInvoice = ValueEnums.toValue(isInvoice);
    }

    public Bool getIsRefund() {
        return ValueEnums.toEnum(Bool.class, isRefund);
    }

    public void setIsRefund(Bool isRefund) {
        this.isRefund = ValueEnums.toValue(isRefund);
    }

    public Bool getIsCure() {
        return ValueEnums.toEnum(Bool.class, isCure);
    }

    public void setIsCure(Bool isCure) {
        this.isCure = ValueEnums.toValue(isCure);
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(erpModeId, name);
    }
}
