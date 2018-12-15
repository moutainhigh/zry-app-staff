package com.zhongmei.beauty.booking.list.manager

import android.app.Activity
import android.content.Intent
import com.zhongmei.beauty.booking.bean.BeautyBookingVo
import com.zhongmei.beauty.booking.util.BeautyBookingShopTool
import com.zhongmei.beauty.utils.BeautyOrderConstants
import com.zhongmei.yunfu.db.entity.booking.Booking
import com.zhongmei.yunfu.monitor.CalmResponseListener
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer
import com.zhongmei.yunfu.net.builder.NetError
import com.zhongmei.beauty.operates.BeautyBookingOperates
import com.zhongmei.beauty.operates.BeautyOperatesImpl
import com.zhongmei.beauty.operates.message.BeautyAcceptOrRefuseReq
import com.zhongmei.beauty.operates.message.BeautyBookingResp
import com.zhongmei.beauty.operates.message.BeautyBookingSubmitResp
import com.zhongmei.beauty.operates.message.BeautyBookingTradeReq
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory
import com.zhongmei.yunfu.db.enums.BookingOrderStatus
import com.zhongmei.yunfu.db.enums.CustomerType
import com.zhongmei.yunfu.resp.ResponseObject
import com.zhongmei.yunfu.context.util.SystemUtils
import com.zhongmei.yunfu.net.volley.VolleyError
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.util.ToastUtil
import com.zhongmei.yunfu.util.ValueEnums

/**
 * 预定开单工具类

 */
class BeautyOpenTradeManager {

    fun openTrade(activity: Activity, beautyBookingVo: BeautyBookingVo, callback: OpenTradeCallBack) {
        var req = buildReq(beautyBookingVo)
        var operates: BeautyBookingOperates = OperatesFactory.create(BeautyBookingOperates::class.java)
        operates.bookingOpenTrade(
                req,
                object : CalmResponseListener<ResponseObject<BeautyBookingSubmitResp>>() {
                    override fun onSuccess(data: ResponseObject<BeautyBookingSubmitResp>?) {
                        if (ResponseObject.isOk(data)) {
                            toDishActivity(activity, data!!.content.trade.id)
                            callback.onOpenTradeSuccess()
                        } else {
                            ToastUtil.showLongToast(data!!.message)
                        }
                    }

                    override fun onError(error: NetError?) {
                        ToastUtil.showLongToast(error?.volleyError?.message)
                    }
                },
                activity
        )
    }

    fun acceptOrRefuseBookingTrade(activity: Activity, beautyBookingVo: BeautyBookingVo, status: BookingOrderStatus, callback: OpenTradeCallBack) {
        var req = buildAcceptOrRefuseReq(beautyBookingVo.booking, status);
        var operates: BeautyBookingOperates = OperatesFactory.create(BeautyBookingOperates::class.java);
        operates.acceptOrRefuseBookingTrade(req, object : CalmResponseListener<ResponseObject<BeautyBookingResp>>() {
            override fun onSuccess(data: ResponseObject<BeautyBookingResp>?) {
                if (ResponseObject.isOk(data)) {
                    callback.onOpenTradeSuccess()
                } else {
                    ToastUtil.showLongToast(data!!.message)
                }
            }

            override fun onError(error: NetError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }, activity)
    }

    fun buildAcceptOrRefuseReq(booking: Booking, status: BookingOrderStatus): BeautyAcceptOrRefuseReq {
        var req = BeautyAcceptOrRefuseReq()
        req.bookingId = booking.id
        req.bookingServerUpdateTime = booking.serverUpdateTime
        req.toOrderStatus = ValueEnums.toValue(status);
        return req;
    }

    /**
     * 构建开单请求
     */
    fun buildReq(beautyBookingVo: BeautyBookingVo): BeautyBookingTradeReq {

        var req = BeautyBookingTradeReq()
        var shopcart = DinnerShoppingCart()
        BeautyBookingShopTool.copyBookingDish(beautyBookingVo, shopcart)
        var tradeReq = BeautyOperatesImpl.toBeautyModifyReq(shopcart.order);
//        Beans.copyProperties(shopcart.order.trade,req.tradeRequest)

        req.tradeRequest = tradeReq.tradeRequest
        req.inventoryRequest = tradeReq.inventoryRequest;
        req.bookingInfo = buildTradeBookingReq(beautyBookingVo)
        req.tradeRequest.tradeCustomers = buildTradeCustomer(beautyBookingVo, req.tradeRequest.uuid)
        return req
    }

    /**
     * 构建订单与预定关联
     */
    fun buildTradeBookingReq(beautyBookingVo: BeautyBookingVo): BeautyBookingTradeReq.TradeBookingRequesst {
        var tradeBookingRequesst = BeautyBookingTradeReq.TradeBookingRequesst()
        tradeBookingRequesst.bookingId = beautyBookingVo.booking.id
        tradeBookingRequesst.bookingUuid = beautyBookingVo.booking.uuid
        tradeBookingRequesst.bookingServerUpdateTime = beautyBookingVo.booking.serverUpdateTime
//        tradeBookingRequesst.shopArriveTime=beautyBookingVo.booking.shopArriveTime
//        tradeBookingRequesst.shopArriveUser=beautyBookingVo.booking.shopArriveUser
        return tradeBookingRequesst
    }

    /**
     * 构建开单会员对象
     */
    fun buildTradeCustomer(beautyBookingVo: BeautyBookingVo, tradeUuid: String): List<TradeCustomer> {
        var customerList = ArrayList<TradeCustomer>()
        customerList.add(buildTradeCustomerByType(beautyBookingVo.booking, tradeUuid, CustomerType.BOOKING))
        customerList.add(buildTradeCustomerByType(beautyBookingVo.booking, tradeUuid, CustomerType.MEMBER))
        return customerList
    }

    fun buildTradeCustomerByType(booking: Booking, tradeUuid: String, customerType: CustomerType): TradeCustomer {
        var tradeCustomer = TradeCustomer()
        tradeCustomer.uuid = SystemUtils.genOnlyIdentifier()
        tradeCustomer.tradeUuid = tradeUuid
        tradeCustomer.customerId = booking.commercialId
        tradeCustomer.customerName = booking.commercialName
        tradeCustomer.customerPhone = booking.commercialPhone
        tradeCustomer.customerType = customerType
        tradeCustomer.customerSex = booking.commercialGender
        tradeCustomer.validateCreate()
        return tradeCustomer
    }


    fun toDishActivity(activity: Activity, tradeId: Long) {
        val intent = Intent()
        intent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, true)
        intent.putExtra(BeautyOrderConstants.ORDER_EDIT_TRADEID, tradeId)
        intent.setAction(BeautyOrderConstants.DISH_ACTIVITY_ACTION)
        activity.startActivity(intent)
    }

    open interface OpenTradeCallBack {
        //开单成功的会调，修改订单状态也调用这个
        public fun onOpenTradeSuccess()
    }
}