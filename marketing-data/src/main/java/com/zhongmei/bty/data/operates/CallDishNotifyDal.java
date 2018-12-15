package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.entity.enums.NotifyOrderType;

public interface CallDishNotifyDal extends IOperates {
    List<OrderNotify> queryOrderNotify(NotifyOrderType notifyOrderType) throws Exception;
}
