package com.zhongmei.bty.basemodule.erp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;


@DatabaseTable(tableName = "erp_currency")
public class ErpCurrency extends OldEntityBase {



    public interface $ extends BasicEntityBase.$ {

        String countryZh = "country_zh";

        String countryEn = "country_en";

        String areaCode = "area_code";

        String phoneRegulation = "phone_regulation";

        String currencyEncode = "currency_encode";

        String currency = "currency";

        String currencySymbol = "currency_symbol";

        String languageEncode = "language_encode";

        String languageName = "language_name";

        String languageDefault = "language_default";

        String timeDifference = "time_difference";

        String createTime = "create_time";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updator_name = "updatorName";

        String updatorId = "updator_id";

        String updateTime = "update_time";

        String status = "status";
    }

    @DatabaseField(columnName = "country_zh")
    private String countryZh;
    @DatabaseField(columnName = "country_en")
    private String countryEn;
    @DatabaseField(columnName = "area_code")
    private String areaCode;
    @DatabaseField(columnName = "phone_regulation")
    private String phoneRegulation;
    @DatabaseField(columnName = "currency_encode")
    private String currencyEncode;
    @DatabaseField(columnName = "currency")
    private String currency;
    @DatabaseField(columnName = "currency_symbol")
    private String currencySymbol;
    @DatabaseField(columnName = "language_encode")
    private String languageEncode;
    @DatabaseField(columnName = "language_name")
    private String languageName;
    @DatabaseField(columnName = "language_default")
    private String languageDefault;
    @DatabaseField(columnName = "time_difference")
    private Double timeDifference;
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;
    @DatabaseField(columnName = "creator_name")
    private String creatorName;
    @DatabaseField(columnName = "updator_name")
    private String updatorName;
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;
    @DatabaseField(columnName = "update_time", canBeNull = false)
    private Long updateTime;

    public String getCountry() {
        return ErpConstants.isChinese() ? getCountryZh() : getCountryEn();
    }

    public String getCountryAreaCode() {
        return getCountry() + " " + (areaCode.trim().startsWith("+") ? areaCode : "+" + areaCode);
    }

    public String getCountryZh() {
        return countryZh;
    }

    public void setCountryZh(String countryZh) {
        this.countryZh = countryZh;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneRegulation() {
        return phoneRegulation;
    }

    public void setPhoneRegulation(String phoneRegulation) {
        this.phoneRegulation = phoneRegulation;
    }

    public String getCurrencyEncode() {
        return currencyEncode;
    }

    public void setCurrencyEncode(String currencyEncode) {
        this.currencyEncode = currencyEncode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getLanguageEncode() {
        return languageEncode;
    }

    public void setLanguageEncode(String languageEncode) {
        this.languageEncode = languageEncode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageDefault() {
        return languageDefault;
    }

    public void setLanguageDefault(String languageDefault) {
        this.languageDefault = languageDefault;
    }

    public Double getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Double timeDifference) {
        this.timeDifference = timeDifference;
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Long verValue() {
        return updateTime;
    }
}
