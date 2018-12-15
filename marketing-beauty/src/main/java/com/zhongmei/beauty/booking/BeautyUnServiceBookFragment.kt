package com.zhongmei.beauty.booking

import com.zhongmei.beauty.booking.list.BeautyBookingListFragment
import com.zhongmei.beauty.booking.list.manager.BeautyListCallback
import com.zhongmei.beauty.enums.BeautyListType

/**
 *预约未服务订单fragment

 */
class BeautyUnServiceBookFragment : BeautyBookingListFragment() {
    override fun getListType(): BeautyListType {
        return BeautyListType.UNSERVICE
    }

    override fun getServerData(callback: BeautyListCallback) {
        beautyListManager.getUnServiceList(activity, callback)
    }

}