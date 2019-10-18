package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceStatus;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;


@DatabaseTable(tableName = "invoice")
public class Invoice extends IdEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {

        String statusFlag = "status_flag";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String brandIdenty = "brand_identy";


        String shopIdenty = "shop_identy";


        String type = "type";


        String source = "source";


        String uuid = "uuid";


        String orderId = "order_id";


        String drawerNo = "drawer_no";


        String drawer = "drawer";


        String totalAmount = "total_amount";


        String amount = "amount";


        String taxAmount = "tax_amount";


        String taxpayerName = "taxpayer_name";


        String taxpayerNo = "taxpayer_no";


        String taxpayerAddr = "taxpayer_addr";


        String taxpayerTel = "taxpayer_tel";


        String taxpayerBankAccount = "taxpayer_bank_account";


        String status = "status";


        String invoiceTitle = "invoice_title";


        String collectorPhone = "collector_phone";


        String issueCode = "issue_code";


        String revoke_code = "revoke_code";


        String invoiceNo = "invoice_no";


        String invoiceCode = "invoice_code";


        String invoiceDate = "invoice_date";


        String pdfUrl = "pdf_url";


        String imgUrl = "img_url";


        String reIssue = "re_issue";
    }


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "source", canBeNull = false)
    private Integer source;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "order_id")
    private Long orderId;

    @DatabaseField(columnName = "drawer_no")
    private String drawerNo;

    @DatabaseField(columnName = "drawer")
    private String drawer;

    @DatabaseField(columnName = "total_amount", canBeNull = false)
    private BigDecimal totalAmount;

    @DatabaseField(columnName = "amount")
    private BigDecimal amount;

    @DatabaseField(columnName = "tax_amount", canBeNull = false)
    private BigDecimal taxAmount;

    @DatabaseField(columnName = "taxpayer_name", canBeNull = false)
    private String taxpayerName;

    @DatabaseField(columnName = "taxpayer_no", canBeNull = false)
    private String taxpayerNo;

    @DatabaseField(columnName = "taxpayer_addr")
    private String taxpayerAddr;

    @DatabaseField(columnName = "taxpayer_tel")
    private String taxpayerTel;

    @DatabaseField(columnName = "taxpayer_bank_account")
    private String taxpayerBankAccount;

    @DatabaseField(columnName = "status", canBeNull = false)
    private Integer status;

    @DatabaseField(columnName = "invoice_title")
    private String invoiceTitle;

    @DatabaseField(columnName = "collector_phone")
    private String collectorPhone;

    @DatabaseField(columnName = "issue_code")
    private String issueCode;

    @DatabaseField(columnName = "revoke_code")
    private String revoke_code;

    @DatabaseField(columnName = "invoice_no")
    private String invoiceNo;

    @DatabaseField(columnName = "invoice_code")
    private String invoiceCode;

    @DatabaseField(columnName = "invoice_date")
    private Long invoiceDate;

    @DatabaseField(columnName = "pdf_url")
    private String pdfUrl;

    @DatabaseField(columnName = "img_url")
    private String imgUrl;

    @DatabaseField(columnName = "re_issue")
    private Integer reIssue;

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public YesOrNo getStatusFlag() {
        return ValueEnums.toEnum(YesOrNo.class, statusFlag);
    }

    public void setStatusFlag(YesOrNo statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public InvoiceType getType() {
        return ValueEnums.toEnum(InvoiceType.class, type);
    }

    public void setType(InvoiceType invoiceType) {
        this.type = ValueEnums.toValue(invoiceType);
    }

    public InvoiceSource getSource() {
        return ValueEnums.toEnum(InvoiceSource.class, source);
    }

    public void setSource(InvoiceSource invoiceSource) {
        this.source = ValueEnums.toValue(invoiceSource);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDrawerNo() {
        return drawerNo;
    }

    public void setDrawerNo(String drawerNo) {
        this.drawerNo = drawerNo;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getTaxpayerNo() {
        return taxpayerNo;
    }

    public void setTaxpayerNo(String taxpayerNo) {
        this.taxpayerNo = taxpayerNo;
    }

    public String getTaxpayerAddr() {
        return taxpayerAddr;
    }

    public void setTaxpayerAddr(String taxpayerAddr) {
        this.taxpayerAddr = taxpayerAddr;
    }

    public String getTaxpayerTel() {
        return taxpayerTel;
    }

    public void setTaxpayerTel(String taxpayerTel) {
        this.taxpayerTel = taxpayerTel;
    }

    public String getTaxpayerBankAccount() {
        return taxpayerBankAccount;
    }

    public void setTaxpayerBankAccount(String taxpayerBankAccount) {
        this.taxpayerBankAccount = taxpayerBankAccount;
    }

    public InvoiceStatus getStatus() {
        return ValueEnums.toEnum(InvoiceStatus.class, status);
    }

    public void setStatus(InvoiceStatus invoiceStatus) {
        this.status = ValueEnums.toValue(invoiceStatus);
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getCollectorPhone() {
        return collectorPhone;
    }

    public void setCollectorPhone(String collectorPhone) {
        this.collectorPhone = collectorPhone;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode;
    }

    public String getRevoke_code() {
        return revoke_code;
    }

    public void setRevoke_code(String revoke_code) {
        this.revoke_code = revoke_code;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public YesOrNo getReIssue() {
        return ValueEnums.toEnum(YesOrNo.class, reIssue);
    }

    public void setReIssue(YesOrNo reIssue) {
        this.reIssue = ValueEnums.toValue(reIssue);
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(YesOrNo.YES, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, brandIdenty, shopIdenty, type,
                source, uuid, totalAmount, taxAmount, taxpayerName, taxpayerNo, status);
    }
}
