package com.zhongmei.beauty.booking

import com.zhongmei.beauty.booking.list.BeautyBookingListFragment
import com.zhongmei.beauty.booking.list.manager.BeautyListCallback
import com.zhongmei.beauty.enums.BeautyListType

/**
 * 已超时预定列表

 */
class BeautyOuttimeFragment : BeautyBookingListFragment() {

    override fun getListType(): BeautyListType {
        return BeautyListType.OUTLINE
    }

    override fun getServerData(callback: BeautyListCallback) {
        beautyListManager.getOutlineList(activity, callback)
    }
}