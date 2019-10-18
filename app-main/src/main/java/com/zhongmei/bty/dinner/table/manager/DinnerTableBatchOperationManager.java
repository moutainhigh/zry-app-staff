package com.zhongmei.bty.dinner.table.manager;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesAreaVo;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DinnerTableBatchOperationManager {

    public static final String TAG = DinnerTableBatchOperationManager.class.getSimpleName();

    private TablesDal tablesDal;

    private TradeDal tradeDal;

    private List<CommercialArea> commercialAreaList;

    private List<Tables> tablesList;


    private Map<Long, List<Tables>> tablesListMap;

    private List<TradeTable> tradeTableList;


    private Map<Long, List<TradeTable>> tradeTableListMap;

    private List<Trade> tradeList;


    public Map<Long, Trade> tradeMap;

    private List<DinnerConnectTablesVo> tablesVoList;

    private Map<Long, List<DinnerConnectTablesVo>> tablesVoListMap;

    private Map<Long, String> areaNameMap;

    private List<TradeMainSubRelation> tradeMainSubRelationList;

    private Map<Long, TradeMainSubRelation> tradeMainSubRelationMap;

    private List<TradeEarnestMoney> tradeEarnestMoneyList;

    private Map<Long, List<TradeEarnestMoney>> tradeEarnestMoneyListMap;

    public DinnerConnectTablesAreaVo allAreaVo;

    private OnDataChangedListener mOnChangedListener;
    private DataObserver mObserver = new DataObserver();

    public DinnerTableBatchOperationManager() {
        tablesDal = OperatesFactory.create(TablesDal.class);
        tradeDal = OperatesFactory.create(TradeDal.class);
        areaNameMap = new HashMap<>();
    }

    public List<DinnerConnectTablesAreaVo> createDinnerTablesAreaVo() {
        queueData();
        initData();
        List<DinnerConnectTablesAreaVo> tablesAreaVoList = new ArrayList<>();
        DinnerConnectTablesAreaVo tablesAreaVo;
        if (Utils.isEmpty(commercialAreaList))
            return null;
        for (CommercialArea area : commercialAreaList) {
            areaNameMap.put(area.getId(), area.getAreaName());
            tablesAreaVo = new DinnerConnectTablesAreaVo();
            tablesAreaVo.area = area;
            tablesAreaVo.isSelected = false;
            tablesAreaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL;
            tablesAreaVo.allTablesVoMap = createTablesVoMap(createTablesVoList(tablesListMap.get(area.getId())));
            initEmptyAndOccupyTablesVoMap(tablesAreaVo, tablesAreaVo.allTablesVoMap);
            tablesAreaVoList.add(tablesAreaVo);
        }
        initEmptyAndOccupyTablesVoMap(allAreaVo, allAreaVo.allTablesVoMap);
        tablesAreaVoList.add(0, allAreaVo);
        return tablesAreaVoList;
    }

    private void queueData() {
        try {
            commercialAreaList = tablesDal.listTableArea();
            tablesList = tablesDal.listValidTables();
            tradeList = tradeDal.getDinnerNotFinishTrade();
            tradeMainSubRelationList = tradeDal.tradeMainSubRelationList();
            if (!Utils.isEmpty(tradeList)) {
                List<Long> tradeIdList = new ArrayList<>();
                for (Trade trade : tradeList) {
                    tradeIdList.add(trade.getId());
                }
                tradeTableList = tablesDal.listTradeTable(tradeIdList);
            }
            tradeEarnestMoneyList = DBHelperManager.getHelper().getDao(TradeEarnestMoney.class).queryBuilder().where().eq(TradeEarnestMoney.$.statusFlag, StatusFlag.VALID).query();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private void initData() {
        initAllAreaVo();
        createTablesListMap();
        createTradeTablesListMap();
        createTradeMap();
        createTradeMainSubRelationMap();
        createTradeEarnestMoneyMap();
    }

    private void initAllAreaVo() {
        allAreaVo = new DinnerConnectTablesAreaVo();
        CommercialArea area = new CommercialArea();
        area.setAreaName(BaseApplication.sInstance.getResources().getString(R.string.batch_operation_table_all_area));
        area.setAreaCode(SystemUtils.genOnlyIdentifier());
        allAreaVo.area = area;
        allAreaVo.status = DinnerConnectTablesAreaVo.TABLES_AREA_STATUS_ALL;
        allAreaVo.isSelected = true;
    }

    private void createTradeTablesListMap() {
        tradeTableListMap = new HashMap<>();
        if (Utils.isEmpty(tradeTableList)) return;
        for (TradeTable tradeTable : tradeTableList) {
            if (tradeTableListMap.containsKey(tradeTable.getTableId())) {
                tradeTableListMap.get(tradeTable.getTableId()).add(tradeTable);
            } else {
                tradeTableListMap.put(tradeTable.getTableId(), new ArrayList<TradeTable>());
                tradeTableListMap.get(tradeTable.getTableId()).add(tradeTable);
            }
        }
    }

    private void createTablesListMap() {
        tablesListMap = new HashMap<>();
        if (Utils.isEmpty(tablesList)) return;
        for (Tables tables : tablesList) {
            if (tablesListMap.containsKey(tables.getAreaId())) {
                tablesListMap.get(tables.getAreaId()).add(tables);
            } else {
                tablesListMap.put(tables.getAreaId(), new ArrayList<Tables>());
                tablesListMap.get(tables.getAreaId()).add(tables);
            }
        }
    }

    private void createTradeMap() {
        tradeMap = new HashMap<>();
        if (Utils.isEmpty(tradeList)) return;
        for (Trade t : tradeList) {
            tradeMap.put(t.getId(), t);
        }
    }

    private void createTradeMainSubRelationMap() {
        tradeMainSubRelationMap = new HashMap<>();
        if (Utils.isEmpty(tradeMainSubRelationList)) return;
        for (TradeMainSubRelation m : tradeMainSubRelationList) {
            tradeMainSubRelationMap.put(m.getSubTradeId(), m);
        }
    }

    private void createTradeEarnestMoneyMap() {
        tradeEarnestMoneyListMap = new HashMap<>();
        if (Utils.isNotEmpty(tradeEarnestMoneyList)) {
            for (TradeEarnestMoney t : tradeEarnestMoneyList) {
                if (tradeEarnestMoneyListMap.containsKey(t.getTradeId())) {
                    tradeEarnestMoneyListMap.get(t.getTradeId()).add(t);
                } else {
                    tradeEarnestMoneyListMap.put(t.getTradeId(), new ArrayList<TradeEarnestMoney>());
                    tradeEarnestMoneyListMap.get(t.getTradeId()).add(t);
                }
            }
        }
    }

    private List<DinnerConnectTablesVo> createTablesVoList(List<Tables> tablesList) {
        tablesVoList = new ArrayList<>();
        if (Utils.isEmpty(tablesList)) return tablesVoList;
        for (Tables tables : tablesList) {
            tablesVoList.add(createDinnerTablesVo(tables));
        }
        return tablesVoList;
    }

    private Map<Long, List<DinnerConnectTablesVo>> createTablesVoMap(List<DinnerConnectTablesVo> tempTablesVoList) {
        tablesVoListMap = new HashMap<>();
        tablesVoListMap.put(0L, new ArrayList<DinnerConnectTablesVo>());
        for (DinnerConnectTablesVo tablesVo : tempTablesVoList) {
            Long seating = tablesVo.tables.getTablePersonCount().longValue();
            if (tablesVoListMap.containsKey(seating)) {
                tablesVoListMap.get(seating).add(tablesVo);
                tablesVoListMap.get(0L).add(tablesVo);
            } else {
                tablesVoListMap.put(seating, new ArrayList<DinnerConnectTablesVo>());
                tablesVoListMap.get(seating).add(tablesVo);
                tablesVoListMap.get(0L).add(tablesVo);
            }
        }
        return tablesVoListMap;
    }

    private void initEmptyAndOccupyTablesVoMap(DinnerConnectTablesAreaVo tablesAreaVo, Map<Long, List<DinnerConnectTablesVo>> allDinnerTablesVoMap) {
        HashMap<Long, List<DinnerConnectTablesVo>> emptyDinnerTablesVoMap = new HashMap<>();
        HashMap<Long, List<DinnerConnectTablesVo>> occupyDinnerTablesVoMap = new HashMap<>();
        for (Long key : allDinnerTablesVoMap.keySet()) {
            for (DinnerConnectTablesVo tablesVo : allDinnerTablesVoMap.get(key)) {
                if (tablesVo.tables.getTableStatus() == TableStatus.EMPTY) {
                    if (emptyDinnerTablesVoMap.containsKey(key)) {
                        emptyDinnerTablesVoMap.get(key).add(tablesVo);
                    } else {
                        emptyDinnerTablesVoMap.put(key, new ArrayList<DinnerConnectTablesVo>());
                        emptyDinnerTablesVoMap.get(key).add(tablesVo);
                    }
                } else if (tablesVo.tables.getTableStatus() == TableStatus.OCCUPIED && !Utils.isEmpty(tablesVo.tradeTableList)) {
                    if (occupyDinnerTablesVoMap.containsKey(key)) {
                        occupyDinnerTablesVoMap.get(key).add(tablesVo);
                    } else {
                        occupyDinnerTablesVoMap.put(key, new ArrayList<DinnerConnectTablesVo>());
                        occupyDinnerTablesVoMap.get(key).add(tablesVo);
                    }
                }
            }
        }
        if (!emptyDinnerTablesVoMap.containsKey(0L)) {
            emptyDinnerTablesVoMap.put(0L, new ArrayList<DinnerConnectTablesVo>());
        }
        if (!occupyDinnerTablesVoMap.containsKey(0L)) {
            occupyDinnerTablesVoMap.put(0L, new ArrayList<DinnerConnectTablesVo>());
        }
        tablesAreaVo.EmptyTablesVoMap = emptyDinnerTablesVoMap;
        tablesAreaVo.occupyTablesVoMap = occupyDinnerTablesVoMap;
    }

    private DinnerConnectTablesVo createDinnerTablesVo(Tables tables) {
        DinnerConnectTablesVo dinnerConnectTablesVo = getDinnerConnectTablesVoInstance();
        if (tables == null) return null;
        dinnerConnectTablesVo.tables = tables;
        dinnerConnectTablesVo.areaName = areaNameMap.get(tables.getAreaId());
        dinnerConnectTablesVo.isSelected = false;
        dinnerConnectTablesVo.isConnected = false;
        dinnerConnectTablesVo.tradeTableList = tradeTableListMap.get(tables.getId());
        dinnerConnectTablesVo.tradeList = new ArrayList<>();
        dinnerConnectTablesVo.tradeMainSubRelationList = new ArrayList<>();
        dinnerConnectTablesVo.tradeEarnestMoneyList = new ArrayList<>();
        if (!Utils.isEmpty(dinnerConnectTablesVo.tradeTableList)) {
            for (TradeTable tradeTable : dinnerConnectTablesVo.tradeTableList) {
                if (tradeMap.get(tradeTable.getTradeId()) != null) {
                    dinnerConnectTablesVo.tradeList.add(tradeMap.get(tradeTable.getTradeId()));
                }
                if (tradeMainSubRelationMap.get(tradeTable.getTradeId()) != null) {
                    dinnerConnectTablesVo.tradeMainSubRelationList.add(tradeMainSubRelationMap.get(tradeTable.getTradeId()));
                }
                if (tradeEarnestMoneyListMap.containsKey(tradeTable.getTradeId())) {
                    dinnerConnectTablesVo.tradeEarnestMoneyList.addAll(tradeEarnestMoneyListMap.get(tradeTable.getTradeId()));
                }
            }
        }
        initAllAreaVo(dinnerConnectTablesVo);
        return dinnerConnectTablesVo;
    }

    protected DinnerConnectTablesVo getDinnerConnectTablesVoInstance() {
        return new DinnerConnectTablesVo();
    }

    private void initAllAreaVo(DinnerConnectTablesVo tablesVo) {
        if (allAreaVo.allTablesVoMap == null) {
            allAreaVo.allTablesVoMap = new HashMap<>();
            allAreaVo.allTablesVoMap.put(0L, new ArrayList<DinnerConnectTablesVo>());
        }
        allAreaVo.allTablesVoMap.get(0L).add(tablesVo);
        Long key = tablesVo.tables.getTablePersonCount().longValue();
        if (allAreaVo.allTablesVoMap.containsKey(key)) {
            allAreaVo.allTablesVoMap.get(key).add(tablesVo);
        } else {
            allAreaVo.allTablesVoMap.put(key, new ArrayList<DinnerConnectTablesVo>());
            allAreaVo.allTablesVoMap.get(key).add(tablesVo);
        }
        initConnectTablesVoMap(tablesVo);
    }

    public void initConnectTablesVoMap(DinnerConnectTablesVo tablesVo) {
        if (allAreaVo.connectTablesVoMap == null) {
            allAreaVo.connectTablesVoMap = new HashMap<>();
        }
        if (!Utils.isEmpty(tablesVo.tradeMainSubRelationList)) {
            Long mainTradeId = tablesVo.tradeMainSubRelationList.get(0).getMainTradeId();
            Long childTradeId = tablesVo.tradeMainSubRelationList.get(0).getSubTradeId();
            Trade trade = tradeMap.get(childTradeId);
            if (trade != null && trade.getBusinessType() == BusinessType.DINNER) {
                if (allAreaVo.connectTablesVoMap.containsKey(mainTradeId)) {
                    allAreaVo.connectTablesVoMap.get(mainTradeId).add(tablesVo);
                } else {
                    allAreaVo.connectTablesVoMap.put(mainTradeId, new ArrayList<DinnerConnectTablesVo>());
                    allAreaVo.connectTablesVoMap.get(mainTradeId).add(tablesVo);
                }
            }
        }
    }


    public void register(OnDataChangedListener listener) {
        this.mOnChangedListener = listener;
        ContentResolver resolver = MainApplication.getInstance().getContentResolver();
        Uri mUri = DBHelperManager.getUri(TradeMainSubRelation.class);
        resolver.registerContentObserver(mUri, true, mObserver);
        if (mOnChangedListener != null) {
            mOnChangedListener.onChanged();
        }
    }

    public void unregister() {
        ContentResolver resolver = MainApplication.getInstance().getContentResolver();
        resolver.unregisterContentObserver(mObserver);
        mOnChangedListener = null;
    }


    private class DataObserver extends ContentObserver {
        public DataObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (mOnChangedListener != null) {
                mOnChangedListener.onChanged();
            }
        }
    }

    public interface OnDataChangedListener {

        void onChanged();
    }
}
