package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.math.BigDecimal;


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

        public static final String commercialGroupId = "commercialGroupId";

        public static final String isIntegral = "isIntegral";

        public static final String integralScale = "integralScale";

        public static final String isCoupons = "isCoupons";

        public static final String isValueCard = "isValueCard";

        public static final String isShareCustomer = "isShareCustomer";

        public static final String status = "status";

        public static final String createDateTime = "createDateTime";

        public static final String modifyDateTime = "modifyDateTime";

        public static final String currency_encode = "currency_encode";

        public static final String languageDefault = "language_default";

        public static final String languageSecond = "language_second";

        public static final String isSupply = "is_supply";

    }


    @DatabaseField(columnName = "commercialGroupId")
    private Long commercialGroupId;


    @DatabaseField(columnName = "isIntegral")
    private int isIntegral;

    @DatabaseField(columnName = "integralScale")
    private BigDecimal integralScale;

    @DatabaseField(columnName = "isCoupons")
    private int isCoupons;

    @DatabaseField(columnName = "isValueCard")
    private int isValueCard;

    @DatabaseField(columnName = "isShareCustomer")
    private int isShareCustomer;

    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;

    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;

    @DatabaseField(columnName = "status")
    private int status;

    @DatabaseField(columnName = "currency_encode")
    private String currencyEncode;

    @DatabaseField(columnName = "language_default")
    private String languageDefault;

    @DatabaseField(columnName = "language_second")
    private String languageSecond;

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
