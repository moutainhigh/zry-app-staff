package com.zhongmei.bty.dinner.table.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.booking.manager.BookingTablesManager;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobile;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModelMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.StateWrapper;
import com.zhongmei.bty.basemodule.trade.bean.TableStateInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.cache.DinnertableStateCache;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.message.ClearDinnertableResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.TransferDinnertableResp;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal.DinnertableWrapper;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.buffet.orderdish.DepositInfoDialog;
import com.zhongmei.bty.cashier.shoppingcart.CopyMoveDishTool;
import com.zhongmei.bty.cashier.util.TradeStatusUtil;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.db.common.CTradeItem;
import com.zhongmei.bty.data.operates.impl.KdsTradeDal;
import com.zhongmei.bty.data.operates.message.content.CustomerRequest;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.event.EventDinnertableTradeVoNotice;
import com.zhongmei.bty.dinner.table.event.EventDinnertableVoNotice;
import com.zhongmei.bty.dinner.table.event.EventMoveDishNotice;
import com.zhongmei.bty.dinner.table.event.EventRefreshDinnertableNotice;
import com.zhongmei.bty.dinner.table.event.EventRefreshOpenTableAsync;
import com.zhongmei.bty.dinner.table.event.EventRefreshTradeAsyncHttp;
import com.zhongmei.bty.dinner.table.event.EventZoneSwitchNotice;
import com.zhongmei.bty.dinner.table.event.EventZonesNotice;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.DinnertableShape;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.dinner.vo.DinnertableVo;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePrintStatus;
import com.zhongmei.yunfu.db.enums.TradeServingStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * @version: 1.0
 * @date 2015年9月16日
 */
@SuppressLint("UseSparseArrays")
public class DinnertableManager implements DinnertableStateCache.OnDataChangeListener {

    private static final String TAG = DinnertableManager.class.getSimpleName();

    private static Context getContext() {
        return MainApplication.getInstance();
    }

    /**
     * 所有桌台区域。key为ZoneModel.id(tableArea.id)
     */
    private final Map<Long, ZoneModel> zoneModelFinder;

    /**
     * 所有桌台。key为DinnertableModel.id(table.id)
     */
    private final Map<Long, DinnertableModel> dinnertableFinder;

    private ExecutorService executorService;

    private TableFragment fragment;

    /**
     * 是否启用上餐功能
     */
    private boolean enableServing;

    /**
     * 是否开启自动清台功能
     */
    private boolean autoClearTable;

    /**
     * 当前选中的桌台ID
     */
    private Long selectedDinnertableId;

    private BookingTablesManager bookingTablesManager;

    public DinnertableManager() {
        zoneModelFinder = new HashMap<Long, ZoneModel>();
        dinnertableFinder = new HashMap<Long, DinnertableModel>();
        setSelectedDinnertableId(null);
        bookingTablesManager = new BookingTablesManager();
    }

    /**
     * @param fragment
     */
    public void open(TableFragment fragment, final String uuid) {
        if (executorService != null) {
            throw new IllegalStateException("The manager is opened!");
        }
        executorService = Executors.newFixedThreadPool(1);
        this.fragment = fragment;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doLoadDate(uuid);
                } catch (Exception e) {
                    Log.e(TAG, "Load data error!", e);
                }
            }
        });
        ///modify v8.2 线程池工具替换独立线程池
//        ThreadUtils.runOnWorkThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    doLoadDate(uuid);
//                } catch (Exception e) {
//                    Log.e(TAG, "Load data error!", e);
//                }
//            }
//        });
    }

    private void doLoadDate(String uuid) throws Exception {
        long time = System.currentTimeMillis();
        //获取已预定桌台列表
        HashMap<String, BookingTable> bookingMap = bookingTablesManager.getCurrentPeriodBookingTables();

        // 调用状态缓存刷新
        // DinnertableStateCache.refresh(); jikun
//		Log.i("zhubo", "trade查询耗时：" + (System.currentTimeMillis() - time));
        // 从数，据库中获取区域和桌台的基本信息存放到zoneModelFinder中
        TablesDal dal = OperatesFactory.create(TablesDal.class);
        Collection<DinnertableWrapper> wrappers = dal.listDinnertables();
        Log.i(TAG, "TableWrapper.size=" + wrappers.size());
        //刷新桌台开台异步
        for (DinnertableWrapper wrapper : wrappers) {
            ZoneModel zoneModel = zoneModelFinder.get(wrapper.getZoneId());
            if (zoneModel == null) {
                zoneModel = new ZoneModel();
                zoneModel.setId(wrapper.getZoneId());
                zoneModel.setCode(wrapper.getZoneCode());
                zoneModel.setName(wrapper.getZoneName());
                zoneModel.setDinnertableModels(new ArrayList<DinnertableModel>());
                zoneModelFinder.put(zoneModel.getId(), zoneModel);
            }

            DinnertableModel dinnertableModel = new DinnertableModel();
            dinnertableModel.setId(wrapper.getId());
            dinnertableModel.setName(wrapper.getName());
            dinnertableModel.setNumberOfSeats(wrapper.getNumberOfSeats());
            dinnertableModel.setWidth(wrapper.getWidth());
            dinnertableModel.setHeight(wrapper.getHeight());
            dinnertableModel.setX(wrapper.getX());
            dinnertableModel.setY(wrapper.getY());
            dinnertableModel.setShape(DinnertableShape.SQUARE);
            dinnertableModel.setZone(zoneModel);
            dinnertableModel.setUuid(wrapper.getUuid());
            dinnertableModel.setTableStatus(wrapper.getTableStatus());
            dinnertableModel.setTableSeats(wrapper.getTableSeats());
            //设置桌台预定状态
            if (bookingMap.get(wrapper.getUuid()) == null) {
                dinnertableModel.setReserved(false);
            } else {
                dinnertableModel.setReserved(true);
            }

            // 计算区域的大小
            int minWidth = wrapper.getWidth() + dinnertableModel.getX();
            if (zoneModel.getWidth() < minWidth) {
                zoneModel.setWidth(minWidth);
            }
            int minHeight = wrapper.getHeight() + dinnertableModel.getY();
            if (zoneModel.getHeight() < minHeight) {
                zoneModel.setHeight(minWidth);
            }

            zoneModel.getDinnertableModels().add(dinnertableModel);
            dinnertableFinder.put(dinnertableModel.getId(), dinnertableModel);
        }
        Log.i(TAG, "zoneModelFinder.size=" + zoneModelFinder.size());

        // 获取正餐设置项的状态
        enableServing = false;
        autoClearTable = false;
        try {
            SystemSettingDal settingDal = OperatesFactory.create(SystemSettingDal.class);
            List<TradeDealSetting> settingList = settingDal.listDinnerSetting();
            for (TradeDealSetting setting : settingList) {
                if (setting.getOperateType() == TradeDealSettingOperateType.$.ENABLE_SERVING) {
                    enableServing = (setting.getIsEnabled() == YesOrNo.YES);
                } else if (setting.getOperateType() == TradeDealSettingOperateType.$.AUTO_CLEAR_TABLE) {
                    autoClearTable = (setting.getIsEnabled() == YesOrNo.YES);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Query setting failed!", e);
        }

        // 刷新桌台状态及桌台上的单据状态
        //doRefreshState(DinnertableStateCache.getStateWrapperDatas()); jikun

        // 区域按编号排序，并发送Event通知数据
        List<ZoneModel> zoneList = new ArrayList<ZoneModel>(zoneModelFinder.values());
        try {
            Collections.sort(zoneList, new Comparator<ZoneModel>() {

                @Override
                public int compare(ZoneModel lhs, ZoneModel rhs) {
                    int v = 0;
                    if (lhs.getCode() != null && rhs.getCode() != null) {
                        v = lhs.getCode().compareTo(rhs.getCode());
                    } else if (lhs.getCode() != null) {
                        return 1;
                    } else if (rhs.getCode() != null) {
                        return -1;
                    }
                    if (v == 0) {
                        v = lhs.getId().compareTo(rhs.getId());
                    }
                    return v;
                }
            });
        } catch (Exception e) {
            Log.e("DinnertableModel", e.getMessage());
        }
        EventBus.getDefault().post(new EventZonesNotice(zoneList, uuid));
        Log.i("zhubo", "loaddata查询耗时：" + (System.currentTimeMillis() - time));
    }

    /**
     * 是否开启上餐功能，开启返回true
     *
     * @return
     */
    public boolean isEnableServing() {
        return enableServing;
    }

    /**
     * 是否开启自动清台功能，开启返回true
     *
     * @return
     */
    public boolean isAutoClearTable() {
        return autoClearTable;
    }

    /**
     * 从缓存中获取桌台状态刷新
     *
     * @param stateWrapperMap
     */
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


                    TradeTableInfo unionTableMainTrade = stateWrapper.unionTableMainTrade;
                    DinnertableTradeModel unionMainTradeModel = null;
                    List<IDinnertableTrade> unionSubTrades = null;
                    if (unionTableMainTrade != null) {
                        if (dinnertableModel.getmListTradeHttpRecord() != null) {
                            unionTableMainTrade.httpRecord = dinnertableModel.getmListTradeHttpRecord().get(unionTableMainTrade.tradeUuid);
                        }

                        unionMainTradeModel = new DinnertableTradeModel(unionTableMainTrade, dinnertableModel);
                        unionMainTradeModel.refreshSpendTime();

                        unionSubTrades = createUnionTableTradeInfo(stateWrapper.unionTableSubTrades);//联台单需要去缓冲查询桌台信息
                    }


                    dinnertableModel.setTableStatus(tableStateInfo.tableStatus);
                    dinnertableModel.setStateInfo(tableStateInfo, dinnertableTrades);
                    dinnertableModel.setUnionTableTradeInfo(unionMainTradeModel, unionSubTrades);
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

    private List<IDinnertableTrade> createUnionTableTradeInfo(Collection<TradeTableInfo> tradeInfo) {
        List<IDinnertableTrade> dinnertableTrades = new ArrayList<IDinnertableTrade>();
        if (Utils.isNotEmpty(tradeInfo)) {
            for (TradeTableInfo info : tradeInfo) {
                DinnertableModel tableModel = dinnertableFinder.get(info.tableId);
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

    private void doRefreshAsyncHttpRecord(Map<Long, List<AsyncHttpRecord>> tableAsyncRecord, Map<String, List<AsyncHttpRecord>> tradeAsyncRecord) {
        for (ZoneModel zoneModel : zoneModelFinder.values()) {
            for (DinnertableModel dinnertableModel : zoneModel.getDinnertableModels()) {
                dinnertableModel.setmListHttpRecord(tableAsyncRecord.get(dinnertableModel.getId()));
                for (IDinnertableTrade iDinnertableTrade : dinnertableModel.getDinnertableTrades()) {
                    ((DinnertableTradeModel) iDinnertableTrade).getTradeTableInfo().httpRecord = tradeAsyncRecord.get(iDinnertableTrade.getTradeUuid());
                }
            }
        }

        EventBus.getDefault().post(new EventRefreshOpenTableAsync(null, null));//刷新桌台
    }

    /**
     * 从数据库获取完整的TradeVo生成购物车条目后发送Event通知
     */
    public void notifyDinnertable(Long tableId) {
        DinnertableModel model = dinnertableFinder.get(tableId);
        notifyDinnertable(model);
    }

    public void notifyDinnertable(final DinnertableModel model) {
        if (model == null) {
            Log.e(TAG, "The model must be not null.");
            return;
        }
        UserActionEvent.start(UserActionEvent.DINNER_TRADEINFO_REFRESH);
        Log.i(TAG, "notifyDinnertable..." + model.getName());

        try {
            IDinnertableTrade unionTrade = model.getDinnerUnionMainTrade();

            Map<String, IDinnertableTrade> mapDinnerTableTrade = new HashMap<>();
            Map<Long, DinnertableModel> mapTableModel = new HashMap<>();
            List<String> unionTradeUUIDs = new ArrayList<>();
            /**
             * 处理联台订单
             */
            if (unionTrade != null) {
                unionTradeUUIDs.add(unionTrade.getTradeUuid());
                mapDinnerTableTrade.put(unionTrade.getTradeUuid(), unionTrade);

                for (IDinnertableTrade dinnertableTrade : model.getDinnerUnionSubTrades()) {
                    unionTradeUUIDs.add(dinnertableTrade.getTradeUuid());
                    mapDinnerTableTrade.put(dinnertableTrade.getTradeUuid(), dinnertableTrade);
                    mapTableModel.put(dinnertableTrade.getTradeId(), (DinnertableModel) dinnertableTrade.getDinnertable());
                }
            }

            /**
             * 处理桌台订单
             */
            for (IDinnertableTrade dinnertableTrade : model.getDinnertableTrades()) {
                unionTradeUUIDs.add(dinnertableTrade.getTradeUuid());
                mapDinnerTableTrade.put(dinnertableTrade.getTradeUuid(), dinnertableTrade);
//                model.getDinnertableTrades().add(dinnertableTrade);
            }


            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            List<Trade> trades = tradeDal.listTradeByUUID(unionTradeUUIDs);
            List<TradeVo> tradeVos = tradeDal.getTradeVosByTrades(trades);

            DinnertableTradeVo unionTableTradeMain = null;//联台主单
            List<DinnertableTradeVo> dinnerTableTradeVos = new ArrayList<DinnertableTradeVo>();
            List<DinnertableTradeVo> unionTableTradesubs = new ArrayList<DinnertableTradeVo>();//联台子单集合
            List<DinnertableTradeInfo> unionTableTradeInfosubs = new ArrayList<DinnertableTradeInfo>();

            if (Utils.isNotEmpty(tradeVos)) {

                Collections.sort(tradeVos, new Comparator<TradeVo>() {
                    @Override
                    public int compare(TradeVo tradeVo, TradeVo t1) {
                        return tradeVo.getTrade().getServerCreateTime().compareTo(t1.getTrade().getServerCreateTime());
                    }
                });


                for (TradeVo tradeVo : tradeVos) {
                    //设置订单菜品价总和（不包含附加费）
                    List<TradeItem> items = new ArrayList<>();
                    if (tradeVo.getTradeItemList() != null) {
                        for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                            items.add(itemVo.getTradeItem());
                        }
                    }

                    //设置kds划菜份数
                    setKdsScratchDishQty(tradeVo);

                    MathShoppingCartTool.setTradeDishAmount(tradeVo.getTrade(), items);

                    DinnertableTradeModel mode = (DinnertableTradeModel) mapDinnerTableTrade.get(tradeVo.getTrade().getUuid());

                    //订单列表显示金额需要用到tradeSaleAmount
                    mode.getTradeTableInfo().tradeSaleAmount = tradeVo.getTrade().getTradeAmount();//tradeVo.getTrade().getDishAmount();实际金额

                    if (tradeVo != null) {
                        DinnertableTradeInfo info = DinnertableTradeInfo.create(mapDinnerTableTrade.get(tradeVo.getTrade().getUuid()), tradeVo);
                        DinnertableTradeVo dinnerTableTradeVo = new DinnertableTradeVo(mapDinnerTableTrade.get(tradeVo.getTrade().getUuid()), info);

                        if (info.getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                            dinnerTableTradeVo.setTableModel(mapTableModel.get(dinnerTableTradeVo.getTradeId()));
                            unionTableTradesubs.add(dinnerTableTradeVo);
                            unionTableTradeInfosubs.add(info);
                        }

                        if (info.getTradeType() != TradeType.UNOIN_TABLE_MAIN && dinnerTableTradeVo.getDinnertable().getId() == model.getId()) {
                            dinnerTableTradeVos.add(dinnerTableTradeVo);
                        }


                        if (info.getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                            unionTableTradeMain = dinnerTableTradeVo;
                            unionTableTradesubs.add(0, dinnerTableTradeVo);
                        }
                    }
                }
            }


            if (unionTableTradeMain != null) {
                unionTableTradeMain.getInfo().setSubTradeInfoList(unionTableTradeInfosubs);
            }

            DinnertableVo dinnertableVo = new DinnertableVo(model);
            dinnertableVo.setDinnertableTradeVos(dinnerTableTradeVos);
            dinnertableVo.setUnionMainTradeVo(unionTableTradeMain);
            dinnertableVo.setUnionSubTradeVos(unionTableTradesubs);
            EventBus.getDefault().post(new EventDinnertableVoNotice(dinnertableVo, enableServing));
        } catch (Exception e) {
            Log.e(TAG, "Select dinnertable error!", e);
        } finally {
            UserActionEvent.end(UserActionEvent.DINNER_TRADEINFO_REFRESH);
        }
    }

    private void setKdsScratchDishQty(TradeVo tradeVo) throws Exception {
        if (tradeVo.getTradeItemList() != null) {
            List<Long> itemIds = new ArrayList<>();
            List<TradeItemVo> tradeItemVoList = tradeVo.getTradeItemList();
            for (TradeItemVo itemVo : tradeItemVoList) {
                itemVo.setKdsScratchDishQty(BigDecimal.ZERO); //清除kds数据
                itemIds.add(itemVo.getTradeItem().getId());
            }
            if (itemIds.size() > 0) {
                KdsTradeDal kdsTradeDal = OperatesFactory.create(KdsTradeDal.class);
                List<KdsTradeItemPart> kdsTradeItemPartList = kdsTradeDal.getTradeItem(itemIds);
                if (kdsTradeItemPartList.size() > 0) {
                    for (KdsTradeItemPart itemPart : kdsTradeItemPartList) {
                        TradeItemVo tradeItemVo = getTradeItemVo(itemPart.tradeItemId, tradeItemVoList);
                        if (tradeItemVo != null) {
                            tradeItemVo.setKdsScratchDishQty(tradeItemVo.getKdsScratchDishQty().add(itemPart.quantity));
                        }
                    }
                }
            }
        }
    }

    public TradeItemVo getTradeItemVo(long tradeItemId, List<TradeItemVo> tradeItemVoList) {
        for (TradeItemVo itemVo : tradeItemVoList) {
            if (itemVo.getTradeItem().getId().equals(tradeItemId)) {
                return itemVo;
            }
        }
        return null;
    }

    /**
     * 数据库中的数据有变更时将回调此方法
     */
    @Override
    public void onDataChanged(Map<Long, StateWrapper> stateWrapperMap) {
        Log.i(TAG, "onDataChanged...");
        if (!zoneModelFinder.isEmpty()) {
            int emptyTableCount = doRefreshState(stateWrapperMap);
            EventBus.getDefault().post(new EventRefreshDinnertableNotice(emptyTableCount));
            // 如果当前有桌台信息是打开的，就发送event通知桌台信息界面刷新
            if (getSelectedDinnertableId() != null) {
                DinnertableModel model = dinnertableFinder.get(selectedDinnertableId);
                if (model != null) {
                    notifyDinnertable(model);
                }
            }
        }
    }

    @Override
    public void onHttpRecordChanged(Map<Long, List<AsyncHttpRecord>> tableAsyncRecord, Map<String, List<AsyncHttpRecord>> tradeAsyncRecord) {
        doRefreshAsyncHttpRecord(tableAsyncRecord, tradeAsyncRecord);
        if (getSelectedDinnertableId() != null) {
            EventBus.getDefault().post(new EventRefreshTradeAsyncHttp());//刷新桌台详情页
        }
    }

    /**
     * 切换区域
     *
     * @param zone
     */
    public void switchZone(IZone zone) {
        ZoneModel zoneModel = zoneModelFinder.get(zone.getId());
        if (zoneModel != null) {
            EventBus.getDefault().post(new EventZoneSwitchNotice(zoneModel));
        } else {
            Log.e(TAG, "Not found ZoneModel! id=" + zone.getId() + ", name=" + zone.getName());
        }
    }

    /**
     * 刷新桌台上指定单据的信息
     *
     * @param tradeUuid
     * @param tableId
     */
    public void refreshDinnertableTrade(String tradeUuid, Long tableId) {
        DinnertableModel model = dinnertableFinder.get(tableId);
        if (model == null) {
            Log.e(TAG, "Not found the dinnertable! tableId=" + tableId);
            return;
        }
        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            // 创建桌台单据信息
            for (IDinnertableTrade dinnertableTrade : model.getDinnertableTrades()) {
                if (tradeUuid.equals(dinnertableTrade.getTradeUuid())) {
                    TradeVo tradeVo = tradeDal.findTrade(tradeUuid, false);
                    if (tradeVo != null) {
                        DinnertableTradeInfo info = DinnertableTradeInfo.create(dinnertableTrade, tradeVo);
                        DinnertableTradeVo dinnerTableTradeVo = new DinnertableTradeVo(dinnertableTrade, info);
                        EventBus.getDefault().post(new EventDinnertableTradeVoNotice(dinnerTableTradeVo));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Refresh dinnertableTrade error!", e);
        }
    }

    /**
     * 选中指定的桌台
     *
     * @param model
     */
    public void select(DinnertableModel model) {
        if (model == null) {
            setSelectedDinnertableId(null);
        } else {
            Log.i(TAG, "show detail..." + model.getName());
            //微信加菜通知，选中桌台但是没有打开详情也，所以会出现没法显示的问题。
//			if (selectedDinnertableId != null) {
//				if (selectedDinnertableId.equals(model.getId())) {
//					Log.w(TAG, "The same, ignore.");
//					return;
//				}
//			}
            setSelectedDinnertableId(model.getId());
            notifyDinnertable(model);
        }
    }

    /**
     * 当前选中的桌台ID
     *
     * @return
     */
    public Long getSelectedDinnertableId() {
        return selectedDinnertableId;
    }

    public void setSelectedDinnertableId(Long selectedDinnertableId) {
        this.selectedDinnertableId = selectedDinnertableId;
    }


    /**
     * 删除指定的桌台单据
     *
     * @param
     */
    public void delete(final IDinnertableTrade dinnertableTrade, final TradeVo tradeVo, final boolean isPrintChecked, Reason reason, List<InventoryItemReq> inventoryItems) {
        Log.i(TAG, "delete..." + dinnertableTrade.getSn());
        ResponseListener<TradeResp> listener = new EventResponseListener<TradeResp>(UserActionEvent.DINNER_DEL_ORDER) {

            @Override
            public void onResponse(final ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    new Thread() {
                        @Override
                        public void run() {
                            final TradeResp resp = response.getContent();
                            Trade trade = resp.trade;
                            if (trade == null) {
                                return;
                            }

                            String uuid = trade.getUuid();
                            //使用打印回调管理进行打印
//                            PrintOperator printOperator = new PrintOperator(false);
//                            if (Utils.isNotEmpty(resp.getTradeTables())) {
//                                printOperator.setTableName(resp.getTradeTables().get(0).getTableName());
//                            }
//                            if (Utils.isNotEmpty(resp.getTradeExtras())) {
//                                printOperator.setSerialNUmber(resp.getTradeExtras().get(0).getSerialNumber());
//                            }
//                            printOperator.cancelTradePrint(uuid, isPrintChecked);
                            /*PRTPrintOperator operator = new PRTPrintOperator();
                            if (UnionUtil.isUnionMainTrade(dinnertableTrade.getTradeType())) {
                                operator.printUnionCancelTicket(tradeVo, null, isPrintChecked, false);
                            } else {
                                operator.printCancelTicket(uuid, null, isPrintChecked, false);
                            }*/
                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CANCEL_ORDER, trade.getId(), uuid, trade.getClientUpdateTime());

                            UserActionEvent.end(UserActionEvent.DINNER_DEL_ORDER);
                        }
                    }.start();

                    if (TradeStatusUtil.checkDespoit(tradeVo)) {
                        DepositInfoDialog.show(fragment.getContext(), tradeVo);
                    }

                } else {
                    String msg = response.getMessage();
                    if (msg != null) {
                        ToastUtil.showShortToast(msg);
                    } else {
                        ToastUtil.showShortToast(R.string.dinner_recision_failed);
                    }
                    AuthLogManager.getInstance().clear();
                    UserActionEvent.end(UserActionEvent.DINNER_DEL_ORDER);
                }
            }

            @Override
            public void onError(VolleyError error) {
                AuthLogManager.getInstance().clear();
                String msg = getContext().getString(R.string.dinner_recision_failed) + ": " + error.getMessage();
                ToastUtil.showLongToast(msg);
            }

        };

        Long tradeId = dinnertableTrade.getTradeId();
        Long serverUpdateTime = dinnertableTrade.getTradeServerUpdateTime();
        // 桌台上只有一单时将状态改为空桌
        DinnertableState dinnertableState = new DinnertableState();
        if (dinnertableTrade.getDinnertable() == null) {
            return;
        }
        dinnertableState.setId(dinnertableTrade.getDinnertable().getId());
        dinnertableState.setModifyDateTime(dinnertableTrade.getDinnertable().getServerUpdateTime());
        if (dinnertableTrade.getDinnertable().getTradeCount() == 1) {
            dinnertableState.setTableStatus(TableStatus.EMPTY);
        } else {
            dinnertableState.setTableStatus(dinnertableTrade.getDinnertable().getTableStatus());
        }
        TradeOperates dal = OperatesFactory.create(TradeOperates.class);
        if (dinnertableTrade.getTradeType() != null) {
            switch (dinnertableTrade.getTradeType()) {
                case SELL:
                    dal.recisionDinner(tradeId, serverUpdateTime, Arrays.asList(dinnertableState), reason, inventoryItems,
                            fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
                    break;
                case UNOIN_TABLE_MAIN:
                    dal.recisionUnionTrade(tradeId, serverUpdateTime, reason, inventoryItems,
                            fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
                    break;
                default:
                    dal.recisionDinner(tradeId, serverUpdateTime, Arrays.asList(dinnertableState), reason, inventoryItems,
                            fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
                    break;
            }
        } else {
            dal.recisionDinner(tradeId, serverUpdateTime, Arrays.asList(dinnertableState), reason, inventoryItems,
                    fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
        }
    }

    /**
     * 换桌
     *
     * @param orginal
     * @param dest
     */
    public void transfer(final List<TradeItemExtraDinner> tradeItemSeats, final IDinnertableTrade orginal, final IDinnertable dest) {
        if (orginal == null || dest == null) {
            Log.e(TAG, "The orginal or dest is null!");
            return;
        }
        Log.i(TAG, "transfer..." + orginal.getSn() + " --> " + dest.getName());
        // 允许一桌多单 update by Zhaos 2016-02-22
        if (!ServerSettingManager.allowMultiTradesOnTable()) {
            if (dest.getTradeCount() >= 1) {
                ToastUtil.showShortToast(R.string.dinner_trade_exceeded_the_limit);
                return;
            }
        }

        ResponseListener<TransferDinnertableResp> listener = new ResponseListener<TransferDinnertableResp>() {

            @Override
            public void onResponse(ResponseObject<TransferDinnertableResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (fragment != null) {
                        DialogUtil.showLoadingDialog(fragment.getFragmentManager(),
                                fragment.getActivity().getResources().getString(R.string.connecting_print));
                    }

                    /*PrintDinnerTable orginalTable = new PrintDinnerTable();
                    orginalTable.setSn(orginal.getSn());
                    orginalTable.setTableName(orginal.getDinnertable().getName());
                    orginalTable.setZoneName(orginal.getDinnertable().getZone().getName());
                    // 转台单需要按原单后厨配置出票，所以新增传入原单桌台id
                    orginalTable.setTableId(orginal.getDinnertable().getId());
                    PrintDinnerTable destTable = new PrintDinnerTable();
                    destTable.setTableName(dest.getName());
                    destTable.setZoneName(dest.getZone().getName());
                    destTable.setTableId(dest.getId());*/

                    /*PrintContentQueue.getInstance().mergeOrTransferDinnerTrade(orginalTable, destTable,
                            orginal.getTradeUuid(), Calm.OPERATE_TYPE_TRANSTER,
                            new TransferOrMergePrintListener(PrintTicketTypeEnum.TRANSFERTABLE));*/


                    /*TradeTableWrapper srcTable = TradeTableWrapper.create(orginal);
                    TradeTableWrapper destTradeTableList = TradeTableWrapper.create(dest);
                    IPrintHelper.Holder.getInstance().printMovingTableTicket(orginal.getTradeUuid(), srcTable, destTradeTableList,
                            new PRTTransferOrMergePrintListener(PrintTicketTypeEnum.TRANSFERTABLE));*/
                } else {
                    String msg = response.getMessage();
                    if (msg != null) {
                        ToastUtil.showShortToast(msg);
                    } else {
                        ToastUtil.showShortToast(R.string.dinner_transfer_failed);
                    }
                }
                AuthLogManager.getInstance().clear();
            }

            @Override
            public void onError(VolleyError error) {
                String msg = getContext().getString(R.string.dinner_transfer_failed) + ": " + error.getMessage();
                ToastUtil.showLongToast(msg);
                AuthLogManager.getInstance().clear();
            }
        };
        TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        operates.transferDinnertable(tradeItemSeats, orginal,
                dest,
                LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
    }

    /**
     * 合单
     *
     * @param orginal
     * @param dest
     */
    public void merge(final List<TradeItemExtraDinner> tradeItemSeats, final IDinnertableTrade orginal, final IDinnertableTrade dest) {
        if (orginal.equals(dest)) {
            ToastUtil.showLongToast(R.string.dinner_merge_self_hint);
            return;
        }
        Log.i(TAG, "merge..." + orginal.getSn() + " --> " + dest.getSn());
        try {
            List<TradeCustomer> tradeCustomerList = DBHelperManager.queryByValue(TradeCustomer.class, TradeCustomer.$.tradeId, dest.getTradeId());
            final TradeCustomer tradeCustomer = CustomerManager.getInstance().getValidMemberOrCardCustomer(tradeCustomerList);
            if (tradeCustomer != null) {
                CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
                customerOperates.getCustomerByType(CustomerRequest.CUSTOMER_ID, tradeCustomer.getCustomerId() + "", LoadingResponseListener.ensure(new ResponseListener<CustomerMobileVo>() {
                    @Override
                    public void onResponse(ResponseObject<CustomerMobileVo> response) {
                        if (ResponseObject.isOk(response)) {
                            CustomerMobile customerMobile = response.getContent().result;
                            doMerge(tradeItemSeats, orginal, dest, customerMobile.levelId);
                        } else {
                            ToastUtil.showShortToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());
                    }
                }, fragment.getFragmentManager()));
            } else {
                doMerge(tradeItemSeats, orginal, dest, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * @param orginal
     * @param dest
     * @param levelId 目标单会员等级id
     */
    private void doMerge(final List<TradeItemExtraDinner> tradeItemSeats, final IDinnertableTrade orginal, final IDinnertableTrade dest, final Long levelId) {
        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (fragment != null) {
                        DialogUtil.showLoadingDialog(fragment.getFragmentManager(),
                                fragment.getActivity().getResources().getString(R.string.connecting_print));
                    }

                    /*PrintDinnerTable orginalTable = new PrintDinnerTable();
                    orginalTable.setSn(orginal.getSn());
                    orginalTable.setTableName(orginal.getDinnertable().getName());
                    orginalTable.setZoneName(orginal.getDinnertable().getZone().getName());
                    // 合单需要按原单后厨配置出票，所以新增传入原单桌台id
                    orginalTable.setTableId(orginal.getDinnertable().getId());
                    PrintDinnerTable destTable = new PrintDinnerTable();
                    destTable.setSn(dest.getSn());
                    // 传入目标桌台的ID，方便判断目标桌台是否被配置为该单据下可打印
                    destTable.setTableId(dest.getDinnertable().getId());
                    destTable.setTableName(dest.getDinnertable().getName());
                    destTable.setZoneName(dest.getDinnertable().getZone().getName());*/

                    /*PrintContentQueue.getInstance().mergeOrTransferDinnerTrade(orginalTable, destTable,
                            dest.getTradeUuid(), Calm.OPERATE_TYPE_MERGE,
                            new TransferOrMergePrintListener(PrintTicketTypeEnum.MERGETABLE));*/
                    // 成功后直接刷新

                    List<String> mergeDishUuids = null;
                    if (Utils.isNotEmpty(response.getContent().getTradeItems())) {
                        mergeDishUuids = new ArrayList<>();
                        for (TradeItem tradeItem : response.getContent().getTradeItems()) {
                            mergeDishUuids.add(tradeItem.getUuid());
                        }
                    }

                    /*TradeTableWrapper srcTradeTableList = TradeTableWrapper.create(orginal);
                    TradeTableWrapper destTradeTableList = TradeTableWrapper.create(dest);
                    IPrintHelper.Holder.getInstance().printMergeTableTicket(dest.getTradeUuid(), srcTradeTableList, destTradeTableList,
                            mergeDishUuids, new PRTTransferOrMergePrintListener(PrintTicketTypeEnum.MERGETABLE));*/
                } else {
                    String msg = response.getMessage();
                    if (msg != null) {
                        ToastUtil.showShortToast(msg);
                    } else {
                        ToastUtil.showShortToast(R.string.dinner_merge_failed);
                    }
                }
                AuthLogManager.getInstance().clear();
            }

            @Override
            public void onError(VolleyError error) {
                String msg = getContext().getString(R.string.dinner_merge_failed) + ": " + error.getMessage();
                ToastUtil.showLongToast(msg);
                AuthLogManager.getInstance().clear();
            }
        };
        TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        operates.mergeDinner(tradeItemSeats, orginal, dest, levelId, LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
    }

    /**
     * 清台
     *
     * @param model
     */
    public void clear(final DinnertableModel model) {
        Log.i(TAG, "clear dinnertable...");
        ResponseListener<ClearDinnertableResp> listener = new EventResponseListener<ClearDinnertableResp>(UserActionEvent.DINNER_TABLE_CLEAR) {

            @Override
            public void onResponse(ResponseObject<ClearDinnertableResp> response) {
                if (!ResponseObject.isOk(response)) {
                    if (response != null) {
                        String msg = response.getMessage();
                        if (msg != null) {
                            ToastUtil.showShortToast(msg);
                        } else {
                            ToastUtil.showShortToast(R.string.dinner_clear_failed);
                        }
                    }
                    AuthLogManager.getInstance().clear();
                }
                UserActionEvent.end(UserActionEvent.DINNER_TABLE_CLEAR);
            }

            @Override
            public void onError(VolleyError error) {
                String msg = getContext().getString(R.string.dinner_clear_failed) + ": " + error.getMessage();
                ToastUtil.showLongToast(msg);
                AuthLogManager.getInstance().clear();
            }

        };
        TablesDal dal = OperatesFactory.create(TablesDal.class);
        dal.clearDinnertable(model, fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()));
    }


    /**
     * 关闭并释放资源
     */
    public void close() {
        executorService.shutdownNow();
        DinnertableStateCache.close();
        zoneModelFinder.clear();
        dinnertableFinder.clear();
        setSelectedDinnertableId(null);
        executorService = null;
        fragment = null;
    }


    /**
     * @Date 2016/6/13
     * @Description:合单移菜
     * @Param
     * @Return
     */
    public void mergeMoveDish(final List<DishQuantityBean> dishItes, final IDinnertableTradeMoveDish orginal,
                              final IDinnertableTrade dest, final Integer actionType, final boolean copyDishProperty,
                              final boolean printKitchen) {
        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) orginal;

        if (modelMoveDish.getSourceTradeVo().getDinnertableTrade().getTradeId().equals(dest.getTradeId())) {
            ToastUtil.showLongToast(R.string.dinner_move_dish_to_sametrade);
            return;
        }

        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            final TradeVo destTradeVo = tradeDal.findTrade(dest.getTradeId(), true);
            if (destTradeVo != null) {
                //调用接口植入会员等级id，临时方案
                final TradeCustomer tradeCustomer = CustomerManager.getInstance().getValidMemberOrCardCustomer(destTradeVo.getTradeCustomerList());
                if (tradeCustomer != null) {
                    CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
                    customerOperates.getCustomerByType(CustomerRequest.CUSTOMER_ID, tradeCustomer.getCustomerId() + "", LoadingResponseListener.ensure(new ResponseListener<CustomerMobileVo>() {
                        @Override
                        public void onResponse(ResponseObject<CustomerMobileVo> response) {
                            if (ResponseObject.isOk(response)) {
                                CustomerMobile customerMobile = response.getContent().result;
                                tradeCustomer.levelId = customerMobile.levelId;
                                doMergeMoveOrCopyDish(dishItes, orginal, dest, actionType, copyDishProperty, printKitchen, destTradeVo);
                            } else {
                                ToastUtil.showShortToast(response.getMessage());
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            ToastUtil.showShortToast(error.getMessage());
                        }
                    }, fragment.getFragmentManager()));
                } else {
                    doMergeMoveOrCopyDish(dishItes, orginal, dest, actionType, copyDishProperty, printKitchen, destTradeVo);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void doMergeMoveOrCopyDish(List<DishQuantityBean> dishItes, final IDinnertableTradeMoveDish orginal,
                                       final IDinnertableTrade dest, final Integer actionType, boolean copyDishProperty,
                                       final boolean printKitchen, TradeVo destTradeVo) {
        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) orginal;
        // 构建移菜数据
        CopyMoveDishTool copyMoveDishTool = new CopyMoveDishTool();
        DinnertableTradeInfo sourceTradeInfo = modelMoveDish.getSourceTradeVo().getInfo();
        final List<IShopcartItem> moveShopcartItems = modelMoveDish.getItems();// 移的菜

        DinnertableTradeInfo targetTradeInfo = DinnertableTradeInfo.create(dest, destTradeVo);
        final Map<Integer, TradeVo> resultMap;
        if (actionType == 1) {
            resultMap = copyMoveDishTool.moveDish(sourceTradeInfo, targetTradeInfo, null, dishItes);
        } else {
            resultMap = copyMoveDishTool.copyDish(sourceTradeInfo, targetTradeInfo, null, moveShopcartItems, dishItes, copyDishProperty);
        }

        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showShortToast(response.getMessage());

                    if (actionType == 1) {
                        //发送移菜成功通知到TableInfoFragment更新界面
                        EventBus.getDefault().post(new EventMoveDishNotice(true));

                        if (fragment != null) {
                            DialogUtil.showLoadingDialog(fragment.getFragmentManager(),
                                    fragment.getActivity().getResources().getString(R.string.connecting_print));
                        }

//                        PrintDinnerTable orginalTable = new PrintDinnerTable();
//                        orginalTable.setSn(orginal.getSn());
//                        orginalTable.setZoneName(orginal.getDinnertable().getZone().getName());
//                        orginalTable.setTableName(orginal.getDinnertable().getName());
//                        // 合单需要按原单后厨配置出票，所以新增传入原单桌台id
//                        orginalTable.setTableId(orginal.getDinnertable().getId());
//                        PrintDinnerTable destTable = new PrintDinnerTable();
//                        destTable.setSn(dest.getSn());
//                        // 传入目标桌台的ID，方便判断目标桌台是否被配置为该单据下可打印
//                        destTable.setTableId(dest.getDinnertable().getId());
//                        destTable.setTableName(dest.getDinnertable().getName());
//                        destTable.setZoneName(dest.getDinnertable().getZone().getName());
//                        destTable.setSn(dest.getSn());
//
//                        ArrayList<TradeItemChange> changes = new ArrayList<TradeItemChange>();
//                        for (TradeItem tradeItem : response.getContent().getTradeItems()) {
//                            if (tradeItem.getStatusFlag() == StatusFlag.VALID) {
//                                TradeItemChange tradeItemChange =
//                                        new TradeItemChange(Calm.DINNER_DISH_ADD, tradeItem.getUuid());
//                                changes.add(tradeItemChange);
//                            }
//
//                        }
//
//                        PrintContentQueue.getInstance().mergeOrTransferOrMoveDinnerTrade(orginalTable,
//                                destTable, dest.getTradeUuid(), Calm.OPERATE_TYPE_MOVE, changes,
//                                new TransferOrMergePrintListener(PrintTicketTypeEnum.MOVEDISH));

                        List<String> mergeDishUuids = null;
                        if (Utils.isNotEmpty(response.getContent().getTradeItems())) {
                            mergeDishUuids = new ArrayList<>();
                            for (TradeItem tradeItem : response.getContent().getTradeItems()) {
                                mergeDishUuids.add(tradeItem.getUuid());
                            }
                        }

                        /*TradeTableWrapper srcTradeTableList = TradeTableWrapper.create(orginal);
                        TradeTableWrapper destTradeTableList = TradeTableWrapper.create(dest);
                        IPrintHelper.Holder.getInstance().printMovingDishTicket(dest.getTradeUuid(), srcTradeTableList, destTradeTableList,
                                mergeDishUuids, new PRTTransferOrMergePrintListener(PrintTicketTypeEnum.MOVEDISH));*/
                    } else {//复制菜品成功后打印
                        //复制次数累加
                        fragment.getInfoFragment().addCopydishTimes();
                        //更新取消复制按钮
                        fragment.getInfoFragment().talbeInfoContentBean.updateCancelMoveDishBtn((byte) 2);

                        if (printKitchen) {//传送后厨
                            printTicktForCopyDish(resultMap, response, true);
                        } else {//未勾选打印客看单
                            printCustomerTicktForCopyDish(resultMap, response, true);
                        }
                    }
                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHANGE_ORDER, orginal.getTradeId(), orginal.getTradeUuid(), orginal.getTradeClientUpdateTime());
                } else {
                    if (response != null && !TextUtils.isEmpty(response.getMessage())) {
                        ToastUtil.showShortToast(response.getMessage());
                    } else {
                        ToastUtil.showShortToast(R.string.diner_movedish_failed);
                    }
                    AuthLogManager.getInstance().clear();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                AuthLogManager.getInstance().clear();
            }
        };

        // 移菜请求
        if (resultMap != null && resultMap.size() == 2) {
            Log.i("zhubo", "发起移菜请求");
            List<TradeItem> tradeItems = new ArrayList<TradeItem>();
            if (dishItes != null) {
                TradeVo tRradeVo = resultMap.get(CopyMoveDishTool.TARGET);
                for (DishQuantityBean item : dishItes) {
                    CTradeItem temp = new CTradeItem();
                    temp.setId(item.shopcartItem.getId());
                    temp.setUuid(item.shopcartItem.getUuid());
                    temp.setMoveQuantity(item.quantity.compareTo(item.shopcartItem.getSingleQty()) == 0 ? null : item.quantity);//设置移动分数
                    tradeItems.add(temp);

                    TradeItemExtraDinner tradeItemExtraDinner = CopyMoveDishTool.buildTradeItemExtraDinner(item.shopcartItem, item.tableSeat);
                    if (tradeItemExtraDinner != null) {
                        tRradeVo.getTradeItemExtraDinners().add(tradeItemExtraDinner);
                    }

                }
            } else {
                for (IShopcartItem item : moveShopcartItems) {
                    CTradeItem temp = new CTradeItem();
                    temp.setId(item.getId());
                    temp.setUuid(item.getUuid());
                    tradeItems.add(temp);
                }
            }


            moveDishRequest(resultMap.get(CopyMoveDishTool.SOURCE),
                    resultMap.get(CopyMoveDishTool.TARGET),
                    tradeItems,
                    listener, actionType, copyDishProperty ? 1 : 2);
        }
    }

    /**
     * @Date 2016/6/13
     * @Description:转台移菜或者复制菜品
     * @Param
     * @Return
     */
    public void transferMoveDish(List<DishQuantityBean> dishItes, final IDinnertableTrade orginal, final IDinnertable dest, final Integer actionType, boolean copyDishProperty, final boolean printKitchen) {
        if (orginal == null || dest == null) {
            Log.e(TAG, "The orginal or dest is null!");
            return;
        }
        Log.i(TAG, "transfer..." + orginal.getSn() + " --> " + dest.getName());

        if (!ServerSettingManager.allowMultiTradesOnTable()) {
            if (dest.getTradeCount() >= 1) {
                ToastUtil.showShortToast(R.string.dinner_trade_exceeded_the_limit);
                return;
            }
        }

        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) orginal;
        CopyMoveDishTool copyMoveDishTool = new CopyMoveDishTool();
        DinnertableTradeInfo sourceTradeInfo = modelMoveDish.getSourceTradeVo().getInfo();
        final List<IShopcartItem> moveShopcartItems = modelMoveDish.getItems();// 移的菜

       /* final TradeTable tradeTable = TradeTableWrapper.createTradeTable(dest);

        final Map<Integer, TradeVo> resultMap;
        if (actionType == 1) {//移菜数据构造
            resultMap = copyMoveDishTool.moveDish(sourceTradeInfo, null, tradeTable, dishItes);
        } else {//复制数据构造
            resultMap = copyMoveDishTool.copyDish(sourceTradeInfo, null, tradeTable, moveShopcartItems, dishItes, copyDishProperty);

        }*/

        ResponseListener<TradeResp> listener = new EventResponseListener<TradeResp>(actionType == 1 ? UserActionEvent.DINNER_DISHES_MOVE : UserActionEvent.DINNER_DISHES_COPY) {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showShortToast(response.getMessage());
                    if (actionType == 1) {
                        //发送移菜成功通知到TableInfoFragment更新界面
                        EventBus.getDefault().post(new EventMoveDishNotice(true));
                        if (fragment != null) {
                            DialogUtil.showLoadingDialog(fragment.getFragmentManager(),
                                    fragment.getActivity().getResources().getString(R.string.connecting_print));
                        }

                        // 调打印接口
                        /*PrintDinnerTable orginalTable = new PrintDinnerTable();
                        orginalTable.setSn(orginal.getSn());
                        orginalTable.setTableName(orginal.getDinnertable().getName());
                        orginalTable.setZoneName(orginal.getDinnertable().getZone().getName());
                        // 转台单需要按原单后厨配置出票，所以新增传入原单桌台id
                        orginalTable.setTableId(orginal.getDinnertable().getId());
                        PrintDinnerTable destTable = new PrintDinnerTable();
                        destTable.setTableName(dest.getName());
                        destTable.setZoneName(dest.getZone().getName());
                        destTable.setTableId(dest.getId());
                        ArrayList<TradeItemChange> changes = new ArrayList<TradeItemChange>();
                        for (TradeItem tradeItem : response.getContent().getTradeItems()) {
                            if (tradeItem.getStatusFlag() == StatusFlag.VALID) {
                                TradeItemChange tradeItemChange =
                                        new TradeItemChange(Calm.DINNER_DISH_ADD, tradeItem.getUuid());
                                changes.add(tradeItemChange);
                            }

                        }*/
                        /*PrintContentQueue.getInstance().mergeOrTransferOrMoveDinnerTrade(orginalTable,
                                destTable, resultMap.get(CopyMoveDishTool.TARGET).getTrade().getUuid(),
                                Calm.OPERATE_TYPE_MOVE, changes,
                                new TransferOrMergePrintListener(PrintTicketTypeEnum.MOVEDISH));*/

                        //String destTradeUuid = resultMap.get(CopyMoveDishTool.TARGET).getTrade().getUuid();

                        /*String serialNumber = null;
                        List<TradeExtra> tradeExtras = response.getContent().getTradeExtras();
                        if (tradeExtras != null) {
                            for (TradeExtra extra : tradeExtras) {
                                if (destTradeUuid.equals(extra.getTradeUuid())) {
                                    serialNumber = extra.getSerialNumber();
                                    break;
                                }
                            }
                        }

                        List<String> mergeDishUuids = null;
                        if (Utils.isNotEmpty(response.getContent().getTradeItems())) {
                            mergeDishUuids = new ArrayList<>();
                            for (TradeItem tradeItem : response.getContent().getTradeItems()) {
                                mergeDishUuids.add(tradeItem.getUuid());
                            }
                        }*/

                        /*TradeTableWrapper srcTradeTableList = TradeTableWrapper.create(orginal);
                        TradeTableWrapper destTradeTableList = TradeTableWrapper.create(serialNumber, tradeTable);
                        IPrintHelper.Holder.getInstance().printMovingDishTicket(destTradeUuid, srcTradeTableList, destTradeTableList,
                                mergeDishUuids, new PRTTransferOrMergePrintListener(PrintTicketTypeEnum.MOVEDISH));*/
                    } else {//复制菜品成功后打印信息
                        //复制次数累加
                        fragment.getInfoFragment().addCopydishTimes();
                        //更新取消复制按钮
                        fragment.getInfoFragment().talbeInfoContentBean.updateCancelMoveDishBtn((byte) 2);
                        /*if (printKitchen) {//勾选传送后厨
                            printTicktForCopyDish(resultMap, response, false);
                        } else {//未勾选打印客看单
                            printCustomerTicktForCopyDish(resultMap, response, false);
                        }*/
                    }
                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHANGE_ORDER, orginal.getTradeId(), orginal.getTradeUuid(), orginal.getTradeClientUpdateTime());
                } else {
                    if (response != null && !TextUtils.isEmpty(response.getMessage())) {
                        ToastUtil.showShortToast(response.getMessage());
                    } else {
                        ToastUtil.showShortToast(R.string.diner_movedish_failed);
                    }
                    AuthLogManager.getInstance().clear();
                }
                if (actionType == 1) {
                    UserActionEvent.end(UserActionEvent.DINNER_DISHES_MOVE);
                } else {
                    UserActionEvent.end(UserActionEvent.DINNER_DISHES_COPY);
                }
            }

            @Override
            public void onError(VolleyError error) {
                AuthLogManager.getInstance().clear();
                ToastUtil.showShortToast(error.getMessage());
            }
        };

        /*if (resultMap != null && resultMap.size() == 2) {
            Log.i("zhubo", "发起转台移菜请求");
            List<TradeItem> tradeItems = new ArrayList<TradeItem>();
            if (dishItes != null) {
                TradeVo tRradeVo = resultMap.get(CopyMoveDishTool.TARGET);
                for (DishQuantityBean item : dishItes) {
                    CTradeItem temp = new CTradeItem();
                    temp.setId(item.shopcartItem.getId());
                    temp.setUuid(item.shopcartItem.getUuid());
                    temp.setMoveQuantity(item.quantity.compareTo(item.shopcartItem.getSingleQty()) == 0 ? null : item.quantity);//设置移动分数
                    tradeItems.add(temp);


                    TradeItemExtraDinner tradeItemExtraDinner = CopyMoveDishTool.buildTradeItemExtraDinner(item.shopcartItem, item.tableSeat);
                    if (tradeItemExtraDinner != null) {
                        tRradeVo.getTradeItemExtraDinners().add(tradeItemExtraDinner);
                    }
                }
            } else {
                for (IShopcartItem item : moveShopcartItems) {
                    CTradeItem temp = new CTradeItem();
                    temp.setId(item.getId());
                    temp.setUuid(item.getUuid());
                    tradeItems.add(temp);
                }
            }*/


           /* moveDishRequest(resultMap.get(CopyMoveDishTool.SOURCE),
                    resultMap.get(CopyMoveDishTool.TARGET),
                    tradeItems,
                    listener, actionType, copyDishProperty ? 1 : 2);*/
        //}
    }

    /*private TradeTableWrapper createTradeTable(IDinnertableTrade dinnertableTrade) {
        String serialNumber = null;
        if (dinnertableTrade instanceof DinnertableTradeModel) {
            serialNumber = ((DinnertableTradeModel) dinnertableTrade).getTradeTableInfo().serialNumber;
        }

        return TradeTableWrapper.create(serialNumber, createTradeTable(dinnertableTrade.getDinnertable()));
    }

    private TradeTable createTradeTable(IDinnertable dest) {
        final TradeTable tradeTable = new TradeTable();
        tradeTable.setTableId(dest.getId());
        tradeTable.setTableName(dest.getName());
        tradeTable.validateCreate();
        String uuid = SystemUtils.genOnlyIdentifier();
        tradeTable.setUuid(uuid);

        tradeTable.setTablePeopleCount(dest.getNumberOfSeats());
        tradeTable.setMemo("");

        // 需要获取waiterId
        //设置登录操作员为默认服务员
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeTable.setWaiterId(user.getId());
            tradeTable.setWaiterName(user.getName());
        }
        return tradeTable;
    }*/

    /**
     * @Date 2016/6/13
     * @Description:调用移菜接口
     * @Param
     * @Return
     */
    public void moveDishRequest(TradeVo sourceTradeVo, TradeVo targetTradeVo, List<TradeItem> tradeItems,
                                ResponseListener<TradeResp> listener, Integer actionType, Integer moveAdd) {
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        mTradeOperates.moveDish(sourceTradeVo,
                targetTradeVo,
                tradeItems,
                fragment == null ? listener : LoadingResponseListener.ensure(listener, fragment.getFragmentManager()), actionType, moveAdd);
    }

    /*private class TransferOrMergePrintListener extends OnSimplePrintListener {

        public TransferOrMergePrintListener(PrintTicketTypeEnum ticketTypeEnum) {
            super(ticketTypeEnum);
        }

        @Override
        public void onResult(int globalCode, LongSparseArray<ReturnCashierTicketBean> returnCashierSparseArray, HashMap<Long, Integer> dishMap, SendData sendData) {
            if (DialogUtil.isLoadingDialogShowing()) {
                DialogUtil.dismissLoadingDialog();
            }
        }
    }

    private class PRTTransferOrMergePrintListener extends PRTBatchModifyPrintListener {

        public PRTTransferOrMergePrintListener(PrintTicketTypeEnum ticketTypeEnum) {
            super(ticketTypeEnum, false);
        }

        @Override
        public void onResult(int globalCode, LongSparseArray<PRTReturnCashierTicketBean> returnCashierSparseArray, Map<Long, Integer> dishMap, SendData sendData) {
            super.onResult(globalCode, returnCashierSparseArray, dishMap, sendData);
            if (DialogUtil.isLoadingDialogShowing()) {
                DialogUtil.dismissLoadingDialog();
            }
        }
    }*/

    /**
     * @Date 2016/7/25
     * @Description:复制菜品后打印
     * @Param
     * @Return
     */
    private void printTicktForCopyDish(Map<Integer, TradeVo> resultMap, ResponseObject<TradeResp> response, final boolean isAdd) {
//		if(fragment!=null){
//			DialogUtil.showLoadingDialog(fragment.getFragmentManager(),fragment.getResources().getString(R.string.connecting_print));
//		}
        //打印客看单
        /*TradeVo target = resultMap.get(CopyMoveDishTool.TARGET);
        final String targetTradeUuid = target.getTrade().getUuid();

        final List<String> tradeItemUuid = new ArrayList<>();
        List<TradeItem> tradeItems = new ArrayList<>();
        for (TradeItem tradeItem : response.getContent().getTradeItems()) {
            if (tradeItem.getTradeUuid().equals(targetTradeUuid)) {
                tradeItemUuid.add(tradeItem.getUuid());
                tradeItems.add(tradeItem);
                tradeItem.setIssueStatus(IssueStatus.ISSUING);
                tradeItem.setGuestPrinted(GuestPrinted.PRINTED);
            }
        }

        IPrintHelper.Holder.getInstance().printCustomerTicket(targetTradeUuid, tradeItemUuid, null,
                null, !isAdd, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));

        //修改批次号
        DinnerPrintManager.modifyPrintStatus(tradeItems, null, null, new ResponseListener<TradeItemResp>() {

            @Override
            public void onResponse(ResponseObject<TradeItemResp> response) {
                if (ResponseObject.isOk(response)) {
                    IPrintHelper.Holder.getInstance().printKitchenAllTicket(targetTradeUuid, tradeItemUuid, null, null, !isAdd, PRTBatchModifyPrintListener.create(PrintTicketTypeEnum.KITCHENALL));
                    IPrintHelper.Holder.getInstance().printKitchenCellTicket(targetTradeUuid, tradeItemUuid, null, null, !isAdd, PRTBatchModifyPrintListener.create(PrintTicketTypeEnum.KITCHENCELL));
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        });*/
    }

    /**
     * @Date 2016/7/27
     * @Description:打印客看单
     * @Param
     * @Return
     */
    private void printCustomerTicktForCopyDish(Map<Integer, TradeVo> resultMap, ResponseObject<TradeResp> response, final boolean isAdd) {
//		if(fragment!=null){
//			DialogUtil.showLoadingDialog(fragment.getFragmentManager(),fragment.getResources().getString(R.string.connecting_print));
//		}
        //打印客看单
        /*TradeVo target = resultMap.get(CopyMoveDishTool.TARGET);
        final String targetTradeUuid = target.getTrade().getUuid();

        final List<String> tradeItemUuid = new ArrayList<>();
        for (TradeItem tradeItem : response.getContent().getTradeItems()) {
            if (tradeItem.getTradeUuid().equals(targetTradeUuid)) {
                tradeItemUuid.add(tradeItem.getUuid());
            }
        }

        IPrintHelper.Holder.getInstance().printCustomerTicket(targetTradeUuid, tradeItemUuid, null,
                null, !isAdd, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));*/
    }

    /**
     * 获取paymentvo
     *
     * @param tradeUuid
     * @return
     */
    public PaymentVo getPaymentVo(String tradeUuid) {
        if (!TextUtils.isEmpty(tradeUuid)) {
            try {
                List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
                TradeDal dal = OperatesFactory.create(TradeDal.class);
                paymentVos = dal.listPayment(tradeUuid);
                if (paymentVos != null && paymentVos.size() > 0) {
                    PaymentVo paymentVo = new PaymentVo();
                    paymentVo.setPaymentItemList(paymentVos.get(0).getPaymentItemList());
                    Payment payment = paymentVos.get(0).getPayment();
                    paymentVo.setPayment(payment);

                    return paymentVo;
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        return null;
    }


    /**
     * 根据联台单子单，构建桌台数据
     *
     * @param trades
     * @return
     * @throws Exception
     */
    private Map<Long, DinnertableModel> getDinnerTableModelByTrades(List<Trade> trades) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return getDinnerTableModelByTrades(trades, helper);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return new HashMap<>();
    }

    private Map<Long, DinnertableModel> getDinnerTableModelByTrades(List<Trade> trades, DatabaseHelper helper) throws Exception {
        Map<Long, DinnertableModel> mapDinnerTableModel = new HashMap<>();
        List<Long> subTradeIDs = new ArrayList<>();

        for (Trade trade : trades) {
            subTradeIDs.add(trade.getId());
        }

        List<TradeTable> tradeTables = helper.getDao(TradeTable.class).queryBuilder().where().in(TradeTable.$.tradeId, subTradeIDs).and()
                .eq(TradeTable.$.statusFlag, StatusFlag.VALID).query();

        List<Long> tableIDs = new ArrayList<>();
        for (TradeTable tradeTable : tradeTables) {
            tableIDs.add(tradeTable.getTableId());
        }

        List<Tables> tables = helper.getDao(Tables.class).queryBuilder().where().in(Tables.$.id, tableIDs).query();

        Set<Long> areaIDs = new HashSet<>();
        Map<Long, Tables> mapTables = new HashMap<>();
        for (Tables table : tables) {
            areaIDs.add(table.getAreaId());
            mapTables.put(table.getId(), table);
        }

        List<CommercialArea> tableAreas = helper.getDao(CommercialArea.class).queryBuilder().where().in(CommercialArea.$.id, areaIDs).query();
        Map<Long, CommercialArea> mapArea = new HashMap<>();
        for (CommercialArea tableArea : tableAreas) {
            mapArea.put(tableArea.getId(), tableArea);
        }

        Map<Long, ZoneModel> mapAreaModel = new HashMap<>();
        for (TradeTable tradeTable : tradeTables) {
            DinnertableModel tableModel = new DinnertableModel();

            Tables table = mapTables.get(tradeTable.getTableId());
            CommercialArea tableArea = mapArea.get(tradeTable.getTableId());

            ZoneModel zoneModel = mapAreaModel.get(tableArea.getId());
            if (zoneModel == null) {
                zoneModel = new ZoneModel();
                zoneModel.setId(tableArea.getId());
                zoneModel.setCode(tableArea.getAreaCode());
                zoneModel.setName(tableArea.getAreaName());
            }

            DinnertableModel dinnertableModel = new DinnertableModel();
            dinnertableModel.setId(table.getId());
            dinnertableModel.setName(table.getTableName());
            dinnertableModel.setNumberOfSeats(table.getTablePersonCount());
            dinnertableModel.setShape(DinnertableShape.SQUARE);
            dinnertableModel.setZone(zoneModel);
            dinnertableModel.setUuid(table.getUuid());
            dinnertableModel.setTableStatus(table.getTableStatus());

            mapDinnerTableModel.put(tradeTable.getTableId(), tableModel);
        }

        return mapDinnerTableModel;
    }


    /**
     * 根据子单获取联台单
     *
     * @param trade
     * @return
     */
    private Trade getDinnerUnionTrade(IDinnertableTrade trade) {
        if (trade.getTradeType() != TradeType.UNOIN_TABLE_SUB) {
            return null;
        }

        DatabaseHelper helper = DBHelperManager.getHelper();
        Trade unionTrade = null;
        try {
            TradeMainSubRelation tradeMainSubRelation = helper.getDao(TradeMainSubRelation.class).queryBuilder().where().eq(TradeMainSubRelation.$.subTradeId, trade.getTradeId())
                    .and().eq(TradeMainSubRelation.$.statusFlag, StatusFlag.VALID).queryForFirst();

            if (tradeMainSubRelation != null) {
                unionTrade = helper.getDao(Trade.class).queryBuilder().where().eq(Trade.$.id, tradeMainSubRelation.getMainTradeId()).queryForFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return unionTrade;
    }

    /**
     * 根据联台单查询所有的子单
     *
     * @param trade
     * @return
     */
    private List<Trade> getSubTrade(Trade trade) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<Trade> unionChildTrades = null;
        try {
            List<TradeMainSubRelation> tradeChildRelation = helper.getDao(TradeMainSubRelation.class).queryBuilder().where().eq(TradeMainSubRelation.$.mainTradeId, trade.getId())
                    .and().eq(TradeMainSubRelation.$.statusFlag, StatusFlag.VALID).query();

            if (Utils.isNotEmpty(tradeChildRelation)) {
                List<Long> childTradeIDs = new ArrayList<>();

                for (TradeMainSubRelation childRelation : tradeChildRelation) {
                    childTradeIDs.add(childRelation.getSubTradeId());
                }

                unionChildTrades = helper.getDao(Trade.class).queryBuilder().where().in(Trade.$.id, childTradeIDs)
                        .and()
                        .eq(Trade.$.statusFlag, StatusFlag.VALID)
                        .query();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return unionChildTrades;
    }

    /**
     * 获取桌台状态
     *
     * @param tradeExtra
     * @return
     */
    private DinnertableStatus getDinnertableStatus(TradeExtra tradeExtra) {
        if (tradeExtra.getHasServing() == TradeServingStatus.SERVED) {
            return DinnertableStatus.SERVING;
        } else {
            if (tradeExtra.getIsPrinted() == TradePrintStatus.PRINTED) {
                return DinnertableStatus.ISSUED;
            } else {
                return DinnertableStatus.UNISSUED;
            }

        }
    }


    public Long getZoneIdByTable(Long tableId) {
        if (dinnertableFinder != null && dinnertableFinder.containsKey(tableId)) {
            dinnertableFinder.get(tableId).getZone().getId();
        }
        return null;
    }
}

