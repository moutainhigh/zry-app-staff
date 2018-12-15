package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthAccountRoleEntity.$.TABLE_NAME)
public class AuthAccountRoleEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_account_role` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `role_id` bigint(20) NOT NULL COMMENT '角色ID',
//            `org_id` bigint(20) NOT NULL COMMENT '机构ID',
//            `account_id` bigint(20) NOT NULL COMMENT '账号ID',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//    PRIMARY KEY (`id`),
//    KEY `idx_role_id` (`role_id`),
//    KEY `idx_org_id` (`org_id`),
//    KEY `idx_account_id` (`account_id`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=18111090756756139 DEFAULT CHARSET=utf8 COMMENT='账号角色关联表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_account_role";
        String ROLE_ID = "role_id";
        String ORG_ID = "org_id";
        String ACCOUNT_ID = "account_id";
    }

    @DatabaseField(columnName = $.ROLE_ID, canBeNull = false)
    private Long roleId;
    @DatabaseField(columnName = $.ORG_ID, canBeNull = false)
    private Long orgId;
    @DatabaseField(columnName = $.ACCOUNT_ID, canBeNull = false)
    private Long accountId;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
