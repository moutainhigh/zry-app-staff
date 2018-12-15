package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthDataPermissionEntity.$.TABLE_NAME)
public class AuthDataPermissionEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_data_permission` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `resource_id` bigint(20) DEFAULT NULL COMMENT '资源ID',
//            `org_id` bigint(20) NOT NULL COMMENT '机构ID',
//            `account_id` bigint(20) NOT NULL COMMENT '账号ID',
//            `role_id` bigint(20) NOT NULL COMMENT '角色ID',
//            `permission_group_id` bigint(20) DEFAULT NULL COMMENT '权限组ID',
//            `permission_subgroup_id` bigint(20) DEFAULT NULL COMMENT '权限分组目录Id',
//            `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//            `configurable` tinyint(4) NOT NULL COMMENT '配置类型:1数值类，2机构类，3账号类，4无',
//            `data_type` tinyint(4) NOT NULL COMMENT '数据类型: 值类1区间，2包含 ；机构类 3仅自己，4仅所在机构，5所在机构及下属机构，6选择机构',
//    PRIMARY KEY (`id`),
//    KEY `idx_resource_id` (`resource_id`),
//    KEY `idx_org_id` (`org_id`),
//    KEY `idx_role_id` (`role_id`),
//    KEY `idx_permission_group_id` (`permission_group_id`),
//    KEY `idx_permission_id` (`permission_id`),
//    KEY `idx_configurable` (`configurable`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=13401924682768910 DEFAULT CHARSET=utf8 COMMENT='数据权限头表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_data_permission";
        String RESOURCE_ID = "resource_id";
        String ORG_ID = "org_id";
        String ACCOUNT_ID = "account_id";
        String ROLE_ID = "role_id";
        String PERMISSION_GROUP_ID = "permission_group_id";
        String PERMISSION_SUB_GROUP_ID = "permission_subgroup_id";
        String PERMISSION_ID = "permission_id";
        String CONFIGURATION = "configurable";
        String DATA_TYPE = "data_type";
    }

    @DatabaseField(columnName = $.RESOURCE_ID)
    private Long resourceId;
    @DatabaseField(columnName = $.ORG_ID, canBeNull = false)
    private Long orgId;
    @DatabaseField(columnName = $.ACCOUNT_ID, canBeNull = false)
    private Long accountId;
    @DatabaseField(columnName = $.ROLE_ID, canBeNull = false)
    private Long roleId;
    @DatabaseField(columnName = $.PERMISSION_ID, canBeNull = false)
    private Long permissionId;
    @DatabaseField(columnName = $.PERMISSION_GROUP_ID)
    private Long permissionGroupId;
    @DatabaseField(columnName = $.PERMISSION_SUB_GROUP_ID)
    private Long permissionSubGroupId;
    @DatabaseField(columnName = $.CONFIGURATION, canBeNull = false)
    private Integer configurable;
    @DatabaseField(columnName = $.DATA_TYPE, canBeNull = false)
    private Integer dataType;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(Long permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

    public Long getPermissionSubGroupId() {
        return permissionSubGroupId;
    }

    public void setPermissionSubGroupId(Long permissionSubGroupId) {
        this.permissionSubGroupId = permissionSubGroupId;
    }

    public Integer getConfigurable() {
        return configurable;
    }

    public void setConfigurable(Integer configurable) {
        this.configurable = configurable;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
