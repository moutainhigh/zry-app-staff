package com.zhongmei.beauty.booking.util

import com.zhongmei.beauty.entity.UserVo
import com.zhongmei.yunfu.context.session.core.user.User
import com.zhongmei.beauty.entity.BookingTradeItemUser

/**
 * beauty 预定的管理类

 * @date 2018/7/31
 */
class BeautyBookingManager {

    /**
     * 预定TradeItemUser转UserVo
     */
    fun bookingTradeItemTransUserVo(bookingTradetItemUser: BookingTradeItemUser): UserVo {
        var user = User()
        user.name = bookingTradetItemUser.userName
        user.id = bookingTradetItemUser.userId
        user.roleId = bookingTradetItemUser.roleId
        user.roleName = bookingTradetItemUser.roleName
        var vo = UserVo(user)
        return vo
    }
}
