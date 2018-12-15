package com.zhongmei.beauty.operates

import android.app.Activity
import com.zhongmei.yunfu.monitor.CalmResponseListener
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType
import com.zhongmei.beauty.operates.message.*
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.ResponseObject

/**
 *美业预定

 * @date 2018/7/20
 */
interface BeautyBookingOperates : IOperates {

    /**
     * 提交预定
     */
    fun bookingSubmit(bookingReq: BeautyBookingReq, listener: ResponseListener<BeautyBookingResp>)

    /**
     * 检查技师
     */
    fun bookingCheckUser(startTime: Long, endTime: Long, userType: TradeUserType, listener: ResponseListener<BeautyBookingCheckUserResp>)

    /**
     * 预定取消
     *
     * @param bookingId -> bookingId
     * @param reason -> 理由
     * @param cancelOrderUser -> 操作人
     */
    fun bookingCancel(bookingId: Long, reason: String, cancelOrderUser: Long, listener: ResponseListener<BeautyBookingCancelResp>)

    /**
     * 获取预定列表
     */
    fun getBookingList(req: BeautyBookingListReq, listener: CalmResponseListener<ResponseObject<BeautyBookingListResp>>, activity: Activity)

    /**
     * 预定开单
     */
    fun bookingOpenTrade(req: BeautyBookingTradeReq, listener: CalmResponseListener<ResponseObject<BeautyBookingSubmitResp>>, activity: Activity)

    /**
     * 编辑预定提交
     */
    fun updateBookingSubmit(req: BeautyBookingUpdateReq, listener: ResponseListener<BeautyBookingResp>)

    /**
     * 接受或拒绝第三方订单
     */
    fun acceptOrRefuseBookingTrade(req: BeautyAcceptOrRefuseReq, listener: CalmResponseListener<ResponseObject<BeautyBookingResp>>, activity: Activity)


}