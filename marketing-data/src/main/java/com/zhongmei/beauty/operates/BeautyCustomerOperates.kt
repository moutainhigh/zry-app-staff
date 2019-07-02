package com.zhongmei.beauty.operates

import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.beauty.operates.message.BeautyCardServiceHistoryResp
import com.zhongmei.beauty.operates.message.BeautyCardServiceResp
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo
import com.zhongmei.bty.basemodule.customer.bean.*
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.bean.YFResponse
import com.zhongmei.yunfu.bean.YFResponseList
import com.zhongmei.yunfu.db.entity.TaskRemind
import com.zhongmei.yunfu.resp.ResponseListener
import com.zhongmei.yunfu.resp.YFResponseListener

/**
 * 美业会员

 * @date 2018/5/29 10:35
 */
interface BeautyCustomerOperates : IOperates {

    /**
     * 查询次卡消费项目
     */
    fun queryConsumeProject(userId: Long, cardNo: String, listener: ResponseListener<BeautyCardServiceHistoryResp>)

    /**
     * 根据顾客id获取次卡服务信息
     *
     * @param userId
     * @param customerId 顾客id
     * @param listener
     * @param context
     */
    fun getCardServiceInfo(userId: Long, customerId: Long, listener: ResponseListener<BeautyCardServiceResp>)

    /**
     * 获取次卡服务
     */
    fun getCardServiceInfo(userId: Long, customerId: Long, listener: YFResponseListener<YFResponseList<BeautyCardServiceInfo>>)

    /**
     * 获取顾客消费记录
     */
    fun getExpenseRecord(customerId: Long, listener: YFResponseListener<YFResponseList<CustomerExpenseRecordResp>>)

    /**
     * 获取会员档案
     */
    fun getDocRecord(customerDocReq: CustomerDocRecordReq, listener: YFResponseListener<YFResponseList<CustomerDocRecordResp>>)

    /**
     * 获取任务信息
     */
    fun getTaskList(taskReq: TaskQueryReq, listener: YFResponseListener<YFResponseList<TaskRemind>>)

    /**
     * 获取会员档案详情
     */
    fun getDocRecordDetail(docId: Long, listener: YFResponseListener<YFResponse<CustomerDocRecordResp>>)

    /**
     * 保存文档信息
     */
    fun saveDocRecord(docDetail: CustomerDocReq, listener: YFResponseListener<YFResponse<CustomerDocRecordResp>>)

    /**
     * 保存文档信息
     */
    fun saveTask(taskReq: TaskCreateOrEditReq, listener: YFResponseListener<YFResponse<TaskRemind>>)

    /**
     * 获取顾客活动购买记录
     *
     * @param customerId 顾客id
     * @param listener
     */
    fun getActivityBuyRecord(customerId: Long, listener: YFResponseListener<YFResponseList<BeautyAcitivityBuyRecordResp>>)
}
