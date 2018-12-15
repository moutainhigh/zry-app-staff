package com.zhongmei.bty.basemodule.session.support;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */

public class TradeDeleter {

    private static final String TAG = TradeDeleter.class.getSimpleName() + "--->";
    private static final String KEY_LAST_CLEAR_TIME = "last_clear_time";
    private static final long MAX_EXPIRED_TIME = 1000 * 60 * 60 * 20;
    private static boolean flag = false;

    public static synchronized void deleteIfNecessary() {
        long lastClearTime = SharedPreferenceUtil.getSpUtil().getLong(KEY_LAST_CLEAR_TIME, 0L);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClearTime >= MAX_EXPIRED_TIME && !flag) {
            SharedPreferenceUtil.getSpUtil().putLong(KEY_LAST_CLEAR_TIME, currentTime);
            new Thread(new DeleterRunnable()).start();
        }
    }

    private static class DeleterRunnable implements Runnable {

        @Override
        public void run() {
            if (flag) {
                return;
            }
            flag = true;
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                doClear(helper);
            } catch (Exception e) {
                Log.e(TAG, "Clear expired data error!", e);
            } finally {
                flag = false;
                DBHelperManager.releaseHelper(helper);
            }
        }

        private void doClear(final DatabaseHelper helper) throws Exception {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Log.i(TAG, "Clear expired data...");
                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                    // 保留最近7天的记录，7天前未结账的正餐单据也保留
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
                    calendar.add(Calendar.DAY_OF_MONTH, -6);
                    long minBizDate = calendar.getTimeInMillis();
                    Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                    QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
                    qb.selectColumns(Trade.$.uuid, Trade.$.businessType, Trade.$.tradeStatus);
                    qb.where().lt(Trade.$.bizDate, minBizDate);
                    List<Trade> tradeList = qb.query();
                    for (Trade trade : tradeList) {
                        if (trade.getBusinessType() == BusinessType.DINNER) {
                            tradeDal.delete(trade.getUuid());
                        } else if (trade.getTradeStatus() != TradeStatus.CONFIRMED
                                && trade.getTradeStatus() != TradeStatus.UNPROCESSED) {
                            tradeDal.delete(trade.getUuid());
                        }
                    }
                    return null;
                }
            };
            helper.callInTransaction(callable);
        }
    }

    private Long lastClearTime = null;

    /**
     * 清除7天之前的单据记录
     */
    private void clearExpired() {
        // 最后一次清除的时间是在20个小时之前，就执行一次清除
        long currentTime = System.currentTimeMillis();
        if (lastClearTime != null && currentTime - lastClearTime < 20 * 3600 * 1000) {
            return;
        }
        lastClearTime = currentTime;
        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }


}
