package com.zhongmei.beauty.entity

import java.io.Serializable

/**
 * 预定时间

 * @date 2018/7/20
 */
class BookingTimeVo : Serializable {
    /**
     * 时间
     */
    lateinit var time: String
    /**
     * 时间戳
     */
    var timestamp: Long = 0L
    /**
     * 是否选择
     */
    var isCheck: Boolean = false
    /**
     * 是否占用
     */
    var isLock: Boolean = false
}