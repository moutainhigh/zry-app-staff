package com.zhongmei.beauty.booking.list.manager

import android.app.Activity
import android.text.TextUtils
import com.zhongmei.beauty.booking.bean.BeautyBookingBoardVo
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.booking.interfaces.BeautyBoardDataListener
import com.zhongmei.beauty.entity.ReserverItemVo
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo
import com.zhongmei.bty.basemodule.booking.entity.BookingPeriod
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty
import com.zhongmei.yunfu.monitor.CalmResponseListener
import com.zhongmei.bty.basemodule.shopmanager.utils.DateTimeUtil
import com.zhongmei.yunfu.net.builder.NetError
import com.zhongmei.yunfu.context.util.DateTimeUtils
import com.zhongmei.beauty.entity.BookingTradeItemUser
import com.zhongmei.beauty.enums.BeautyListType
import com.zhongmei.beauty.operates.BeautyBookingOperates
import com.zhongmei.beauty.operates.message.BeautyBookingListReq
import com.zhongmei.beauty.operates.message.BeautyBookingListResp
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.ShopInfoManager
import com.zhongmei.yunfu.resp.ResponseObject
import com.zhongmei.yunfu.util.ToastUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by demo on 2018/12/15
 */
class BeautyBoardManager {
    var mStartTime: String = "";//营业时间开始 hh:mm:ss
    var mStopTime: String = "";//营业时间结束 hh:mm:ss


    /**
     * 获取未服务预定列表
     */
    fun getUnServiceList(activty: Activity, callback: BeautyBoardDataListener, date: Date) {
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = DateTimeUtils.getDayStart(date);
        bookingListReq.endTime = DateTimeUtils.getDayEnd(date);
        bookingListReq.page = 1
        bookingListReq.pageCount = 100;
        bookingListReq.type = BeautyListType.UNSERVICE.value()
        doListReq(activty, bookingListReq, callback)
    }


    /**
     * 请求的实际处理逻辑
     */
    private fun doListReq(activty: Activity, bookingListReq: BeautyBookingListReq, callback: BeautyBoardDataListener) {
        var operates = OperatesFactory.create(BeautyBookingOperates::class.java)
        operates.getBookingList(
                bookingListReq,
                getResponseListener(callback),
                activty)
    }


    fun getTodayUnServiceList(activty: Activity, callback: BeautyBoardDataListener, date: Date) {
        var bookingListReq = BeautyBookingListReq()
        bookingListReq.startTime = DateTimeUtils.getDayStart(date);
        bookingListReq.endTime = DateTimeUtils.getDayEnd(date);
        bookingListReq.page = 1
        bookingListReq.pageCount = 100;
        bookingListReq.type = BeautyListType.UNSERVICE.value()
        doTodayListReq(activty, bookingListReq, callback)
    }

    /**
     * 请求今天的预约订单
     */
    private fun doTodayListReq(activty: Activity, bookingListReq: BeautyBookingListReq, callback: BeautyBoardDataListener) {
        var operates = OperatesFactory.create(BeautyBookingOperates::class.java)
        operates.getBookingList(
                bookingListReq,
                getTodayResponseListener(callback),
                activty)
    }


    private fun getResponseListener(callback: BeautyBoardDataListener): CalmResponseListener<ResponseObject<BeautyBookingListResp>> {
        val listener = object : CalmResponseListener<ResponseObject<BeautyBookingListResp>>() {
            override fun onSuccess(data: ResponseObject<BeautyBookingListResp>?) {
                if (ResponseObject.isOk(data)) {
                    callback?.onSuccess(buildTodayTradeListVos(data!!.content))
                } else {
                    ToastUtil.showLongToast(data?.message)
                }

            }

            override fun onError(error: NetError?) {
                ToastUtil.showLongToast(error?.volleyError?.message)

            }
        }
        return listener
    }


    /**
     * 处理今天预约订单
     */
    private fun getTodayResponseListener(callback: BeautyBoardDataListener): CalmResponseListener<ResponseObject<BeautyBookingListResp>> {
        val listener = object : CalmResponseListener<ResponseObject<BeautyBookingListResp>>() {
            override fun onSuccess(data: ResponseObject<BeautyBookingListResp>?) {
                if (ResponseObject.isOk(data)) {
                    callback?.onSuccess(buildListVos(data!!.content))
                } else {
                    ToastUtil.showLongToast(data?.message)
                }

            }

            override fun onError(error: NetError?) {
                ToastUtil.showLongToast(error?.volleyError?.message)

            }
        }
        return listener
    }


    private fun buildListVos(content: BeautyBookingListResp): BeautyBookingBoardVo? {
        var bookingBoardVo = BeautyBookingBoardVo();
        var mapTechnician = buildTradeItemUser(content.bookingTradeItemUsers);
        var listTechnician = buildTradeItemUserBookingTrade(content.bookingTradeItemUsers);
        var mapTradeItemProperty = buildTradeItemProperties(content.bookingTradeItemProperties);
        var mapBookingItemVo = buildTradeItemVos(content.bookingTradeItems, mapTradeItemProperty, mapTechnician);
        var listNoTechnicianBookingItemVo = ArrayList<BeautyBookingVo>();
        var mMapBookingItemVos = HashMap<Long, ArrayList<ReserverItemVo>>();



        content.bookings?.forEach { bookingTrade ->
            var reserverItemVo = ReserverItemVo()
            var reserverTradeVo = BeautyBookingVo()

            reserverTradeVo.booking = bookingTrade
            reserverTradeVo.bookingTradeItemVos = mapBookingItemVo?.get(bookingTrade.id)

            reserverItemVo.setmReserverVo(reserverTradeVo)

            listNoTechnicianBookingItemVo.add(reserverTradeVo)

            var itemUser = reserverTradeVo.bookingTradeItemVos?.get(0)?.bookingTradeItemUsers?.get(0)

            if (itemUser != null) {
                if (!mMapBookingItemVos.containsKey(itemUser.userId)) {
                    mMapBookingItemVos.put(itemUser.userId, ArrayList<ReserverItemVo>())
                }
                mMapBookingItemVos.get(itemUser.userId)?.add(reserverItemVo)
            }
        }


//        bookingBoardVo.setmTechnicians(listTechnician);
        bookingBoardVo.setmMapBookingItemVos(mMapBookingItemVos);
        bookingBoardVo.setmNoTechnicianBookingItemVos(listNoTechnicianBookingItemVo);

        return bookingBoardVo;
    }


    private fun buildTodayTradeListVos(content: BeautyBookingListResp): BeautyBookingBoardVo? {
        var bookingBoardVo = BeautyBookingBoardVo()
        var mapTechnician = buildTradeItemUser(content.bookingTradeItemUsers)
        var mapTechnicianBookingTrade = buildTradeItemUserBookingTrade(content.bookingTradeItemUsers)
        var mapTradeItemProperty = buildTradeItemProperties(content.bookingTradeItemProperties)
        var mapBookingItemVo = buildTradeItemVos(content.bookingTradeItems, mapTradeItemProperty, mapTechnician)

        var listBookingItemVo = ArrayList<BeautyBookingVo>();
        var listBookingItemVoNoTechnical = ArrayList<BeautyBookingVo>();
        var mMapBookingItemVos = HashMap<Long, ArrayList<ReserverItemVo>>();

        content.bookings?.forEach { bookingTrade ->
            var reserverItemVo = ReserverItemVo();
            var reserverTradeVo = BeautyBookingVo();

            reserverTradeVo.booking = bookingTrade;
            reserverTradeVo.bookingTradeItemVos = mapBookingItemVo?.get(bookingTrade.id);
            reserverItemVo.setmReserverVo(reserverTradeVo);

            var technical = mapTechnicianBookingTrade!!.get(bookingTrade.id)

            if (technical != null) {//有技师
                listBookingItemVo.add(reserverTradeVo)
            } else {
                listBookingItemVoNoTechnical.add(reserverTradeVo)
            }

            if (technical != null) {
                if (!mMapBookingItemVos.containsKey(technical.userId)) {
                    mMapBookingItemVos.put(technical.userId, ArrayList<ReserverItemVo>());
                }

                mMapBookingItemVos.get(technical.userId)!!.add(reserverItemVo);

            }


        }
        bookingBoardVo.setmBookingItemVos(listBookingItemVo)
        bookingBoardVo.setmMapBookingItemVos(mMapBookingItemVos)
        bookingBoardVo.setmNoTechnicianBookingItemVos(listBookingItemVoNoTechnical)

        return bookingBoardVo;
    }

    fun buildTradeItemVos(tradeItems: List<BookingTradeItem>?, mapItemProperty: Map<Long, List<BookingTradeItemProperty>>?, mapTechnician: Map<Long, List<BookingTradeItemUser>>?): Map<Long, List<BookingTradeItemVo>>? {
        var mapTradeItems = HashMap<Long, ArrayList<BookingTradeItemVo>>();
        tradeItems?.forEach { tradeItem ->

            var itemItemVo = buildTradeItemVo(tradeItem, mapItemProperty?.get(tradeItem.id), mapTechnician?.get(tradeItem.id));
            if (!mapTradeItems.containsKey(tradeItem.bookingId)) {
                mapTradeItems.put(tradeItem.bookingId, ArrayList<BookingTradeItemVo>());
            }
            mapTradeItems.get(tradeItem.bookingId)?.add(itemItemVo);

        }
        return mapTradeItems;
    }

    fun buildTradeItemVo(tradeItem: BookingTradeItem, tradeItemPropertys: List<BookingTradeItemProperty>?, technicians: List<BookingTradeItemUser>?): BookingTradeItemVo {
        var itemItemVo = BookingTradeItemVo();
        itemItemVo.tradeItem = tradeItem;
        itemItemVo.tradeItemPropertyList = tradeItemPropertys;
        itemItemVo.bookingTradeItemUsers = technicians;
        return itemItemVo;
    }

    fun buildTradeItemProperties(itemProperties: List<BookingTradeItemProperty>?): Map<Long, List<BookingTradeItemProperty>>? {
        var mapTradeItemProperties = HashMap<Long, ArrayList<BookingTradeItemProperty>>();
        itemProperties?.forEach { property ->
            if (!mapTradeItemProperties.containsKey(property.bookingTradeItemId)) {
                mapTradeItemProperties.put(property.bookingTradeItemId, ArrayList<BookingTradeItemProperty>());
            }
            mapTradeItemProperties.get(property.bookingTradeItemId)?.add(property);

        }
        return mapTradeItemProperties;
    }

    fun buildPeriod(periods: List<BookingPeriod>?): Map<Long, List<BookingPeriod>>? {
        var mapPeriod = HashMap<Long, ArrayList<BookingPeriod>>();
        periods?.forEach { period ->
            if (!mapPeriod.containsKey(period.bookingId)) {
                mapPeriod.put(period.bookingId, ArrayList<BookingPeriod>());
            }
            mapPeriod.get(period.bookingId)?.add(period)
        }
        return mapPeriod;
    }

    fun buildTradeItemUser(tradeItemUsers: List<BookingTradeItemUser>?): Map<Long, List<BookingTradeItemUser>>? {
        var mapTradeItemUser = HashMap<Long, ArrayList<BookingTradeItemUser>>();
        tradeItemUsers?.forEach { tradeItemUser ->
            if (!mapTradeItemUser.containsKey(tradeItemUser.bookingTradeItemId)) {
                mapTradeItemUser.put(tradeItemUser.bookingTradeItemId, ArrayList<BookingTradeItemUser>());
            }
            mapTradeItemUser.get(tradeItemUser.bookingTradeItemId)?.add(tradeItemUser);

        }
        return mapTradeItemUser;
    }

    /**
     * 获取去重之后的技师
     */
    fun buildTradeItemUserBookingTrade(tradeItemUsers: List<BookingTradeItemUser>?): Map<Long, BookingTradeItemUser>? {
        var mapTradeItemUser = HashMap<Long, BookingTradeItemUser>();
        tradeItemUsers?.forEach { tradeItemUser ->
            if (!mapTradeItemUser.containsKey(tradeItemUser.id)) {
                mapTradeItemUser.put(tradeItemUser.bookingId, tradeItemUser);
            }

        }
        return mapTradeItemUser;
    }

    /**
     * 初始化营业时间
     */
    fun initBusinessTime() {
        mStartTime = ShopInfoManager.getInstance().getShopInfo().startTime
        mStopTime = ShopInfoManager.getInstance().getShopInfo().endTime
//        if (!TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mStopTime)) {
//            return;
//        }
//
//        var openTimes = DateTimeUtil.getBusinessTime();
//
//        openTimes?.forEach() { openTime ->
//
//            if (TextUtils.isEmpty(mStartTime)) {
//                mStartTime = openTime.startTime;
//            } else {
//                var localStartTime = getDateTime(mStartTime);
//                var curStartTime = getDateTime(openTime.startTime);
//
//                if (localStartTime!!.time > curStartTime!!.time) {
//                    mStartTime = openTime.startTime;
//                }
//            }
//
//
//            if (TextUtils.isEmpty(mStopTime)) {
//                mStopTime = openTime.endTime;
//            } else {
//                var localStopTime = getDateTime(mStopTime);
//                var curStopTime = getDateTime(openTime.endTime);
//
//                if (localStopTime!!.time < curStopTime!!.time) {
//                    mStopTime = openTime.endTime;
//                }
//            }
//        }
//
//        if (TextUtils.isEmpty(mStartTime)) {
//            mStartTime = "00:00:00"
//        }
//
//        if (TextUtils.isEmpty(mStopTime)) {
//            mStopTime = "23:59:59"
//        }

    }

    fun getDateTime(time: String): Date? {
        //拼接今天所属的开门时间
        var dateTimeStr = DateTimeUtils.getCurrentDate() + " " + time;
        var format = SimpleDateFormat(DateTimeUtils.DATE_TIME_FORMAT);
        var date = format.parse(dateTimeStr);
        return date;
    }

    fun getStartBusinessType(date: Date): Long? {
        initBusinessTime()
        var dateTimeStr = DateTimeUtils.formatDate(date, DateTimeUtils.DATE_FORMAT) + " " + mStartTime;
        var format = SimpleDateFormat(DateTimeUtils.DATE_TIME_FORMAT);
        var date = format.parse(dateTimeStr);
        return date.time;
    }

    fun getStopBusinessType(date: Date): Long? {
        initBusinessTime();
        var dateTimeStr = DateTimeUtils.formatDate(date, DateTimeUtils.DATE_FORMAT) + " " + mStopTime;
        var format = SimpleDateFormat(DateTimeUtils.DATE_TIME_FORMAT);
        var date = format.parse(dateTimeStr);
        return date.time;
    }
}