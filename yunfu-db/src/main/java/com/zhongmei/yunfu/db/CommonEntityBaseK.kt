package com.zhongmei.yunfu.db

import com.j256.ormlite.field.DatabaseField

abstract class CommonEntityBaseK<ID> : OperateEntityBase<ID>() {

    companion object {
        const val _creatorId = "creatorId"
        const val _creatorName = "creatorName"
        const val _updatorId = "updatorId"
        const val _updatorName = "updatorName"
    }

    /**
     * 创建者id
     */
    @DatabaseField(columnName = CommonEntityBaseK._creatorId)
    var creatorId: Long? = null
    /**
     * 创建者名称
     */
    @DatabaseField(columnName = CommonEntityBaseK._creatorName)
    var creatorName: String? = null
    /**
     * 更新者id
     */
    @DatabaseField(columnName = CommonEntityBaseK._updatorId)
    var updatorId: Long? = null
    /**
     * 最后修改者姓名
     */
    @DatabaseField(columnName = CommonEntityBaseK._updatorName)
    var updatorName: String? = null


}