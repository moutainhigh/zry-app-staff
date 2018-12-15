package com.zhongmei.beauty.booking.order

import com.zhongmei.bty.basemodule.orderdish.bean.DishVo

/**
 * 服务选择callback

 */
interface BeautyServiceCallback {
    fun onCallback(dishVos: List<DishVo>)
}