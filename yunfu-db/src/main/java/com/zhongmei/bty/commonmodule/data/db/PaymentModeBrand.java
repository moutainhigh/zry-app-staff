package com.zhongmei.bty.commonmodule.data.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


@DatabaseTable(tableName = PaymentModeBrand.$.TABLE_NAME)
public class PaymentModeBrand extends IdEntityBase {


    public interface $ extends IdEntityBase.$ {
        String TABLE_NAME = "payment_mode_brand";
        String ERP_MODE_ID = "erp_mode_id";
        String NAME = "name";
        String ALIAS_NAME = "alias_name";
        String FACE_VALUE = "face_value";
        String ACTUAL_VALUE = "actual_value";
        String PAYMENT_MODE_TYPE = "payment_mode_type";
        String IS_CHANGE = "is_change";
        String IS_DISCOUNT = "is_discount";
        String SORT = "sort";
        String BRAND_IDENTITY = "brand_identity";
        String IS_INVOICE = "is_invoice";
        String IS_REFUND = "is_refund";
        String IS_CURE = "is_cure";
        String ENABLE_FLAG = "enable_flag";
        String STATUS_FLAG = "status_flag";
        String SERVER_CREATE_TIME = "server_create_time";
        String SERVER_UPDATE_TIME = "server_update_time";
        String CREATOR_NAME = "creator_name";
        String CREATOR_ID = "creator_id";
        String UPDATER_NAME = "updater_name";
        String UPDATER_ID = "updater_id";
        String IS_SETTLEMENT = "is_settlement";
    }

    @DatabaseField(columnName = $.ERP_MODE_ID)
    private Long erpModeId;
    @DatabaseField(columnName = $.NAME)
    private String name;
    @DatabaseField(columnName = $.ALIAS_NAME)
    private String aliasName;
    @DatabaseField(columnName = $.FACE_VALUE)
    private BigDecimal faceValue;
    @DatabaseField(columnName = $.ACTUAL_VALUE)
    private BigDecimal actualValue;
    @DatabaseField(columnName = $.PAYMENT_MODE_TYPE)
    private Integer paymentModeType;
    @DatabaseField(columnName = $.IS_CHANGE)
    private Integer isChange;
    @DatabaseField(columnName = $.IS_DISCOUNT)
    private Integer isDiscount;
    @DatabaseField(columnName = $.SORT)
    private Integer sort;
    @DatabaseField(columnName = $.BRAND_IDENTITY)
    private Long brandIdentity;
    @DatabaseField(columnName = $.IS_INVOICE)
    private Integer isInvoice;
    @DatabaseField(columnName = $.IS_REFUND)
    private Integer isRefund;
    @DatabaseField(columnName = $.IS_CURE)
    private Integer isCure;
    @DatabaseField(columnName = $.ENABLE_FLAG)
    private Integer enableFlag;
    @DatabaseField(columnName = $.STATUS_FLAG)
    private Integer statusFlag;
    @DatabaseField(columnName = $.SERVER_CREATE_TIME)
    private Long serverCreateTime;
    @DatabaseField(columnName = $.SERVER_UPDATE_TIME)
    private Long serverUpdateTime;
    @DatabaseField(columnName = $.CREATOR_NAME)
    private String creatorName;
    @DatabaseField(columnName = $.CREATOR_ID)
    private Long creatorId;
    @DatabaseField(columnName = $.UPDATER_NAME)
    private String updaterName;
    @DatabaseField(columnName = $.UPDATER_ID)
    private Long updaterId;
    @DatabaseField(columnName = $.IS_SETTLEMENT)
    private Integer isSettlement;

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

    public BigDecimal getActualValue() {
        return actualValue;
    }

    public void setActualValue(BigDecimal actualValue) {
        this.actualValue = actualValue;
    }

    public Integer getPaymentModeType() {
        return paymentModeType;
    }

    public void setPaymentModeType(Integer paymentModeType) {
        this.paymentModeType = paymentModeType;
    }

    public Integer getIsChange() {
        return isChange;
    }

    public void setIsChange(Integer isChange) {
        this.isChange = isChange;
    }

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getBrandIdentity() {
        return brandIdentity;
    }

    public void setBrandIdentity(Long brandIdentity) {
        this.brandIdentity = brandIdentity;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getIsCure() {
        return isCure;
    }

    public void setIsCure(Integer isCure) {
        this.isCure = isCure;
    }

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
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

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Integer getIsSettlement() {
        return isSettlement;
    }

    public void setIsSettlement(Integer isSettlement) {
        this.isSettlement = isSettlement;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
