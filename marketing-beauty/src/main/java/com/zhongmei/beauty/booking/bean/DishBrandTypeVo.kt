package com.zhongmei.beauty.booking.bean

import android.text.TextUtils
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.yunfu.db.entity.dish.DishBrandType
import com.zhongmei.yunfu.context.base.BaseApplication

/**

 */
class DishBrandTypeVo {
    var brandType: DishBrandType? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }
    //是否是全部
    var isAll: Boolean = false
        set(value) {
            field = value
        }
        get() {
            return field
        }
    //是否选中
    var isSelected: Boolean = false
        set(value) {
            field = value
        }
        get() {
            return field
        }

    fun getName(): String {
        if (isAll) {
            return BaseApplication.sInstance.getString(R.string.all)
        } else if (brandType == null) {
            return ""
        }
        return getName(brandType)
    }

    private fun getName(type: DishBrandType?): String {
        var name = type!!.name
        if (!TextUtils.isEmpty(type.aliasName))
            name = type.aliasName
        return name
    }
}