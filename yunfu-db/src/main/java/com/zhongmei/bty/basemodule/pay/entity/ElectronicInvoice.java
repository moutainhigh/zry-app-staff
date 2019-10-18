package com.zhongmei.bty.basemodule.pay.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "electronic_invoice")
public class ElectronicInvoice extends BasicEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {

        String shopIdenty = "shop_identy";


        String taxpayerName = "taxpayer_name";


        String taxpayerNumber = "taxpayer_number";


        String address = "address";


        String telephone = "telephone";


        String bankOfDeposit = "bank_of_deposit";


        String accountNumber = "account_number";


        String creatorName = "creator_name";


        String creatorId = "creator_id";


        String updatorName = "updator_name";


        String updator_id = "updator_id";
    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "taxpayer_name", canBeNull = false)
    private String taxpayerName;

    @DatabaseField(columnName = "taxpayer_number", canBeNull = false)
    private String taxpayerNumber;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "telephone")
    private String telephone;

    @DatabaseField(columnName = "bank_of_deposit")
    private String bankOfDeposit;

    @DatabaseField(columnName = "account_number")
    private String accountNumber;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "updator_id")
    private String updatorId;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBankOfDeposit() {
        return bankOfDeposit;
    }

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, taxpayerName, taxpayerNumber);
    }
}
