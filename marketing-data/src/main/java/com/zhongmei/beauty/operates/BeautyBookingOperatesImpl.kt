package com.zhongmei.beauty.operates

import android.app.Activity
import com.zhongmei.yunfu.http.CalmNetWorkRequest
import com.zhongmei.yunfu.monitor.CalmResponseListener
import com.zhongmei.yunfu.http.OpsRequest
import com.zhongmei.yunfu.http.processor.CalmDatabaseProcessor
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType
import com.zhongmei.beauty.operates.message.*
import com.zhongmei.beauty.utils.BeautyServerAddressUtil
import com.zhongmei.yunfu.context.base.BaseApplication
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.db.entity.booking.Booking
import com.zhongmei.yunfu.net.builder.NetworkRequest
import com.zhongmei.yunfu.orm.DBHelperManager
import com.zhongmei.yunfu.orm.DatabaseHelper
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.ResponseObject
import java.util.concurrent.Callable

/**
 * 美业预定
 * Created by demo on 2018/12/15
 */
class BeautyBookingOperatesImpl : AbstractOpeartesImpl, BeautyBookingOperates {

    constructor(context: IOperates.ImplContext) : super(context)

    override fun getBookingList(req: BeautyBookingListReq, listener: CalmResponseListener<ResponseObject<BeautyBookingListResp>>, activity: Activity) {
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.getBookingList())
                .requestContent(req)
                .responseClass(BeautyBookingListResp::class.java)
                .successListener(listener)
                .errorListener(listener)
                .with(activity)
                .tag("getBookingList")
                .showLoading()
                .create()
    }

    override fun bookingSubmit(bookingReq: BeautyBookingReq, listener: ResponseListener<BeautyBookingResp>) {
        var url = BeautyServerAddressUtil.bookingSubmit()
        val executor = OpsRequest.Executor.create<BeautyBookingReq, BeautyBookingResp>(url)
        executor.requestValue(bookingReq).responseProcessor(BeautyBookingCreateProcessor()).responseClass(BeautyBookingResp::class.java).execute(listener, "bookingSubmit")

    }

    override fun updateBookingSubmit(req: BeautyBookingUpdateReq, listener: ResponseListener<BeautyBookingResp>) {
        var url = BeautyServerAddressUtil.updateBookingSubmit()
        val executor = OpsRequest.Executor.create<BeautyBookingUpdateReq, BeautyBookingResp>(url)
        executor.requestValue(req).responseClass(BeautyBookingResp::class.java).execute(listener, "updateBookingSubmit")
    }

    override fun acceptOrRefuseBookingTrade(req: BeautyAcceptOrRefuseReq, listener: CalmResponseListener<ResponseObject<BeautyBookingResp>>, activity: Activity) {
//        var url = BeautyServerAddressUtil.acceptOrRefuseBooking()
//        val executor = OpsRequest.Executor.create<BeautyAcceptOrRefuseReq, BeautyBookingResp>(url)
//        executor.requestValue(req).responseClass(BeautyBookingResp::class.java).execute(listener, "acceptOrRefuseBookingTrade")

        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.acceptOrRefuseBooking())
                .requestContent(req)
                .responseClass(BeautyBookingResp::class.java)
                .responseProcessor(BeautyBookingProcessor())
                .successListener(listener)
                .errorListener(listener)
                .with(activity)
                .tag("acceptOrRefuseBookingTrade")
                .showLoading()
                .create()


    }

    override fun bookingCheckUser(startTime: Long, endTime: Long, userType: TradeUserType, listener: ResponseListener<BeautyBookingCheckUserResp>) {
        var req = BeautyBookingCheckUserReq()
        req.startTime = startTime
        req.endTime = endTime
        req.userType = userType.value()
        var url = BeautyServerAddressUtil.bookingCheckUser()
        val executor = OpsRequest.Executor.create<BeautyBookingCheckUserReq, BeautyBookingCheckUserResp>(url)
        executor.requestValue(req).responseClass(BeautyBookingCheckUserResp::class.java).execute(listener, "bookingCheckUser")
    }

    override fun bookingCancel(bookingId: Long, reason: String, cancelOrderUser: Long, listener: ResponseListener<BeautyBookingCancelResp>) {
        var req = BeautyBookingCancelReq()
        req.bookingId = bookingId
        req.reason = reason
        req.cancelOrderUser = cancelOrderUser
        req.brandIdenty = BaseApplication.getInstance().brandIdenty;
        req.shopIdenty = BaseApplication.getInstance().shopIdenty
        var url = BeautyServerAddressUtil.bookingCancel()
        val executor = OpsRequest.Executor.create<BeautyBookingCancelReq, BeautyBookingCancelResp>(url)
        executor.requestValue(req).responseProcessor(BeautyBookingCancelProcessor()).responseClass(BeautyBookingCancelResp::class.java).execute(listener, "bookingCancel")
    }

    override fun bookingOpenTrade(req: BeautyBookingTradeReq, listener: CalmResponseListener<ResponseObject<BeautyBookingSubmitResp>>, activity: Activity) {
        CalmNetWorkRequest.with(BaseApplication.getInstance())
                .url(BeautyServerAddressUtil.bookingOpenTrade())
                .requestContent(req)
                .responseClass(BeautyBookingSubmitResp::class.java)
                .responseProcessor(BeautyBookingRespProcessor())
                .successListener(listener)
                .errorListener(listener)
                .with(activity)
                .tag("bookingOpenTrade")
                .showLoading()
                .create()

    }

    class BeautyBookingRespProcessor : CalmDatabaseProcessor<BeautyBookingSubmitResp>() {

        override fun transactionCallable(helper: DatabaseHelper?, resp: BeautyBookingSubmitResp?) {
            DBHelperManager.saveEntities(helper, Booking::class.java, resp!!.booking);
            BeautyOperatesImpl.saveData(helper, resp)
        }
    }

    class BeautyBookingCreateProcessor : OpsRequest.SaveDatabaseResponseProcessor<BeautyBookingResp>() {
        override fun getCallable(helper: DatabaseHelper?, resp: BeautyBookingResp?): Callable<Void> {
            return Callable<Void> {
                DBHelperManager.saveEntities(helper, Booking::class.java, resp!!.booking);
                null
            }
        }
    }


    class BeautyBookingCancelProcessor : OpsRequest.SaveDatabaseResponseProcessor<BeautyBookingCancelResp>() {
        override fun getCallable(helper: DatabaseHelper?, resp: BeautyBookingCancelResp?): Callable<Void> {
            return Callable<Void> {
                DBHelperManager.saveEntities(helper, Booking::class.java, resp);
                null
            }
        }
    }


    class BeautyBookingProcessor : CalmDatabaseProcessor<BeautyBookingResp>() {
        override fun transactionCallable(helper: DatabaseHelper?, resp: BeautyBookingResp?) {
            DBHelperManager.saveEntities(helper, Booking::class.java, resp)
        }
    }

}
