package com.zhongmei.beauty.booking.list.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo
import com.zhongmei.yunfu.db.enums.Sex
import com.zhongmei.yunfu.context.util.Utils

/**
 * 预定列表view

 */
class BookingListItemView : RelativeLayout {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        View.inflate(context, R.layout.beauty_booking_list_item, this)
    }

    fun refreshView(bookingVo: BeautyBookingVo) {

    }

    private fun getCustomerHead(sex: Sex): Int {
        var headRes = R.drawable.beauty_customer_default
        if (sex == Sex.FEMALE) {
            headRes = R.drawable.beauty_customer_female
        } else if (sex == Sex.MALE) {
            headRes = R.drawable.beauty_customer_male
        }
        return headRes
    }

    private fun getTradeItemInfo(tradeItemVos: List<TradeItemVo>): String {
        if (Utils.isEmpty(tradeItemVos)) {
            return resources.getString(R.string.beauty_no_service)
        }

        val itemLen = if (tradeItemVos.size > 3) 3 else tradeItemVos.size

        val itemNameBuffer = StringBuffer()
        for (i in 0 until itemLen) {
            val vo = tradeItemVos[i]
            itemNameBuffer.append(vo.tradeItem.dishName)
            itemNameBuffer.append("x" + vo.tradeItem.quantity)
            itemNameBuffer.append(",\n")
        }

        return itemNameBuffer.substring(0, itemNameBuffer.length - 2)
    }

}