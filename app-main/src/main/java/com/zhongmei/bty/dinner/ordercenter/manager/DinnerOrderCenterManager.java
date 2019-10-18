package com.zhongmei.bty.dinner.ordercenter.manager;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.dinner.ordercenter.bean.BillCenterOrderType;
import com.zhongmei.bty.dinner.ordercenter.view.SearchTypePopwindow;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DinnerOrderCenterManager {

    private static final String TAG = DinnerOrderCenterManager.class.getSimpleName();

    public static final long PAGE_SIZE = 20L;

    private IDataChangedListener mListener;

    private OrderCenterChangeObserver mDataObserver;

    private int mCurrentChildTab;

    private AsyncTask asyncTask;

    public DinnerOrderCenterManager() {
    }

    public DinnerOrderCenterManager(IDataChangedListener listener) {
        mListener = listener;
    }

    public void colse() {
        unregisterObserver();
        if (mListener != null) {
            mListener = null;
        }
    }

    public void registerObserver(int currentChildTab) {
        mCurrentChildTab = currentChildTab;
        if (mDataObserver == null) {
            mDataObserver = new OrderCenterChangeObserver();
        }
        DatabaseHelper.Registry.register(mDataObserver);
    }

    public void unregisterObserver() {
        if (mDataObserver != null) {
            DatabaseHelper.Registry.unregister(mDataObserver);
            mDataObserver = null;
        }
    }

    public void search(final int currentChildTab, final String keyword, final byte searchType) {
        new AsyncTask<Void, Void, List<TradePaymentVo>>() {

            @Override
            protected List<TradePaymentVo> doInBackground(Void... params) {
                List<Trade> tradeList = queryTrade(currentChildTab, keyword, searchType);
                return queryTradePaymentVo1(tradeList);
            }

            protected void onPostExecute(List<TradePaymentVo> result) {
                if (mListener != null) {
                    mListener.onSearchFinish(result);
                }
            }

            ;

        }.execute();
    }

    public void switchTab(final int currentChildTab, final Trade trade) {
        mCurrentChildTab = currentChildTab;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        asyncTask = new AsyncTask<Void, Void, List<TradePaymentVo>>() {

            protected void onPreExecute() {
                if (trade == null && mListener != null) {
                    mListener.onBegin();
                }

            }


            @Override
            protected List<TradePaymentVo> doInBackground(Void... params) {
                if (currentChildTab == BillCenterOrderType.ADJUST) {
                    return queryAdjustTradePaymentVo();
                } else if (currentChildTab == BillCenterOrderType.ONLINEPAY_PAY                        || currentChildTab == BillCenterOrderType.ONLINEPAY_REFUND) {
                    List<Trade> tradeList = queryTradByOnline(currentChildTab, trade);
                    return queryTradePaymentVo1(tradeList);
                } else {
                    List<Trade> tradeList = queryTrade(currentChildTab, trade);
                    return queryTradePaymentVo1(tradeList);
                }
            }

            protected void onPostExecute(List<TradePaymentVo> result) {
                if (mListener != null) {
                    if (trade != null) {
                        mListener.onLoadFinish(result);
                    } else {
                        mListener.onDataChanged(result);
                    }
                }
            }

            ;

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressWarnings("unchecked")
    private Where<Trade, String> createWhere(QueryBuilder<Trade, String> tradeQB,
                                             int currentChildTab) throws Exception {
                Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        Where<Trade, String> tradeWhere = tradeQB.where();
        switch (currentChildTab) {
            case BillCenterOrderType.NETWORKORDER_UNHANDLE:
                tradeWhere
                        .or(tradeWhere.eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                                tradeWhere.ne(Trade.$.tradePayForm, TradePayForm.ONLINE))
                        .and()
                        .eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                        .and()
                        .ne(Trade.$.sourceId, SourceId.POS);
                break;
            case BillCenterOrderType.NETWORKORDER_REFUSED:
                tradeWhere.eq(Trade.$.tradeStatus, TradeStatus.REFUSED).and().ne(Trade.$.sourceId, SourceId.POS);
                break;
            case BillCenterOrderType.SALESORDER_NOTPAY:
                tradeWhere.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT).and().eq(Trade.$.tradePayStatus,
                        TradePayStatus.UNPAID);
                break;
            case BillCenterOrderType.SALESORDER_PAYED:

                tradeWhere.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT)
                        .and()
                        .in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID);

                break;
            case BillCenterOrderType.SALESORDER_REFUNDED:
                tradeWhere.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT)
                        .and()
                        .eq(Trade.$.tradePayStatus, TradePayStatus.REFUNDED);
                break;
            case BillCenterOrderType.SALESORDER_INVALID:
                tradeWhere.eq(Trade.$.tradeStatus, TradeStatus.INVALID);
                break;
            case BillCenterOrderType.RETURNGOODS_TICKET:
                tradeWhere.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT)
                        .and()
                        .ne(Trade.$.relateTradeUuid, "");
                break;
            case BillCenterOrderType.RETURNGOODS_NOTTICKET:
                tradeWhere.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT)
                        .and()
                        .eq(Trade.$.relateTradeUuid, "");
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument currentChildTab");
        }
        tradeWhere.and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .eq(Trade.$.businessType, BusinessType.DINNER)
                .and()
                .gt(Trade.$.serverCreateTime, calendar.getTime().getTime());

        return tradeWhere;
    }

    public List<Trade> queryTrade(int currentChildTab, String keyword, byte searchType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");

            Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
            QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
            tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
            tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");

            Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
            QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
            tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
            tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");

            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            Where<Trade, String> tradeWhere = createWhere(tradeQB, currentChildTab);

            Where<Trade, String> tradeWhere1 = null;
            if (searchType == SearchTypePopwindow.ALL) {
                tradeWhere1 = tradeWhere.or(tradeWhere.like(Trade.$.tradeNo, "%" + keyword + "%"),
                        tradeWhere.in(Trade.$.uuid, tradeExtraQB),
                        tradeWhere.in(Trade.$.uuid, tradeTableQB),
                        tradeWhere.in(Trade.$.uuid, tradeCustomerQB));

            } else if (searchType == SearchTypePopwindow.TRADE_NUMBER) {
                tradeWhere1 = tradeWhere.like(Trade.$.tradeNo, "%" + keyword + "%");
            } else if (searchType == SearchTypePopwindow.SERAIL_NUMBER) {
                tradeWhere1 = tradeWhere.in(Trade.$.uuid, tradeExtraQB);
            } else if (searchType == SearchTypePopwindow.TABLE_NUMBER) {
                tradeWhere1 = tradeWhere.in(Trade.$.uuid, tradeTableQB);
            } else if (searchType == SearchTypePopwindow.PHONE_NUMBER) {
                tradeWhere1 = tradeWhere.in(Trade.$.uuid, tradeCustomerQB);
            }

            tradeWhere.and(tradeWhere, tradeWhere1);
            Log.e(TAG, tradeWhere.getStatement());

            tradeQB.orderBy(Trade.$.serverUpdateTime, false);
            return tradeQB.query();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    public List<Trade> queryTrade(int currentChildTab, Trade trade) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            Where<Trade, String> tradeWhere = createWhere(tradeQB, currentChildTab);
            if (trade != null) {
                tradeWhere.and().le(Trade.$.serverUpdateTime, trade.getServerUpdateTime()).and().ne(Trade.$.id,
                        trade.getId());
            }
            tradeQB.limit(PAGE_SIZE);
            tradeQB.orderBy(Trade.$.serverUpdateTime, false);
            return tradeQB.query();
        } catch (Exception exception) {
            Log.e(TAG, "", exception);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }


    public List<Trade> queryTradByOnline(int type, Trade trade) {
                DatabaseHelper helper = DBHelperManager.getHelper();
        try {

                        Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            calendar.add(Calendar.DAY_OF_MONTH, -7);

                        Dao<PaymentItem, String> paymentItemDao = helper.getDao(PaymentItem.class);
            QueryBuilder<PaymentItem, String> paymentItemBuilder = paymentItemDao.queryBuilder();
            Where<PaymentItem, String> paymentItemWhere =
                    paymentItemBuilder.distinct().selectColumns(PaymentItem.$.paymentUuid).where();
            paymentItemWhere.in(PaymentItem.$.payModeId,
                    PayModeId.WEIXIN_PAY.value(),
                    PayModeId.ALIPAY.value(),
                    PayModeId.BAIFUBAO.value(), PayModeId.DIANPING_FASTPAY.value());
            if (type == BillCenterOrderType.ONLINEPAY_PAY) {
                paymentItemWhere.and().in(PaymentItem.$.payStatus,
                        TradePayStatus.UNPAID.value(),
                        TradePayStatus.PAID.value(),
                        TradePayStatus.PREPAID.value(),
                        TradePayStatus.PAID_FAIL.value());
            } else if (type == BillCenterOrderType.ONLINEPAY_REFUND) {
                paymentItemWhere.and().in(PaymentItem.$.payStatus,
                        TradePayStatus.REFUNDING.value(),
                        TradePayStatus.REFUNDED.value(),
                        TradePayStatus.REFUND_FAILED.value(),
                        TradePayStatus.WAITING_REFUND.value());
            }


                        Dao<Payment, String> paymentDao = helper.getDao(Payment.class);
            QueryBuilder<Payment, String> paymentQueryBuilder = paymentDao.queryBuilder();
            Where<Payment, String> paymentWhere = paymentQueryBuilder.where();
            paymentWhere.in(Payment.$.uuid, paymentItemBuilder);
            List<Payment> payments = paymentQueryBuilder.query();

                        List<String> relateuuid = new ArrayList<String>();
            for (Payment payment : payments) {
                relateuuid.add(payment.getRelateUuid());
            }
                        Dao<Payment, String> allPaymentDao = helper.getDao(Payment.class);
            QueryBuilder<Payment, String> allPaymentQueryBuilder = allPaymentDao.queryBuilder();
            Where<Payment, String> allpaymentWhere = allPaymentQueryBuilder.where();
            allpaymentWhere.in(Payment.$.relateUuid, relateuuid.toArray());
            List<Payment> allpayments = allPaymentQueryBuilder.query();

                        List<Payment> havePayment = new ArrayList<Payment>();

            List<Payment> nothavePayment = new ArrayList<Payment>();

            for (int i = 0; i < allpayments.size(); i++) {                if (allpayments.get(i).getIsPaid() == Bool.YES) {
                    havePayment.add(allpayments.get(i));

                } else {
                    nothavePayment.add(allpayments.get(i));
                }
            }

                        List<String> deleteuuid = new ArrayList<String>();

            Dao<PaymentItem, String> havepaymentItemDao = helper.getDao(PaymentItem.class);
            Iterator<Payment> iterator = havePayment.iterator();
            while (iterator.hasNext()) {
                Payment p = iterator.next();
                if (isContainNotOnlinePay(havepaymentItemDao.queryForEq(PaymentItem.$.paymentUuid, p.getUuid()))) {
                    iterator.remove();
                    deleteuuid.add(p.getRelateUuid());
                }
            }
                        Iterator<Payment> iterator2 = nothavePayment.iterator();
            while (iterator2.hasNext()) {
                Payment p = iterator2.next();
                if (isContainRelatUUid(p, deleteuuid)) {                    iterator2.remove();
                }

            }

                        List<Payment> filteredPayment = new ArrayList<Payment>();
            filteredPayment.addAll(havePayment);
            filteredPayment.addAll(nothavePayment);

            List<String> traduuid = new ArrayList<String>();
            for (Payment filtpayment : filteredPayment) {
                traduuid.add(filtpayment.getRelateUuid());
            }

                        Dao<Trade, String> tradDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQueryBuilder = tradDao.queryBuilder();
            Where<Trade, String> tradWhere = tradeQueryBuilder.where();
            tradWhere.in(Trade.$.uuid, traduuid.toArray());
            if (type == BillCenterOrderType.ONLINEPAY_PAY) {
                tradWhere.and()
                        .in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT)
                        .and()
                        .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.FINISH);
            } else if (type == BillCenterOrderType.ONLINEPAY_REFUND) {
                tradWhere.and().in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT);
            }


            tradWhere.and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(Trade.$.businessType, BusinessType.DINNER)
                    .and()
                    .gt(Trade.$.serverCreateTime, calendar.getTime().getTime());

            if (trade != null) {
                tradWhere.and().le(Trade.$.serverUpdateTime, trade.getServerUpdateTime()).and().ne(Trade.$.id,
                        trade.getId());
            }
            tradeQueryBuilder.limit(PAGE_SIZE);
            tradeQueryBuilder.orderBy(Trade.$.serverUpdateTime, false);

                        return tradeQueryBuilder.query();
        } catch (Exception exception) {
            Log.e(TAG, "", exception);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    private boolean isContainRelatUUid(Payment payment, List<String> relateuuids) {        for (String uuid : relateuuids) {
            if (payment.getRelateUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private boolean isContainNotOnlinePay(List<PaymentItem> items) {
        for (PaymentItem paymentItem : items) {
            if (paymentItem.getPayModeId() != PayModeId.WEIXIN_PAY.value()
                    && paymentItem.getPayModeId() != PayModeId.ALIPAY.value()
                    && paymentItem.getPayModeId() != PayModeId.BAIFUBAO.value() && paymentItem.getPayModeId() != PayModeId.DIANPING_FASTPAY.value()
                    && paymentItem.getPayStatus() == TradePayStatus.PAID) {
                return true;
            }

        }
        return false;
    }

    private boolean contains(List<Payment> temp, Payment payment) {
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).getUuid().equals(payment.getUuid());
            return true;
        }
        return false;
    }

    public List<TradePaymentVo> queryTradePaymentVo1(List<Trade> tradeList) {
        long start = System.currentTimeMillis();
        if (Utils.isNotEmpty(tradeList)) {
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                List<TradePaymentVo> tradePaymentVos = new ArrayList<TradePaymentVo>();
                List<String> tradeUuidList = new ArrayList<String>();
                for (Trade trade : tradeList) {
                    tradeUuidList.add(trade.getUuid());
                }
                                List<TradeExtra> tradeExtraList = helper.getDao(TradeExtra.class)
                        .queryBuilder()
                        .distinct()
                        .where()
                        .in(TradeExtra.$.tradeUuid, tradeUuidList.toArray())
                        .query();

                Map<String, List<TradeExtra>> tradeExtraMap = new HashMap<String, List<TradeExtra>>();
                for (TradeExtra tradeExtra : tradeExtraList) {
                    if (tradeExtraMap.get(tradeExtra.getTradeUuid()) != null) {
                        tradeExtraMap.get(tradeExtra.getTradeUuid()).add(tradeExtra);
                    } else {
                        List<TradeExtra> list = new ArrayList<TradeExtra>();
                        list.add(tradeExtra);
                        tradeExtraMap.put(tradeExtra.getTradeUuid(), list);
                    }

                }

                                ArrayList<TradeItemVo> tiVoList = new ArrayList<TradeItemVo>();
                Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
                QueryBuilder<TradeItem, String> tiQb = tiDao.queryBuilder();
                Where<TradeItem, String> tiWhere = tiQb.where();
                tiWhere.in(TradeItem.$.tradeUuid, tradeUuidList.toArray());
                tiWhere.and().eq(TradeItem.$.statusFlag, StatusFlag.VALID);
                tiQb.orderBy(TradeItem.$.sort, true);
                List<TradeItem> tis = tiQb.query();

                Map<String, List<TradeItem>> tradeItemMap = new HashMap<String, List<TradeItem>>();
                for (TradeItem tradeItem : tis) {
                    if (tradeItemMap.get(tradeItem.getTradeUuid()) != null) {
                        tradeItemMap.get(tradeItem.getTradeUuid()).add(tradeItem);
                    } else {
                        List<TradeItem> list = new ArrayList<TradeItem>();
                        list.add(tradeItem);
                        tradeItemMap.put(tradeItem.getTradeUuid(), list);
                    }

                }

                                Dao<TradeTable, String> tabledao = helper.getDao(TradeTable.class);
                QueryBuilder<TradeTable, String> tradetablequery = tabledao.queryBuilder();
                Where<TradeTable, String> tableWhere = tradetablequery.where();
                tableWhere.in(TradeTable.$.tradeUuid, tradeUuidList.toArray());
                List<TradeTable> tradeTableList = tableWhere.query();

                Map<String, List<TradeTable>> tradeTableMap = new HashMap<String, List<TradeTable>>();
                for (TradeTable tradeTable : tradeTableList) {
                    if (tradeTableMap.get(tradeTable.getTradeUuid()) != null) {
                        tradeTableMap.get(tradeTable.getTradeUuid()).add(tradeTable);
                    } else {
                        List<TradeTable> list = new ArrayList<TradeTable>();
                        list.add(tradeTable);
                        tradeTableMap.put(tradeTable.getTradeUuid(), list);
                    }

                }

                                Dao<Payment, String> dao = helper.getDao(Payment.class);
                List<Payment> paymentList = dao.queryBuilder()
                        .where()
                        .in(Payment.$.relateUuid, tradeUuidList.toArray())
                        .and()
                        .eq(Payment.$.isPaid, Bool.YES)
                        .query();

                Map<String, List<Payment>> paymentMap = new HashMap<String, List<Payment>>();
                for (Payment payment : paymentList) {
                    if (paymentMap.get(payment.getRelateUuid()) != null) {
                        paymentMap.get(payment.getRelateUuid()).add(payment);
                    } else {
                        List<Payment> list = new ArrayList<Payment>();
                        list.add(payment);
                        paymentMap.put(payment.getRelateUuid(), list);
                    }
                }

                                List<String> paymentuuidList = new ArrayList<String>();
                for (Payment payment : paymentList) {
                    paymentuuidList.add(payment.getUuid());
                }
                Dao<PaymentItem, String> itemDao = helper.getDao(PaymentItem.class);
                QueryBuilder<PaymentItem, String> paymentItemquery = itemDao.queryBuilder();
                Where<PaymentItem, String> paymentitemwhere = paymentItemquery.where();
                paymentitemwhere.in(PaymentItem.$.paymentUuid, paymentuuidList.toArray());
                List<PaymentItem> paymentItems = paymentitemwhere.query();

                Map<String, List<PaymentItem>> paymentItemMap = new HashMap<String, List<PaymentItem>>();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItemMap.get(paymentItem.getPaymentUuid()) != null) {
                        paymentItemMap.get(paymentItem.getPaymentUuid()).add(paymentItem);
                    } else {
                        List<PaymentItem> list = new ArrayList<PaymentItem>();
                        list.add(paymentItem);
                        paymentItemMap.put(paymentItem.getPaymentUuid(), list);
                    }
                }

                for (Trade trade : tradeList) {
                    TradeVo tradeVo = new TradeVo();

                                        tradeVo.setTrade(trade);
                    if (tradeExtraMap.get(trade.getUuid()) != null && tradeExtraMap.get(trade.getUuid()).size() >= 1) {
                        tradeVo.setTradeExtra(tradeExtraMap.get(trade.getUuid()).get(0));
                    }

                                        List<TradeItem> tradeItems = tradeItemMap.get(trade.getUuid());
                    ArrayList<TradeItemVo> traditemvoList = new ArrayList<TradeItemVo>();
                    if (tradeItems != null) {
                        for (TradeItem tradeItem : tradeItems) {
                            TradeItemVo tiVo = new TradeItemVo();
                            tiVo.setTradeItem(tradeItem);
                            traditemvoList.add(tiVo);
                        }
                    }
                    tradeVo.setTradeItemList(traditemvoList);

                                        List<TradeTable> tradeTables = tradeTableMap.get(trade.getUuid());
                    tradeVo.setTradeTableList(tradeTables);

                                        List<Payment> payments = paymentMap.get(trade.getUuid());
                    List<PaymentVo> voList = new ArrayList<PaymentVo>();
                    if (payments != null) {
                        for (Payment payment : payments) {
                            PaymentVo vo = new PaymentVo();
                            vo.setPayment(payment);
                            vo.setPaymentItemList(paymentItemMap.get(payment.getUuid()));
                            voList.add(vo);

                        }
                    }

                    TradePaymentVo tradePaymentVo = new TradePaymentVo();
                    tradePaymentVo.setTradeVo(tradeVo);
                    tradePaymentVo.setPaymentVoList(voList);
                    tradePaymentVos.add(tradePaymentVo);

                }
                long end = System.currentTimeMillis();
                Log.d(TAG, TAG + "time------queryTradePaymentVo1 end:" + (end - start));
                return tradePaymentVos;
            } catch (Exception e) {
                Log.e(TAG, "", e);
            } finally {
                DBHelperManager.releaseHelper(helper);
            }
        }

        return new ArrayList<TradePaymentVo>();
    }

    public List<TradePaymentVo> queryTradePaymentVo(List<Trade> tradeList) {
        if (Utils.isNotEmpty(tradeList)) {
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                List<TradePaymentVo> tradePaymentVos = new ArrayList<TradePaymentVo>();
                for (Trade trade : tradeList) {
                    TradeVo tradeVo = new TradeVo();
                    tradeVo.setTrade(trade);
                                        TradeExtra tradeExtra = helper.getDao(TradeExtra.class)
                            .queryBuilder()
                            .where()
                            .eq(TradeExtra.$.tradeUuid, trade.getUuid())
                            .queryForFirst();
                    tradeVo.setTradeExtra(tradeExtra);
                                                            ArrayList<TradeItemVo> tiVoList = new ArrayList<TradeItemVo>();
                    Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
                    QueryBuilder<TradeItem, String> tiQb = tiDao.queryBuilder();
                    Where<TradeItem, String> tiWhere = tiQb.where();
                    tiWhere.eq(TradeItem.$.tradeUuid, trade.getUuid());
                    tiWhere.and().eq(TradeItem.$.statusFlag, StatusFlag.VALID);
                    tiQb.orderBy(TradeItem.$.sort, true);
                    List<TradeItem> tis = tiQb.query();
                    for (TradeItem tradeItem : tis) {
                        TradeItemVo tiVo = new TradeItemVo();
                        tiVo.setTradeItem(tradeItem);
                        tiVoList.add(tiVo);
                    }
                    tradeVo.setTradeItemList(tiVoList);
                    List<TradeTable> tradeTableList =
                            helper.getDao(TradeTable.class).queryForEq(TradeTable.$.tradeUuid, trade.getUuid());
                    tradeVo.setTradeTableList(tradeTableList);
                                        Dao<Payment, String> dao = helper.getDao(Payment.class);
                    Dao<PaymentItem, String> itemDao = helper.getDao(PaymentItem.class);
                    List<PaymentVo> voList = new ArrayList<PaymentVo>();
                    List<Payment> paymentList = dao.queryBuilder()
                            .where()
                            .eq(Payment.$.relateUuid, trade.getUuid())
                            .and()
                            .eq(Payment.$.isPaid, Bool.YES)
                            .query();
                    for (Payment payment : paymentList) {
                        PaymentVo vo = new PaymentVo();
                        vo.setPayment(payment);
                        vo.setPaymentItemList(itemDao.queryForEq(PaymentItem.$.paymentUuid, payment.getUuid()));
                        voList.add(vo);
                    }

                    TradePaymentVo tradePaymentVo = new TradePaymentVo();
                    tradePaymentVo.setTradeVo(tradeVo);
                    tradePaymentVo.setPaymentVoList(voList);
                    tradePaymentVos.add(tradePaymentVo);
                }
                return tradePaymentVos;
            } catch (Exception e) {
                Log.e(TAG, "", e);
            } finally {
                DBHelperManager.releaseHelper(helper);
            }
        }

        return new ArrayList<TradePaymentVo>();
    }

    public List<TradePaymentVo> queryAdjustTradePaymentVo() {
        List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                        paymentVos = tradeDal.listAdjustPayment();        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        List<TradePaymentVo> tradePaymentVos = new ArrayList<TradePaymentVo>();
        if (Utils.isNotEmpty(paymentVos)) {
            for (PaymentVo paymentVo : paymentVos) {
                TradePaymentVo vo = new TradePaymentVo();
                List<PaymentVo> subPaymentVos = new ArrayList<PaymentVo>();
                subPaymentVos.add(paymentVo);
                vo.setPaymentVoList(subPaymentVos);
                tradePaymentVos.add(vo);
            }
        }

        return tradePaymentVos;
    }


    public interface IDataChangedListener {

        void onBegin();

        void onDataChanged(List<TradePaymentVo> tradePaymentVos);

        void onLoadFinish(List<TradePaymentVo> tradePaymentVos);

        void onSearchFinish(List<TradePaymentVo> tradePaymentVos);
    }

    private static final Uri URI_TRADE = DBHelperManager.getUri(Trade.class);

    private static final Uri URI_TRADE_ITEM = DBHelperManager.getUri(TradeItem.class);

    private static final Uri URI_PAYMENT = DBHelperManager.getUri(Payment.class);

    private static final Uri URI_PAYMENT_ITEM = DBHelperManager.getUri(PaymentItem.class);


    private class OrderCenterChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(URI_TRADE) || uris.contains(URI_TRADE_ITEM) || uris.contains(URI_PAYMENT)
                    || uris.contains(URI_PAYMENT_ITEM)) {
                try {
                    switchTab(mCurrentChildTab, null);
                } catch (Exception e) {
                    Log.e(TAG, "refresh error!", e);
                }
            }
        }

    }
}
