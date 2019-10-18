package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.math.BigDecimal;
import java.util.List;


public interface KdsTradeDal extends IOperates {



    long getTradeItem(Long id) throws Exception;


    BigDecimal getDishBatservingCount(long tradeItemId) throws Exception;


    List<KdsTradeItemPart> getTradeItem(List<Long> tradeItemIds) throws Exception;
}
