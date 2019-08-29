package com.zhongmei.beauty.operates

import com.zhongmei.beauty.operates.message.*
import com.zhongmei.beauty.utils.BeautyServerAddressUtil
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo
import com.zhongmei.bty.basemodule.customer.bean.*
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.bean.YFResponse
import com.zhongmei.yunfu.bean.YFResponseList
import com.zhongmei.yunfu.db.entity.TaskRemind
import com.zhongmei.yunfu.http.RequestObject
import com.zhongmei.yunfu.http.JFRequest
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
        /*val req = toBeautyCardServiceReq(userId, customerId)
        val transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer()
        val transferReq = TransferReq<BeautyCardServiceReq>()
        transferReq.url = BeautyServerAddressUtil.cardServiceInfo()
        transferReq.setPostData(req)
        val executor = OpsRequest.Executor.create<TransferReq<BeautyCardServiceReq>, BeautyCardServiceResp>(transferUrl)
        executor.requestValue(transferReq).responseClass(BeautyCardServiceResp::class.java).execute(listener, "getCardServiceInfo")*/
    }

    override fun getCardServiceInfo(userId: Long, customerId: Long, listener: YFResponseListener<YFResponseList<BeautyCardServiceInfo>>) {
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


    override fun getDocRecord(customerDocReq: CustomerDocRecordReq, listener: YFResponseListener<YFResponseList<CustomerDocRecordResp>>) {
        var url = BeautyServerAddressUtil.getCustomerDoc()
        val executor = JFRequest.create(url)
        executor.requestValue(customerDocReq)
                .execute(listener, "CustomerDocRecord")
    }

    override fun getTaskList(taskReq: TaskQueryReq, listener: YFResponseListener<YFResponseList<TaskRemind>>) {
        var url = BeautyServerAddressUtil.getTask()
        val executor = JFRequest.create(url)
        executor.requestValue(taskReq)
                .execute(listener, "taskRecord")
    }

    override fun getDocRecordDetail(docId: Long, listener: YFResponseListener<YFResponse<CustomerDocRecordResp>>) {
        val req = mapOf("archivesId" to docId)
        var url = BeautyServerAddressUtil.getCustomerDocDetail()
        val executor = JFRequest.create(url)
        executor.requestValue(req)
                .execute(listener, "CustomerDocRecordDetail")
    }

    override fun saveDocRecord(docDetail: CustomerDocReq, listener: YFResponseListener<YFResponse<CustomerDocRecordResp>>) {
        var url = BeautyServerAddressUtil.saveCustomerDoc()
        val executor = JFRequest.create(url)
        executor.requestValue(docDetail)
                .execute(listener, "saveDocRecord")
    }


    override fun saveTask(taskReq: TaskCreateOrEditReq, listener: YFResponseListener<YFResponse<TaskRemind>>) {
        var url = BeautyServerAddressUtil.saveTask()
        if(taskReq.taskId!=null){
            url = BeautyServerAddressUtil.saveEditTask()
        }
        val executor = JFRequest.create(url)
        executor.requestValue(taskReq)
                .execute(listener, "saveTask")
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
        /*val req = toBeautyCardServiceHistortReq(userId, cardNo)
        val transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer()
        val transferReq = TransferReq<BeautyCardServiceHistoryReq>()
        transferReq.url = BeautyServerAddressUtil.cardServiceHistory()
        transferReq.setPostData(req)
        val executor = OpsRequest.Executor.create<TransferReq<BeautyCardServiceHistoryReq>, BeautyCardServiceHistoryResp>(transferUrl)
        executor.requestValue(transferReq).responseClass(BeautyCardServiceHistoryResp::class.java).execute(listener, "queryConsumeProject")*/
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
    override fun getActivityBuyRecord(customerId: Long, listener: YFResponseListener<YFResponseList<BeautyActivityBuyRecordResp>>) {
        val req = toBeautyAcitivityBuyRecordReq(customerId)
        val url = BeautyServerAddressUtil.getActivityBuyRecord()
        val executor = JFRequest.create(url)
        val request = RequestObject.create<BeautyActivityBuyRecordReq>(req)
        executor.requestValue(request)
                .execute<YFResponseList<BeautyActivityBuyRecordResp>>(listener, "getActivityBuyRecord")
    }

    /**
     * 根据券码获取活动信息
     */
    override fun getActivityByCode(code: String, listener: YFResponseListener<YFResponse<BeautyActivityBuyRecordResp>>) {
        val req = toBeautyActivityByCodeReq(code)
        val url = BeautyServerAddressUtil.getActivityByCode()
        val executor = JFRequest.create(url)
        val request = RequestObject.create<BeautyActivityByCodeReq>(req)
        executor.requestValue(request)
                .execute<YFResponse<BeautyActivityBuyRecordResp>>(listener, "getActivityByCode")
    }

    /**
     * 获取顾客活动购买记录 请求参数
     */
    private fun toBeautyAcitivityBuyRecordReq(customerId: Long): BeautyActivityBuyRecordReq {
        val req = BeautyActivityBuyRecordReq()
        req.customerId = customerId
        return req
    }

    private fun toBeautyActivityByCodeReq(code:String) : BeautyActivityByCodeReq{
        var req=BeautyActivityByCodeReq()
        req.code=code
        return req
    }


}