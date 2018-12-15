package com.zhongmei.beauty.interfaces;

import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;

import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public interface ITableTradeRefresh {
    public void refreshTableTrade(Map<Long, StateWrapper> stateWrapper);
}
