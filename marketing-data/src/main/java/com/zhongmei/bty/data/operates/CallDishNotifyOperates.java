package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.impl.CallDishNotifyOperatesImpl.NotifyReq;


public interface CallDishNotifyOperates extends IOperates {


    void notifyVoice(NotifyReq req, ResponseListener<OrderNotify> listener);

}
