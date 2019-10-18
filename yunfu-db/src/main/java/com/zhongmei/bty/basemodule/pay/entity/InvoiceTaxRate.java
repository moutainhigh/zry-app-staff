package com.zhongmei.bty.basemodule.pay.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;

import java.math.BigDecimal;


@DatabaseTable(tableName = "invoice_tax_rate")
public class InvoiceTaxRate extends CommonEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends CommonEntityBase.$ {

        String invoiceId = "invoiceId";


        String invoiceName = "invoice_name";


        String invoiceCode = "invoice_code";


        String taxRate = "tax_rate";


        String isDefault = "is_default";
    }

    @DatabaseField(columnName = "invoiceId", canBeNull = false)
    private Long invoiceId;

    @DatabaseField(columnName = "invoice_name", canBeNull = false)
    private String invoiceName;

    @DatabaseField(columnName = "invoice_code", canBeNull = false)
    private String invoiceCode;

    @DatabaseField(columnName = "tax_rate", canBeNull = false)
    private BigDecimal taxRate;

    @DatabaseField(columnName = "is_default", canBeNull = false)
    private Integer isDefault;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Bool getIsDefault() {
        return ValueEnums.toEnum(Bool.class, isDefault);
    }

    public void setIsDefault(Bool isDefault) {
        this.isDefault = ValueEnums.toValue(isDefault);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(invoiceId, invoiceName, invoiceCode, taxRate, isDefault);
    }
}
