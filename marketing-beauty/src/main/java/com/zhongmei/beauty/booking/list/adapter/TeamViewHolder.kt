package com.zhongmei.beauty.booking.list.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.zhongmei.beauty.booking.bean.BeautyBookingListVo
import kotlinx.android.synthetic.main.beauty_booking_list_title.view.*

/**

 */
class TeamViewHolder : RecyclerView.ViewHolder {

    constructor(itemView: View) : super(itemView) {

    }


    fun update(holder: TeamViewHolder, listVo: BeautyBookingListVo) {
        holder.itemView.tv_title.text = listVo.title
    }

}