package com.zhongmei.bty.basemodule.erp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.entity.base.OldEntityBase;

/**
 * 国籍数据
 * <p>
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "erp_currency")
public class ErpCurrency extends OldEntityBase {


//    CREATE TABLE `erp_currency` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT,
//  `country_zh` varchar(100) DEFAULT NULL COMMENT '国家(中文)',
//            `country_en` varchar(100) DEFAULT NULL COMMENT '国家(英文)',
//            `area_code` varchar(20) DEFAULT NULL COMMENT '地区电话区码',
//            `phone_regulation` varchar(120) DEFAULT NULL COMMENT '电话号码验证规则:如正则表达式',
//            `currency_encode` varchar(20) DEFAULT NULL COMMENT '货币编码',
//            `currency` varchar(30) DEFAULT NULL COMMENT '货币名称',
//            `currency_symbol` varchar(10) DEFAULT NULL COMMENT '货币符号,类似$,￥等等',
//            `language_encode` varchar(20) DEFAULT NULL COMMENT '语系编码',
//            `language_name` varchar(100) DEFAULT NULL COMMENT '语系名称',
//            `language_default` varchar(30) DEFAULT NULL COMMENT '默认语系',
//            `time_difference` double(5,2) DEFAULT NULL COMMENT '时差',
//            `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
//            `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
//            `updator_name` varchar(50) DEFAULT NULL COMMENT '修改人姓名',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
//            `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
//            `status` tinyint(4) DEFAULT '0' COMMENT '是否有效 0:有效 -1:无效',
//    PRIMARY KEY (`id`)
//) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 COMMENT='币种语言表'

    public interface $ extends BasicEntityBase.$ {
        /**
         * 国家(中文)
         */
        String countryZh = "country_zh";
        /**
         * 国家(英文)
         */
        String countryEn = "country_en";
        /**
         * 地区电话区码
         */
        String areaCode = "area_code";
        /**
         * 电话号码验证规则:如正则表达式
         */
        String phoneRegulation = "phone_regulation";
        /**
         * 货币编码
         */
        String currencyEncode = "currency_encode";
        /**
         * 货币名称
         */
        String currency = "currency";
        /**
         * 货币符号,类似$,￥等等
         */
        String currencySymbol = "currency_symbol";
        /**
         * 语系编码
         */
        String languageEncode = "language_encode";
        /**
         * 语系名称
         */
        String languageName = "language_name";
        /**
         * 默认语系
         */
        String languageDefault = "language_default";
        /**
         * 时差
         */
        String timeDifference = "time_difference";
        /**
         * 创建时间
         */
        String createTime = "create_time";
        /**
         * 创建人ID
         */
        String creatorId = "creator_id";
        /**
         * 创建人姓名
         */
        String creatorName = "creator_name";
        /**
         * 修改人姓名
         */
        String updator_name = "updatorName";
        /**
         * 修改人ID
         */
        String updatorId = "updator_id";
        /**
         * 修改时间
         */
        String updateTime = "update_time";
        /**
         * 是否有效 0:有效 -1:无效
         */
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
