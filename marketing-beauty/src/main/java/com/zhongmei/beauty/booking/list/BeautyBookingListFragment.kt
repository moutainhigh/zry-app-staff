package com.zhongmei.beauty.booking.list

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhongmei.yunfu.beauty.R
import com.zhongmei.beauty.booking.bean.BeautyBookingListVo
import com.zhongmei.beauty.booking.groundrecycleradapter.GroupItemDecoration
import com.zhongmei.beauty.booking.list.adapter.BookingListAdapter
import com.zhongmei.beauty.booking.list.manager.BeautyBookListManager
import com.zhongmei.beauty.booking.list.manager.BeautyListCallback
import com.zhongmei.yunfu.ui.base.BasicFragment
import kotlinx.android.synthetic.main.beauty_booking_list_layout.*
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.enums.BeautyListType
import com.zhongmei.beauty.operates.message.BeautyBookingResp
import com.zhongmei.yunfu.context.util.Utils

/**

 */
open abstract class BeautyBookingListFragment : BasicFragment(), OnRefreshListener, OnLoadMoreListener {
    //当前所在的页面
    var currentPage: Int = 0
    lateinit var bookingListAdapter: BookingListAdapter
    lateinit var beautyListManager: BeautyBookListManager
    //是否支持上拉下拉模式
    var mIsSupportLoadMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beautyListManager = BeautyBookListManager(activity)
        beautyListManager.setRefreshListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.beauty_booking_list_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        refreshView()
    }

    fun initAdapter() {
        var mBookingList = ArrayList<BeautyBookingListVo>()
        bookingListAdapter = BookingListAdapter(this, beautyListManager, mBookingList)
        swipe_target.adapter = bookingListAdapter
        swipe_target.layoutManager = LinearLayoutManager(activity)
        val decoration = GroupItemDecoration(bookingListAdapter)
        decoration.setGroupDivider(ResourcesCompat.getDrawable(resources, R.drawable.divider_height_16_dp, null))
        decoration.setTitleDivider(ResourcesCompat.getDrawable(resources, R.drawable.divider_height_1_px, null))
        decoration.setChildDivider(ResourcesCompat.getDrawable(resources, R.drawable.divider_white_header, null))
        swipe_target.addItemDecoration(decoration)
        swipeToLoadLayout.setOnRefreshListener(this)
        swipeToLoadLayout.setOnLoadMoreListener(this)

    }

    fun refreshView() {
        getServerData(mCallback)
    }

    var mCallback = object : BeautyListCallback {
        override fun onQuerySuccess(listVos: ArrayList<BeautyBookingListVo>) {
            showEmptyView(listVos)
            bookingListAdapter!!.update(listVos)

        }

        override fun onOpenTradeSuccess(beautyBookingVo: BeautyBookingVo) {
        }

        override fun onModifySuccess(beautyBookingVo: BeautyBookingVo) {

        }

        override fun onFail(listVos: ArrayList<BeautyBookingListVo>) {
            showEmptyView(listVos)
        }
    }

    /**
     * 预定成功后处理
     */
    open fun onBookingSuccess(resp: BeautyBookingResp) {
//        beautyListManager.addBooking(resp, bookingListAdapter)
        refreshView()
    }

    fun showEmptyView(listVos: ArrayList<BeautyBookingListVo>) {
        if (Utils.isEmpty(listVos)) {
            empty_view.visibility = View.VISIBLE
        } else {
            empty_view.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        beautyListManager.refresh(activity, mCallback, getListType())
        refreshOver()
    }

    override fun onLoadMore() {
        beautyListManager.loadMore(activity, mCallback, getListType())
        refreshOver()
    }

    private fun refreshOver() {
        if (swipeToLoadLayout.isRefreshing) {
            swipeToLoadLayout.isRefreshing = false
        }
        if (swipeToLoadLayout.isLoadingMore) {
            swipeToLoadLayout.isLoadingMore = false
        }
    }

    abstract fun getListType(): BeautyListType

    /**
     * 从服务端获取数据
     */
    abstract fun getServerData(callback: BeautyListCallback)
}