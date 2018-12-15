package com.zhongmei.beauty.operates;

import android.net.Uri;
import android.util.Log;

import com.zhongmei.bty.basemodule.booking.manager.BookingTablesManager;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.DinnertableShape;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.ArrayList;
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

/**
 * Created by demo on 2018/12/15
 */

public abstract class TableManagerBase {
    private final String TAG = TableManagerBase.class.getSimpleName();

    private BookingTablesManager bookingTablesManager;

    /**
     * 所有桌台区域。key为ZoneModel.id(tableArea.id)
     */
    protected final Map<Long, ZoneModel> zoneModelFinder;

    /**
     * 所有桌台。key为DinnertableModel.id(table.id)
     */
    protected final Map<Long, DinnertableModel> dinnertableFinder;

    private ExecutorService executorService;

    protected Set<ITableOperator> mTableOperatorListeners = new HashSet<>();

    private DataChangeObserver mObserver;


    public TableManagerBase() {
        executorService = Executors.newFixedThreadPool(1);
        zoneModelFinder = new HashMap<Long, ZoneModel>();
        dinnertableFinder = new HashMap<Long, DinnertableModel>();
        bookingTablesManager = new BookingTablesManager();
        mObserver = new DataChangeObserver();
        DatabaseHelper.Registry.register(mObserver);

    }

    public ZoneModel getDinnerTableModeByZone(Long zoneId) {
        return zoneModelFinder.get(zoneId);
    }

    public void setmTableOperatorListener(ITableOperator listener) {
        mTableOperatorListeners.add(listener);
    }

    public void removeTableOperatorListener(ITableOperator listener) {
        if (mTableOperatorListeners != null) {
            mTableOperatorListeners.remove(listener);
        }
    }

    public void loadTableInfos() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (zoneModelFinder.size() <= 0 || dinnertableFinder.size() <= 0) {
                        doLoadDate();
                    }
                    List<ZoneModel> tableZoneModes = sortTables(zoneModelFinder);
                    List<DinnertableModel> tableModes = new ArrayList<DinnertableModel>(dinnertableFinder.values());
                    if (mTableOperatorListeners != null) {
                        for (ITableOperator listener : mTableOperatorListeners) {
                            listener.refreshZones(tableZoneModes);
                            listener.refreshTables(tableModes);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Load data error!", e);
                }
            }
        });

    }

    private List<ZoneModel> sortTables(Map<Long, ZoneModel> cahceZoneModes) throws Exception {
        List<ZoneModel> zoneList = new ArrayList<ZoneModel>(cahceZoneModes.values());
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

        return zoneList;
    }

    private void doLoadDate() throws Exception {
        //获取已预定桌台列表
//        HashMap<String, BookingTable> bookingMap = bookingTablesManager.getCurrentPeriodBookingTables();

        // 从数，据库中获取区域和桌台的基本信息存放到zoneModelFinder中
        TablesDal dal = OperatesFactory.create(TablesDal.class);
        Collection<TablesDal.DinnertableWrapper> wrappers = dal.listDinnertables();
        Log.i(TAG, "TableWrapper.size=" + wrappers.size());
        //刷新桌台开台异步
        for (TablesDal.DinnertableWrapper wrapper : wrappers) {
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
//            if (bookingMap.get(wrapper.getUuid()) == null) {
//                dinnertableModel.setReserved(false);
//            } else {
//                dinnertableModel.setReserved(true);
//            }

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
    }


    public class DataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            Uri URI_TRADE = DBHelperManager.getUri(Trade.class);
            if (uris.contains(URI_TRADE)) {
                if (getTableTradeCache() != null) {
                    getTableTradeCache().loadTableTrades(null);
                }
            }
        }
    }

    public void loadTableTrades(Long zoneId) {
        getTableTradeCache().loadTableTrades(zoneId);
    }

    public void onDestory() {
        zoneModelFinder.clear();
        dinnertableFinder.clear();
        executorService.shutdown();
        mTableOperatorListeners.clear();
        DatabaseHelper.Registry.unregister(mObserver);
    }

    public abstract TableTradeCacheBase getTableTradeCache();
}
