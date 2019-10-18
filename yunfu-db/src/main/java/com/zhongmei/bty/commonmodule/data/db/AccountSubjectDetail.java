package com.zhongmei.bty.commonmodule.data.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


@DatabaseTable(tableName = AccountSubjectDetail.$.TABLE_NAME)
public class AccountSubjectDetail extends IdEntityBase {

    public interface $ extends IdEntityBase.$ {
        String TABLE_NAME = "account_subject_detail";
        String BRAND_IDENTITY = "brand_identity";
        String SHOP_IDENTITY = "shop_identity";
        String DEVICE_IDENTITY = "device_identity";
        String BIZ_DATE = "biz_date";
        String TYPE = "type";
        String AMOUNT = "amount";
        String ACCOUNT_SUBJECT_ID = "account_subject_id";
        String PAY_MODE_ID = "pay_mode_id";
        String PAY_MODE_NAME = "pay_mode_name";
        String PAY_MODE_GROUP = "pay_mode_group";
        String CREATOR_ID = "creator_id";
        String CREATOR_NAME = "creator_name";
        String UPDATER_ID = "updater_id";
        String UPDATER_NAME = "updater_name";
        String SERVER_CREATE_TIME = "server_create_time";
        String SERVER_UPDATE_TIME = "server_update_time";
        String CLIENT_CREATE_TIME = "client_create_time";
        String CLIENT_UPDATE_TIME = "client_update_time";
        String STATUS_FLAG = "status_flag";
    }

    @DatabaseField(columnName = $.BRAND_IDENTITY)
    private Long brandIdentity;
    @DatabaseField(columnName = $.SHOP_IDENTITY)
    private Long shopIdentity;
    @DatabaseField(columnName = $.DEVICE_IDENTITY)
    private String deviceIdentity;
    @DatabaseField(columnName = $.BIZ_DATE)
    private String bizDate;
    @DatabaseField(columnName = $.TYPE)
    private Integer type;
    @DatabaseField(columnName = $.AMOUNT)
    private BigDecimal amount;
    @DatabaseField(columnName = $.ACCOUNT_SUBJECT_ID)
    private Long accountSubjectId;
    @DatabaseField(columnName = $.PAY_MODE_ID)
    private Long payModeId;
    @DatabaseField(columnName = $.PAY_MODE_NAME)
    private String payModeName;
    @DatabaseField(columnName = $.PAY_MODE_GROUP)
    private Long payModeGroup;
    @DatabaseField(columnName = $.CREATOR_ID)
    private Long creatorId;
    @DatabaseField(columnName = $.CREATOR_NAME)
    private String creatorName;
    @DatabaseField(columnName = $.UPDATER_ID)
    private Long updaterId;
    @DatabaseField(columnName = $.UPDATER_NAME)
    private Long updaterName;
    @DatabaseField(columnName = $.SERVER_CREATE_TIME)
    private Long serverCreateTime;
    @DatabaseField(columnName = $.SERVER_UPDATE_TIME)
    private Long serverUpdateTime;
    @DatabaseField(columnName = $.CLIENT_CREATE_TIME)
    private Long clientCreateTime;
    @DatabaseField(columnName = $.CLIENT_UPDATE_TIME)
    private Long clientUpdateTime;
    @DatabaseField(columnName = $.STATUS_FLAG)
    private Integer statusFlag;

    public Long getBrandIdentity() {
        return brandIdentity;
    }

    public void setBrandIdentity(Long brandIdentity) {
        this.brandIdentity = brandIdentity;
    }

    public Long getShopIdentity() {
        return shopIdentity;
    }

    public void setShopIdentity(Long shopIdentity) {
        this.shopIdentity = shopIdentity;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAccountSubjectId() {
        return accountSubjectId;
    }

    public void setAccountSubjectId(Long accountSubjectId) {
        this.accountSubjectId = accountSubjectId;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public Long getPayModeGroup() {
        return payModeGroup;
    }

    public void setPayModeGroup(Long payModeGroup) {
        this.payModeGroup = payModeGroup;
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

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Long getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(Long updaterName) {
        this.updaterName = updaterName;
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

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }
}
