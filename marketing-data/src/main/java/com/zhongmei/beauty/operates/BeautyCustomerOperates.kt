package com.zhongmei.beauty.operates

import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceAccount
import com.zhongmei.bty.basemodule.customer.bean.CustomerExpenseRecordResp
import com.zhongmei.beauty.operates.message.BeautyAcitivityBuyRecordResp
import com.zhongmei.beauty.operates.message.BeautyCardServiceHistoryResp
import com.zhongmei.beauty.operates.message.BeautyCardServiceResp
import com.zhongmei.bty.basemodule.beauty.BeautyCardServiceInfo
import com.zhongmei.bty.commonmodule.data.operate.IOperates
import com.zhongmei.yunfu.bean.YFResponseList
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
     * 获取顾客活动购买记录
     *
     * @param customerId 顾客id
     * @param listener
     */
    fun getActivityBuyRecord(customerId: Long, listener: YFResponseListener<YFResponseList<BeautyAcitivityBuyRecordResp>>)
}
