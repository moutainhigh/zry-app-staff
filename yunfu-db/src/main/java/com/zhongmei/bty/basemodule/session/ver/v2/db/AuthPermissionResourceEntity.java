package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthPermissionResourceEntity.$.TABLE_NAME)
public class AuthPermissionResourceEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_permission_resource` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `permission_id` bigint(20) NOT NULL COMMENT '权限ID，',
//            `resource_id` bigint(20) NOT NULL COMMENT '资源ID，',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//    PRIMARY KEY (`id`),
//    KEY `idx_permission_id` (`permission_id`),
//    KEY `idx_resource_id` (`resource_id`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=20263976579588097 DEFAULT CHARSET=utf8 COMMENT='权限资源关系表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_permission_resource";
        String PERMISSION_ID = "permission_id";
        String RESOURCE_ID = "resource_id";
    }

    @DatabaseField(columnName = $.PERMISSION_ID, canBeNull = false)
    private Long permissionId;
    @DatabaseField(columnName = $.RESOURCE_ID, canBeNull = false)
    private Long resourceId;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
