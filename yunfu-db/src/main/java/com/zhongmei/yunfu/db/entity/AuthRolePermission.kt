package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase

/**
 * <p>
 * 角色权限关系表
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "auth_role_permission")
class AuthRolePermission : IdCommonEntityBase() {

    companion object {
        const val _roleId = "roleId"
        const val _permissionId = "permissionId"
        const val _brandIdenty = "brandIdenty"
        const val _platform = "platform"
        const val _groupFlag = "groupFlag"
    }

    /**
     * 角色id : 角色id
     */
    @DatabaseField(columnName = AuthRolePermission._roleId)
    var roleId: Long? = null
    /**
     * 权限id
     */
    @DatabaseField(columnName = AuthRolePermission._permissionId)
    var permissionId: Long? = null
    /**
     * 品牌标识
     */
    @DatabaseField(columnName = AuthRolePermission._brandIdenty)
    var brandIdenty: Long? = null

    @DatabaseField(columnName = AuthRolePermission._platform)
    var platform: Integer? = null
    /**
     * 是门店资源还是品牌资源，1品牌，2门店，3pos收银，4OnMobile
     */
    @DatabaseField(columnName = AuthRolePermission._groupFlag)
    var groupFlag: Integer? = null


    override fun toString(): String {
        return "AuthRolePermission{" +
                ", id=" + id +
                ", roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", brandIdenty=" + brandIdenty +
                ", platform=" + platform +
                ", groupFlag=" + groupFlag +
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
