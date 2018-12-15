package com.zhongmei.beauty.booking.list.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.BeautyBookingListVo
import com.zhongmei.beauty.booking.groundrecycleradapter.GroupRecyclerAdapter
import com.zhongmei.beauty.booking.list.manager.BeautyBookListManager

/**
 * 预定待服务、已超时服务列表

 */
open class BookingListAdapter : GroupRecyclerAdapter<BeautyBookingListVo, TeamViewHolder, BookingListViewHolder> {

    var mContext: Fragment
    var beautyListManager: BeautyBookListManager

    constructor(fragment: Fragment, beautyListManager: BeautyBookListManager, bookingList: List<BeautyBookingListVo>) : super(bookingList) {
        mContext = fragment
        this.beautyListManager = beautyListManager
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?): TeamViewHolder {
        return TeamViewHolder(LayoutInflater.from(mContext.context).inflate(R.layout.beauty_booking_list_title, parent, false))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?): BookingListViewHolder {
        return BookingListViewHolder(LayoutInflater.from(mContext.context).inflate(R.layout.beauty_booking_list_item, parent, false), beautyListManager, this)
    }

    override fun onBindGroupViewHolder(holder: TeamViewHolder?, groupPosition: Int) {
        holder!!.update(holder, getGroup(groupPosition))
    }

    override fun onBindChildViewHolder(holder: BookingListViewHolder?, groupPosition: Int, childPosition: Int, itemPosition: Int) {
        holder!!.update(mContext, holder, getGroup(groupPosition).bookingVoList[childPosition])
    }

    override fun getChildCount(group: BeautyBookingListVo?): Int {
        return group!!.bookingVoList.size
    }

}