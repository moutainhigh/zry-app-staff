package com.zhongmei.beauty.booking.bean

import com.zhongmei.bty.basemodule.booking.bean.BookingVo

/**
 * 美业listvo对象

 */
class BeautyBookingListVo : Comparable<BeautyBookingListVo> {

    var title: String
    //title对应的时间
    var time: String
    var bookingVoList: ArrayList<BeautyBookingVo>
    //默认升序
    var isAsc = true

    constructor(title: String, time: String, bookingVoList: ArrayList<BeautyBookingVo>) {
        this.title = title
        this.bookingVoList = bookingVoList
        this.time = time
    }

    override fun compareTo(other: BeautyBookingListVo): Int {
        var result: Int = this.time.compareTo(other.time)
        if (isAsc) {
            if (result > 0) {
                return -1
            } else {
                return 1
            }
        } else {
            if (result > 0) {
                return 1
            } else {
                return -1
            }
        }
    }
}