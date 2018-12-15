package com.zhongmei.bty.basemodule.session.ver.v2.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = AuthResourceEntity.$.TABLE_NAME)
public class AuthResourceEntity extends AuthEntity {

//    CREATE TABLE `ps_auth_resource` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
//            `resource_code` varchar(250) DEFAULT NULL COMMENT '资源编码',
//            `resource_name` varchar(50) NOT NULL COMMENT '资源名称',
//            `resource_type` tinyint(4) NOT NULL COMMENT '资源类型:1页面控制，2按钮控制,3特殊资源',
//            `pid` bigint(20) NOT NULL COMMENT '上级ID',
//            `url` varchar(400) NOT NULL COMMENT '资源路径',
//            `level_id` varchar(45) DEFAULT NULL COMMENT '层级',
//            `configurable` tinyint(4) NOT NULL COMMENT '配置类型:1数值类，2机构类，3账号类，4无\r\n\r\n',
//            `org_type` tinyint(4) NOT NULL COMMENT '机构类型：1公司，2门店，3配送站',
//            `business` tinyint(4) NOT NULL COMMENT '业态:1餐饮，2零售，3餐饮+零售',
//            `use_type` tinyint(4) NOT NULL COMMENT '使用者:1用户，2',
//            `platform` tinyint(20) DEFAULT NULL COMMENT '产品线',
//            `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
//            `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
//            `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '更新者ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '删除标识:1正常，0逻辑删除',
//            `enable_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用标识:1启用，0禁用',
//            `validate_regex` varchar(100) DEFAULT NULL COMMENT '验证正则表达式',
//            `validate_describe` varchar(255) DEFAULT NULL COMMENT '验证描述',
//            `sort` int(11) NOT NULL COMMENT '排序号',
//    PRIMARY KEY (`id`),
//    KEY `idx_configurable` (`configurable`),
//    KEY `idx_platform` (`platform`)
//            ) ENGINE=InnoDB AUTO_INCREMENT=20263911114891265 DEFAULT CHARSET=utf8 COMMENT='资源表'

    public interface $ extends AuthEntity.$ {
        String TABLE_NAME = "ps_auth_resource";
        String RESOURCE_CODE = "resource_code";
        String RESOURCE_NAME = "resource_name";
        String RESOURCE_TYPE = "resource_type";
        String PARENT_ID = "pid";
        String URL = "url";
        String LEVEL_ID = "level_id";
        String CONFIGURATION = "configurable";
        String ORG_TYPE = "org_type";
        String BUSINESS = "business";
        String USE_TYPE = "use_type";
        String PLATFORM = "platform";
        String VALIDATE_REGEX = "validate_regex";
        String VALIDATE_DESCRIBE = "validate_describe";
        String SORT = "sort";
    }

    @DatabaseField(columnName = $.RESOURCE_CODE)
    private String resourceCode;
    @DatabaseField(columnName = $.RESOURCE_NAME, canBeNull = false)
    private String resourceName;
    @DatabaseField(columnName = $.RESOURCE_TYPE, canBeNull = false)
    private Integer resourceType;
    @DatabaseField(columnName = $.PARENT_ID)
    private Long pid;
    @DatabaseField(columnName = $.URL)
    private String url;
    @DatabaseField(columnName = $.LEVEL_ID, canBeNull = false)
    private String levelId;
    @DatabaseField(columnName = $.CONFIGURATION, canBeNull = false)
    private Integer configurable;
    @DatabaseField(columnName = $.ORG_TYPE, canBeNull = false)
    private Integer orgType;
    @DatabaseField(columnName = $.BUSINESS, canBeNull = false)
    private Integer business;
    @DatabaseField(columnName = $.USE_TYPE, canBeNull = false)
    private Integer useType;
    @DatabaseField(columnName = $.PLATFORM)
    private Integer platform;
    @DatabaseField(columnName = $.VALIDATE_REGEX)
    private String validateRegex;
    @DatabaseField(columnName = $.VALIDATE_DESCRIBE)
    private String validateDescribe;
    @DatabaseField(columnName = $.SORT)
    private Integer sort;

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public Integer getConfigurable() {
        return configurable;
    }

    public void setConfigurable(Integer configurable) {
        this.configurable = configurable;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Integer getBusiness() {
        return business;
    }

    public void setBusiness(Integer business) {
        this.business = business;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getValidateRegex() {
        return validateRegex;
    }

    public void setValidateRegex(String validateRegex) {
        this.validateRegex = validateRegex;
    }

    public String getValidateDescribe() {
        return validateDescribe;
    }

    public void setValidateDescribe(String validateDescribe) {
        this.validateDescribe = validateDescribe;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
