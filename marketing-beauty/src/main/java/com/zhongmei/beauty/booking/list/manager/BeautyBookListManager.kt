package com.zhongmei.beauty.booking.list.manager

import android.app.Activity
import android.content.Context
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.zhongmei.beauty.booking.bean.BeautyBookingListVo
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.booking.list.adapter.BookingListAdapter
import com.zhongmei.beauty.booking.util.BeautyDateTool
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty
import com.zhongmei.yunfu.monitor.CalmResponseListener
import com.zhongmei.yunfu.net.builder.NetError
import com.zhongmei.yunfu.context.util.DateTimeUtils
import com.zhongmei.beauty.entity.BookingTradeItemUser
import com.zhongmei.beauty.enums.BeautyListType
import com.zhongmei.beauty.operates.BeautyBookingOperates
import com.zhongmei.beauty.operates.message.BeautyBookingListReq
import com.zhongmei.beauty.operates.message.BeautyBookingListResp
import com.zhongmei.beauty.operates.message.BeautyBookingResp
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.context.session.Session
import com.zhongmei.yunfu.db.enums.BookingOrderStatus
import com.zhongmei.yunfu.resp.ResponseObject
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.yunfu.context.util.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 美业预定管理类

 */
class BeautyBookListManager {
    //一次查询多少条数据 默认 30
    var COMMON_PAGE_COUNT: Int = 18
    //查询多少天的数据
    var QUERY_DAYS = 10
    var startTime: Long = 0
    var endTime: Long = 0
    var currentPage = 1
    var dateMap: TreeMap<String, BeautyBookingListVo>? = null
    var isNeedRefreshTime = true
    var mContext: Context
    //接口类型
    var type: BeautyListType = BeautyListType.UNSERVICE

    var  mRefreshListener:OnRefreshListener? = null


    constructor(context: Context) {
        mContext = context

    }

    fun setRefreshListener(refreshListene:OnRefreshListener){
        this.mRefreshListener=refreshListene
    }

    /**
     * @param isAsc 是否是升序
     */
    fun checkMapInit() {
        dateMap = TreeMap<String, BeautyBookingListVo>(object : Comparator<String> {
            override fun compare(o1: String, o2: String): Int {
                var isAsc = getSortType()
                if (isAsc)
                    return o1.compareTo(o2)
                else
                    return o2.compareTo(o1)
            }
        })
    }

    /**
     * 获取未服务预定列表
     */
    fun getUnServiceList(activty: Activity, callback: BeautyListCallback) {
        type = BeautyListType.UNSERVICE
        if (isNeedRefreshTime) {
            checkMapInit()
            startTime = System.currentTimeMillis()
            endTime = DateTimeUtils.afterDays(QUERY_DAYS)
        }
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = startTime
        bookingListReq.endTime = endTime
        bookingListReq.page = currentPage
        bookingListReq.pageCount = COMMON_PAGE_COUNT
        bookingListReq.type = BeautyListType.UNSERVICE.value()
        bookingListReq.userId = Session.getAuthUser().id
        doListReq(activty, bookingListReq, callback)
    }


    /**
     * 获取已超时列表
     * 已超时 startTime 和 endTime 相同
     */
    fun getOutlineList(activty: Activity, callback: BeautyListCallback) {
        type = BeautyListType.OUTLINE
        if (isNeedRefreshTime) {
            checkMapInit()
            startTime = System.currentTimeMillis()
            endTime = startTime
        }
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = startTime
        bookingListReq.endTime = endTime
        bookingListReq.page = currentPage
        bookingListReq.pageCount = COMMON_PAGE_COUNT
        bookingListReq.type = BeautyListType.OUTLINE.value()
        bookingListReq.userId = Session.getAuthUser().id
        doListReq(activty, bookingListReq, callback)
    }

    /**
     * 获取已取消列表
     */
    fun getCancelList(activty: Activity, callback: BeautyListCallback) {
        type = BeautyListType.CANCELD
        if (isNeedRefreshTime) {
            checkMapInit()
            startTime = DateTimeUtils.getCurrentDayStart()
            endTime = DateTimeUtils.afterDays(QUERY_DAYS)
        }
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = startTime
        bookingListReq.endTime = endTime
        bookingListReq.page = currentPage
        bookingListReq.pageCount = COMMON_PAGE_COUNT
        bookingListReq.type = BeautyListType.CANCELD.value()
        bookingListReq.userId = Session.getAuthUser().id
        doListReq(activty, bookingListReq, callback)
    }


    /**
     * 获取未处理列表
     */
    fun getUnDealList(activty: Activity, callback: BeautyListCallback) {
        type = BeautyListType.UNDEAL
        if (isNeedRefreshTime) {
            checkMapInit()
            startTime = DateTimeUtils.getCurrentDayStart()
            endTime = DateTimeUtils.afterDays(QUERY_DAYS)
        }
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = startTime
        bookingListReq.endTime = endTime
        bookingListReq.page = currentPage
        bookingListReq.pageCount = COMMON_PAGE_COUNT
        bookingListReq.type = BeautyListType.UNDEAL.value()
        bookingListReq.userId = Session.getAuthUser().id
        doListReq(activty, bookingListReq, callback)
    }

    /**
     * 请求的实际处理逻辑
     */
    private fun doListReq(activty: Activity, bookingListReq: BeautyBookingListReq, callback: BeautyListCallback) {
        var operates = OperatesFactory.create(BeautyBookingOperates::class.java)
        operates.getBookingList(
                bookingListReq,
                getResponseListener(callback),
                activty)
    }

    private fun getResponseListener(callback: BeautyListCallback): CalmResponseListener<ResponseObject<BeautyBookingListResp>> {
        val listener = object : CalmResponseListener<ResponseObject<BeautyBookingListResp>>() {
            override fun onSuccess(data: ResponseObject<BeautyBookingListResp>?) {
                if (ResponseObject.isOk(data)) {
                    if(Utils.isEmpty(data!!.content.bookings)) {
                        currentPage--
                        callback?.onQuerySuccess(ArrayList(dateMap!!.values),false)
                    }else {
                        callback?.onQuerySuccess(buildListVos(data!!.content),true)
                    }
                } else {
                    ToastUtil.showLongToast(data?.message)
                }

            }

            override fun onError(error: NetError?) {
                callback?.onFail(ArrayList(dateMap!!.values))
                ToastUtil.showLongToast(error?.volleyError?.message)

            }
        }
        return listener
    }

    /**
     * 上拉刷新
     */
    fun refresh(activty: Activity, callback: BeautyListCallback, type: BeautyListType) {
        this.type = type
        clearCache()
        when (type) {
            BeautyListType.UNSERVICE -> {
                startTime = System.currentTimeMillis()
                endTime = DateTimeUtils.afterDays(QUERY_DAYS)
                getUnServiceList(activty, callback)
            }
            BeautyListType.OUTLINE -> {
                getOutlineList(activty, callback)
            }
            BeautyListType.CANCELD -> {
                getCancelList(activty, callback)
            }
            BeautyListType.UNDEAL->{
                getUnDealList(activty,callback)
            }
        }

    }

    fun getSortType(): Boolean {
        when (type) {
            BeautyListType.UNSERVICE ->
                return true
            BeautyListType.OUTLINE ->
                return false
            BeautyListType.CANCELD ->
                return true
        }
        return true
    }

    /**
     * 加载更多
     */
    fun loadMore(activty: Activity, callback: BeautyListCallback, type: BeautyListType) {
        this.type = type
        isNeedRefreshTime = false
        currentPage++
        when (type) {
            BeautyListType.UNSERVICE ->
                getUnServiceList(activty, callback)
            BeautyListType.OUTLINE ->
                getOutlineList(activty, callback)
            BeautyListType.CANCELD ->
                getCancelList(activty, callback)
        }
    }


    /**
     * 清除缓存
     */
    fun clearCache() {
        dateMap!!.clear()
        currentPage = 1
        isNeedRefreshTime = true
    }

    /**
     * 构建界面显示数据
     */
    fun buildListVos(resp: BeautyBookingListResp): ArrayList<BeautyBookingListVo> {
        if (Utils.isEmpty(resp.bookings)) {
            return ArrayList(dateMap!!.values)
        }

        var bookingItemPropertiesMap = buildItemProperMap(resp.bookingTradeItemProperties)
        var bookingItemUserMap = buildItemUserMap(resp.bookingTradeItemUsers)
        var bookingItemMap = buildItemMap(resp.bookingTradeItems, bookingItemPropertiesMap, bookingItemUserMap)
        resp.bookings.forEach { book ->
            var sortTime = ""
            if (book.orderStatus == BookingOrderStatus.CANCEL) {
                sortTime = DateTimeUtils.formatDate(book.serverUpdateTime)
            } else {
                sortTime = DateTimeUtils.formatDate(book.orderTime)
            }
            var titleTime = BeautyDateTool.getTitleDay(mContext, sortTime)

            var bookingVo = BeautyBookingVo()
            bookingVo.interfaceType = type
            bookingVo.booking = book
            bookingVo.bookingTradeItemVos = bookingItemMap[book.id]

            putBookingVoToMap(titleTime, sortTime, bookingVo)

        }

        return ArrayList(dateMap!!.values)
    }

    /**
     * 预定开单成功或者取消预定成功后 处理
     */
    fun removeBooking(vo: BeautyBookingVo): ArrayList<BeautyBookingListVo> {
        var removeTitleList = ArrayList<String>()
        for ((title, booking) in dateMap!!) {
            var iteartor = booking.bookingVoList.iterator()
            iteartor.forEach { it ->
                if (it.booking.id == vo.booking.id) {
                    iteartor.remove()
                }
            }

            if (Utils.isEmpty(booking.bookingVoList)) {
                removeTitleList.add(title)
            }
        }
        for (title in removeTitleList) {
            dateMap!!.remove(title)
        }
        return ArrayList(dateMap!!.values)
    }

    /**
     * 更新预定
     */
    fun updateBooking(resp: BeautyBookingResp, bookingVo: BeautyBookingVo, mAdapter: BookingListAdapter) {
        mRefreshListener!!.onRefresh()

//        var newBookingTime = DateTimeUtils.formatDate(resp.booking.orderTime)
//        var oldBookingTime = DateTimeUtils.formatDate(bookingVo.booking.orderTime)
//        //时间未改变，只是更新
//        if (newBookingTime.equals(oldBookingTime)) {
//            mAdapter.notifyDataSetChanged()
//        } else {
//            var removeTitleList = ArrayList<String>()
//            for ((title, booking) in dateMap!!) {
//                var iteartor = booking.bookingVoList.iterator()
//                iteartor.forEach { it ->
//                    if (it.booking.id == bookingVo.booking.id) {
//                        iteartor.remove()
//                    }
//                }
//                if (Utils.isEmpty(booking.bookingVoList)) {
//                    removeTitleList.add(title)
//                }
//            }
//
//            for (title in removeTitleList) {
//                dateMap!!.remove(title)
//            }
//
//            var newBookingVo = convertBookingRespToVo(resp)
//            putBookingVoToMap(BeautyDateTool.getTitleDay(mContext, newBookingTime), newBookingTime, newBookingVo)
//            mAdapter.update(ArrayList(dateMap!!.values))
//            mAdapter.notifyDataSetChanged()
//        }
    }

    /**
     * 添加预定
     */
    fun addBooking(resp: BeautyBookingResp, mAdapter: BookingListAdapter) {
        var bookingVo = convertBookingRespToVo(resp)
        var sortTime = DateTimeUtils.formatDate(resp.booking.orderTime)
        var timeTitle = BeautyDateTool.getTitleDay(mContext, sortTime)
        putBookingVoToMap(timeTitle, sortTime, bookingVo)
        mAdapter.update(ArrayList(dateMap!!.values))
        mAdapter.notifyDataSetChanged()
    }

    fun convertBookingRespToVo(resp: BeautyBookingResp): BeautyBookingVo {
        var bookingVo = BeautyBookingVo()
        bookingVo.booking = resp.booking
        bookingVo.bookingTradeItemVos = ArrayList()
        var bookingItemPropertiesMap = buildItemProperMap(resp.bookingTradeItemProperties)
        var bookingItemUserMap = buildItemUserMap(resp.bookingTradeItemUsers)
        var bookingItemMap = buildItemMap(resp.bookingTradeItems, bookingItemPropertiesMap, bookingItemUserMap)

        bookingVo.bookingTradeItemVos = bookingItemMap[bookingVo.booking.id]
//        if(resp.bookingPeriod!=null){
//            bookingVo.bookingPeriods=ArrayList<BookingPeriod>()
//            bookingVo.bookingPeriods.add(resp.bookingPeriod)
//        }
        return bookingVo
    }

    fun putBookingVoToMap(titleTime: String, sorTime: String, bookingVo: BeautyBookingVo) {
        if (dateMap!![sorTime] == null) {
            var bookingList = ArrayList<BeautyBookingVo>()
            bookingList.add(bookingVo)
            var beautyBookingListVo = BeautyBookingListVo(titleTime, sorTime, bookingList)
            beautyBookingListVo.isAsc = getSortType()
            dateMap!!.put(sorTime, beautyBookingListVo)
        } else {
            var beautyBookingListVo = dateMap!![sorTime]
            beautyBookingListVo!!.bookingVoList!!.add(bookingVo)
        }
    }


    /**
     * 将list转换成map
     */
    fun buildItemMap(list: List<BookingTradeItem>?, propertyMap: Map<Long, List<BookingTradeItemProperty>>, itemUserMap: Map<Long, List<BookingTradeItemUser>>): Map<Long, List<BookingTradeItemVo>> {
        var map = HashMap<Long, ArrayList<BookingTradeItemVo>>()
        if (Utils.isEmpty(list)) {
            return map
        }
        list!!.forEach { item ->
            var itemVo = BookingTradeItemVo()
            itemVo.tradeItem = item
            itemVo.tradeItemPropertyList = propertyMap[item.id]
            itemVo.bookingTradeItemUsers = itemUserMap[item.id]
            if (map[item.bookingId] == null) {
                var itemList = ArrayList<BookingTradeItemVo>()
                itemList.add(itemVo)
                map.put(item.bookingId, itemList)
            } else {
                map[item.bookingId]!!.add(itemVo)
            }
        }
        return map
    }


    fun buildItemProperMap(list: List<BookingTradeItemProperty>?): Map<Long, List<BookingTradeItemProperty>> {
        var map = HashMap<Long, ArrayList<BookingTradeItemProperty>>()
        if (Utils.isEmpty(list)) {
            return map
        }
        list!!.forEach { item ->
            if (map[item.bookingTradeItemId] == null) {
                var itemList = ArrayList<BookingTradeItemProperty>()
                itemList.add(item)
                map.put(item.bookingTradeItemId, itemList)
            } else {
                map[item.bookingTradeItemId]!!.add(item)
            }
        }
        return map
    }

    fun buildItemUserMap(list: List<BookingTradeItemUser>?): Map<Long, List<BookingTradeItemUser>> {
        var map = HashMap<Long, ArrayList<BookingTradeItemUser>>()
        if (Utils.isEmpty(list)) {
            return map
        }
        list!!.forEach { item ->
            if (map[item.bookingTradeItemId] == null) {
                var itemList = ArrayList<BookingTradeItemUser>()
                itemList.add(item)
                map.put(item.bookingTradeItemId, itemList)
            } else {
                map[item.bookingTradeItemId]!!.add(item)
            }
        }
        return map
    }


    fun buildPeriodrMap(list: List<BookingPeriod>?): Map<Long, List<BookingPeriod>> {
        var map = HashMap<Long, ArrayList<BookingPeriod>>()
        if (Utils.isEmpty(list)) {
            return map
        }
        list!!.forEach { item ->
            if (map[item.bookingId] == null) {
                var itemList = ArrayList<BookingPeriod>()
                itemList.add(item)
                map.put(item.bookingId, itemList)
            } else {
                map[item.bookingId]!!.add(item)
            }
        }
        return map
    }


}