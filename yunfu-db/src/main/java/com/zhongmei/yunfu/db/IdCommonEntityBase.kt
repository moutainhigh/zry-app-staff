package com.zhongmei.yunfu.db

import com.j256.ormlite.field.DatabaseField

/**
 *

 * @version: 1.0
 * @date 2015年9月11日
 */
abstract class IdCommonEntityBase : CommonEntityBaseK<Long>() {

    companion object {
        const val _id = "id"
    }

    @DatabaseField(columnName = IdCommonEntityBase._id, id = true, canBeNull = false)
    var id: Long? = null

    override fun pkValue(): Long? {
        return id
    }

    override fun checkNonNull(): Boolean {
        return EntityBase.checkNonNull(id)
    }

    override fun toString(): String {
        return "IdEntityBase{" +
                "id=" + id +
                '}'
    }
}
