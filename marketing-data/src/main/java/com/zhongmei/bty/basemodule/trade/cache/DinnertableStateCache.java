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

/**
 * @version: 1.0
 * @date 2015年9月18日
 */
@SuppressLint("UseSparseArrays")
public class DinnertableStateCache {

    private static final String TAG = DinnertableStateCache.class.getSimpleName();

    private BusinessType mBusinessType;

    // 催菜数目
    private long tradeItemOperationCount = 0;

    private boolean isFreshedFinish = true;//桌台刷新，是否刷新完成

    private static class LazySingletonHolder {
        private static final DinnertableStateCache INSTANCE = new DinnertableStateCache();
    }

    /**
     * 需要在UI线程中调用
     *
     * @param listener
     */
    public static void open(OnDataChangeListener listener, BusinessType businessType) {
        LazySingletonHolder.INSTANCE._open(listener, businessType);
    }

    public static void onStop() {
        LazySingletonHolder.INSTANCE._onStop();
    }

    public static void onResume() {
        LazySingletonHolder.INSTANCE._onResume();
    }

    /**
     * 需要在UI线程中调用
     */
    public static void close() {
        LazySingletonHolder.INSTANCE._close();
    }

    public static boolean isClosed() {
        return LazySingletonHolder.INSTANCE._isClosed();
    }

    /**
     * 刷新所有缓存数据。将阻塞调用线程
     *
     * @throws Exception
     */
	/*public synchronized static void refresh() {
		try {
			LazySingletonHolder.INSTANCE._refresh();
		} catch (Exception e) {
			Log.e(TAG, "refresh error!", e);
		}
	}*/

    /**
     * 刷新所有缓存数据。将阻塞调用线程
     *
     * @throws Exception
     */
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

    /**
     * @version: 1.0
     * @date 2015年9月18日
     */
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

    /**
     * 刷新所有缓存数据
     *
     * @throws Exception
     */
	/*synchronized void _refresh() {
		try {
			dataHolder.refresh();
		} catch (Exception e) {
			Log.e(TAG, "refresh dinnertable state error!", e);
		}
	}*/

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

    /**
     * @param <>
     * @param <>
     * @version: 1.0
     * @date 2015年9月18日
     */
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
//			if(mBusinessType== BusinessType.DINNER){
//				where.in(Trade.$.businessType,mBusinessType,BusinessType.GROUP);
//			}else{
//				where.eq(Trade.$.businessType, mBusinessType);
//			}

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

        /**
         * 查询或刷新，根据坐台id刷新数据，如果不传入桌台，则刷新所有的数据
         *
         * @param areaId
         */
        private void queryAndRefresh(String areaId) {
            UserActionEvent.start(UserActionEvent.DINNER_TABLE_DATA_QUERY);
            long cur = System.currentTimeMillis();

            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                // 查询Tables
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

                List<Long> unionTradeIds = new ArrayList<>();//存放所有联台单id(包括子单与主单id)
                for (TradeMainSubRelation trademainSub : trademainSubs) {
                    if (!unionTradeIds.contains(trademainSub.getMainTradeId())) {
                        unionTradeIds.add(trademainSub.getMainTradeId());
                    }

                    unionTradeIds.add(trademainSub.getSubTradeId());
                }

                // 查询TradeTable
//					Calendar cad = Calendar.getInstance();
//					cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
//					cad.add(Calendar.DAY_OF_MONTH, -7);
                Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
                List<TradeTable> tradeTableListAll = tradeTableDao.queryBuilder()
                        .where()
                        .in(TradeTable.$.selfTableStatus, TableStatus.OCCUPIED, TableStatus.EMPTY)
                        .and()
                        .in(TradeTable.$.tableId, tablesIds.toArray())
                        .or()
                        .in(TradeTable.$.tradeId, unionTradeIds)
//							.and().gt(TradeTable.$.serverCreateTime,cad.getTime().getTime())
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

                // 查询Trade
                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
//					List<Trade> tradeListAll = tradeDao.queryBuilder()
//							.selectColumns(Trade.$.uuid,
//									Trade.$.id,
//									Trade.$.serverUpdateTime,
//									Trade.$.tradeStatus,
//									Trade.$.sourceId,
//									Trade.$.saleAmount,
//									Trade.$.tradeAmount,
//									Trade.$.deliveryType,
//									Trade.$.tradePayStatus
//							)
//							.where()
//							.eq(Trade.$.businessType, mBusinessType)
//							.and()
//							.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
//							.and()
//							.eq(Trade.$.statusFlag, StatusFlag.VALID)
//							.and()
//							.eq(Trade.$.tradeType, TradeType.SELL)
//							.and()
//							.in(Trade.$.id, tradeIdsList.toArray())
//							// .and().gt(Trade.$.serverCreateTime,
//							// cad.getTime().getTime())
//							.query();

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

//					List<Long> tradeIds=new ArrayList<>();
//					if (Utils.isNotEmpty(tradeListAll)) {
//						for (Trade trade : tradeListAll) {
//							tradeIds.add(trade.getId());
//						}
//					}

                Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);

                // 查询TradeExtra .and().notIn(TradeExtra.$.isPrinted,PrintStatus.FAILED,PrintStatus.PRINTING,PrintStatus.UNPRINT)
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
                    // 查询TradeItem
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
                    //仅查询tradeExtra isPrinted为空的订单的tradeItems
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

                //查询订单操作列表
                Dao<PrintOperation, String> printOperationsDao = helper.getDao(PrintOperation.class);
                List<PrintOperation> printOperationsListAll = printOperationsDao.queryBuilder()
                        .selectColumns(PrintOperation.$.extendsStr, PrintOperation.$.opType)
                        .where()
                        .eq(PrintOperation.$.opType, PrintOperationOpType.DINNER_PRE_CASH).and().in(PrintOperation.$.sourceTradeId, tradeIdsList)
                        .query();


                //将订单对应的预结单操作记录菜存入map
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


//					Dao<AsyncHttpRecord, String> httpRecordDao = helper.getDao(AsyncHttpRecord.class);
//					//查询订单的http请求状态
//					List<AsyncHttpRecord> httpRecords=httpRecordDao.queryBuilder()
//							.orderBy(AsyncHttpRecord.$.clientUpdateTime,false)
//							.where()
//							.ne(AsyncHttpRecord.$.status, AsyncHttpState.SUCCESS)
//							.query();

                //查询加菜表
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

                //将订单http请求记录存入map
//					Map<String, List<AsyncHttpRecord>> tradeHttpRecordMap = null;//订单上的异步记录
//					if(httpRecords!=null){
//						tradeHttpRecordMap = new HashMap<>();
//						for(AsyncHttpRecord record:httpRecords){
//							if(tradeHttpRecordMap.get(record.getTradeUuId())==null){
//								List<AsyncHttpRecord> httpRecordList=new ArrayList<>();
//								tradeHttpRecordMap.put(record.getTradeUuId(),httpRecordList);
//							}
//							tradeHttpRecordMap.get(record.getTradeUuId()).add(record);
//						}
//					}

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


                //将加菜记录加入到map中
                Map<Long, List<AddItemVo>> addItemMap = new HashMap<>();
                if (Utils.isNotEmpty(addItemBatches)) {
                    for (AddItemBatch addItemBatch : addItemBatches) {

                        if (!addItemMap.containsKey(addItemBatch.getTradeId())) {
                            List<AddItemVo> addItemVos = new ArrayList<>();
                            addItemMap.put(addItemBatch.getTradeId(), addItemVos);
                        }

                        //构建AddItemVo
                        AddItemVo addItemVo = new AddItemVo();
                        addItemVo.setmAddItemBatch(addItemBatch);

                        if (mapAdditemRecrods != null && mapAdditemRecrods.containsKey(addItemBatch.getId())) {
                            addItemVo.setmAddItemRecords(mapAdditemRecrods.get(addItemBatch.getId()));
                        }

                        addItemMap.get(addItemBatch.getTradeId()).add(addItemVo);
                    }
                }

                Map<Long, List<TradeTableInfo>> ttInfosFinder = new HashMap<Long, List<TradeTableInfo>>();//key 桌台id
                Map<String, DinnertableStatus> tradeTableStatusMap = new HashMap<String, DinnertableStatus>();//key tradeTableUUid

                // 查三天之内的单据
                // Calendar cad = Calendar.getInstance();
                // cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
                // cad.add(Calendar.DAY_OF_MONTH, -3);
                Map<Long, BusinessType> tableTradeBusiness = new HashMap<>();
                Map<Long, TradeTableInfo> mapSubTrade = new HashMap<>();//联台单子单集合
                Map<Long, TradeTableInfo> mapMainTrade = new HashMap<>();//联台单主单集合
                for (Trade trade : tradeListAll) {
                    // 桌台过滤未确认的点评订单 20160428 begin
                    if ((trade.getSource() == SourceId.DIANPING /*|| trade.getSource() == SourceId.XIN_MEI_DA*/)
                            && (trade.getTradeStatus() == TradeStatus.UNPROCESSED || trade.getTradeStatus() == TradeStatus.FINISH)) {
                        continue;
                    }

                    // 桌台过滤未确认的点评订单 20160428 end
                    String uuid = trade.getUuid();
                    // 流水号
                    TradeExtra tradeExtra = tradeExtraMap.get(trade.getUuid());
                    if (tradeExtra == null) {
                        continue;
                    }

                    //设置订单菜品总额（不包含附加费）
                    MathShoppingCartTool.setTradeDishAmount(trade, tradeItemMap.get(uuid));
                    if (trade.getTradeStatus() == TradeStatus.CONFIRMED) {
                        if (tradeExtraMap.get(uuid).getIsPrinted() == null || tradeExtra.getIsPrinted() == TradePrintStatus.__UNKNOWN__) {//老数据根据tradeItem计算订单状态

                            // 菜品的出单状态
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
                                        // 全退产生的数量为0的记录如果已经出单成功了，就排除掉
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

                        } else {//新数据根据tradeExtra计算订单状态

//								DinnertableStatus status;
//								TradeExtra tradeExtral=tradeExtraMap.get(uuid);
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


                    // 单据桌台
                    List<TradeTable> tradeTableList = tradeTableMap.get(uuid);
                    if (tradeTableList != null) {
                        for (TradeTable tradeTable : tradeTableList) {
                            DinnertableStatus dishStatus = tradeTableStatusMap.get(tradeTable.getTradeUuid());
                            if (dishStatus == null) {
                                dishStatus = DinnertableStatus.UNISSUED;
                            }

                            PrintOperation preCashPrintOperation = printOperationMap.get(trade.getId());
//								List<AsyncHttpRecord> httpRecord=tradeHttpRecordMap.get(trade.getUuid());//订单http请求信息
                            List<AddItemVo> addItemVoList = addItemMap.get(trade.getId());//订单加菜信息

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
                                    && (ttInfo.businessType == BusinessType.DINNER || ttInfo.businessType == BusinessType.BUFFET)) {//如果是联台子单

                                mapSubTrade.put(ttInfo.tradeId, ttInfo);
                            }
                        }
                    } else {//联台单是没有tradeTable数据的
                        DinnertableStatus dishStatus = tradeTableStatusMap.get(trade.getUuid());
                        if (dishStatus == null) {
                            dishStatus = DinnertableStatus.UNISSUED;
                        }

                        PrintOperation preCashPrintOperation = printOperationMap.get(trade.getId());
//								List<AsyncHttpRecord> httpRecord=tradeHttpRecordMap.get(trade.getUuid());//订单http请求信息
                        List<AddItemVo> addItemVoList = addItemMap.get(trade.getId());//订单加菜信息

                        TradeTableInfo ttInfo = new TradeTableInfo(trade, new TradeTable(),
                                dishStatus, preCashPrintOperation, addItemVoList);
                        //联台单TradeTableInfo
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


                //trade for end
                Map<Long, StateWrapper> stateWrapperMap = new HashMap<Long, StateWrapper>();//存储所有桌台状态
                for (Tables tables : tablesList) {
                    TradeTableInfo unionMainTrade = null;
                    List<TradeTableInfo> unionSubTrades = null;

                    if (mapUnionMainTrade.containsKey(tables.getId())) {
                        unionMainTrade = mapUnionMainTrade.get(tables.getId());
                        unionSubTrades = mapUnionSubTrades.get(unionMainTrade.tradeId);
                    }

                    TableStateInfo stateInfo = new TableStateInfo(tables, tableTradeBusiness.get(tables.getId()));//桌台状态
                    List<TradeTableInfo> ttInfos = ttInfosFinder.get(tables.getId());//桌台上的订单状态
                    StateWrapper stateWrapper = new StateWrapper(stateInfo, ttInfos, unionMainTrade, unionSubTrades);//桌台状态
                    stateWrapperMap.put(tables.getId(), stateWrapper);
                }

                stateWrapperDatas.putAll(stateWrapperMap);

                // stateWrapperDatas = ;
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
//			refreshOutTime();
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

    /**
     * 数据改变监听器
     *
     * @version: 1.0
     * @date 2015年10月10日
     */
    public void _onStop() {
    }

    public void _onResume() {
    }


    public class DinnertableDataChangeObserver implements DatabaseHelper.DataChangeObserver, AsyncNetworkManager.AsyncHttpRecordChange {

        @Override
        public void onChange(Collection<Uri> uris) {
            // add 20160525 yutang

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
                //获取已预定桌台列表
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
            //将订单http请求记录存入map
            Map<String, List<AsyncHttpRecord>> tradeHttpRecordMap = null;//订单上的异步记录
            Map<Long, List<AsyncHttpRecord>> tableHttpRecordMap = null;//订单上的异步记录

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

    /**
     *

     * @version: 1.0
     * @date 2015年9月18日
     *
     */
//	public static class StateWrapper {
//		public TradeTableInfo unionTableMainTrade;
//
//		public List<TradeTableInfo> unionTableSubTrades;
//
//		public final TableStateInfo tableStateInfo;
//
//		public final Collection<TradeTableInfo> tradeTableInfos;
//
//		StateWrapper(TableStateInfo tableStateInfo, Collection<TradeTableInfo> tradeTableInfos) {
//			this.tableStateInfo = tableStateInfo;
//			this.tradeTableInfos = tradeTableInfos;
//		}
//
//
//		StateWrapper(TableStateInfo tableStateInfo, Collection<TradeTableInfo> tradeTableInfos,TradeTableInfo unionTableMainTrade,List<TradeTableInfo> unionTableSubTrades) {
//			this.tableStateInfo = tableStateInfo;
//			this.tradeTableInfos = tradeTableInfos;
//			this.unionTableMainTrade=unionTableMainTrade;
//			this.unionTableSubTrades=unionTableSubTrades;
//		}
//	}

    /**
     *

     * @version: 1.0
     * @date 2015年9月18日
     *
     */
//	public static class TableStateInfo {
//
//		public final Long id;
//
//		public final TableStatus tableStatus;
//
//		public final Long serverUpdateTime;
//
//		public final boolean isGroup;//团餐
//
//		public final boolean isBuffet;//团餐
//
//		public final boolean isDinner;//团餐
//
//		public TableStateInfo(Tables table,boolean isGroup,boolean isBuffet,boolean isDinner) {
//			this.id = table.getId();
//			this.tableStatus = table.getTableStatus();
//			this.serverUpdateTime = table.verValue();
//			this.isGroup=isGroup;
//			this.isBuffet=isBuffet;
//			this.isDinner=isDinner;
//		}
//
//	}

    /**
     * 未结账的单据桌台信息
     *

     * @version: 1.0
     * @date 2015年9月18日
     *
     */
//	public static class TradeTableInfo implements Comparable<TradeTableInfo> {
//
//		/**
//		 * trade_table.id
//		 */
//		public final Long id;
//
//		/**
//		 * trade_table.uuid
//		 */
//		public final String uuid;
//
//		/**
//		 * trade_table.server_update_time
//		 */
//		public final Long serverUpdateTime;
//
//		/**
//		 * trade.uuid
//		 */
//		public final String tradeUuid;
//
//		/**
//		 * trade.id
//		 */
//		public final Long tradeId;
//
//		/**
//		 * trade.server_update_time
//		 */
//		public final Long tradeServerUpdateTime;
//
//		/**
//		 * trade.client_update_time
//		 */
//		public final Long tradeClientUpdateTime;
//
//		/**
//		 * trade.trade_status
//		 */
//		public final TradeStatus tradeStatus;
//
//		public final BusinessType businessType;
//
//		/**
//		 * trade_extra.serial_number
//		 */
//		public final String serialNumber;
//
//		public final TradePayStatus tradePayStatus;
//
//		/**
//		 * tables.id
//		 */
//		public final Long tableId;
//
//		/**
//		 * trade_talbe.client_create_time
//		 */
//		public final Long startTimeMillis;
//
//		/**
//		 * 单据在桌台的就餐人数
//		 */
//		public final int numberOfMeals;
//
//		public final DinnertableStatus dishStatus;
//
//		public final TradeType tradeType;
//
//		public BigDecimal tradeSaleAmount;//订单金额
//
//		public YesOrNo printPreCash=YesOrNo.NO;//是否已经打印预结单
//		public List<AsyncHttpRecord> httpRecord;//订单相关httpqing
//
//		public List<AddItemVo> addItemVoList;//订单相关加菜数据
//
//
//		public TradeTableInfo(Trade trade, TradeExtra tradeExtra, TradeTable tradeTable,
//					   DinnertableStatus dishStatus,PrintOperation preCashPrintOperation,
//					   List<AsyncHttpRecord> httpRecord,List<AddItemVo> addItemVoList) {
//			this.id = tradeTable.getId();
//			this.uuid = tradeTable.getUuid();
//			if(tradeTable.verValue()==null){//联台主单没有tradeTable数据。该数据在订单转台的时候会用
//				this.serverUpdateTime = tradeExtra.verValue();
//				this.startTimeMillis = tradeExtra.getServerCreateTime();
//			}else{
//				this.serverUpdateTime = tradeTable.verValue();
//				this.startTimeMillis = tradeTable.getServerCreateTime();
//			}
//
//			this.tableId = tradeTable.getTableId();
//			// this.startTimeMillis =
//			// tradeTable.getClientCreateTime();
//			this.numberOfMeals = tradeTable.getTablePeopleCount();
//			this.tradeUuid = trade.getUuid();
//			this.tradeId = trade.getId();
//			this.tradeServerUpdateTime = trade.getServerUpdateTime();
//			this.tradeStatus = trade.getTradeStatus();
//			this.serialNumber = tradeExtra.getSerialNumber();
//			this.dishStatus = dishStatus;
////			tradeSaleAmount=trade.getDishAmount();
//			tradeSaleAmount=trade.getTradeAmount();
//			tradeClientUpdateTime=trade.getClientUpdateTime();
//			printPreCash=preCashPrintOperation==null?YesOrNo.NO:YesOrNo.YES;
////			this.httpRecord=httpRecord;
//			this.addItemVoList=addItemVoList;
//			this.tradePayStatus=trade.getTradePayStatus();
//			this.businessType=trade.getBusinessType();
//			this.tradeType=trade.getTradeType();
//		}
//
//		public int getNumberOfMeals() {
//			return numberOfMeals;
//		}
//
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			TradeTableInfo other = (TradeTableInfo)obj;
//			if (uuid == null) {
//				if (other.uuid != null)
//					return false;
//			} else if (!uuid.equals(other.uuid))
//				return false;
//			return true;
//		}
//
//		@Override
//		public int compareTo(TradeTableInfo another) {
//			int v = startTimeMillis.compareTo(another.startTimeMillis);
//			if (v == 0) {
//				v = serialNumber.compareTo(another.serialNumber);
//			}
//			if (v == 0) {
//				v = uuid.compareTo(another.uuid);
//			}
//			return v;
//		}
//
//	}

}
