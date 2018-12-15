package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthPermissionEntity.$.TABLE_NAME)
public class AuthPermissionEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_permission` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `permission_name` varchar(200) NOT NULL COMMENT '权限名称',
//            `permission_code` varchar(20) DEFAULT NULL COMMENT '权限编码',
//            `classify` tinyint(4) NOT NULL COMMENT '分类：1管理类，2业务类,3特殊类',
//            `business` tinyint(4) NOT NULL COMMENT '业态：1餐饮，2零售，3餐饮+零售',
//            `org_type` tinyint(4) NOT NULL COMMENT '机构类型：1公司，2门店，3配送站',
//            `platform` tinyint(4) DEFAULT NULL COMMENT '产品线',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//            `zone` tinyint(4) DEFAULT NULL COMMENT '分区',
//    PRIMARY KEY (`id`),
//    KEY `idx_platform` (`platform`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=17725360180200142 DEFAULT CHARSET=utf8 COMMENT='权限表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_permission";
        String PERMISSION_NAME = "permission_name";
        String PERMISSION_CODE = "permission_code";
        String CLASSIFY = "classify";
        String BUSINESS = "business";
        String ORG_TYPE = "org_type";
        String PLATFORM = "platform";
        String ZONE = "zone";
    }

    @DatabaseField(columnName = $.PERMISSION_NAME, canBeNull = false)
    private String permissionName;
    @DatabaseField(columnName = $.PERMISSION_CODE)
    private String permissionCode;
    @DatabaseField(columnName = $.CLASSIFY, canBeNull = false)
    private Integer classify;
    @DatabaseField(columnName = $.BUSINESS, canBeNull = false)
    private Integer business;
    @DatabaseField(columnName = $.ORG_TYPE, canBeNull = false)
    private Integer orgType;
    @DatabaseField(columnName = $.PLATFORM)
    private Integer platform;
    @DatabaseField(columnName = $.ZONE)
    private Integer zone;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public Integer getBusiness() {
        return business;
    }

    public void setBusiness(Integer business) {
        this.business = business;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getZone() {
        return zone;
    }

    public void setZone(Integer zone) {
        this.zone = zone;
    }
}
