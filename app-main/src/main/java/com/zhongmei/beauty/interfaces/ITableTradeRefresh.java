package com.zhongmei.beauty.interfaces;

import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;

import java.util.Map;



public interface ITableTradeRefresh {
    public void refreshTableTrade(Map<Long, StateWrapper> stateWrapper);
}
