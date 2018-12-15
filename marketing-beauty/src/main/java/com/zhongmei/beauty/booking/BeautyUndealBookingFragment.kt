package com.zhongmei.beauty.booking

import com.zhongmei.beauty.booking.list.BeautyBookingListFragment
import com.zhongmei.beauty.booking.list.manager.BeautyListCallback
import com.zhongmei.beauty.enums.BeautyListType

/**
 * 已取消预定列表

 */
class BeautyUndealBookingFragment : BeautyBookingListFragment() {

    override fun getListType(): BeautyListType {
        return BeautyListType.UNDEAL
    }

    override fun getServerData(callback: BeautyListCallback) {
        beautyListManager.getUnDealList(activity, callback)
    }
}