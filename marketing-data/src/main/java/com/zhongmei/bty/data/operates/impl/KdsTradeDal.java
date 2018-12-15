package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.math.BigDecimal;
import java.util.List;

/**
 * Kds划菜、取消划菜
 *
 * @created 2017/6/9
 */
public interface KdsTradeDal extends IOperates {


    /**
     * 查询kds tradeId
     *
     * @param id
     * @return
     * @throws Exception
     */
    long getTradeItem(Long id) throws Exception;

    /**
     * 查询菜品划菜状态
     *
     * @param tradeItemId
     * @return
     * @throws Exception
     */
    BigDecimal getDishBatservingCount(long tradeItemId) throws Exception;


    List<KdsTradeItemPart> getTradeItem(List<Long> tradeItemIds) throws Exception;
}
