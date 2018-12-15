package com.zhongmei.beauty.booking.list.manager

import com.zhongmei.beauty.booking.bean.BeautyBookingListVo
import com.zhongmei.beauty.booking.bean.BeautyBookingVo

/**
 * 美业列表回调

 */
interface BeautyListCallback {
    /**
     * 预定列表查询成功
     */
    fun onQuerySuccess(listVos: ArrayList<BeautyBookingListVo>)

    /**
     *预定开单成功
     */
    fun onOpenTradeSuccess(beautyBookingVo: BeautyBookingVo)

    /**
     * 预定修改成功
     */
    fun onModifySuccess(beautyBookingVo: BeautyBookingVo)

    fun onFail(listVos: ArrayList<BeautyBookingListVo>)

}