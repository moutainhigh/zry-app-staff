package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.impl.CallDishNotifyOperatesImpl.NotifyReq;

/**
 * 订单中心叫号
 */
public interface CallDishNotifyOperates extends IOperates {

    /**
     * 语音通知
     *
     * @param req
     * @param listener
     */
    void notifyVoice(NotifyReq req, ResponseListener<OrderNotify> listener);

}
