package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * <p>
 * `id` bigint(20) NOT NULL AUTO_INCREMENT,
 * `trade_id` bigint(20) NOT NULL COMMENT '订单id',
 * `tax_type` varchar(20) NOT NULL COMMENT '税类型',
 * `tax_type_name` varchar(45) NOT NULL COMMENT '税类型名称',
 * `tax_plan` varchar(45) NOT NULL COMMENT '税收方案',
 * `tax_amount` decimal(10,2) NOT NULL COMMENT '税收金额',
 * `
 * ` bigint(20) NOT NULL,
 * `brand_identy` bigint(20) DEFAULT NULL,
 * `status_flag` int(11) NOT NULL DEFAULT '1' COMMENT '数据有效状态 :1 有效 2:无效',
 * `client_create_time` timestamp(3) NULL DEFAULT NULL,
 * `client_update_time` timestamp(3) NULL DEFAULT NULL,
 * `server_create_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
 * `server_update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
 * `uuid` varchar(45) NOT NULL,
 * `taxable_income` decimal(10,2) NOT NULL COMMENT '应纳税所得额',
 */

@DatabaseTable(tableName = "trade_tax")
public class TradeTax extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {
        public final String uuid = "uuid";
        public final String tradeId = "trade_id";
        public final String taxType = "tax_type";
        public final String taxTypeName = "tax_type_name";
        public final String taxPlan = "tax_plan";
        public final String taxAmount = "tax_amount";
        public final String taxableIncome = "taxable_income";
        public final String clientCreateTime = "client_create_time";
        public final String clientUpdateTime = "client_update_time";

        String effectType = "effect_type";
        String discountType = "discount_type";
        String taxKind = "tax_kind";
        String taxMethod = "tax_method";
    }

    @DatabaseField(columnName = "uuid")
    String uuid;
    @DatabaseField(columnName = "trade_id")
    Long tradeId;
    @DatabaseField(columnName = "tax_type")
    String taxType;
    @DatabaseField(columnName = "tax_type_name")
    String taxTypeName;
    @DatabaseField(columnName = "tax_plan")
    String taxPlan;
    @DatabaseField(columnName = "tax_amount")
    BigDecimal taxAmount;
    @DatabaseField(columnName = "taxable_income")
    BigDecimal taxableIncome;
    @DatabaseField(columnName = "client_create_time")
    Long clientCreateTime;
    @DatabaseField(columnName = "client_update_time")
    Long clientUpdateTime;

    @DatabaseField(columnName = $.effectType)
    Integer effectType;
    @DatabaseField(columnName = $.discountType)
    Integer discountType;
    @DatabaseField(columnName = $.taxKind)
    String taxKind;
    @DatabaseField(columnName = $.taxMethod)
    String taxMethod;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxTypeName() {
        return taxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }

    public String getTaxPlan() {
        return taxPlan;
    }

    public void setTaxPlan(String taxPlan) {
        this.taxPlan = taxPlan;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
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

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getTaxKind() {
        return taxKind;
    }

    public void setTaxKind(String taxKind) {
        this.taxKind = taxKind;
    }

    public String getTaxMethod() {
        return taxMethod;
    }

    public void setTaxMethod(String taxMethod) {
        this.taxMethod = taxMethod;
    }

    @Override
    public void validateCreate() {
        super.validateCreate();
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(ShopInfoCfg.getInstance().getShopInfo().getBrandIdLong());
        setShopIdenty(ShopInfoCfg.getInstance().getShopInfo().getShopIdLong());
        setClientCreateTime(System.currentTimeMillis());
    }

    @Override
    public void validateUpdate() {
        super.validateUpdate();
        setClientUpdateTime(System.currentTimeMillis());
    }

    public static TradeTax create(TradeTax tradeTax) {
        TradeTax newTradeTax = new TradeTax();
        newTradeTax.validateCreate();
        newTradeTax.uuid = tradeTax.uuid;
        newTradeTax.tradeId = tradeTax.tradeId;
        newTradeTax.taxType = tradeTax.taxType;
        newTradeTax.taxTypeName = tradeTax.taxTypeName;
        newTradeTax.taxPlan = tradeTax.taxPlan;
        newTradeTax.taxAmount = tradeTax.taxAmount;
        newTradeTax.taxableIncome = tradeTax.taxableIncome;
        newTradeTax.clientCreateTime = tradeTax.clientCreateTime;
        newTradeTax.clientUpdateTime = tradeTax.clientUpdateTime;
        newTradeTax.effectType = tradeTax.effectType;
        newTradeTax.discountType = tradeTax.discountType;
        newTradeTax.taxKind = tradeTax.taxKind;
        newTradeTax.taxMethod = tradeTax.taxMethod;
        return newTradeTax;
    }
}
