package com.zhongmei.beauty.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by demo on 2018/12/15
 */

public class BeautyDaoImpl {
    private static final String TAG = "BeautyDaoImpl";

    /**
     * 查询所有的tradeItemUser
     *
     * @param identity 根据用户类型
     * @return
     */
    public static List<TradeUser> getTradeItemListByIdentity(int identity, BusinessType businessType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List tradeIds = queryUnFinishedTrades(businessType);
            Dao<TradeUser, String> tradeItemUserrDao = helper.getDao(TradeUser.class);
            return tradeItemUserrDao.queryBuilder().where()
//                    .eq(TradeUser.$.userType,identity)
//                    .and()
                    .eq(TradeUser.$.statusFlag, StatusFlag.VALID)
                    .and().in(TradeUser.$.tradeId, tradeIds)
                    .query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<>();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    /**
     * 查询未完成的订单
     *
     * @param businessType
     * @return List<Long> tradeIds集合
     */
    private static List<Long> queryUnFinishedTrades(BusinessType businessType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, Long> tradeDao = helper.getDao(Trade.class);
            Where tradeWhere = tradeDao.queryBuilder().selectColumns(Trade.$.id).where()
                    .eq(Trade.$.businessType, businessType)
                    .and().in(Trade.$.tradeStatus, TradeStatus.UNPROCESSED, TradeStatus.CONFIRMED)
                    .and().in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT)
                    .and().eq(Trade.$.statusFlag, StatusFlag.VALID);
            List<Trade> tradeList = tradeWhere.query();
            List<Long> tradeIds = new ArrayList<>();
            if (Utils.isNotEmpty(tradeList)) {
                for (Trade trade : tradeList) {
                    tradeIds.add(trade.getId());
                }
            }
            return tradeIds;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<>();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    /**
     * 查询指定行业下的技师、顾问、店长
     *
     * @param businessType
     * @return
     */
    public static List<TradeUser> getTradeUserListNo(BusinessType businessType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List tradeIds = queryUnFinishedTrades(businessType);
            Dao<TradeUser, String> tradeUserDao = helper.getDao(TradeUser.class);
            return tradeUserDao.queryBuilder().where()
                    .eq(TradeUser.$.statusFlag, StatusFlag.VALID)
//                    .and().in(TradeUser.$.userType, TradeUserType.SHOPOWER, TradeUserType.ADVISER, TradeUserType.TECHNICIAN)
                    .and().in(TradeUser.$.tradeId, tradeIds)
                    .query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
