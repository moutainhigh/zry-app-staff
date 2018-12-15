package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.List;
import java.util.concurrent.Callable;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.pay.operates.PaymentItemDal;
import com.zhongmei.bty.basemodule.pay.bean.PaymentItemUnionpayVo;

/**
 * @Date：2016-2-17 下午6:13:22
 * @Description: 银联刷卡记录表操作接口
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class PaymentItemDalImpl extends AbstractOpeartesImpl implements PaymentItemDal {
    private static final String TAG = PaymentItemDalImpl.class.getSimpleName();

    public PaymentItemDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public PaymentItemUnionpay findByPaymentItemId(Long paymentItemId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentItemUnionpay, Long> dao = helper.getDao(PaymentItemUnionpay.class);
            QueryBuilder<PaymentItemUnionpay, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemUnionpay.$.paymentItemId, paymentItemId)
                    .and()
                    .eq(PaymentItemUnionpay.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemUnionpay.$.id, false);
            return qb.queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, "findByPaymentItemId error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public PaymentItemUnionpay findByPaymentItemUuid(String paymentItemUuid) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentItemUnionpay, Long> dao = helper.getDao(PaymentItemUnionpay.class);
            QueryBuilder<PaymentItemUnionpay, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemUnionpay.$.paymentItemUuid, paymentItemUuid)
                    .and()
                    .eq(PaymentItemUnionpay.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemUnionpay.$.id, false);
            return qb.queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, "findByPaymentItemUuid error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public void insert(final PaymentItemUnionpay paymentItemUnionpay) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Dao<PaymentItemUnionpay, String> dao = helper.getDao(PaymentItemUnionpay.class);
                    dao.create(paymentItemUnionpay);
                    return null;
                }
            };
            helper.callInTransaction(callable);
        } catch (Exception e) {
            Log.e(TAG, "insert error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public PaymentItemUnionpayVo findPaymentItemUnionpayVoByPaymentItemId(Long paymentItemId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        PaymentItemUnionpayVo vo = null;
        try {
            Dao<PaymentItemUnionpay, Long> dao = helper.getDao(PaymentItemUnionpay.class);
            QueryBuilder<PaymentItemUnionpay, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemUnionpay.$.paymentItemId, paymentItemId)
                    .and()
                    .eq(PaymentItemUnionpay.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemUnionpay.$.id, false);
            PaymentItemUnionpay paymentItemUnionpay = qb.queryForFirst();

            if (paymentItemUnionpay != null) {
                Dao<PaymentDevice, Long> pDdao = helper.getDao(PaymentDevice.class);
                QueryBuilder<PaymentDevice, Long> pDqb = pDdao.queryBuilder();
                pDqb.where()
                        .eq(PaymentDevice.$.id, paymentItemUnionpay.getPaymentDeviceId())
                        .and()
                        .eq(PaymentDevice.$.statusFlag, StatusFlag.VALID);
                pDqb.orderBy(PaymentDevice.$.id, false);

                PaymentDevice paymentDevice = pDqb.queryForFirst();
                vo = new PaymentItemUnionpayVo();
                vo.setPaymentItemUnionpay(paymentItemUnionpay);
                vo.setPaymentDevice(paymentDevice);
            }
        } catch (Exception e) {
            Log.e(TAG, "findPaymentItemUnionpayVoByPaymentItemId error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return vo;
    }

    @Override
    public PaymentItemUnionpayVo findPaymentItemUnionpayVoByPaymentItemUuid(String paymentItemUuid) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        PaymentItemUnionpayVo vo = null;
        try {
            Dao<PaymentItemUnionpay, Long> dao = helper.getDao(PaymentItemUnionpay.class);
            QueryBuilder<PaymentItemUnionpay, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemUnionpay.$.paymentItemUuid, paymentItemUuid)
                    .and()
                    .eq(PaymentItemUnionpay.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemUnionpay.$.id, false);
            PaymentItemUnionpay paymentItemUnionpay = qb.queryForFirst();

            if (paymentItemUnionpay != null) {
                Dao<PaymentDevice, Long> pDdao = helper.getDao(PaymentDevice.class);
                QueryBuilder<PaymentDevice, Long> pDqb = pDdao.queryBuilder();
                pDqb.where()
                        .eq(PaymentDevice.$.id, paymentItemUnionpay.getPaymentDeviceId())
                        .and()
                        .eq(PaymentDevice.$.statusFlag, StatusFlag.VALID);
                pDqb.orderBy(PaymentDevice.$.id, false);

                PaymentDevice paymentDevice = pDqb.queryForFirst();
                vo = new PaymentItemUnionpayVo();
                vo.setPaymentItemUnionpay(paymentItemUnionpay);
                vo.setPaymentDevice(paymentDevice);
            }
        } catch (Exception e) {
            Log.e(TAG, "findPaymentItemUnionpayVoByPaymentItemUuid error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return vo;
    }

    @Override
    public PaymentItemExtra findExtraByPaymentItemId(Long paymentItemId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentItemExtra, Long> dao = helper.getDao(PaymentItemExtra.class);
            QueryBuilder<PaymentItemExtra, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemExtra.$.paymentItemId, paymentItemId)
                    .and()
                    .eq(PaymentItemExtra.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemExtra.$.id, false);
            return qb.queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, "findPaymentItemExtra error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<PaymentItemGrouponDish> findPaymentItemGroupDishsByTradeId(Long tradeId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentItemGrouponDish, Long> dao = helper.getDao(PaymentItemGrouponDish.class);
            QueryBuilder<PaymentItemGrouponDish, Long> qb = dao.queryBuilder();
            qb.where()
                    .eq(PaymentItemGrouponDish.$.tradeId, tradeId)
                    .and()
                    .eq(PaymentItemGrouponDish.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(PaymentItemExtra.$.id, false);
            return qb.query();
        } catch (Exception e) {
            Log.e(TAG, "PaymentItemGrouponDish error!", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }
}
