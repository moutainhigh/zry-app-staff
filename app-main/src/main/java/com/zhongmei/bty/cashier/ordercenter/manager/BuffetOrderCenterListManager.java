package com.zhongmei.bty.cashier.ordercenter.manager;

import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.changeAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.faceAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.paySource;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.payStatus;


public class BuffetOrderCenterListManager extends OrderCenterListManager {
    private static final String TAG = BuffetOrderCenterListManager.class.getSimpleName();

    @Override
    public Where<Trade, String> generateTradeBusinessTypeWhere(QueryBuilder<Trade, String> queryBuilder) throws Exception {
        return queryBuilder.where().eq(Trade.$.businessType, BusinessType.BUFFET);
    }

    @Override
    public void clearAccounts(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener) {
        mTradeOperates.clearAccounts(tradeVos, listener);
    }

    @Override
    public List<TradePaymentVo> search(int tab, int position, String keyword, FilterCondition condition, Trade lastData) {
        DatabaseHelper helper = DBHelperManager.getHelper();

        int childTab = DbQueryConstant.UNPROCESSED_ALL;
        if (tab == DbQueryConstant.SALES) {
            childTab = DbQueryConstant.SALES_ALL;
        }

        List<Trade> trades = queryTrade(helper, childTab, position, keyword, condition, lastData);
        try {
            List<TradePaymentVo> tradePaymentVos = new ArrayList<TradePaymentVo>();
            for (Trade trade : trades) {
                TradePaymentVo tradePaymentVo = new TradePaymentVo();
                TradeVo tradeVo = new TradeVo();
                tradeVo.setTrade(trade);
                tradeVo.setTradeExtra(getTradeExtra(helper, trade.getUuid()));
                tradeVo.setTradeDeposit(getTradeDeposit(helper, trade.getUuid()));
                tradeVo.setTradeTableList(getTradeTableList(helper, trade.getUuid()));
                tradeVo.setTradeCustomerList(getTradeCustomerList(helper, trade.getUuid()));
                tradePaymentVo.setTradeVo(tradeVo);
                tradePaymentVo.setPaymentVoList(listPaymentVo(helper, trade.getUuid()));
                tradePaymentVos.add(tradePaymentVo);
            }
            return tradePaymentVos;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return Collections.emptyList();
    }

    @Override
    public List<TradePaymentVo> loadData(int childTab, FilterCondition condition, Trade lastData) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<Trade> trades = queryTrade(helper, childTab, -1, null, condition, lastData);
        try {
            List<TradePaymentVo> tradePaymentVos = new ArrayList<TradePaymentVo>();
            for (Trade trade : trades) {
                List<PaymentVo> paymentVos = listPaymentVo(helper, trade.getUuid());
                if (Utils.isNotEmpty(condition.getTradeStatus())
                        && condition.getTradeStatus().contains(TradeStatus.SQUAREUP)
                        && condition.getPayModeId() == PayModeId.CASH
                        && condition.getPaySource() == PaySource.ON_MOBILE) {
                    Long lastClosingTime = SystemSettingsManager.queryLastClosingAccountRecord();
                    boolean isClosedOrder = isClosedOrder(paymentVos, lastClosingTime);
                    if (!isClosedOrder) {
                        TradePaymentVo tradePaymentVo = new TradePaymentVo();
                        TradeVo tradeVo = new TradeVo();
                        tradeVo.setTrade(trade);
                        tradeVo.setTradeExtra(getTradeExtra(helper, trade.getUuid()));
                        tradeVo.setTradeDeposit(getTradeDeposit(helper, trade.getUuid()));
                        tradeVo.setTradeTableList(getTradeTableList(helper, trade.getUuid()));
                        tradeVo.setTradeCustomerList(getTradeCustomerList(helper, trade.getUuid()));

                        tradePaymentVo.setTradeVo(tradeVo);
                        tradePaymentVo.setTradeUnionType(getTradeUnionType(helper, trade));
                        tradePaymentVo.setPaymentVoList(paymentVos);

                        tradePaymentVos.add(tradePaymentVo);
                    }
                } else {
                    TradePaymentVo tradePaymentVo = new TradePaymentVo();
                    TradeVo tradeVo = new TradeVo();
                    tradeVo.setTrade(trade);
                    tradeVo.setTradeExtra(getTradeExtra(helper, trade.getUuid()));
                    tradeVo.setTradeDeposit(getTradeDeposit(helper, trade.getUuid()));
                    tradeVo.setTradeTableList(getTradeTableList(helper, trade.getUuid()));
                    tradeVo.setTradeCustomerList(getTradeCustomerList(helper, trade.getUuid()));

                    tradePaymentVo.setTradeVo(tradeVo);
                    tradePaymentVo.setTradeUnionType(getTradeUnionType(helper, trade));
                    tradePaymentVo.setPaymentVoList(paymentVos);

                    tradePaymentVos.add(tradePaymentVo);
                }

            }
            return tradePaymentVos;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return Collections.emptyList();
    }

    protected boolean isClosedOrder(List<PaymentVo> paymentVos, long lastClosingTime) {
        long lastPayTime = System.currentTimeMillis();
        if (Utils.isNotEmpty(paymentVos)) {
            PaymentVo paymentVo = paymentVos.get(0);
            if (paymentVo != null && paymentVo.getPayment() != null) {
                lastPayTime = paymentVo.getPayment().getServerUpdateTime();
            }
        }
        return lastPayTime <= lastClosingTime;
    }


    private TradeExtra getTradeExtra(DatabaseHelper helper, String tradeUuid) {
        try {
            return helper.getDao(TradeExtra.class)
                    .queryBuilder()
                    .selectColumns(TradeExtra.$.deliveryStatus,
                            TradeExtra.$.deliveryAddress,
                            TradeExtra.$.receiverPhone,
                            TradeExtra.$.serialNumber,
                            TradeExtra.$.thirdSerialNo,
                            TradeExtra.$.numberPlate,
                            TradeExtra.$.expectTime)
                    .where()
                    .eq(TradeExtra.$.tradeUuid, tradeUuid)
                    .queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    private List<TradeBuffetPeople> getTradeBuffetPeoples(DatabaseHelper helper, String tradeUuid) {
        try {
            return helper.getDao(TradeBuffetPeople.class)
                    .queryBuilder()
                    .selectColumns(TradeBuffetPeople.$.id,
                            TradeBuffetPeople.$.carteNormsName,
                            TradeBuffetPeople.$.cartePrice,
                            TradeBuffetPeople.$.peopleCount
                    )
                    .where()
                    .eq(TradeBuffetPeople.$.tradeUuid, tradeUuid)
                    .query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }


    private TradeDeposit getTradeDeposit(DatabaseHelper helper, String tradeUuid) {
        try {
            return helper.getDao(TradeDeposit.class)
                    .queryBuilder()
                    .selectColumns(TradeDeposit.$.depositRefund)
                    .where()
                    .eq(TradeExtra.$.tradeUuid, tradeUuid)
                    .queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }


    private List<TradeTable> getTradeTableList(DatabaseHelper helper, String tradeUuid) {
        try {
            return helper.getDao(TradeTable.class)
                    .queryBuilder()
                    .selectColumns(TradeTable.$.tableName)
                    .where()
                    .eq(TradeTable.$.tradeUuid, tradeUuid)
                    .query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return Collections.emptyList();
    }


    private List<TradeCustomer> getTradeCustomerList(DatabaseHelper helper, String tradeUuid) {
        try {
            return helper.getDao(TradeCustomer.class)
                    .queryBuilder()
                    .selectColumns(TradeCustomer.$.customerType,
                            TradeCustomer.$.customerPhone)
                    .where()
                    .eq(TradeCustomer.$.tradeUuid, tradeUuid)
                    .query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return Collections.emptyList();
    }


    private List<PaymentVo> listPaymentVo(DatabaseHelper helper, String tradeUuid) throws Exception {
        List<PaymentVo> voList = new ArrayList<PaymentVo>();

        Dao<Payment, String> dao = helper.getDao(Payment.class);
        QueryBuilder<Payment, String> paymentBuild = dao.queryBuilder();
        paymentBuild.selectColumns(Payment.$.uuid, Payment.$.serverUpdateTime, Payment.$.paymentType);
                        paymentBuild.where().eq(Payment.$.relateUuid, tradeUuid);
        paymentBuild.orderBy(Payment.$.serverCreateTime, false);
        List<Payment> paymentList = paymentBuild.query();

        Dao<PaymentItem, String> itemDao = helper.getDao(PaymentItem.class);
        for (Payment payment : paymentList) {
            PaymentVo vo = new PaymentVo();
            vo.setPayment(payment);

            List<PaymentItem> listPaymentItems = itemDao.queryBuilder()
                    .selectColumns(paySource, payStatus, faceAmount, changeAmount)
                    .where().eq(PaymentItem.$.paymentUuid, payment.getUuid())
                    .query();
            vo.setPaymentItemList(listPaymentItems);

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public long countOrder(int tab) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            createTradeWhere(helper, tradeQB, tab, -1, null, null, null);
            return tradeQB.countOf();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return 0L;
    }

    private List<Trade> queryTrade(DatabaseHelper helper, int childTab, int postion, String keyword, FilterCondition condition, Trade lastData) {
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            tradeQB.selectColumns(
                    Trade.$.id,
                    Trade.$.businessType,
                    Trade.$.deliveryType,
                    Trade.$.tradeStatus,
                    Trade.$.tradeType,
                    Trade.$.tradePayForm,
                    Trade.$.tradePayStatus,
                    Trade.$.tradeNo,
                    Trade.$.sourceId,
                    Trade.$.uuid,
                    Trade.$.tradeAmount,
                    Trade.$.relateTradeUuid,
                    Trade.$.serverUpdateTime);
            createTradeWhere(helper, tradeQB, childTab, postion, keyword, condition, lastData);
            tradeQB.limit(PAGE_SIZE);
                        if (childTab == DbQueryConstant.UNPROCESSED_NEW_ORDER
                    || childTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST) {
                tradeQB.orderBy(Trade.$.serverUpdateTime, true);
            } else {
                tradeQB.orderBy(Trade.$.serverUpdateTime, false);
            }
            return tradeQB.query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return Collections.emptyList();
    }

    private Where<Trade, String> createTradeWhere(DatabaseHelper helper, QueryBuilder<Trade, String> queryBuilder, int childTab, int position, String keyword, FilterCondition condition, Trade lastData) throws Exception {
        Where<Trade, String> where = generateTradeBusinessTypeWhere(queryBuilder);
        where.and().eq(Trade.$.statusFlag, StatusFlag.VALID);
        where.and().ne(Trade.$.tradeType, TradeType.UNOIN_TABLE_MAIN);

                if (lastData != null) {
            if (childTab == DbQueryConstant.UNPROCESSED_NEW_ORDER
                    || childTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST) {
                where.and().ge(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.id, lastData.getId());
            } else {
                where.and().le(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.id, lastData.getId());
            }
        }

                if (!TextUtils.isEmpty(keyword)) {
            Where<Trade, String> searchWhere = null;
            if (childTab == DbQueryConstant.UNPROCESSED_ALL) {
                switch (position) {
                    case 0: {
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
                        searchWhere = where.or(where.in(Trade.$.uuid, tradeExtraQB),
                                where.in(Trade.$.uuid, tradeTableQB),
                                where.in(Trade.$.uuid, tradeCustomerQB));
                    }
                    break;
                    case 1: {
                        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
                        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeExtraQB);
                    }
                    break;
                    case 2: {
                        Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
                        QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                        tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                        tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeCustomerQB);
                    }
                    break;
                    case 3: {
                        Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
                        QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                        tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                        tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeTableQB);
                    }
                    break;
                }
            } else {
                switch (position) {
                    case 0:                        if (Utils.isNum(keyword)) {
                            BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
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
                            searchWhere = where.or(where.in(Trade.$.uuid, tradeExtraQB),
                                    where.in(Trade.$.uuid, tradeTableQB),
                                    where.in(Trade.$.uuid, tradeCustomerQB),
                                    where.eq(Trade.$.tradeAmount, amount));
                        } else {
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
                            searchWhere = where.or(where.like(Trade.$.tradeNo, "%" + keyword + "%"),
                                    where.in(Trade.$.uuid, tradeExtraQB),
                                    where.in(Trade.$.uuid, tradeTableQB),
                                    where.in(Trade.$.uuid, tradeCustomerQB));
                        }
                        break;
                    case 1: {
                        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
                        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeExtraQB);
                    }
                    break;
                    case 2:                        if (Utils.isNum(keyword)) {
                            BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
                            searchWhere = where.eq(Trade.$.tradeAmount, amount);
                        }
                        break;
                    case 3: {
                        Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
                        QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                        tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                        tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeCustomerQB);
                    }
                    break;
                    case 4: {
                        Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
                        QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                        tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                        tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                        searchWhere = where.in(Trade.$.uuid, tradeTableQB);
                    }
                    break;
                }
            }
            if (searchWhere != null) {
                where.and(where, searchWhere);
            }
        }

                if (condition != null) {
            if (Utils.isNotEmpty(condition.getDeliveryTypes())) {
                where.and().in(Trade.$.deliveryType, condition.getDeliveryTypes());
            }

            if (Utils.isNotEmpty(condition.getSourceIds())) {
                where.and().in(Trade.$.sourceId, condition.getSourceIds());
            }

            if (Utils.isNotEmpty(condition.getTradeStatus())) {
                where.and().in(Trade.$.tradeStatus, condition.getTradeStatus());
            }

            if (condition.getPaySource() != null || condition.getPayModeId() != null) {
                where.and().in(Trade.$.uuid, getPaymentQueryBuilder(helper, condition.getPaySource(), condition.getPayModeId()));
            }

            if (condition.getUpdatorId() != 0) {
                Dao<Payment, String> paymentDao = helper.getDao(Payment.class);
                QueryBuilder<Payment, String> paymentBuilder = paymentDao.queryBuilder();
                paymentBuilder.selectColumns(Payment.$.relateUuid);
                paymentBuilder.where().eq(Payment.$.updatorId, condition.getUpdatorId());
                where.and().in(Trade.$.uuid, paymentBuilder);
            }
        }

                Where<Trade, String> tabWhere;
        switch (childTab) {
            case DbQueryConstant.UNPROCESSED_ALL:
                tabWhere = where.or(where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.ne(Trade.$.tradePayForm, TradePayForm.ONLINE))
                        .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                        .and().ne(Trade.$.sourceId, SourceId.POS), where.in(Trade.$.tradeStatus, TradeStatus.REFUSED, TradeStatus.CANCELLED).and().ne(Trade.$.sourceId, SourceId.POS));
                break;
            case DbQueryConstant.UNPROCESSED_NEW_ORDER:
                tabWhere = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.ne(Trade.$.tradePayForm, TradePayForm.ONLINE))
                        .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                        .and().ne(Trade.$.sourceId, SourceId.POS);
                break;
            case DbQueryConstant.UNPROCESSED_INVALID:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.REFUSED, TradeStatus.CANCELLED).and().ne(Trade.$.sourceId, SourceId.POS);
                break;
            case DbQueryConstant.SALES_ALL:
                tabWhere = where.or(where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT).and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID),
                        where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT).and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID).and().in(Trade.$.uuid, getTradeExtraQueryBuilder(helper)),
                        where.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT),
                        where.eq(Trade.$.tradeStatus, TradeStatus.INVALID), where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                                .and().eq(Trade.$.tradePayStatus, TradePayStatus.PAYING),
                        where.in(Trade.$.tradeType, TradeType.SELL).and().in(Trade.$.tradePayStatus, TradePayStatus.PREPAID));
                break;
            case DbQueryConstant.SALES_UNPAID:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID);
                break;
            case DbQueryConstant.SALES_PAID:
                tabWhere = where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT)
                        .and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP)
                        .and().in(Trade.$.uuid, getTradeExtraQueryBuilder(helper))
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID);
                break;
            case DbQueryConstant.SALES_REFUNDED:
                tabWhere = where.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT);
                break;
            case DbQueryConstant.SALES_INVALID:
                tabWhere = where.eq(Trade.$.tradeStatus, TradeStatus.INVALID);
                break;
            case DbQueryConstant.SALES_PAYING:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                        .and().in(Trade.$.tradePayStatus, TradePayStatus.PAYING, TradePayStatus.PREPAID);
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument tab");
        }
        where.and(where, tabWhere);
        Log.e(TAG, where.getStatement());

        return where;
    }

    @Override
    public List<TableNumberSetting> getTableNumberSetting() {
        try {
            SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
            return systemSettingDal.listTableNumberSetting(false);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

        return Collections.emptyList();
    }

    private QueryBuilder<PaymentItem, String> getPaymentItemQueryBuilder(DatabaseHelper helper, PaySource paySource) throws Exception {
        Dao<PaymentItem, String> paymentItemDao = helper.getDao(PaymentItem.class);
        QueryBuilder<PaymentItem, String> paymentItemQB = paymentItemDao.queryBuilder();
        paymentItemQB.selectColumns(PaymentItem.$.paymentUuid);
        paymentItemQB.where().eq(PaymentItem.$.paySource, paySource)
                .and().eq(PaymentItem.$.statusFlag, StatusFlag.VALID);

        return paymentItemQB;
    }


    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper helper) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().isNotNull(TradeExtra.$.serialNumber);

        return tradeExtraQB;
    }
}
