package com.zhongmei.beauty.operates;

import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BeautyTableTradeCache extends TableTradeCacheBase {
    private final String TAG = BeautyTableTradeCache.class.getSimpleName();

    @Override
    public void loadTableTrades(Long zoneId) {
        DatabaseHelper dbHelper = getDBHelper();

        try {
                        List<Tables> tables = getTablesByZoneID(dbHelper, zoneId);
            List<Long> tableIds = new ArrayList<>();
            for (Tables table : tables) {
                tableIds.add(table.getId());
            }

                        List<TradeTable> tradeTables = getTradeTableByTables(dbHelper, tableIds);
            List<Long> tradeIds = new ArrayList<>();
            Map<Long, List<TradeTable>> mapTradeTables = new HashMap<>();
            if (tradeTables != null) {
                for (TradeTable tradeTable : tradeTables) {
                    tradeIds.add(tradeTable.getTradeId());

                    if (!mapTradeTables.containsKey(tradeTable.getTradeId())) {
                        mapTradeTables.put(tradeTable.getTradeId(), new ArrayList<TradeTable>());
                    }
                    mapTradeTables.get(tradeTable.getTradeId()).add(tradeTable);
                }
            }

                        List<Trade> trades = getTradeByTradeIds(dbHelper, tradeIds);
            Map<Long, Trade> mapTrades = new HashMap<>();
            if (trades != null) {                tableIds.clear();
                for (Trade trade : trades) {
                    tableIds.add(trade.getId());

                    mapTrades.put(trade.getId(), trade);
                }
            }




            Map<Long, List<TradeTableInfo>> ttInfosFinder = new HashMap<>();
            for (Trade trade : trades) {


                List<TradeTable> tradeTableList = mapTradeTables.get(trade.getId());
                if (tradeTableList != null) {
                    for (TradeTable tradeTable : tradeTableList) {
                        TradeTableInfo ttInfo = new TradeTableInfo(trade, tradeTable,
                                DinnertableStatus.SERVING, null, null);
                        List<TradeTableInfo> list = ttInfosFinder.get(ttInfo.tableId);
                        if (list == null) {
                            list = new ArrayList<TradeTableInfo>();
                            ttInfosFinder.put(ttInfo.tableId, list);
                        }
                        list.add(ttInfo);
                    }
                }
            }


            Map<Long, StateWrapper> stateWrapperMap = new HashMap<Long, StateWrapper>();            for (Tables table : tables) {
                TableStateInfo stateInfo = new TableStateInfo(table, BusinessType.BEAUTY);                List<TradeTableInfo> ttInfos = ttInfosFinder.get(table.getId());                StateWrapper stateWrapper = new StateWrapper(stateInfo, ttInfos);                stateWrapperMap.put(table.getId(), stateWrapper);
            }


            if (getmTableTradeRefreshListener() != null) {
                getmTableTradeRefreshListener().refreshTableTrade(stateWrapperMap);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realseDBhelper(dbHelper);
        }

    }


    @Override
    public List<BusinessType> getBusinessType() {
        List<BusinessType> listBusiness = new ArrayList<>();
        listBusiness.add(BusinessType.BEAUTY);
        return listBusiness;
    }
}
