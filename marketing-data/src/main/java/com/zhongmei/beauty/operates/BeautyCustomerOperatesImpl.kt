package com.zhongmei.beauty.operates

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount
import com.zhongmei.yunfu.http.OpsRequest
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil
import com.zhongmei.bty.basemodule.customer.bean.CustomerExpenseRecordResp
import com.zhongmei.beauty.operates.message.*
import com.zhongmei.beauty.utils.BeautyServerAddressUtil
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.bean.YFResponseList
import com.zhongmei.yunfu.http.RequestObject
import com.zhongmei.yunfu.http.JFRequest
import com.zhongmei.yunfu.resp.data.TransferReq
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.YFResponseListener

/**
 * 美业会员实现
 * Created by demo on 2018/12/15
 */
class BeautyCustomerOperatesImpl : AbstractOpeartesImpl, BeautyCustomerOperates {

    constructor(context: IOperates.ImplContext) : super(context)

    /**
     * 获取次卡服务
     */
    override fun getCardServiceInfo(userId: Long, customerId: Long, listener: ResponseListener<BeautyCardServiceResp>) {
        val req = toBeautyCardServiceReq(userId, customerId)
        val transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer()
        val transferReq = TransferReq<BeautyCardServiceReq>()
        transferReq.url = BeautyServerAddressUtil.cardServiceInfo()
        transferReq.setPostData(req)
        val executor = OpsRequest.Executor.create<TransferReq<BeautyCardServiceReq>, BeautyCardServiceResp>(transferUrl)
        executor.requestValue(transferReq).responseClass(BeautyCardServiceResp::class.java).execute(listener, "getCardServiceInfo")
    }

    override fun getCardServiceInfo(userId: Long, customerId: Long, listener: YFResponseListener<YFResponseList<BeautyCardServiceAccount>>) {
        val req = toBeautyCardServiceReq(userId, customerId)
        var url = BeautyServerAddressUtil.cardServiceInfo()
        val executor = JFRequest.create(url)
        executor.requestValue(req)
                .execute(listener, "getCardServiceInfo")
    }

    override fun getExpenseRecord(customerId: Long, listener: YFResponseListener<YFResponseList<CustomerExpenseRecordResp>>) {
        val req = mapOf("customerId" to customerId)
        var url = BeautyServerAddressUtil.getCustomerExpense()
        val executor = JFRequest.create(url)
        executor.requestValue(req)
                .execute(listener, "CustomerExpenseRecord")
    }


    /**
     * 转换 次卡服务信息 请求参数
     */
    private fun toBeautyCardServiceReq(userId: Long, customerId: Long): BeautyCardServiceReq {
        val req = BeautyCardServiceReq()
        req.customerId = customerId
        req.userId = userId
        return req
    }

    /**
     * 卡服务历史
     */
    override fun queryConsumeProject(userId: Long, cardNo: String, listener: ResponseListener<BeautyCardServiceHistoryResp>) {
        val req = toBeautyCardServiceHistortReq(userId, cardNo)
        val transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer()
        val transferReq = TransferReq<BeautyCardServiceHistoryReq>()
        transferReq.url = BeautyServerAddressUtil.cardServiceHistory()
        transferReq.setPostData(req)
        val executor = OpsRequest.Executor.create<TransferReq<BeautyCardServiceHistoryReq>, BeautyCardServiceHistoryResp>(transferUrl)
        executor.requestValue(transferReq).responseClass(BeautyCardServiceHistoryResp::class.java).execute(listener, "queryConsumeProject")
    }


    /**
     * 卡服务历史 请求参数
     */
    private fun toBeautyCardServiceHistortReq(userId: Long, cardNo: String): BeautyCardServiceHistoryReq {
        val req = BeautyCardServiceHistoryReq()
        req.userId = userId
        req.cardNo = cardNo
        return req
    }

    /**
     * 获取顾客活动购买记录
     */
    override fun getActivityBuyRecord(customerId: Long, listener: YFResponseListener<YFResponseList<BeautyAcitivityBuyRecordResp>>) {
        val req = toBeautyAcitivityBuyRecordReq(customerId)
        val url = BeautyServerAddressUtil.getActivityBuyRecord()
        val executor = JFRequest.create(url)
        val request = RequestObject.create<BeautyAcitivityBuyRecordReq>(req)
        executor.requestValue(request)
                .execute<YFResponseList<BeautyAcitivityBuyRecordResp>>(listener, "getActivityBuyRecord")
    }

    /**
     * 获取顾客活动购买记录 请求参数
     */
    private fun toBeautyAcitivityBuyRecordReq(customerId: Long): BeautyAcitivityBuyRecordReq {
        val req = BeautyAcitivityBuyRecordReq()
        req.customerId = customerId
        return req
    }


}