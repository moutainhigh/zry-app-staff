package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase

/**
 * <p>
 * 角色表 : 商户角色信息
 * </p>
 *

 * @since 2018-09-15
 */
@DatabaseTable(tableName = "auth_role")
class AuthRole : IdCommonEntityBase() {

    companion object {
        const val _name = "name"
        const val _code = "code"
        const val _sort = "sort"
        const val _isCreateAccountByDealer = "isCreateAccountByDealer"
        const val _isCreateAccountByShop = "isCreateAccountByShop"
        const val _sourceFlag = "sourceFlag"
        const val _shopIdenty = "shopIdenty"
        const val _brandIdenty = "brandIdenty"
        const val _enableFlag = "enableFlag"
    }

    /**
     * 角色名称 : 角色名称
     */
    @DatabaseField(columnName = AuthRole._name)
    var name: String? = null
    /**
     * 角色编码
     */
    @DatabaseField(columnName = AuthRole._code)
    var code: String? = null
    /**
     * 排序 : 排序
     */
    @DatabaseField(columnName = AuthRole._sort)
    var sort: Integer? = null
    /**
     * 是否允许经销商创建该角色类型的账户1:是2:否
     */
    @DatabaseField(columnName = AuthRole._isCreateAccountByDealer)
    var isCreateAccountByDealer: Integer? = null
    /**
     * 是否允许门店创建该角色类型的账户1:是2:否(只有当isCreateAccountByDealer为1时显示)
     */
    @DatabaseField(columnName = AuthRole._isCreateAccountByShop)
    var isCreateAccountByShop: Integer? = null
    /**
     * 1:用户(b.kry用户创建,)
    2:系统(b.kry不可见,系统自动创建,如营销发布员)
    3:品牌(只读模式,系统自动创建如admin)
     */
    @DatabaseField(columnName = AuthRole._sourceFlag)
    var sourceFlag: Integer? = null

    @DatabaseField(columnName = AuthRole._shopIdenty)
    var shopIdenty: Long? = null
    /**
     * 品牌标识
     */
    @DatabaseField(columnName = AuthRole._brandIdenty)
    var brandIdenty: Long? = null
    /**
     * 启用停用标识 : 1:启用;2:停用
     */
    @DatabaseField(columnName = AuthRole._enableFlag)
    var enableFlag: Integer? = null


    override fun toString(): String {
        return "AuthRole{" +
                ", id=" + id +
                ", name=" + name +
                ", code=" + code +
                ", sort=" + sort +
                ", isCreateAccountByDealer=" + isCreateAccountByDealer +
                ", isCreateAccountByShop=" + isCreateAccountByShop +
                ", sourceFlag=" + sourceFlag +
                ", shopIdenty=" + shopIdenty +
                ", brandIdenty=" + brandIdenty +
                ", enableFlag=" + enableFlag +
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
