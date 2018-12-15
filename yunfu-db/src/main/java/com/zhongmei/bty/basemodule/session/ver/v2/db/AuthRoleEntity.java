package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthRoleEntity.$.TABLE_NAME)
public class AuthRoleEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_role` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `role_name` varchar(60) NOT NULL COMMENT '角色名称',
//            `role_code` varchar(20) DEFAULT NULL COMMENT '角色编码',
//            `role_description` varchar(200) DEFAULT NULL COMMENT '角色描述',
//            `role_type` tinyint(4) NOT NULL COMMENT '角色类型：1管理角色 、2业务角色、3模板角色 ',
//            `source_org_id` bigint(20) NOT NULL COMMENT '来源机构Id',
//            `source_id` bigint(20) DEFAULT NULL COMMENT '来源ID',
//            `create_type` tinyint(4) NOT NULL COMMENT '创建类型：1自建、2透传、3模板，4系统自定义',
//            `org_id` bigint(20) DEFAULT NULL COMMENT '所属机构',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//    PRIMARY KEY (`id`),
//    KEY `idx_role_type` (`role_type`),
//    KEY `idx_org_id` (`org_id`),
//    KEY `idx_source_id` (`source_id`),
//    KEY `idx_create_type` (`create_type`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=18111084620490244 DEFAULT CHARSET=utf8 COMMENT='角色表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_role";
        String ROLE_NAME = "role_name";
        String ROLE_CODE = "role_code";
        String ROLE_DESCRIPTION = "role_description";
        String ROLE_TYPE = "role_type";
        String SOURCE_ORG_ID = "source_org_id";
        String SOURCE_ID = "source_id";
        String CREATE_TYPE = "create_type";
        String ORG_ID = "org_id";
    }

    @DatabaseField(columnName = $.ROLE_NAME, canBeNull = false)
    private String roleName;
    @DatabaseField(columnName = $.ROLE_CODE)
    private String roleCode;
    @DatabaseField(columnName = $.ROLE_DESCRIPTION)
    private String roleDescription;
    @DatabaseField(columnName = $.ROLE_TYPE, canBeNull = false)
    private Integer roleType;
    @DatabaseField(columnName = $.SOURCE_ORG_ID, canBeNull = false)
    private Long sourceOrgId;
    @DatabaseField(columnName = $.SOURCE_ID, canBeNull = false)
    private Long sourceId;
    @DatabaseField(columnName = $.CREATE_TYPE, canBeNull = false)
    private Integer createType;
    @DatabaseField(columnName = $.ORG_ID)
    private Long orgId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Long getSourceOrgId() {
        return sourceOrgId;
    }

    public void setSourceOrgId(Long sourceOrgId) {
        this.sourceOrgId = sourceOrgId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
