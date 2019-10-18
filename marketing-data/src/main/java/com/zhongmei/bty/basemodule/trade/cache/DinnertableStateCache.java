package com.zhongmei.bty.basemodule.trade.cache;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.bty.basemodule.booking.event.EventRefreshReserveNotice;
import com.zhongmei.bty.basemodule.booking.manager.BookingTablesManager;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AddItemRecordStatus;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePrintStatus;
import com.zhongmei.yunfu.db.enums.TradeServingStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.context.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;


@SuppressLint("UseSparseArrays")
public class DinnertableStateCache {

    private static final String TAG = DinnertableStateCache.class.getSimpleName();

    private BusinessType mBusinessType;

        private long tradeItemOperationCount = 0;

    private boolean isFreshedFinish = true;
    private static class LazySingletonHolder {
        private static final DinnertableStateCache INSTANCE = new DinnertableStateCache();
    }


    public static void open(OnDataChangeListener listener, BusinessType businessType) {
        LazySingletonHolder.INSTANCE._open(listener, businessType);
    }

    public static void onStop() {
        LazySingletonHolder.INSTANCE._onStop();
    }

    public static void onResume() {
        LazySingletonHolder.INSTANCE._onResume();
    }


    public static void close() {
        LazySingletonHolder.INSTANCE._close();
    }

    public static boolean isClosed() {
        return LazySingletonHolder.INSTANCE._isClosed();
    }





    public static void querybyId(String id) {
        try {
            LazySingletonHolder.INSTANCE._querybyId(id);
        } catch (Exception e) {
            Log.e(TAG, "refresh error!", e);
        }
    }

    public static Map<Long, StateWrapper> getStateWrapperDatas() {
        return LazySingletonHolder.INSTANCE._getStateWrapperDatas();
    }


    public static interface OnDataChangeListener {

        void onDataChanged(Map<Long, StateWrapper> stateWrapperMap);

        void onHttpRecordChanged(Map<Long, List<AsyncHttpRecord>> tableAsyncRecord, Map<String, List<AsyncHttpRecord>> tradeAsyncRecord);

    }

    private final DataHodler dataHolder;

    private final DinnertableDataChangeObserver observer;

    private OnDataChangeListener listener;

    private boolean opened = false;

    private DinnertableStateCache() {
        dataHolder = new DataHodler();
        observer = new DinnertableDataChangeObserver();
    }

    void _open(OnDataChangeListener listener, BusinessType businessType) {
        this.mBusinessType = businessType;
        if (opened) {
            Log.e(TAG, "Is already opened!");
        } else {
            opened = true;
            this.listener = listener;
            DatabaseHelper.Registry.registerWithDinnerTable(observer);
            AsyncNetworkManager.getInstance().addAsyncRecordChangeListener(observer);
        }
    }

    void _close() {
        this.listener = null;
        DatabaseHelper.Registry.unregister(observer);
        AsyncNetworkManager.getInstance().removeAsyncRecordChangeListener(observer);
        dataHolder.clear();
        opened = false;
    }

    boolean _isClosed() {
        return !opened;
    }




    void _querybyId(String id) {
        try {
            dataHolder.querybyId(id);
        } catch (Exception e) {
            Log.e(TAG, "refresh dinnertable state error!", e);
        }
    }

    Map<Long, StateWrapper> _getStateWrapperDatas() {
        return dataHolder.getStateWrapperDatas();
    }


    private class DataHodler {

        private Map<Long, StateWrapper> stateWrapperDatas;

        private Map<String, String> stateAreaIdDatas;

        protected DataHodler() {
            stateWrapperDatas = new HashMap<Long, StateWrapper>();
            stateAreaIdDatas = new HashMap<String, String>();
        }

        void clear() {
            stateWrapperDatas.clear();
            stateAreaIdDatas.clear();
        }

        Map<Long, StateWrapper> getStateWrapperDatas() {
            return stateWrapperDatas;
        }


        private Where buildTradeWhere(Where where, List<Long> tradeIds) throws Exception {

            where.in(Trade.$.businessType, BusinessType.DINNER, BusinessType.GROUP, BusinessType.BUFFET);

            return where
                    .and()
                    .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN)
                    .and()
                    .in(Trade.$.id, tradeIds.toArray());
        }


        private void queryAndRefresh(String areaId) {
            UserActionEvent.start(UserActionEvent.DINNER_TABLE_DATA_QUERY);
            long cur = System.currentTimeMillis();

            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                                long curTable = System.currentTimeMillis();
                Dao<Tables, Long> tablesDao = helper.getDao(Tables.class);
                QueryBuilder queryBuild = tablesDao.queryBuilder();
                queryBuild.selectColumns(Tables.$.id, Tables.$.tableStatus, Tables.$.modifyDateTime);

                Where whereTables = queryBuild.where();
                whereTables.eq(Tables.$.statusFlag, Status.VALID);

                if (!TextUtils.isEmpty(areaId)) {
                    whereTables.and().eq(Tables.$.areaId, areaId);
                }

                List<Tables> tablesList = queryBuild.query();

                List<Long> tablesIds = new ArrayList<Long>();

                for (Tables tables : tablesList) {
                    tablesIds.add(tables.getId());
                }


                List<TradeMainSubRelation> trademainSubs = helper.getDao(TradeMainSubRelation.class).queryBuilder().query();

                List<Long> unionTradeIds = new ArrayList<>();                for (TradeMainSubRelation trademainSub : trademainSubs) {
                    if (!unionTradeIds.contains(trademainSub.getMainTradeId())) {
                        unionTradeIds.add(trademainSub.getMainTradeId());
                    }

                    unionTradeIds.add(trademainSub.getSubTradeId());
                }

                                Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
                List<TradeTable> tradeTableListAll = tradeTableDao.queryBuilder()
                        .where()
                        .in(TradeTable.$.selfTableStatus, TableStatus.OCCUPIED, TableStatus.EMPTY)
                        .and()
                        .in(TradeTable.$.tableId, tablesIds.toArray())
                        .or()
                        .in(TradeTable.$.tradeId, unionTradeIds)
                        .query();


                List<Long> tradeIdsList = new ArrayList<Long>();
                Map<String, List<TradeTable>> tradeTableMap = new HashMap<String, List<TradeTable>>();
                Map<Long, TradeTable> mapTradeTableTradeID = new HashMap<>();

                if (Utils.isNotEmpty(unionTradeIds)) {
                    tradeIdsList.addAll(unionTradeIds);
                }

                for (TradeTable tradeTable : tradeTableListAll) {
                    tradeIdsList.add(tradeTable.getTradeId());
                    mapTradeTableTradeID.put(tradeTable.getTradeId(), tradeTable);
                    if (tradeTableMap.containsKey(tradeTable.getTradeUuid())) {
                        tradeTableMap.get(tradeTable.getTradeUuid()).add(tradeTable);
                    } else {
                        List<TradeTable> tempList = new ArrayList<TradeTable>();
                        tempList.add(tradeTable);
                        tradeTableMap.put(tradeTable.getTradeUuid(), tempList);
                    }
                }

                                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);

                QueryBuilder tradeBuilder = tradeDao.queryBuilder();
                tradeBuilder.selectColumns(Trade.$.uuid,
                        Trade.$.id,
                        Trade.$.serverUpdateTime,
                        Trade.$.tradeStatus,
                        Trade.$.sourceId,
                        Trade.$.saleAmount,
                        Trade.$.tradeAmount,
                        Trade.$.deliveryType,
                        Trade.$.tradePayStatus,
                        Trade.$.tradeType,
                        Trade.$.businessType
                );
                buildTradeWhere(tradeBuilder.where(), tradeIdsList);
                List<Trade> tradeListAll = tradeBuilder.query();


                Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);

                                List<TradeExtra> tradeExtrasListAll = tradeExtraDao.queryBuilder()
                        .selectColumns(TradeExtra.$.tradeUuid,
                                TradeExtra.$.serialNumber,
                                TradeExtra.$.isPrinted,
                                TradeExtra.$.serverCreateTime,
                                TradeExtra.$.serverUpdateTime,
                                TradeExtra.$.tradeId,
                                TradeExtra.$.hasServing)
                        .where()
                        .in(TradeExtra.$.tradeId, tradeIdsList.toArray())
                        .query();

                List<Long> oldTradeIdsList = new ArrayList<>();
                Map<String, TradeExtra> tradeExtraMap = new HashMap<String, TradeExtra>();

                if (Utils.isNotEmpty(tradeExtrasListAll)) {
                    for (TradeExtra tradeExtra : tradeExtrasListAll) {
                        if (tradeExtra.getIsPrinted() == null || tradeExtra.getIsPrinted() == TradePrintStatus.__UNKNOWN__) {
                            oldTradeIdsList.add(tradeExtra.getTradeId());
                        }
                        tradeExtraMap.put(tradeExtra.getTradeUuid(), tradeExtra);
                    }
                }

                List<TradeItem> tradeItemListAll = null;
                if (oldTradeIdsList.size() != 0) {
                                        Dao<TradeItem, String> itemDao = helper.getDao(TradeItem.class);
                    QueryBuilder<TradeItem, String> qb = itemDao.queryBuilder();
                    qb.selectColumns(TradeItem.$.tradeTableUuid,
                            TradeItem.$.issueStatus,
                            TradeItem.$.servingStatus,
                            TradeItem.$.quantity,
                            TradeItem.$.tradeUuid,
                            TradeItem.$.actualAmount
                    );
                    Where<TradeItem, String> where = qb.where();
                                        where.and(where.in(TradeItem.$.tradeId, oldTradeIdsList.toArray()),
                            where.and(where.eq(TradeItem.$.statusFlag, StatusFlag.VALID),
                                    where.isNull(TradeItem.$.parentUuid).or().eq(TradeItem.$.parentUuid, ""),
                                    where.isNotNull(TradeItem.$.tradeTableUuid),
                                    where.ne(TradeItem.$.tradeTableUuid, "")));

                    tradeItemListAll = qb.query();
                }

                Map<String, List<TradeItem>> tradeItemMap = new HashMap<String, List<TradeItem>>();
                if (Utils.isNotEmpty(tradeItemListAll)) {
                    for (TradeItem tradeItem : tradeItemListAll) {
                        if (tradeItemMap.containsKey(tradeItem.getTradeUuid())) {
                            tradeItemMap.get(tradeItem.getTradeUuid()).add(tradeItem);
                        } else {
                            List<TradeItem> tempList = new ArrayList<TradeItem>();
                            tempList.add(tradeItem);
                            tradeItemMap.put(tradeItem.getTradeUuid(), tempList);
                        }
                    }

                }

                Dao<TradeMainSubRelation, Long> tradeMainSubRelDao = helper.getDao(TradeMainSubRelation.class);
                List<TradeMainSubRelation> listTradeMainSubRel = tradeMainSubRelDao.queryBuilder().selectColumns(TradeMainSubRelation.$.mainTradeId).distinct().where().eq(TradeMainSubRelation.$.statusFlag, StatusFlag.VALID).query();

                Set<Long> SettradeMainSubRelation = new HashSet<>();
                for (TradeMainSubRelation tradeMainSubRelation : listTradeMainSubRel) {
                    SettradeMainSubRelation.add(tradeMainSubRelation.getMainTradeId());
                }

                                Dao<PrintOperation, String> printOperationsDao = helper.getDao(PrintOperation.class);
                List<PrintOperation> printOperationsListAll = printOperationsDao.queryBuilder()
                        .selectColumns(PrintOperation.$.extendsStr, PrintOperation.$.opType)
                        .where()
                        .eq(PrintOperation.$.opType, PrintOperationOpType.DINNER_PRE_CASH).and().in(PrintOperation.$.sourceTradeId, tradeIdsList)
                        .query();


                                Map<Long, PrintOperation> printOperationMap = new HashMap<Long, PrintOperation>();
                if (printOperationsListAll != null) {
                    for (PrintOperation printOperation : printOperationsListAll) {
                        JSONObject extendsStr = null;
                        try {
                            extendsStr = new JSONObject(printOperation.getExtendsStr());
                            long tradeId = extendsStr.optLong("tradeId");
                            if (!printOperationMap.containsKey(tradeId)) {
                                printOperationMap.put(tradeId, printOperation);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }



                                Dao<AddItemBatch, Long> addItemBatchDao = helper.getDao(AddItemBatch.class);
                List<AddItemBatch> addItemBatches = addItemBatchDao.queryBuilder()
                        .orderBy(AddItemBatch.$.serverCreateTime, true)
                        .where()
                        .in(AddItemBatch.$.tradeId, tradeIdsList.toArray())
                        .and()
                        .eq(AddItemBatch.$.handleStatus, 1)
                        .and()
                        .eq(AddItemBatch.$.statusFlag, StatusFlag.VALID)
                        .query();

                Dao<AddItemRecord, Long> addItemRecordDao = helper.getDao(AddItemRecord.class);

                List<AddItemRecord> addItemRecords = addItemRecordDao.queryBuilder()
                        .orderBy(AddItemRecord.$.serverCreateTime, true)
                        .where()
                        .in(AddItemRecord.$.tradeId, tradeIdsList.toArray())
                        .and()
                        .eq(AddItemRecord.$.handleStatus, AddItemRecordStatus.UNDEAL)
                        .and()
                        .eq(AddItemRecord.$.statusFlag, StatusFlag.VALID)
                        .query();


                Map<Long, List<AddItemRecord>> mapAdditemRecrods = null;
                if (Utils.isNotEmpty(addItemRecords)) {
                    mapAdditemRecrods = new HashMap<>();
                    for (AddItemRecord addItemRecord : addItemRecords) {
                        if (mapAdditemRecrods.containsKey(addItemRecord.getBatchId())) {
                            mapAdditemRecrods.get(addItemRecord.getBatchId()).add(addItemRecord);
                        } else {
                            List<AddItemRecord> listAdditemRecord = new ArrayList<>();
                            listAdditemRecord.add(addItemRecord);
                            mapAdditemRecrods.put(addItemRecord.getBatchId(), listAdditemRecord);
                        }
                    }
                }


                                Map<Long, List<AddItemVo>> addItemMap = new HashMap<>();
                if (Utils.isNotEmpty(addItemBatches)) {
                    for (AddItemBatch addItemBatch : addItemBatches) {

                        if (!addItemMap.containsKey(addItemBatch.getTradeId())) {
                            List<AddItemVo> addItemVos = new ArrayList<>();
                            addItemMap.put(addItemBatch.getTradeId(), addItemVos);
                        }

                                                AddItemVo addItemVo = new AddItemVo();
                        addItemVo.setmAddItemBatch(addItemBatch);

                        if (mapAdditemRecrods != null && mapAdditemRecrods.containsKey(addItemBatch.getId())) {
                            addItemVo.setmAddItemRecords(mapAdditemRecrods.get(addItemBatch.getId()));
                        }

                        addItemMap.get(addItemBatch.getTradeId()).add(addItemVo);
                    }
                }

                Map<Long, List<TradeTableInfo>> ttInfosFinder = new HashMap<Long, List<TradeTableInfo>>();                Map<String, DinnertableStatus> tradeTableStatusMap = new HashMap<String, DinnertableStatus>();
                                                                                Map<Long, BusinessType> tableTradeBusiness = new HashMap<>();
                Map<Long, TradeTableInfo> mapSubTrade = new HashMap<>();                Map<Long, TradeTableInfo> mapMainTrade = new HashMap<>();                for (Trade trade : tradeListAll) {
                                        if ((trade.getSource() == SourceId.DIANPING )
                            && (trade.getTradeStatus() == TradeStatus.UNPROCESSED || trade.getTradeStatus() == TradeStatus.FINISH)) {
                        continue;
                    }

                                        String uuid = trade.getUuid();
                                        TradeExtra tradeExtra = tradeExtraMap.get(trade.getUuid());
                    if (tradeExtra == null) {
                        continue;
                    }

                                        MathShoppingCartTool.setTradeDishAmount(trade, tradeItemMap.get(uuid));
                    if (trade.getTradeStatus() == TradeStatus.CONFIRMED) {
                        if (tradeExtraMap.get(uuid).getIsPrinted() == null || tradeExtra.getIsPrinted() == TradePrintStatus.__UNKNOWN__) {
                                                        List<TradeItem> itemList = tradeItemMap.get(uuid);
                            if (itemList != null) {
                                for (TradeItem item : itemList) {
                                    String tradeUuid = item.getTradeUuid();
                                    DinnertableStatus status;
                                    IssueStatus issueStatus = item.getIssueStatus();
                                    if (issueStatus == null) {
                                        status = DinnertableStatus.UNISSUED;
                                    } else if (item.getServingStatus() == ServingStatus.SERVING) {
                                        status = DinnertableStatus.SERVING;
                                    } else if (issueStatus == IssueStatus.FINISHED) {
                                                                                if (item.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                                            continue;
                                        }
                                        status = DinnertableStatus.ISSUED;
                                    } else {
                                        status = DinnertableStatus.UNISSUED;
                                    }
                                    DinnertableStatus oldStatus = tradeTableStatusMap.get(tradeUuid);
                                    if (oldStatus == null || oldStatus.value > status.value) {
                                        tradeTableStatusMap.put(tradeUuid, status);
                                    }
                                }
                            }

                        } else {
                            String tradeUuid = trade.getUuid();
                            if (tradeExtra.getHasServing() == TradeServingStatus.SERVED) {
                                tradeTableStatusMap.put(tradeUuid, DinnertableStatus.SERVING);
                            } else {
                                if (tradeExtra.getIsPrinted() == TradePrintStatus.PRINTED) {
                                    tradeTableStatusMap.put(tradeUuid, DinnertableStatus.ISSUED);
                                } else {
                                    tradeTableStatusMap.put(tradeUuid, DinnertableStatus.UNISSUED);
                                }

                            }
                        }


                    }


                                        List<TradeTable> tradeTableList = tradeTableMap.get(uuid);
                    if (tradeTableList != null) {
                        for (TradeTable tradeTable : tradeTableList) {
                            DinnertableStatus dishStatus = tradeTableStatusMap.get(tradeTable.getTradeUuid());
                            if (dishStatus == null) {
                                dishStatus = DinnertableStatus.UNISSUED;
                            }

                            PrintOperation preCashPrintOperation = printOperationMap.get(trade.getId());
                            List<AddItemVo> addItemVoList = addItemMap.get(trade.getId());
                            tableTradeBusiness.put(tradeTable.getTableId(), trade.getBusinessType());

                            TradeTableInfo ttInfo = new TradeTableInfo(trade, tradeTable,
                                    dishStatus, preCashPrintOperation, addItemVoList);
                            List<TradeTableInfo> list = ttInfosFinder.get(ttInfo.tableId);
                            if (list == null) {
                                list = new ArrayList<TradeTableInfo>();
                                ttInfosFinder.put(ttInfo.tableId, list);
                            }
                            list.add(ttInfo);

                            if (ttInfo.tradeType == TradeType.UNOIN_TABLE_SUB
                                    && (ttInfo.businessType == BusinessType.DINNER || ttInfo.businessType == BusinessType.BUFFET)) {
                                mapSubTrade.put(ttInfo.tradeId, ttInfo);
                            }
                        }
                    } else {                        DinnertableStatus dishStatus = tradeTableStatusMap.get(trade.getUuid());
                        if (dishStatus == null) {
                            dishStatus = DinnertableStatus.UNISSUED;
                        }

                        PrintOperation preCashPrintOperation = printOperationMap.get(trade.getId());
                        List<AddItemVo> addItemVoList = addItemMap.get(trade.getId());
                        TradeTableInfo ttInfo = new TradeTableInfo(trade, new TradeTable(),
                                dishStatus, preCashPrintOperation, addItemVoList);
                                                mapMainTrade.put(ttInfo.tradeId, ttInfo);
                    }


                }


                Map<Long, TradeTableInfo> mapUnionMainTrade = new HashMap<>();
                Map<Long, List<TradeTableInfo>> mapUnionSubTrades = new HashMap<>();
                for (TradeMainSubRelation trademainSub : trademainSubs) {
                    TradeTableInfo mainTradeInfo = mapMainTrade.get(trademainSub.getMainTradeId());

                    if (mainTradeInfo == null || !mapSubTrade.containsKey(trademainSub.getSubTradeId())) {
                        continue;
                    }

                    TradeTableInfo subTradeInfo = mapSubTrade.get(trademainSub.getSubTradeId());
                    TradeTable subTradeTable = mapTradeTableTradeID.get(subTradeInfo.tradeId);

                    mapUnionMainTrade.put(subTradeTable.getTableId(), mainTradeInfo);


                    if (!mapUnionSubTrades.containsKey(mainTradeInfo.tradeId)) {
                        mapUnionSubTrades.put(mainTradeInfo.tradeId, new ArrayList<TradeTableInfo>());
                    }

                    mapUnionSubTrades.get(mainTradeInfo.tradeId).add(subTradeInfo);
                }


                                Map<Long, StateWrapper> stateWrapperMap = new HashMap<Long, StateWrapper>();                for (Tables tables : tablesList) {
                    TradeTableInfo unionMainTrade = null;
                    List<TradeTableInfo> unionSubTrades = null;

                    if (mapUnionMainTrade.containsKey(tables.getId())) {
                        unionMainTrade = mapUnionMainTrade.get(tables.getId());
                        unionSubTrades = mapUnionSubTrades.get(unionMainTrade.tradeId);
                    }

                    TableStateInfo stateInfo = new TableStateInfo(tables, tableTradeBusiness.get(tables.getId()));                    List<TradeTableInfo> ttInfos = ttInfosFinder.get(tables.getId());                    StateWrapper stateWrapper = new StateWrapper(stateInfo, ttInfos, unionMainTrade, unionSubTrades);                    stateWrapperMap.put(tables.getId(), stateWrapper);
                }

                stateWrapperDatas.putAll(stateWrapperMap);

                                Log.i(TAG, "stateWrapperDatas.count=" + stateWrapperDatas.size());
                if (listener != null) {
                    listener.onDataChanged(stateWrapperDatas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DBHelperManager.releaseHelper(helper);
                isFreshedFinish = true;
                UserActionEvent.start(UserActionEvent.DINNER_TABLE_DATA_QUERY);
                Log.e("queryAndRefresh", "queryAndRefresh=====>" + (System.currentTimeMillis() - cur));
            }
        }


        public void refresh() throws Exception {
            queryAndRefresh(null);
        }

        public void querybyId(String id) {
            if (TextUtils.isEmpty(id) || stateAreaIdDatas.containsKey(id)) {
                return;
            }

            queryAndRefresh(id);

            AsyncNetworkManager.getInstance().queryAllAsyncRecord(observer);

            stateAreaIdDatas.put(id, id);
        }
    }

    private static final Uri URI_TABLES = DBHelperManager.getUri(Tables.class);

    private static final Uri URI_TRADE = DBHelperManager.getUri(Trade.class);

    private static final Uri URI_TRADE_TABLE = DBHelperManager.getUri(TradeTable.class);

    private static final Uri URI_TRADE_ITEM = DBHelperManager.getUri(TradeItem.class);

    private static final Uri URI_TRADE_ITEM_OPERATION = DBHelperManager.getUri(TradeItemOperation.class);

    private static final Uri URI_BOOKING = DBHelperManager.getUri(Booking.class);

    private static final Uri URI_PRINT_OPERATION = DBHelperManager.getUri(PrintOperation.class);

    private static final Uri URI_ASYNC_HTTP_RECORD = DBHelperManager.getUri(AsyncHttpRecord.class);

    private static final Uri URI_ADD_ITEM_BATCH = DBHelperManager.getUri(AddItemBatch.class);

    private static final Uri URI_ADD_ITEM_RECORD = DBHelperManager.getUri(AddItemRecord.class);

    private static final Uri URI_KDS_TRADE_ITEM = DBHelperManager.getUri(KdsTradeItem.class);

    private static final Uri URI_TRADE_TAX = DBHelperManager.getUri(TradeTax.class);

    private static final Uri URI_TRADE_INIT_CONFIG = DBHelperManager.getUri(TradeInitConfig.class);


    public void _onStop() {
    }

    public void _onResume() {
    }


    public class DinnertableDataChangeObserver implements DatabaseHelper.DataChangeObserver, AsyncNetworkManager.AsyncHttpRecordChange {

        @Override
        public void onChange(Collection<Uri> uris) {

            if (uris.contains(URI_TABLES) || uris.contains(URI_TRADE) || uris.contains(URI_TRADE_TABLE)
                    || uris.contains(URI_TRADE_ITEM)
                    || uris.contains(URI_PRINT_OPERATION)
                    || uris.contains(URI_ADD_ITEM_BATCH)
                    || uris.contains(URI_ADD_ITEM_RECORD)
                    || uris.contains(URI_KDS_TRADE_ITEM)
                    || uris.contains(URI_TRADE_TAX)
                    || uris.contains(URI_TRADE_INIT_CONFIG)) {
                try {

                    if (isFreshedFinish) {
                        isFreshedFinish = false;
                        dataHolder.refresh();
                        isFreshedFinish = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "refresh error!", e);
                }
            }

            if (uris.contains(URI_TRADE_ITEM_OPERATION)) {
                try {
                    long count = DBHelperManager.countOf(TradeItemOperation.class);
                    if (count > tradeItemOperationCount) {
                        if (isFreshedFinish) {
                            isFreshedFinish = false;
                            dataHolder.refresh();
                            isFreshedFinish = true;
                        }
                    }
                    tradeItemOperationCount = count;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "refresh error!", e);
                }
            }

            if (uris.contains(URI_BOOKING)) {
                Log.i("zhubo", "booking表变化");
                BookingTablesManager manager = new BookingTablesManager();
                                HashMap<String, BookingTable> bookingMap = null;
                try {
                    bookingMap = manager.getCurrentPeriodBookingTables();
                } catch (Exception e) {
                    Log.e(TAG, "refresh error!", e);
                }

                if (bookingMap != null) {
                    EventBus.getDefault().post(new EventRefreshReserveNotice(bookingMap));
                }

            }
        }

        @Override
        public void onChange(List<AsyncHttpRecord> allRecord) {
                        Map<String, List<AsyncHttpRecord>> tradeHttpRecordMap = null;            Map<Long, List<AsyncHttpRecord>> tableHttpRecordMap = null;
            if (allRecord != null) {
                tableHttpRecordMap = new HashMap<>();
                tradeHttpRecordMap = new HashMap<>();
                for (AsyncHttpRecord record : allRecord) {
                    if (record.getStatus() == AsyncHttpState.SUCCESS) {
                        continue;
                    }

                    if (tradeHttpRecordMap.get(record.getTradeUuId()) == null) {
                        List<AsyncHttpRecord> httpRecordList = new ArrayList<>();
                        tradeHttpRecordMap.put(record.getTradeUuId(), httpRecordList);
                    }
                    if (record.getType() != AsyncHttpType.OPENTABLE) {
                        tradeHttpRecordMap.get(record.getTradeUuId()).add(record.clone());
                    }


                    if (tableHttpRecordMap.get(record.getTableId()) == null) {
                        List<AsyncHttpRecord> httpRecordList = new ArrayList<>();
                        tableHttpRecordMap.put(record.getTableId(), httpRecordList);
                    }
                    tableHttpRecordMap.get(record.getTableId()).add(record.clone());
                }
            }


            if (listener != null) {
                listener.onHttpRecordChanged(tableHttpRecordMap, tradeHttpRecordMap);
            }
        }
    }







}
