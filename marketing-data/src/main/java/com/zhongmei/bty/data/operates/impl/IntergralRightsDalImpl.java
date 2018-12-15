package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.bty.data.operates.IntergralRightsDal;

public class IntergralRightsDalImpl extends AbstractOpeartesImpl implements IntergralRightsDal {
    private static final String TAG = IntergralRightsDalImpl.class.getSimpleName();

    public IntergralRightsDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public CrmCustomerLevelRights findIntergralRightsByLevel(long levelId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CrmCustomerLevelRights, Long> paymentModeDao = helper.getDao(CrmCustomerLevelRights.class);
            QueryBuilder<CrmCustomerLevelRights, Long> qb = paymentModeDao.queryBuilder();
            qb.where()
                    .eq(CrmCustomerLevelRights.$.isExchangeCash, SuccessOrFaild.SUCCESS)
                    .and()
                    .eq(CrmCustomerLevelRights.$.customerLevelId, levelId);
            List<CrmCustomerLevelRights> list = qb.query();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return null;
    }

}
