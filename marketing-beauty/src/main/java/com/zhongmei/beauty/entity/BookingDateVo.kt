package com.zhongmei.beauty.entity

import java.io.Serializable

/**
 * 预定日期

 * @date 2018/7/20
 */
class BookingDateVo : Serializable {
    /**
     * 日期
     */
    lateinit var date: String
    /**
     * 时间Vo
     */
    lateinit var timeVos: MutableList<BookingTimeVo>
    /**
     * 是否选择
     */
    var isCheck: Boolean = false
}