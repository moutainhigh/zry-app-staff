package com.zhongmei.yunfu.db

import com.j256.ormlite.field.DatabaseField

abstract class OperateEntityBase<ID> : EntityBase<ID>() {

    companion object {
        const val _statusFlag = "statusFlag"
        const val _serverCreateTime = "serverCreateTime"
        const val _serverUpdateTime = "serverUpdateTime"
    }

    /**
     * 状态标识1:有效 2:无效
     */
    @DatabaseField(columnName = OperateEntityBase._statusFlag)
    var statusFlag: Integer? = null
    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = OperateEntityBase._serverCreateTime)
    var serverCreateTime: Long? = null
    /**
     * 服务器更新时间
     */
    @DatabaseField(columnName = OperateEntityBase._serverUpdateTime)
    var serverUpdateTime: Long? = null

    override fun isValid(): Boolean {
        return statusFlag?.toInt() == 1
    }

    override fun verValue(): Long {
        return serverUpdateTime!!
    }

}