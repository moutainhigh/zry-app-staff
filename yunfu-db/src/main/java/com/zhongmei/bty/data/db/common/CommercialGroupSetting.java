package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "commercial_group_setting")
public class CommercialGroupSetting extends IdEntityBase {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isValid() {
        if (status == 0)
            return true;
        return false;
    }

    @Override
    public Long verValue() {

        return modifyDateTime;
    }

    public interface $ {
        /**
         * commercialGroupId
         */
        public static final String commercialGroupId = "commercialGroupId";
        /**
         * isIntegral
         */
        public static final String isIntegral = "isIntegral";
        /**
         * integralScale
         */
        public static final String integralScale = "integralScale";
        /**
         * isCoupons
         */
        public static final String isCoupons = "isCoupons";
        /**
         * isValueCard
         */
        public static final String isValueCard = "isValueCard";
        /**
         * isShareCustomer
         */
        public static final String isShareCustomer = "isShareCustomer";
        /**
         * status
         */
        public static final String status = "status";
        /**
         * createDateTime
         */
        public static final String createDateTime = "createDateTime";
        /**
         * modifyDateTime
         */
        public static final String modifyDateTime = "modifyDateTime";
        /**
         * currency_encode
         */
        public static final String currency_encode = "currency_encode";
        /**
         * language_default
         */
        public static final String languageDefault = "language_default";
        /**
         * language_second
         */
        public static final String languageSecond = "language_second";
        /**
         * isSupply
         */
        public static final String isSupply = "is_supply";

    }

    /**
     * 商户主ID
     */
    @DatabaseField(columnName = "commercialGroupId")
    private Long commercialGroupId;

    /**
     * 是否开通积分
     */
    @DatabaseField(columnName = "isIntegral")
    private int isIntegral;
    /**
     * 积分比例
     */
    @DatabaseField(columnName = "integralScale")
    private BigDecimal integralScale;
    /**
     * 是否下发优惠券
     */
    @DatabaseField(columnName = "isCoupons")
    private int isCoupons;
    /**
     * 是否开通储值
     */
    @DatabaseField(columnName = "isValueCard")
    private int isValueCard;
    /**
     * 是否共享客户基础数据
     */
    @DatabaseField(columnName = "isShareCustomer")
    private int isShareCustomer;
    /**
     * 创建时间
     */
    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;
    /**
     * 修改时间
     */
    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;
    /**
     * 记录是否有效
     */
    @DatabaseField(columnName = "status")
    private int status;
    /**
     * 货币编码
     */
    @DatabaseField(columnName = "currency_encode")
    private String currencyEncode;
    /**
     * 默认语系
     */
    @DatabaseField(columnName = "language_default")
    private String languageDefault;
    /**
     * 第二语言
     */
    @DatabaseField(columnName = "language_second")
    private String languageSecond;
    /**
     * 是否开通供应链
     */
    @DatabaseField(columnName = "is_supply")
    private int isSupply;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCommercialGroupId() {
        return commercialGroupId;
    }

    public void setCommercialGroupId(Long commercialGroupId) {
        this.commercialGroupId = commercialGroupId;
    }

    public int getIsIntegral() {
        return isIntegral;
    }

    public void setIsIntegral(int isIntegral) {
        this.isIntegral = isIntegral;
    }

    public BigDecimal getIntegralScale() {
        return integralScale;
    }

    public void setIntegralScale(BigDecimal integralScale) {
        this.integralScale = integralScale;
    }

    public int getIsCoupons() {
        return isCoupons;
    }

    public void setIsCoupons(int isCoupons) {
        this.isCoupons = isCoupons;
    }

    public int getIsValueCard() {
        return isValueCard;
    }

    public void setIsValueCard(int isValueCard) {
        this.isValueCard = isValueCard;
    }

    public int getIsShareCustomer() {
        return isShareCustomer;
    }

    public void setIsShareCustomer(int isShareCustomer) {
        this.isShareCustomer = isShareCustomer;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrencyEncode() {
        return currencyEncode;
    }

    public void setCurrencyEncode(String currencyEncode) {
        this.currencyEncode = currencyEncode;
    }

    public String getLanguageDefault() {
        return languageDefault;
    }

    public void setLanguageDefault(String languageDefault) {
        this.languageDefault = languageDefault;
    }

    public String getLanguageSecond() {
        return languageSecond;
    }

    public void setLanguageSecond(String languageSecond) {
        this.languageSecond = languageSecond;
    }

    public int getIsSupply() {
        return isSupply;
    }

    public void setIsSupply(int isSupply) {
        this.isSupply = isSupply;
    }
}
