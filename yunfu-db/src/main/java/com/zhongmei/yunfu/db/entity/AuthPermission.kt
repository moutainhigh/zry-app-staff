package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase

/**
 * <p>
 * 权限
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "auth_permission")
class AuthPermission : IdCommonEntityBase() {

    companion object {
        const val _parentId = "parentId"
        const val _levelId = "levelId"
        const val _name = "name"
        const val _code = "code"
        const val _type = "type"
        const val _url = "url"
        const val _sort = "sort"
        const val _platform = "platform"
        const val _supportVersion = "supportVersion"
        const val _sourceFlag = "sourceFlag"
        const val _groupFlag = "groupFlag"
        const val _checked = "checked"
        const val _zoneCode = "zoneCode"
    }

    /**
     * 上级权限id
     */
    @DatabaseField(columnName = AuthPermission._parentId)
    var parentId: Long? = null
    /**
     * 权限层级id : 主要是为了构建权限树，三位表示一级。
     */
    @DatabaseField(columnName = AuthPermission._levelId)
    var levelId: String? = null
    /**
     * 权限名称 : 菜单的权限名称同菜单名称
     */
    @DatabaseField(columnName = AuthPermission._name)
    var name: String? = null
    /**
     * 权限编码 : 判断是否有权限的编码
     */
    @DatabaseField(columnName = AuthPermission._code)
    var code: String? = null
    /**
     * 权限类型 : 1：菜单；2：按钮；3：数据(onpos)
     */
    @DatabaseField(columnName = AuthPermission._type)
    var type: Integer? = null
    /**
     * 连接 : 权限关联的菜单的连接，注意只在菜单下有效
     */
    @DatabaseField(columnName = AuthPermission._url)
    var url: String? = null
    /**
     * 排序号
     */
    @DatabaseField(columnName = AuthPermission._sort)
    var sort: Integer? = null
    /**
     * 平台类型 : 1-云后台；2-云营销；3-POS
     */
    @DatabaseField(columnName = AuthPermission._platform)
    var platform: Integer? = null
    /**
     * 1、V4=4；2、V5 =5
     */
    @DatabaseField(columnName = AuthPermission._supportVersion)
    var supportVersion: Integer? = null
    /**
     * 权限来源1-业务权限，2-管理权限
     */
    @DatabaseField(columnName = AuthPermission._sourceFlag)
    var sourceFlag: Integer? = null
    /**
     * 组织结构类型，1-公司、2-门店、6-配送站、7-公司及门店、8-公司及配送站、9-门店及配送站、10-公司、门店及配送站
     */
    @DatabaseField(columnName = AuthPermission._groupFlag)
    var groupFlag: Integer? = null
    /**
     * 是否选中，1,勾选;2,不勾选
     */
    @DatabaseField(columnName = AuthPermission._checked)
    var checked: Integer? = null
    /**
     * 分区code 关联枚举值
     */
    @DatabaseField(columnName = AuthPermission._zoneCode)
    var zoneCode: String? = null

    override fun toString(): String {
        return "AuthPermission{" +
                ", id=" + id +
                ", parentId=" + parentId +
                ", levelId=" + levelId +
                ", name=" + name +
                ", code=" + code +
                ", type=" + type +
                ", url=" + url +
                ", sort=" + sort +
                ", platform=" + platform +
                ", supportVersion=" + supportVersion +
                ", sourceFlag=" + sourceFlag +
                ", groupFlag=" + groupFlag +
                ", checked=" + checked +
                ", zoneCode=" + zoneCode +
                ", statusFlag=" + statusFlag +
                ", creatorId=" + creatorId +
                ", creatorName=" + creatorName +
                ", updatorId=" + updatorId +
                ", updatorName=" + updatorName +
                ", serverCreateTime=" + serverCreateTime +
                ", serverUpdateTime=" + serverUpdateTime +
                "}"
    }
}
