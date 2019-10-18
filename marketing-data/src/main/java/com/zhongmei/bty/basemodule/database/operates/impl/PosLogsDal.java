package com.zhongmei.bty.basemodule.database.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.PosSettlementLog;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;

import java.util.List;
import java.util.concurrent.Callable;



public class PosLogsDal {
    static final String TAG = PosLogsDal.class.getSimpleName();


    public PosSettlementLog storePosSettlementLog(final PosSettlementLog log) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            final Dao<PosSettlementLog, String> dao = helper.getDao(PosSettlementLog.class);

            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    dao.create(log);
                    dao.deleteBuilder().where().lt(PosSettlementLog.$.transDate, log.getTransDate());
                    return null;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Store log error!", e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return log;
    }


    public PosTransLog storePosTransLog(final PosTransLog log) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, PosTransLog.class, log);
                    return null;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Store log error!", e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return log;
    }

    public List<PosTransLog> queryAllPosTransLog() {
        List<PosTransLog> posRecords = null;

        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<PosTransLog, String> posLogDao = helper.getDao(PosTransLog.class);
            posRecords = posLogDao.queryForAll();
        } catch (Exception e) {
            Log.e(TAG, "queryAllPosTransLog  error!", e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return posRecords;
    }
}
