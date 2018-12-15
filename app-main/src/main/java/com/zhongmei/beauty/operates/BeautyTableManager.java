package com.zhongmei.beauty.operates;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.beauty.interfaces.ITableTradeRefresh;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyTableManager extends TableManagerBase implements ITableTradeRefresh {

    private BeautyTableTradeCache mBeautyTableTradeCache;


    @Override
    public TableTradeCacheBase getTableTradeCache() {
        if (mBeautyTableTradeCache == null) {
            mBeautyTableTradeCache = new BeautyTableTradeCache();
            mBeautyTableTradeCache.setmTableTradeRefreshListener(this);
        }
        return mBeautyTableTradeCache;
    }

    @Override
    public void refreshTableTrade(Map<Long, StateWrapper> stateWrapper) {
        //跟新桌台上的订单信息。
        doRefreshState(stateWrapper);
        //刷新UI
        if (mTableOperatorListeners != null) {
            List<ZoneModel> listZoneModel = new ArrayList<ZoneModel>(zoneModelFinder.values());
            for (ITableOperator listener : mTableOperatorListeners) {
                listener.refreshTableTrades(listZoneModel);
            }
        }
    }


    private int doRefreshState(Map<Long, StateWrapper> stateWrapperMap) {
        int emptyTableCount = 0;
        for (ZoneModel zoneModel : zoneModelFinder.values()) {
            for (DinnertableModel dinnertableModel : zoneModel.getDinnertableModels()) {
                Long tableId = dinnertableModel.getId();
                StateWrapper stateWrapper = stateWrapperMap.get(tableId);
                if (stateWrapper != null) {
                    TableStateInfo tableStateInfo = stateWrapper.tableStateInfo;
                    // 创建桌台上的单据数据
                    List<IDinnertableTrade> dinnertableTrades = createTableTradeInfo(stateWrapper.tradeTableInfos, dinnertableModel);

                    dinnertableModel.setTableStatus(tableStateInfo.tableStatus);
                    dinnertableModel.setStateInfo(tableStateInfo, dinnertableTrades);
                }

                if (dinnertableModel.getPhysicsTableStatus() == TableStatus.EMPTY) {
                    //桌台状态为null
                    emptyTableCount++;
                }
            }
        }
        return emptyTableCount;
    }


    private List<IDinnertableTrade> createTableTradeInfo(Collection<TradeTableInfo> tradeInfo, DinnertableModel tableModel) {
        List<IDinnertableTrade> dinnertableTrades = new ArrayList<IDinnertableTrade>();
        if (Utils.isNotEmpty(tradeInfo)) {
            for (TradeTableInfo info : tradeInfo) {
                DinnertableTradeModel tradeModel = createDinnerTableTradeModel(info, tableModel);
                dinnertableTrades.add(tradeModel);
            }
        }
        return dinnertableTrades;
    }


    private DinnertableTradeModel createDinnerTableTradeModel(TradeTableInfo tradeInfo, DinnertableModel tableModel) {
        DinnertableTradeModel tradeModel = new DinnertableTradeModel(tradeInfo, tableModel);

        if (tableModel.getmListTradeHttpRecord() != null) {
            tradeInfo.httpRecord = tableModel.getmListTradeHttpRecord().get(tradeInfo.tradeUuid);
        }

        tradeModel.refreshSpendTime();

        return tradeModel;
    }
}
