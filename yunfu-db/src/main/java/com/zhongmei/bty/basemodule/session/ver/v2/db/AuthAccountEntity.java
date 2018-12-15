package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthAccountEntity.$.TABLE_NAME)
public class AuthAccountEntity extends AuthEntity {

//    CREATE TABLE `act_account` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
//            `name` varchar(255) DEFAULT NULL COMMENT '账号名称',
//            `mobile` varchar(15) DEFAULT NULL COMMENT '账号绑定电话',
//            `email` varchar(255) DEFAULT NULL COMMENT '账号绑定邮箱',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
//            `password` varchar(50) DEFAULT NULL COMMENT '账号密码',
//            `activation_flag` tinyint(4) DEFAULT NULL COMMENT '账号激活状态，1代表已激活，0代表未激活',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
//            `creator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人id',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
//            `updator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
//            `status_flag` tinyint(4) DEFAULT '1' COMMENT '状态信息:1.正常  0.删除',
//            `enable_flag` tinyint(4) DEFAULT NULL COMMENT '账号启用状态，1代表已启用，0代表未启用',
//            `mobile_checked_flag` tinyint(4) DEFAULT NULL COMMENT '电话是否已验证，1代表已验证，0代表未验证',
//            `email_checked_flag` tinyint(4) DEFAULT NULL COMMENT '邮箱是否已验证，1代表已验证，0代表未验证',
//            `password_salt` varchar(50) DEFAULT NULL COMMENT '密码盐值',
//            `account_type` tinyint(4) DEFAULT NULL COMMENT '账户类型，保留字段，可以用作多种账户类型',
//            `password_num` varchar(20) DEFAULT '888888' COMMENT '客户端密码',
//            `organization_id` bigint(20) DEFAULT NULL COMMENT '门店id',
//            `commercial_account_number` bigint(20) DEFAULT NULL COMMENT '客户账号',
//    PRIMARY KEY (`id`),
//    UNIQUE KEY `uk_email` (`email`) USING BTREE COMMENT '邮箱唯一索引',
//    UNIQUE KEY `uk_mobile` (`mobile`) COMMENT '手机号唯一索引'
//            ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='账号表';

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_act_account";
        String NAME = "name";
        String MOBILE = "mobile";
        String EMAIL = "email";
        String PASSWORD = "password";
        String ACTIVATION_FLAG = "activation_flag";
        String MOBILE_CHECK_FLAG = "mobile_checked_flag";
        String EMAIL_CHECK_FLAG = "email_checked_flag";
        String PASSWORD_SALT = "password_salt";
        String ACCOUNT_TYPE = "account_type";
        String PASSWORD_NUM = "password_num";
        String ORGANIZATION_ID = "organization_id";
        String COMMERCIAL_ACCOUNT_NUMBER = "commercial_account_number";
        String FACE_CODE = "facecode";//add v8.16
    }

    @DatabaseField(columnName = $.NAME)
    private String name;
    @DatabaseField(columnName = $.MOBILE)
    private String mobile;
    @DatabaseField(columnName = $.EMAIL)
    private String email;
    @DatabaseField(columnName = $.PASSWORD)
    private String password;
    @DatabaseField(columnName = $.ACTIVATION_FLAG)
    private Integer activationFlag;
    @DatabaseField(columnName = $.MOBILE_CHECK_FLAG)
    private Integer mobileCheckFlag;
    @DatabaseField(columnName = $.EMAIL_CHECK_FLAG)
    private Integer emailCheckFlag;
    @DatabaseField(columnName = $.PASSWORD_SALT)
    private String passwordSalt;
    @DatabaseField(columnName = $.ACCOUNT_TYPE)
    private Integer accountType;
    @DatabaseField(columnName = $.PASSWORD_NUM)
    private String passwordNum;
    @DatabaseField(columnName = $.ORGANIZATION_ID)
    private String organizationId;
    @DatabaseField(columnName = $.COMMERCIAL_ACCOUNT_NUMBER)
    private String commercialAccountNumber;

    @DatabaseField(columnName = $.FACE_CODE)
    private String faceCode;//add v8.16  人脸信息,临时方案

    public String getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public YesOrNo getActivationFlag() {
        return ValueEnums.toEnum(YesOrNo.class, activationFlag);
    }

    public void setActivationFlag(YesOrNo activationFlag) {
        this.activationFlag = ValueEnums.toValue(activationFlag);
    }

    public Integer getMobileCheckFlag() {
        return mobileCheckFlag;
    }

    public void setMobileCheckFlag(Integer mobileCheckFlag) {
        this.mobileCheckFlag = mobileCheckFlag;
    }

    public Integer getEmailCheckFlag() {
        return emailCheckFlag;
    }

    public void setEmailCheckFlag(Integer emailCheckFlag) {
        this.emailCheckFlag = emailCheckFlag;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getPasswordNum() {
        return passwordNum;
    }

    public void setPasswordNum(String passwordNum) {
        this.passwordNum = passwordNum;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCommercialAccountNumber() {
        return commercialAccountNumber;
    }

    public void setCommercialAccountNumber(String commercialAccountNumber) {
        this.commercialAccountNumber = commercialAccountNumber;
    }
}
