package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.bty.basemodule.pay.operates.PaymentModeDal;
import com.zhongmei.yunfu.db.enums.StatusFlag;


public class PaymentModeDalImpl extends AbstractOpeartesImpl implements PaymentModeDal {
    private static final String TAG = PaymentModeDalImpl.class.getSimpleName();

    public PaymentModeDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<PaymentModeShop> findAllPaymentMode() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentModeShop, Long> paymentModeDao = helper.getDao(PaymentModeShop.class);
            QueryBuilder<PaymentModeShop, Long> qb = paymentModeDao.queryBuilder();
            qb.selectColumns(PaymentModeShop.$.erpModeId, PaymentModeShop.$.name, PaymentModeShop.$.faceValue, PaymentModeShop.$.paymentModeType, PaymentModeShop.$.isRefund, PaymentModeShop.$.sort, PaymentModeShop.$.enabledFlag, PaymentModeShop.$.shopIdenty);
            qb.orderBy(PaymentModeShop.$.serverUpdateTime, true);            qb.where().eq(PaymentModeShop.$.enabledFlag, Bool.YES);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PaymentModeScene> findAllPaymentModeScene() {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        List<PaymentModeScene> dataList = null;
        try {
            Dao<PaymentModeScene, Long> paymentModeDao = helper.getDao(PaymentModeScene.class);
            QueryBuilder<PaymentModeScene, Long> qb = paymentModeDao.queryBuilder();
            qb.selectColumns(PaymentModeScene.$.shopErpModeId, PaymentModeScene.$.sceneCode, PaymentModeScene.$.statusFlag);
            qb.where().eq(PaymentModeScene.$.statusFlag, StatusFlag.VALID);
            dataList = qb.query();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return dataList;
    }

    @Override
    public Set<Integer> findAllPaymentModeSceneCode() {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        List<PaymentModeScene> dataList = null;
        Set<Integer> sceneSet = new HashSet<Integer>();
        try {
            Dao<PaymentModeScene, Long> paymentModeDao = helper.getDao(PaymentModeScene.class);
            QueryBuilder<PaymentModeScene, Long> qb = paymentModeDao.queryBuilder();
            qb.selectColumns(PaymentModeScene.$.shopErpModeId, PaymentModeScene.$.sceneCode, PaymentModeScene.$.statusFlag);
            qb.where().eq(PaymentModeScene.$.statusFlag, StatusFlag.VALID);
            qb.groupBy(PaymentModeScene.$.sceneCode);
            dataList = qb.query();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        if (dataList != null && !dataList.isEmpty()) {
            sceneSet = new HashSet<Integer>();
            for (PaymentModeScene paymentModeScene : dataList) {
                sceneSet.add(paymentModeScene.getSceneCode());
            }
        }
        return sceneSet;
    }

    @Override
    public List<PaymentModeShop> findPaymentModeByGroup(PayModelGroup group) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentModeShop, Long> paymentModeDao = helper.getDao(PaymentModeShop.class);
            QueryBuilder<PaymentModeShop, Long> qb = paymentModeDao.queryBuilder();
            qb.where().eq(PaymentModeShop.$.enabledFlag, Bool.YES).and().eq(PaymentModeShop.$.paymentModeType, group);
            qb.orderByRaw(PaymentModeShop.$.sort);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public String findPaymentModeNameByErpModeId(Long erpModeId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentModeShop, Long> paymentModeDao = helper.getDao(PaymentModeShop.class);
            QueryBuilder<PaymentModeShop, Long> qb = paymentModeDao.queryBuilder();
            qb.where().eq(PaymentModeShop.$.enabledFlag, Bool.YES).and().eq(PaymentModeShop.$.erpModeId, erpModeId);
            qb.orderBy(PaymentModeShop.$.sort, true);

            List<PaymentModeShop> list = qb.query();

            if (list != null && !list.isEmpty()) {
                return list.get(0).getName();
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<MobilePaySetting> findAllMobilePaySettings() {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<MobilePaySetting, Long> mobilePaySettingDao = helper.getDao(MobilePaySetting.class);
            QueryBuilder<MobilePaySetting, Long> qb = mobilePaySettingDao.queryBuilder();
            qb.selectColumns(MobilePaySetting.$.enumValue, MobilePaySetting.$.payModeCode, MobilePaySetting.$.isSupportOneCodePay);
            qb.where().eq(MobilePaySetting.$.enable, 1).and().eq(MobilePaySetting.$.statusFlag, 1);
            qb.orderByRaw(MobilePaySetting.$.id);
            return qb.query();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }
}
