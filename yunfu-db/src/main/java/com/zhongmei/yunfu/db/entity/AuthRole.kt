package com.zhongmei.yunfu.db.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.zhongmei.yunfu.db.IdCommonEntityBase

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

    @DatabaseField(columnName = AuthRole._name)
    var name: String? = null
    @DatabaseField(columnName = AuthRole._code)
    var code: String? = null
    @DatabaseField(columnName = AuthRole._sort)
    var sort: Integer? = null
    @DatabaseField(columnName = AuthRole._isCreateAccountByDealer)
    var isCreateAccountByDealer: Integer? = null
    @DatabaseField(columnName = AuthRole._isCreateAccountByShop)
    var isCreateAccountByShop: Integer? = null
    @DatabaseField(columnName = AuthRole._sourceFlag)
    var sourceFlag: Integer? = null

    @DatabaseField(columnName = AuthRole._shopIdenty)
    var shopIdenty: Long? = null
    @DatabaseField(columnName = AuthRole._brandIdenty)
    var brandIdenty: Long? = null
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
