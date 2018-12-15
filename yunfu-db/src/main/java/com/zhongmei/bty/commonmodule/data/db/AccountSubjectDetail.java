package com.zhongmei.bty.commonmodule.data.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AccountSubjectDetail.$.TABLE_NAME)
public class AccountSubjectDetail extends IdEntityBase {
//    CREATE TABLE `account_subject_detail` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
//            `brand_identy` bigint(20) NOT NULL COMMENT '品牌id : 品牌id',
//            `shop_identy` bigint(20) NOT NULL COMMENT '门店标识',
//            `account_subject_id` bigint(20) DEFAULT NULL COMMENT 'account_subject表的主键',
//            `biz_date` date NOT NULL COMMENT '营业日期',
//            `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1:收入项,2:支出项',
//            `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易记录金额，收入是为正，支出时为负',
//            `pay_mode_id` bigint(20) NOT NULL COMMENT '支付方式ID(cashTypeId)：-1:会员卡余额,-2:优惠劵(废弃),-3:现金,-4:银行卡,-5:微信,-6:支付宝,-7:百度钱包,-8:百度直达号,-9:积分抵现(废弃),-10:百度地图,-11:银联刷卡,-12:百糯到店付,-13:百度外卖,-14:饿了么,-15:实体卡支付,-16:大众点评,-17:美团外卖,-18:点评团购券,-19:点评闪惠-20:临时卡余额 -21:糯米点菜',
//            `pay_mode_name` varchar(20) DEFAULT '' COMMENT '支付方式名称，冗余字段',
//            `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务器创建时间 : 服务器创建时间',
//            `server_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务器更新时间 : 服务器更新时间',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态标识 : 状态标识 1:启用 2:禁用',
//            `creator_name` varchar(32) DEFAULT NULL COMMENT '创建者名称 : 创建者名称',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者id : 创建者id',
//            `updator_name` varchar(32) DEFAULT NULL COMMENT '最后修改者姓名 : 最后修改者姓名',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者id : 更新者id',
//            `pay_model_group` bigint(20) DEFAULT NULL COMMENT 'payment_mode_brand.payment_mode_type  1：在线支付  2：现金  3：银行卡  4：储值卡  5：券  6：其它收款 \r',
//            `device_identy` varchar(36) DEFAULT NULL COMMENT '设备标识',
//    PRIMARY KEY (`id`),
//    KEY `idx_brand_identy_shop_biz_date` (`brand_identy`,`shop_identy`,`biz_date`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=93411133893079041 DEFAULT CHARSET=utf8 COMMENT='收支明细'

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
