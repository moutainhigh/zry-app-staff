package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthUserEntity.$.TABLE_NAME)
public class AuthUserEntity extends AuthEntity implements IAuthUser {

//	CREATE TABLE `ps_auth_user` (
//			`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//			`user_name` varchar(20) NOT NULL COMMENT '用户名称',
//			`gender` tinyint(4) NOT NULL COMMENT '性别',
//			`birthday` date DEFAULT NULL COMMENT '生日',
//			`icon` varchar(200) DEFAULT NULL COMMENT '头像',
//			`mobile` varchar(20) NOT NULL COMMENT '手机号',
//			`email` varchar(30) DEFAULT NULL COMMENT '邮箱',
//			`QQ` bigint(20) DEFAULT NULL COMMENT 'QQ',
//			`address` varchar(300) DEFAULT NULL COMMENT '地址',
//			`account_id` bigint(20) DEFAULT NULL COMMENT '账号ID',
//			`password` varchar(20) DEFAULT NULL COMMENT '密码',
//			`salt` varchar(10) DEFAULT NULL COMMENT '颜值',
//			`org_id` bigint(20) NOT NULL COMMENT '机构ID',
//			`server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//			`server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//			`creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//			`updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//			`status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//			`enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//			`account` varchar(40) DEFAULT NULL COMMENT '账号',
//	PRIMARY KEY (`id`)
//) ENGINE=InnoDB AUTO_INCREMENT=16682856531189767 DEFAULT CHARSET=utf8 COMMENT='用户表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_user";
        String NAME = "user_name";
        String GENDER = "gender";
        String BIRTHDAY = "birthday";
        String ICON = "icon";
        String MOBILE = "mobile";
        String EMAIL = "email";
        String QQ = "QQ";
        String ADDRESS = "address";
        String ACCOUNT_ID = "account_id";
        String PASSWORD = "password";
        String SALT = "salt";
        String ORG_ID = "org_id";
        String ACCOUNT = "account";
    }

    @DatabaseField(columnName = $.NAME, canBeNull = false)
    private String userName;
    @DatabaseField(columnName = $.GENDER, canBeNull = false)
    private Integer gender;
    @DatabaseField(columnName = $.BIRTHDAY)
    private String birthday;
    @DatabaseField(columnName = $.ICON)
    private String icon;
    @DatabaseField(columnName = $.MOBILE, canBeNull = false)
    private String mobile;
    @DatabaseField(columnName = $.EMAIL)
    private String email;
    @DatabaseField(columnName = $.QQ)
    private String qq;
    @DatabaseField(columnName = $.ADDRESS)
    private String address;
    @DatabaseField(columnName = $.ACCOUNT_ID, canBeNull = false)
    private Long accountId;
    @DatabaseField(columnName = $.PASSWORD)
    private String password;
    @DatabaseField(columnName = $.SALT)
    private String salt;
    @DatabaseField(columnName = $.ORG_ID, canBeNull = false)
    private Long orgId;
    @DatabaseField(columnName = $.ACCOUNT)
    private String account;

    @Override
    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
