package com.zhongmei.bty.data.operates;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberReq;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberResp;
import com.zhongmei.bty.data.operates.message.content.QueueModifyReq;
import com.zhongmei.bty.data.operates.message.content.QueuePredictWaitTimeResp;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;


/**
 * 预订的一些接口
 */
public interface QueueOperates extends IOperates {


    /**
     * 发送短信。
     *
     * @param tradeVo
     * @param listener
     */
    void sendMessage(Queue queue, Listener<String> listener, ErrorListener errorListener);

    /**
     * 自助语音。
     *
     * @param tradeVo
     * @param listener
     */
    void queuePhone(Queue queue, Listener<String> listener, ErrorListener errorListener);

    /**
     * 排队叫号
     *
     * @param queueID
     * @param listener
     */
    void queueCallNumber(Long queueID, ResponseListener<QueueCallNumberResp> listener);

    /**
     * 更新队列
     *
     * @param queue
     * @param listener
     */
    void updateQueue(@QueueReq.Type int type, Queue queue, ResponseListener<QueueResp> listener);

    /**
     * 排队入场，排队过号，排队清零，取消排队
     *
     * @param type
     * @param serverId
     * @param queueLineId
     * @param lastSyncMarker
     * @param listener
     */
    void changeQueue(@QueueReq.Type int type, String serverId, Long queueLineId, Long lastSyncMarker, ResponseListener<QueueResp> listener);

    /**
     * 创建和更新排队;
     *
     * @param type
     * @param
     * @param queue
     * @param listener
     */
    void createAndUpdateQueue(int type, Queue queue, ResponseListener<Queue> listener);

    void queueRecoverInvalid(String queueUuid, Long modifyDateTime, FragmentActivity fragment, CalmResponseListener<ResponseObject<Queue>> listener);

    void queueModify(FragmentActivity fragment, QueueModifyReq req, CalmResponseListener<ResponseObject<Queue>> listener);

    void queueCallNum(FragmentActivity fragment, QueueCallNumberReq req, CalmResponseListener<ResponseObject<QueueCallNumberResp>> listener);

    void predictWaitTime(Context context, String queueUuid, ResponseListener<QueuePredictWaitTimeResp> listener);

    /**
     * 美大二维码
     *
     * @param queueId
     * @param listener
     */
    void queryqtcode(Long queueId, ResponseListener<QueueExtra> listener);
}
