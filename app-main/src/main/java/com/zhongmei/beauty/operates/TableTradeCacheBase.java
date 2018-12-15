package com.zhongmei.beauty.operates;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.beauty.interfaces.ITableTradeRefresh;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 桌台订单缓存
 */

public abstract class TableTradeCacheBase {

    protected ITableTradeRefresh mTableTradeRefreshListener;

    public ITableTradeRefresh getmTableTradeRefreshListener() {
        return mTableTradeRefreshListener;
    }

    public void setmTableTradeRefreshListener(ITableTradeRefresh mTableTradeRefreshListener) {
        this.mTableTradeRefreshListener = mTableTradeRefreshListener;
    }

    protected DatabaseHelper getDBHelper() {
        return DBHelperManager.getHelper();
    }

    protected void realseDBhelper(DatabaseHelper helper) {
        DBHelperManager.releaseHelper(helper);
    }

    /**
     * 根据区域id查询桌台信息(桌台id,桌台状态，桌台更新时间)
     *
     * @param zoneId
     * @return
     * @throws Exception
     */
    protected List<Tables> getTablesByZoneID(DatabaseHelper dbHelper, Long zoneId) throws Exception {
        Dao<Tables, Long> tablesDao = dbHelper.getDao(Tables.class);
        QueryBuilder queryBuild = tablesDao.queryBuilder();
        queryBuild.selectColumns(Tables.$.id, Tables.$.tableStatus, Tables.$.modifyDateTime);

        Where whereTables = queryBuild.where();
        whereTables.eq(Tables.$.statusFlag, StatusFlag.VALID);

        if (zoneId != null) {
            whereTables.and().eq(Tables.$.areaId, zoneId);
        }

        return queryBuild.query();
    }


    /**
     * 根据桌台返回tradeTables(tradeTables表所有字段)
     *
     * @param dbHelper
     * @param tableIds
     * @return
     */
    protected List<TradeTable> getTradeTableByTables(DatabaseHelper dbHelper, List<Long> tableIds) throws Exception {
        Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
        return tradeTableDao.queryBuilder()
                .where()
                .eq(TradeTable.$.selfTableStatus, TableStatus.OCCUPIED)
                .and()
                .in(TradeTable.$.tableId, tableIds.toArray())
                .query();
    }


    /**
     * 根据订单id查询订单列表
     *
     * @param dbHelper
     * @param tradeIds
     * @return
     * @throws Exception
     */
    protected List<Trade> getTradeByTradeIds(DatabaseHelper dbHelper, List<Long> tradeIds) throws Exception {
        Dao<Trade, String> tradeDao = dbHelper.getDao(Trade.class);
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
                Trade.$.businessType,
                Trade.$.serialNumber
        );

        Where where = tradeBuilder.where();

        where.in(Trade.$.businessType, getBusinessType())
                .and()
                .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN)
                .and()
                .in(Trade.$.id, tradeIds.toArray());

        return tradeBuilder.query();
    }

    protected List<TradeExtra> getTradeExtraByTradeIds(DatabaseHelper dbHelper, List<Long> tradeIds) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        return tradeExtraDao.queryBuilder()
                .selectColumns(TradeExtra.$.tradeUuid,
                        TradeExtra.$.serialNumber,
                        TradeExtra.$.isPrinted,
                        TradeExtra.$.serverCreateTime,
                        TradeExtra.$.serverUpdateTime,
                        TradeExtra.$.tradeId,
                        TradeExtra.$.hasServing)
                .where()
                .in(TradeExtra.$.tradeId, tradeIds.toArray())
                .query();
    }


    public abstract List<BusinessType> getBusinessType();

    public abstract void loadTableTrades(Long zoneId);

}
