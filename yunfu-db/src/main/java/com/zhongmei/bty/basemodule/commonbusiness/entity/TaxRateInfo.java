package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;


@DatabaseTable(tableName = "tax_rate_info")
public class TaxRateInfo extends EntityBase<Long> {
    private static final long serialVersionUID = 1L;

    public interface $ {
        String brandId = "brand_id";
        String shopId = "shop_id";
                String taxId = "tax_id";
        String taxCode = "tax_code";
                String taxDesc = "tax_desc";
        String taxRate = "tax_rate";
        String effectType = "effect_type";
        String discountType = "discount_type";
        String taxKind = "tax_kind";
        String taxMethod = "tax_method";
        String statusFlag = "status_flag";
        String serverUpdateTime = "server_update_time";
    }

    @DatabaseField(columnName = $.brandId)
    Long brandId;
    @DatabaseField(columnName = $.shopId)
    Long shopId;
            @DatabaseField(columnName = $.taxId, id = true)
    Long taxId;
    @DatabaseField(columnName = $.taxCode)
    String taxCode;
            @DatabaseField(columnName = $.taxDesc)
    String taxDesc;
    @DatabaseField(columnName = $.taxRate)
    BigDecimal taxRate;
    @DatabaseField(columnName = $.effectType)
    Integer effectType;
    @DatabaseField(columnName = $.discountType)
    Integer discountType;
    @DatabaseField(columnName = $.taxKind)
    String taxKind;
    @DatabaseField(columnName = $.taxMethod)
    String taxMethod;
    @DatabaseField(columnName = $.statusFlag)
    Integer statusFlag;
    @DatabaseField(columnName = $.serverUpdateTime)
    Long serverUpdateTime;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public boolean isTaxSupplyOpen() {
        return "Supply".equals(taxMethod) && Utils.equals(effectType, 1);
    }

    public boolean isDiscountAfter() {
        return Utils.equals(discountType, 1);
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxDesc() {
        return taxDesc;
    }

    public void setTaxDesc(String taxDesc) {
        this.taxDesc = taxDesc;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long pkValue() {
        return taxId;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public TradeTax toTradeTax(Long tradeId) {
        return toTradeTax(tradeId, this);
    }

    private TradeTax toTradeTax(Long tradeId, TaxRateInfo taxRateInfo) {
        TradeTax tradeTax = new TradeTax();
        tradeTax.validateCreate();
        tradeTax.setTradeId(tradeId);
        tradeTax.setUuid(SystemUtils.genOnlyIdentifier());
        tradeTax.setTaxType(taxRateInfo.getTaxCode());
        tradeTax.setTaxPlan(taxRateInfo.getTaxRate().toString());
        tradeTax.setTaxTypeName(taxRateInfo.getTaxDesc());
        tradeTax.setTaxAmount(BigDecimal.ZERO);
        tradeTax.setTaxableIncome(BigDecimal.ZERO);
        tradeTax.setEffectType(taxRateInfo.getEffectType());
        tradeTax.setDiscountType(taxRateInfo.getDiscountType());
        tradeTax.setTaxKind(taxRateInfo.getTaxKind());
        tradeTax.setTaxMethod(taxRateInfo.getTaxMethod());
        return tradeTax;
    }
}
