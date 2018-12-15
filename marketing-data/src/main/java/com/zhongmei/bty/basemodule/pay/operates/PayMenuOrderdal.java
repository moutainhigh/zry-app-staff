package com.zhongmei.bty.basemodule.pay.operates;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.local.PayMenuOrder;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */

public class PayMenuOrderdal {
    private final String TAG = "PayMenuOrderdal";

    public List<PayMenuOrder> findAllPayMenuOrder() {
        List<PayMenuOrder> list = null;
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<PayMenuOrder, Long> payMenuOrderDao = helper.getDao(PayMenuOrder.class);
            QueryBuilder<PayMenuOrder, Long> payMenuOrderQb = payMenuOrderDao.queryBuilder();
            payMenuOrderQb.orderBy(PayMenuOrder.$.order, false);
            list = payMenuOrderQb.query();

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return list;
    }

    public void saveOrder(final List<PayMenuOrder> list) {
        if (!Utils.isEmpty(list)) {
            final DatabaseHelper helper = LocalDBManager.getHelper();
            try {
                helper.callInTransaction(new Callable<Void>() {
                    @Override
                    public Void call()
                            throws Exception {
                        DBHelperManager.saveEntities(helper, PayMenuOrder.class, list);
                        return null;
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "", e);
            } finally {
                LocalDBManager.releaseHelper();
            }
        }
    }
}
