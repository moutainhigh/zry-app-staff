package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.basemodule.trade.bean.TableAreaPopupVo;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.IsDelete;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.data.db.common.TablePhysicalLayout;
import com.zhongmei.bty.data.db.common.TablePosition;
import com.zhongmei.bty.basemodule.trade.entity.TableType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;
import com.zhongmei.bty.data.operates.message.content.ClearDinnertableReq;
import com.zhongmei.bty.basemodule.trade.message.ClearDinnertableResp;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.entity.enums.LayoutType;
import com.zhongmei.bty.commonmodule.database.enums.TrueOrFalse;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.bean.SnackTableVo;
import com.zhongmei.bty.basemodule.trade.bean.TableTradeVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @version: 1.0
 * @date 2015年7月27日
 */
@SuppressLint("UseSparseArrays")
public class TablesDalImpl extends AbstractOpeartesImpl implements TablesDal {

    public TablesDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<TableType> listTableType() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(TableType.class)
                    .queryBuilder()
                    .orderBy(TableType.$.sort, true)
                    .orderBy(TableType.$.id, true)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CommercialArea> listTableArea() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(CommercialArea.class).queryForEq(CommercialArea.$.isDelete, IsDelete.VALID);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tables> listTables() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> dao = helper.getDao(Tables.class);
            Where<Tables, Long> where = dao.queryBuilder().orderBy(Tables.$.sort, true).where();
            return where.and(
                    where.eq(Tables.$.statusFlag, Status.VALID),
                    where.or(where.eq(Tables.$.canBooking, TrueOrFalse.TRUE),
                            where.eq(Tables.$.tableStatus, TableStatus.LOCKING))
            ).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TableSeat> listTableSeats() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TableSeat, Long> dao = helper.getDao(TableSeat.class);
            Where<TableSeat, Long> where = dao.queryBuilder().selectColumns(TableSeat.$.id, TableSeat.$.tableId, TableSeat.$.seatName).where();
            return where.eq(TableSeat.$.statusFlag, StatusFlag.VALID).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TableSeat> listTableSeatsByTableId(Long tableId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TableSeat, Long> dao = helper.getDao(TableSeat.class);
            Where<TableSeat, Long> where = dao.queryBuilder().selectColumns(TableSeat.$.id, TableSeat.$.tableId, TableSeat.$.seatName).where();
            return where.eq(TableSeat.$.statusFlag, StatusFlag.VALID).and().eq(TableSeat.$.tableId, tableId).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Tables> listAllPhysicalLayoutTables() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            //查询已发布的有效物理布局
            Dao<TablePhysicalLayout, Long> tplDao = helper.getDao(TablePhysicalLayout.class);
            QueryBuilder<TablePhysicalLayout, Long> tplQb = tplDao.queryBuilder().selectColumns(TablePhysicalLayout.$.id);
            Where<TablePhysicalLayout, Long> tplWhere = tplQb.where();
            tplWhere.and(tplWhere.eq(TablePhysicalLayout.$.layoutType, LayoutType.AREA),
                    tplWhere.eq(TablePhysicalLayout.$.publishStatus, 2),
                    tplWhere.eq(TablePhysicalLayout.$.isDelete, IsDelete.VALID));
            //查询已发布的有效物理布局下的桌台位置
            Dao<TablePosition, Long> tpDao = helper.getDao(TablePosition.class);
            QueryBuilder<TablePosition, Long> tpQb = tpDao.queryBuilder().selectColumns(TablePosition.$.tableId);
            Where<TablePosition, Long> tpWhere = tpQb.where();
            tpWhere.and(tpWhere.eq(TablePosition.$.layoutType, LayoutType.AREA),
                    tpWhere.eq(TablePosition.$.isDelete, IsDelete.VALID),
                    tpWhere.in(TablePosition.$.layoutId, tplQb));
            //查询桌台位置对应的桌台
            Dao<Tables, Long> tableDao = helper.getDao(Tables.class);
            Where<Tables, Long> tableWhere = tableDao.queryBuilder().orderBy(Tables.$.sort, true).where();
            return tableWhere.and(tableWhere.eq(Tables.$.statusFlag, Status.VALID),
                    tableWhere.ne(Tables.$.tableStatus, TableStatus.LOCKING),
                    tableWhere.in(Tables.$.id, tpQb)).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CommercialArea> listDinnerArea() throws Exception {
        List<Tables> tablesList = listAllPhysicalLayoutTables();
        List<CommercialArea> areaList = new ArrayList<CommercialArea>();
        ArrayList<Long> areaIds = new ArrayList<Long>();
        for (Tables table : tablesList) {
            if (!areaIds.contains(table.getAreaId())) {
                areaIds.add(table.getAreaId());
            }
        }
        for (Long areaId : areaIds) {
            areaList.add(findAreaById(areaId));
        }
        return areaList;
    }

    @Override
    public List<Tables> listDinnerEmptyTablesByStatus(TableStatus tableStatus) throws Exception {
        List<Tables> tablesList = listAllPhysicalLayoutTables();
        List<Tables> retTableList = new ArrayList<Tables>();
        for (Tables table : tablesList) {
            if (table.getTableStatus() == tableStatus) {
                retTableList.add(table);
            }
        }
        return retTableList;
    }

    @Override
    public List<Tables> listTablesByAreaId(Long areaId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<Tables> tableList = new ArrayList<Tables>();
        try {
            Map<Long, Tables> tablesFinder = new HashMap<Long, Tables>();
            Dao<Tables, Long> tableDao = helper.getDao(Tables.class);
            QueryBuilder<Tables, Long> tableQb = tableDao.queryBuilder();
            List<Tables> list =
                    tableQb.where().eq(Tables.$.statusFlag, Status.VALID).and().eq(Tables.$.areaId, areaId).query();
            for (Tables tables : list) {
                tablesFinder.put(tables.getId(), tables);
            }

            Dao<TablePhysicalLayout, Long> layoutDao = helper.getDao(TablePhysicalLayout.class);
            QueryBuilder<TablePhysicalLayout, Long> subQb = layoutDao.queryBuilder();
            subQb.selectColumns(TablePhysicalLayout.$.id).where().eq(TablePhysicalLayout.$.layoutType, LayoutType.AREA)
                    .and().eq(Tables.$.areaId, areaId)
                    .and().eq(TablePhysicalLayout.$.publishStatus, 2);
            Dao<TablePosition, Long> tpDao = helper.getDao(TablePosition.class);
            List<TablePosition> tpList = tpDao.queryBuilder().where().in(TablePosition.$.layoutId, subQb).query();
            for (TablePosition tp : tpList) {
                Tables tables = tablesFinder.get(tp.getTableId());
                if (tables == null) {
                    continue;
                }
                tableList.add(tables);
            }
            return tableList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Tables> listTables(Long tableTypeId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            QueryBuilder<Tables, String> qb = helper.getDao(Tables.class);
            qb.where().eq(Tables.$.tableTypeID, tableTypeId);
            return qb.orderBy(Tables.$.sort, true).orderBy(Tables.$.id, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Collection<DinnertableWrapper> listDinnertables() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Map<Long, CommercialArea> areaFinder = new HashMap<Long, CommercialArea>();
            List<CommercialArea> areaList = helper.getDao(CommercialArea.class).queryForAll();
            for (CommercialArea area : areaList) {
                areaFinder.put(area.getId(), area);
            }

            Map<Long, Tables> tablesFinder = new HashMap<Long, Tables>();
            List<Long> tablseIds = new ArrayList<Long>();
//			List<Tables> tablesList = helper.getDao(Tables.class).queryForEq(Tables.$.statusFlag, StatusFlag.VALID);
            List<Tables> tablesList = helper.getDao(Tables.class).queryBuilder().orderBy(Tables.$.id, true).where().eq(Tables.$.statusFlag, StatusFlag.VALID).query();
//			for (Tables tables : tablesList) {
//				tablesFinder.put(tables.getId(), tables);
//				tablseIds.add(tables.getId());
//			}

            Map<Long, DinnertableWrapper> wrapperFinder = new LinkedHashMap<Long, DinnertableWrapper>();
//
//			Dao<TablePhysicalLayout, Long> layoutDao = helper.getDao(TablePhysicalLayout.class);
//			QueryBuilder<TablePhysicalLayout, Long> subQb = layoutDao.queryBuilder();
//			subQb.selectColumns(TablePhysicalLayout.$.id)
//					.where()
//					.eq(TablePhysicalLayout.$.layoutType, LayoutType.AREA)
//					.and()
//					.eq(TablePhysicalLayout.$.publishStatus, 2);
//			Dao<TablePosition, Long> tpDao = helper.getDao(TablePosition.class);
//			List<TablePosition> tpList = tpDao.queryBuilder().where().in(TablePosition.$.layoutId, subQb).query();


//			Map<Long,List<TableSeat>> mapTableSeat=new HashMap<Long,List<TableSeat>>();

//			if(Utils.isNotEmpty(tablseIds)){
//				Dao<TableSeat,Long> seatDao=helper.getDao(TableSeat.class);
//				QueryBuilder<TableSeat,Long> seatQueryBuild = seatDao.queryBuilder();
//				seatQueryBuild.selectColumns(TableSeat.$.id,TableSeat.$.tableId,TableSeat.$.seatName)
//						.where()
//						.in(TableSeat.$.tableId, tablseIds)
//						.and()
//						.eq(TableSeat.$.statusFlag, StatusFlag.VALID);
//				List<TableSeat> tableSeats=seatQueryBuild.query();
//
//				if(Utils.isNotEmpty(tableSeats)){
//					for (TableSeat tableSeat : tableSeats) {
//						if(!mapTableSeat.containsKey(tableSeat.getTableId())){
//							mapTableSeat.put(tableSeat.getTableId(),new ArrayList<TableSeat>());
//						}
//
//						mapTableSeat.get(tableSeat.getTableId()).add(tableSeat);
//					}
//				}
//			}


            for (Tables tables : tablesList) {
//				Tables tables = tablesFinder.get(tp.getTableId());
                if (tables == null) {
                    continue;
                }
                CommercialArea area = areaFinder.get(tables.getAreaId());
                if (area == null) {
                    continue;
                }
//				if (tp.getxAxis() == null || tp.getyAxis() == null) {
//					continue;
//				}
                DinnertableWrapper wrapper = new DinnertableWrapper();
                wrapper.setId(tables.getId());
                wrapper.setUuid(tables.getUuid());//
                wrapper.setName(tables.getTableName());
                wrapper.setTableStatus(tables.getTableStatus());
                wrapper.setServerUpdateTime(tables.verValue());
                wrapper.setNumberOfSeats(tables.getTablePersonCount());
//				wrapper.setX(tp.getxAxis());
//				wrapper.setY(tp.getyAxis());
//				wrapper.setTableSeats(mapTableSeat.get(tables.getId()));
//				if (tp.getWidth() == null) {
//					wrapper.setWidth(DinnertableWrapper.MIN_WIDTH);
//				} else {
//					wrapper.setWidth(Math.max(DinnertableWrapper.MIN_WIDTH, tp.getWidth()));
//				}
//				if (tp.getHeight() == null) {
//					wrapper.setHeight(DinnertableWrapper.MIN_HEIGHT);
//				} else {
//					wrapper.setHeight(Math.max(DinnertableWrapper.MIN_HEIGHT, tp.getHeight()));
//				}
                wrapper.setZoneId(area.getId());
                wrapper.setZoneCode(area.getAreaCode());
                wrapper.setZoneName(area.getAreaName());

                wrapperFinder.put(wrapper.getId(), wrapper);
            }
            return wrapperFinder.values();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void clearDinnertable(IDinnertable dinnertable, ResponseListener<ClearDinnertableResp> listener) {
        /*String url = ServerAddressUtil.getInstance().clearDinnertable();
        ClearDinnertableReq req = new ClearDinnertableReq();
        req.setTableId(dinnertable.getId());
        req.setServerUpdateTime(dinnertable.getServerUpdateTime());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }
        OpsRequest.Executor<ClearDinnertableReq, ClearDinnertableResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(ClearDinnertableResp.class)
                .responseProcessor(new TablesRespProcessor())
                .execute(listener, "clearDinnertable");*/
    }

    /**
     * 将TradeResp保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
    private static class TablesRespProcessor extends SaveDatabaseResponseProcessor<ClearDinnertableResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final ClearDinnertableResp resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                    DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                    return null;
                }
            };
        }

    }

    @Override
    public CommercialArea listArea(Long tableId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> qbDao = helper.getDao(Tables.class);
            Long areaId = qbDao.queryForId(tableId).getAreaId();
            Dao<CommercialArea, Long> qaDao = helper.getDao(CommercialArea.class);
            return qaDao.queryForId(areaId);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Map<CommercialArea, List<Tables>> listAreaByTableId(Long... tableId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Map<CommercialArea, List<Tables>> commercialAreaListMap = new HashMap<>();
            Dao<Tables, Long> qbDao = helper.getDao(Tables.class);
            List<Tables> tables = qbDao.queryBuilder().where().in(Tables.$.id, Arrays.asList(tableId)).query();
            List<Long> areaIds = new ArrayList<>();
            for (Tables tab : tables) {
                areaIds.add(tab.getAreaId());
            }
            Dao<CommercialArea, Long> qaDao = helper.getDao(CommercialArea.class);
            List<CommercialArea> commercialAreaList = qaDao.queryBuilder().where().in(CommercialArea.$.id, areaIds).query();
            if (commercialAreaList != null) {
                for (CommercialArea area : commercialAreaList) {
                    List<Tables> list = commercialAreaListMap.get(area);
                    if (list == null) {
                        list = new ArrayList<>();
                        commercialAreaListMap.put(area, list);
                    }

                    for (Tables tab : tables) {
                        if (area.getId().equals(tab.getAreaId())) {
                            list.add(tab);
                        }
                    }
                }
            }
            return commercialAreaListMap;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Tables findTablesById(Long tabelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> dao = helper.getDao(Tables.class);
            return dao.queryForId(tabelId);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CommercialArea findAreaById(Long areaId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialArea, Long> dao = helper.getDao(CommercialArea.class);
            return dao.queryForId(areaId);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CommercialArea> findAreaById(List<Long> areaId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialArea, Long> dao = helper.getDao(CommercialArea.class);
            return dao.queryBuilder().where().in(CommercialArea.$.id, areaId).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public int countTotalPersonCount(Long tabelId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // 查三天之内的单据
            Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Dao<TradeTable, Long> tradeTableDao = helper.getDao(TradeTable.class);

            Dao<Trade, Long> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, Long> tradeQb = tradeDao.queryBuilder();
            tradeQb.selectColumns(Trade.$.uuid)
                    .where()
                    .eq(Trade.$.businessType, BusinessType.DINNER)
                    .and()
                    .ne(Trade.$.tradeType, TradeType.SPLIT)
                    .and()
                    .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID);

            List<TradeTable> tradeTableList = tradeTableDao.queryBuilder()
                    .selectColumns(TradeTable.$.tradeUuid, TradeTable.$.tablePeopleCount)
                    .where()
                    .eq(TradeTable.$.tableId, tabelId)
                    .and()
                    .eq(TradeTable.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .in(TradeTable.$.tradeUuid, tradeQb)
                    .and()
                    .gt(TradeTable.$.serverCreateTime, cad.getTime().getTime())
                    .query();
            int totalPersonCount = 0;
            for (TradeTable tradeTable : tradeTableList) {
                totalPersonCount += tradeTable.getTablePeopleCount();
            }
            return totalPersonCount;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public DinnerTableInfo getDinnerTableByTradeVo(TradeVo tradeVo) throws Exception {
        DinnerTableInfo dinnerTable = new DinnerTableInfo();
        Long tableId = null;
        Tables table = null;
        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            tableId = tradeVo.getTradeTableList().get(0).getTableId();
            table = findTablesById(tableId);
        }

        if (table != null) {
            dinnerTable.setTableId(tableId);
            // 桌台桌位数
            dinnerTable.setTableSeatCount(table.getTablePersonCount());
            // 桌台区域名称
            CommercialArea area = findAreaById(table.getAreaId());
            dinnerTable.setTableZoneName(area.getAreaName());
        } else {
            dinnerTable.setTableSeatCount(0);
            dinnerTable.setTableZoneName("");
        }
        // 桌台就餐总人数
        dinnerTable.setTableMealCount(tableId == null ? 0 : countTotalPersonCount(tableId));
        return dinnerTable;
    }

    @Override
    public List<TableAreaPopupVo> findAllTableAreaVo() throws Exception {
        List<TableAreaPopupVo> voList = new ArrayList<TableAreaPopupVo>();
        List<CommercialArea> areaList = listTableArea();
        if (areaList != null && areaList.size() > 0) {
            for (CommercialArea area : areaList) {
                TableAreaPopupVo vo = new TableAreaPopupVo();
                vo.setSelect(false);
                vo.setTableArea(area);
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public List<Trade> findTradesInTable(TradeTable tradeTable) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // 查询table上的三天内的trade数
            Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
            //查询桌子上所有的tradeTable
            QueryBuilder<TradeTable, String> tradeTableQb = tradeTableDao.queryBuilder();
            tradeTableQb.selectColumns(TradeTable.$.tradeId).where()
                    .eq(TradeTable.$.tableId, tradeTable.getTableId())
                    .and().eq(TradeTable.$.statusFlag, StatusFlag.VALID);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            List<Trade> trades = tradeDao.queryBuilder().where()
                    .eq(Trade.$.businessType, BusinessType.DINNER).and()
                    .eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED).and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID).and()
                    .in(Trade.$.id, tradeTableQb).and()
                    .gt(Trade.$.serverCreateTime, cad.getTime().getTime()).query();

            return trades;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Tables> findTablesByIds(List<Long> tableIds, TableStatus status) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            if (tableIds != null && !tableIds.isEmpty()) {
                Dao<Tables, Long> dao = helper.getDao(Tables.class);
                if (status != null) {
                    return dao.queryBuilder()
                            .where().in(Tables.$.id, tableIds).and().eq(Tables.$.tableStatus, status).query();
                } else
                    return dao.queryBuilder().where().in(Tables.$.id, tableIds).query();
            }

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<SnackTableVo> findSnackTableVo(boolean haveLimitServiceTime) throws Exception {
        List<SnackTableVo> groupList = new ArrayList<SnackTableVo>();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List<CommercialArea> commercialAreaList = listTableArea();
            List<Tables> allTableList =
                    helper.getDao(Tables.class)
                            .queryBuilder()
                            .orderBy(Tables.$.sort, true)
                            .orderBy(Tables.$.tableName, true)
                            .where()
                            .eq(Tables.$.statusFlag, Status.VALID)
                            .query();
            Map<Long, SnackTableVo> areaFinder = new LinkedHashMap<Long, SnackTableVo>();
            for (CommercialArea commercialArea : commercialAreaList) {
                SnackTableVo btv = new SnackTableVo();
                btv.setCommercialArea(commercialArea);
                btv.setTableTradeVo(new ArrayList<TableTradeVo>());
                areaFinder.put(commercialArea.getId(), btv);
            }
            for (Tables table : allTableList) {
                TableTradeVo tableTardeVo = new TableTradeVo();

                if (areaFinder.get(table.getAreaId()) != null) {
                    tableTardeVo.setCommercialArea(areaFinder.get(table.getAreaId()).getCommercialArea());
                }

                if (table.getTableStatus() == TableStatus.OCCUPIED && haveLimitServiceTime) {//有设置就餐时长并且桌台状为就餐中才查询订单
                    TradeTable tradeTable = helper.getDao(TradeTable.class)
                            .queryBuilder()
                            .orderBy(TradeTable.$.id, false)
                            .orderBy(TradeTable.$.serverUpdateTime, false)
                            .where()
                            .eq(TradeTable.$.statusFlag, StatusFlag.VALID)
                            .and()
                            .eq(TradeTable.$.tableId, table.getId())
                            .queryForFirst();

                    tableTardeVo.setTradeTable(tradeTable);

                    if (tradeTable != null) {
                        Trade trade = helper.getDao(Trade.class)
                                .queryBuilder()
                                .where()
                                .eq(Trade.$.id, tradeTable.getTradeId())
                                .queryForFirst();
                        tableTardeVo.setTrade(trade);

                        if (trade != null) {
                            // 交易扩展
                            TradeExtra tradeExtra =
                                    helper.getDao(TradeExtra.class)
                                            .queryBuilder()
                                            .where()
                                            .eq(TradeExtra.$.tradeUuid, trade.getUuid())
                                            .queryForFirst();
                            tableTardeVo.setTradeExtra(tradeExtra);

                            // 就餐时长
                            if (trade.getUuid() != null) {
                                List<TradeStatusLog> tradeStatusLogList = new ArrayList<TradeStatusLog>();

                                //开始时间(TradeStatus = 4  && tradePayStatus = 3 order by serverCreateTime )的第一条数据表示开始时间
                                TradeStatusLog startTradeStatusLog = helper.getDao(TradeStatusLog.class)
                                        .queryBuilder()
                                        .orderBy(TradeStatusLog.$.serverCreateTime, true)
                                        .where()
                                        .eq(TradeStatusLog.$.tradeUuid, trade.getUuid())
                                        .and()
                                        .eq(TradeStatusLog.$.tradeStatus, TradeStatus.FINISH)
                                        .and()
                                        .eq(TradeStatusLog.$.tradePayStatus, TradePayStatus.PAID)
                                        .queryForFirst();
                                if (startTradeStatusLog != null) {
                                    tradeStatusLogList.add(startTradeStatusLog);
                                }

                                //结束时间(tradeStatus -1表示就餐结束，方案很不好，求后面优化)
                                TradeStatusLog endTradeStatusLog = helper.getDao(TradeStatusLog.class)
                                        .queryBuilder()
                                        .where()
                                        .eq(TradeStatusLog.$.tradeUuid, trade.getUuid())
                                        .and()
                                        .eq(TradeStatusLog.$.tradeStatus, -1)
                                        .queryForFirst();
                                if (endTradeStatusLog != null) {
                                    tradeStatusLogList.add(endTradeStatusLog);
                                }

                                tableTardeVo.setTradeStatusLogs(tradeStatusLogList);
                            }
                        }

                    }
                }

                tableTardeVo.setTable(table);
                SnackTableVo buffteTableVo = areaFinder.get(table.getAreaId());
                if (buffteTableVo != null) {
                    buffteTableVo.getTableTradeVo().add(tableTardeVo);
                }
            }
            groupList.addAll(areaFinder.values());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return groupList;
    }

    @Override
    public List<Tables> listTables(TableStatus tableStatus) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> tableDao = helper.getDao(Tables.class);
            QueryBuilder<Tables, Long> tableQb = tableDao.queryBuilder();
            tableQb.where().eq(Tables.$.tableStatus, tableStatus);
            return tableQb.orderBy(Tables.$.sort, true).orderBy(Tables.$.id, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Tables> listValidTables() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> tableDao = helper.getDao(Tables.class);
            Dao<TablePosition, Long> positionDao = helper.getDao(TablePosition.class);
            QueryBuilder<Tables, Long> tableQb = tableDao.queryBuilder();
            QueryBuilder<TablePosition, Long> positionQb = positionDao.queryBuilder();
            List<TablePosition> tablePositions = positionQb.selectColumns(TablePosition.$.tableId).where().eq(TablePosition.$.isDelete, IsDelete.VALID).query();
            List<Long> tablesId = new ArrayList<>();
            if (Utils.isNotEmpty(tablePositions)) {
                for (TablePosition tablePosition : tablePositions) {
                    tablesId.add(tablePosition.getTableId());
                }
            }
            tableQb.where()
                    .eq(Tables.$.statusFlag, Status.VALID)
                    .and()
                    .in(Tables.$.id, tablesId);
            return tableQb.orderBy(Tables.$.sort, true).orderBy(Tables.$.id, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeTable> listTradeTable(List<Long> tradeIdList) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
            QueryBuilder<TradeTable, String> tradeTableQb = tradeTableDao.queryBuilder();
            List<TradeTable> tradeTables = tradeTableQb.where().eq(TradeTable.$.selfTableStatus, TableStatus.OCCUPIED).and().in(TradeTable.$.tradeId,
                    tradeIdList).query();
            return tradeTables;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long countOfTablesByPeopleCount(int minPeopleCount, int maxPeopleCount) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Tables, Long> tableDao = helper.getDao(Tables.class);
            return tableDao.queryBuilder().where().between(Tables.$.tablePersonCount, minPeopleCount, maxPeopleCount).countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}

