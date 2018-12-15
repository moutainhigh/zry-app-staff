package com.zhongmei.bty.cashier.ordercenter.manager;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.bty.basemodule.trade.bean.TradeUnionType;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.BatchQueryDeliveryFeeReq;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.changeAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.faceAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.paySource;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.payStatus;

/**
 * 订单中心订单列表Model，业务处理类主要负责数据库操作、网络操作
 */

public class OrderCenterListManager implements IOrderCenterListManager {
    private static final String TAG = OrderCenterListManager.class.getSimpleName();

    /**
     * 一页数据
     */
    public static final long PAGE_SIZE = 15L;

    protected TradeOperates mTradeOperates;
    protected TradeDal mTradeDal;

    public OrderCenterListManager() {
        mTradeOperates = OperatesFactory.create(TradeOperates.class);
        mTradeDal = OperatesFactory.create(TradeDal.class);
    }

    @Override
    public void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo, ResponseListener<OrderNotify> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryPayment(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearAccounts(List<TradeVo> tradeVos, ResponseListener<TradePaymentResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void batchQueryDeliveryFee(BatchQueryDeliveryFeeReq req, ResponseListener<GatewayTransferResp<JsonArray>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void bindDeliveryUser(List<TradeVo> tradeVos, User authUser, ResponseListener<BindOrderResp> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderDispatch(DeliveryOrderDispatchReq req, ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deliveryOrderList(DeliveryOrderListReq req, ResponseListener<GatewayTransferResp<DeliveryOrderListResp>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpenKoubeiBiz() {
        throw new UnsupportedOperationException();
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
                if (condition.getDeliveryStatus() == DeliveryStatus.REAL_DELIVERY
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
                        tradePaymentVo.setPaymentVoList(paymentVos);
                        tradePaymentVos.add(tradePaymentVo);
                        if (trade.getSource() == SourceId.KOU_BEI) {
                            tradeVo.setVerifyKoubeiOrder(getVerifyKoubeiOrder(helper, trade));
                        }
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
                    tradePaymentVo.setPaymentVoList(paymentVos);
                    tradePaymentVos.add(tradePaymentVo);
                    if (trade.getSource() == SourceId.KOU_BEI) {
                        tradeVo.setVerifyKoubeiOrder(getVerifyKoubeiOrder(helper, trade));
                    }
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

    //口碑核销
    protected VerifyKoubeiOrder getVerifyKoubeiOrder(DatabaseHelper helper, Trade trade) {
        try {
            return helper.getDao(VerifyKoubeiOrder.class)
                    .queryBuilder()
                    .where()
                    .eq(VerifyKoubeiOrder.$.tradeUuid, trade.getUuid())
                    .and()
                    .eq(VerifyKoubeiOrder.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean isClosedOrder(List<PaymentVo> paymentVos, long lastClosingTime) {
        long lastPayTime = System.currentTimeMillis();
        try {
            if (Utils.isNotEmpty(paymentVos)) {
                PaymentVo paymentVo = paymentVos.get(0);
                if (paymentVo != null && paymentVo.getPayment() != null) {
                    lastPayTime = paymentVo.getPayment().getServerUpdateTime();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return lastPayTime <= lastClosingTime;
    }

    /**
     * 获取交易扩展
     *
     * @param helper    helper
     * @param tradeUuid tradeUuid
     * @return TradeExtra
     */
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
                            TradeExtra.$.openIdenty,
                            TradeExtra.$.expectTime)
                    .where()
                    .eq(TradeExtra.$.tradeUuid, tradeUuid)
                    .queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    /**
     * 获取交易押金
     *
     * @param helper    helper
     * @param tradeUuid tradeUuid
     * @return TradeExtra
     */
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

    /**
     * 获取桌台信息
     *
     * @param helper    helper
     * @param tradeUuid tradeUuid
     * @return getTradeTableList
     */
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

    /**
     * 获取顾客信息
     *
     * @param helper    helper
     * @param tradeUuid tradeUuid
     * @return getTradeCustomerList
     */
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

    protected static TradeUnionType getTradeUnionType(DatabaseHelper helper, Trade trade) {
        switch (trade.getTradeType()) {
            case UNOIN_TABLE_MAIN:
                return TradeUnionType.UNION_MAIN;
            case UNOIN_TABLE_SUB:
                return TradeUnionType.UNION_SUB;
            default:
                try {
                    Dao<TradeMainSubRelation, Long> dao = helper.getDao(TradeMainSubRelation.class);
                    Where<TradeMainSubRelation, Long> where = dao.queryBuilder()
                            .where().eq(Trade.$.statusFlag, StatusFlag.VALID);

                    Where<TradeMainSubRelation, Long> whereInner = where
                            .eq(TradeMainSubRelation.$.mainTradeId, trade.getId())
                            .or()
                            .eq(TradeMainSubRelation.$.subTradeId, trade.getId());
                    List<TradeMainSubRelation> tradeMainSubRelationList = where.and(where, whereInner).query();
                    for (TradeMainSubRelation relation : tradeMainSubRelationList) {
                        if (trade.getId().equals(relation.getMainTradeId())) {
                            return TradeUnionType.UNION_MAIN;
                        }
                        if (trade.getId().equals(relation.getSubTradeId())) {
                            return TradeUnionType.UNION_SUB;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return TradeUnionType.NONE;
        }
    }

    /**
     * 加载订单相关支付信息（仅取了部分表的部分字段）
     *
     * @param helper    helper
     * @param tradeUuid tradeUuid
     * @return List<PaymentVo>
     * @throws Exception
     */
    private List<PaymentVo> listPaymentVo(DatabaseHelper helper, String tradeUuid) throws Exception {
        List<PaymentVo> voList = new ArrayList<PaymentVo>();

        Dao<Payment, String> dao = helper.getDao(Payment.class);
        QueryBuilder<Payment, String> paymentBuild = dao.queryBuilder();
        paymentBuild.selectColumns(Payment.$.uuid, Payment.$.serverUpdateTime, Payment.$.paymentType);
        //paymentBuild.where().eq(Payment.$.relateUuid, tradeUuid)
        //        .and().eq(Payment.$.isPaid, Bool.YES);
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
                    Trade.$.serialNumber,
                    Trade.$.serverUpdateTime);
            createTradeWhere(helper, tradeQB, childTab, postion, keyword, condition, lastData);
            tradeQB.limit(PAGE_SIZE);
            //新订单||取消请求升序排列
            if (childTab == DbQueryConstant.UNPROCESSED_NEW_ORDER
                    || childTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST
                    || childTab == DbQueryConstant.SALES_UNPAID) {
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

        //偏移条件
        if (lastData != null) {
            if (childTab == DbQueryConstant.UNPROCESSED_NEW_ORDER
                    || childTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST
                    || childTab == DbQueryConstant.SALES_UNPAID) {
                where.and().ge(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.id, lastData.getId());
            } else {
                where.and().le(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.id, lastData.getId());
            }
        }

        //搜索条件
        if (!TextUtils.isEmpty(keyword)) {
            Where<Trade, String> searchWhere = null;
            if (childTab == DbQueryConstant.UNPROCESSED_ALL) {
                switch (position) {
                    case 0://全部
                        searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(helper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(helper, keyword)));
                        break;
                    case 1://流水号
                        searchWhere = where.in(Trade.$.uuid, getSerialNumberQueryBuilder(helper, keyword));
                        break;
                    case 2://手机号
                        searchWhere = where.or(where.in(Trade.$.uuid, getReceiverPhoneQueryBuilder(helper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(helper, keyword)));
                        break;
                }
            } else {
                switch (position) {
                    case 0://全部
                        if (Utils.isNum(keyword)) {
                            searchWhere = getTradeSearchWhere(helper, keyword, where);
                        } else {
                            searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(helper, keyword)),
                                    where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(helper, keyword)));
                        }
                        break;
                    case 1://流水号
                        searchWhere = where.in(Trade.$.uuid, getSerialNumberQueryBuilder(helper, keyword));
                        break;
                    case 2://订单金额
                        if (Utils.isNum(keyword)) {
                            searchWhere = getTradeAmountSearchWhere(helper, keyword, where);
                        }
                        break;
                    case 3://手机号
                        searchWhere = where.or(where.in(Trade.$.uuid, getReceiverPhoneQueryBuilder(helper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(helper, keyword)));
                        break;
                }
            }
            if (searchWhere != null) {
                where.and(where, searchWhere);
            }
        }

        //筛选条件
        if (condition != null) {
            if (Utils.isNotEmpty(condition.getDeliveryTypes())) {
                where.and().in(Trade.$.deliveryType, condition.getDeliveryTypes());
            }

            List<SourceId> sourceIds = condition.getSourceIds();
            if (Utils.isNotEmpty(sourceIds)) {
                if (sourceIds.contains(SourceId.KIOSK)) {
                    sourceIds.add(SourceId.KIOSK_ANDROID);
                }
                where.and().in(Trade.$.sourceId, sourceIds);
            }

            if (condition.getDeliveryStatus() != null) {
                where.and().in(Trade.$.uuid, getTradeExtraQueryBuilder(helper, condition.getDeliveryStatus()));
            } else {
                if (Utils.isNotEmpty(condition.getDeliveryOrderStatuses())) {
                    Where<Trade, String> deliverStatusWhere = where.or(where.eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED), where.eq(Trade.$.tradeStatus, TradeStatus.FINISH))
                            .and().in(Trade.$.uuid, getDeliveryStatusQueryBuilder(helper,
                                    condition.getDeliveryOrderStatuses()))
                            .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                            .and().eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                    where.and(where, deliverStatusWhere);
                }
            }

            if (condition.getPaySource() != null || condition.getPayModeId() != null) {
                where.and().in(Trade.$.uuid, getPaymentQueryBuilder(helper, condition.getPaySource(), condition.getPayModeId()));
            }

        }

        //分栏条件
        Where<Trade, String> tabWhere;
        switch (childTab) {
            case DbQueryConstant.UNPROCESSED_ALL:
                tabWhere = where.or(
                        where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.ne(Trade.$.tradePayForm, TradePayForm.ONLINE)).and().in(Trade.$.tradeStatus, TradeStatus.UNPROCESSED, TradeStatus.CANCELLED).and().ne(Trade.$.sourceId, SourceId.POS),
                        where.in(Trade.$.id, getTradeReturnInfoQueryBuilder(helper)),
                        where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE)).and().in(Trade.$.tradeStatus, TradeStatus.REFUSED, TradeStatus.CANCELLED).and().ne(Trade.$.sourceId, SourceId.POS));
                break;
            case DbQueryConstant.UNPROCESSED_NEW_ORDER:
                tabWhere = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE))
                        .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED);
                break;
            case DbQueryConstant.UNPROCESSED_CANCEL_REQUEST:
                tabWhere = where.in(Trade.$.id, getTradeReturnInfoQueryBuilder(helper));
                break;
            case DbQueryConstant.UNPROCESSED_INVALID:
                tabWhere = where.and(where.eq(Trade.$.tradeStatus, TradeStatus.CANCELLED),
                        (where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE).or().in(Trade.$.tradePayStatus, TradePayStatus.REFUNDED, TradePayStatus.PAID)))
                        .or().eq(Trade.$.tradeStatus, TradeStatus.REFUSED)
                        .and().ne(Trade.$.sourceId, SourceId.POS);
                break;
            case DbQueryConstant.SALES_ALL:
                tabWhere = where.or(where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT).and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID),
                        where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT).and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                        where.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT),
                        where.eq(Trade.$.tradeStatus, TradeStatus.INVALID), where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                                .and().eq(Trade.$.tradePayStatus, TradePayStatus.PAYING));
                break;
            case DbQueryConstant.SALES_UNPAID:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID);
                break;
            case DbQueryConstant.SALES_PAID:
                tabWhere = where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT)
                        .and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP)
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
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.PAYING);
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument tab");
        }
        where.and(where, tabWhere);
        Log.e(TAG, where.getStatement());

        return where;
    }

    private Where<Trade, String> getTradeSearchWhere(DatabaseHelper helper, String keyword, Where<Trade, String> where) throws Exception {
        Where<Trade, String> searchWhere;
        BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
        searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(helper, keyword)),
                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(helper, keyword)), where.eq(Trade.$.tradeAmount, amount));
        return searchWhere;
    }

    private Where<Trade, String> getTradeAmountSearchWhere(DatabaseHelper helper, String keyword, Where<Trade, String> where) throws Exception {
        Where<Trade, String> searchWhere;
        BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
        searchWhere = where.eq(Trade.$.tradeAmount, amount);
        return searchWhere;
    }

    @Override
    public Where<Trade, String> generateTradeBusinessTypeWhere(QueryBuilder<Trade, String> queryBuilder) throws Exception {
        throw new UnsupportedOperationException();
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

    private QueryBuilder<TradeReturnInfo, String> getTradeReturnInfoQueryBuilder(DatabaseHelper helper) throws Exception {
        Dao<TradeReturnInfo, String> tradeReturnInfoDao = helper.getDao(TradeReturnInfo.class);
        QueryBuilder<TradeReturnInfo, String> tradeReturnInfoQB = tradeReturnInfoDao.queryBuilder();
        tradeReturnInfoQB.selectColumns(TradeReturnInfo.$.tradeId);
        tradeReturnInfoQB.where().eq(TradeReturnInfo.$.returnStatus, TradeReturnInfoReturnStatus.APPLY)
                .and().eq(TradeReturnInfo.$.statusFlag, StatusFlag.VALID);

        return tradeReturnInfoQB;
    }

    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper helper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyWord + "%")
                .or().like(TradeExtra.$.receiverPhone, "%" + keyWord + "%");

        return tradeExtraQB;
    }

    private QueryBuilder<TradeExtra, String> getSerialNumberQueryBuilder(DatabaseHelper helper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyWord + "%");
        return tradeExtraQB;
    }

    private QueryBuilder<TradeExtra, String> getReceiverPhoneQueryBuilder(DatabaseHelper helper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.receiverPhone, "%" + keyWord + "%");
        return tradeExtraQB;
    }

    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper helper, DeliveryStatus deliveryStatus) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, deliveryStatus);

        return tradeExtraQB;
    }

    protected QueryBuilder<TradeExtra, String> getDeliveryStatusQueryBuilder(DatabaseHelper dbHelper, List<DeliveryOrderStatus> deliveryStatuses) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);

        List<DeliveryStatus> tempStatus = new ArrayList<>();
        //配送完成
        if (deliveryStatuses.contains(DeliveryOrderStatus.REAL_DELIVERY)) {
            tempStatus.add(DeliveryStatus.SQUARE_UP);
            tempStatus.add(DeliveryStatus.REAL_DELIVERY);
        }
        //配送中
        if (deliveryStatuses.contains(DeliveryOrderStatus.DELIVERYING)) {
            tempStatus.add(DeliveryStatus.DELIVERYING);
        }
        //待配送
        if (deliveryStatuses.contains(DeliveryOrderStatus.WAITING_PICK_UP)) {
            tempStatus.add(DeliveryStatus.WAITINT_DELIVERY);
        }
        Where<TradeExtra, String> where = tradeExtraQB.where();
        where.in(TradeExtra.$.deliveryStatus, tempStatus);
        return tradeExtraQB;
    }

    private QueryBuilder<TradeCustomer, String> getTradeCustomerQueryBuilder(DatabaseHelper helper, String keyWord) throws Exception {
        Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
        QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
        tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
        tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyWord + "%");

        return tradeCustomerQB;
    }

    protected QueryBuilder<Payment, String> getPaymentQueryBuilder(DatabaseHelper dbHelper, PaySource paySource, PayModeId payModeId) throws Exception {
        Dao<Payment, String> paymentDao = dbHelper.getDao(Payment.class);
        QueryBuilder<Payment, String> paymentQB = paymentDao.queryBuilder();
        paymentQB.selectColumns(Payment.$.relateUuid);
        paymentQB.where().in(Payment.$.uuid, getPaymentItemQueryBuilder(dbHelper, paySource, payModeId))
                .and().eq(Payment.$.isPaid, Bool.YES)
                .and().eq(Payment.$.paymentType, PaymentType.TRADE_SELL)
                .and().eq(Payment.$.statusFlag, StatusFlag.VALID);

        return paymentQB;
    }

    private QueryBuilder<PaymentItem, String> getPaymentItemQueryBuilder(DatabaseHelper dbHelper, PaySource paySource, PayModeId payModeId)
            throws Exception {
        Dao<PaymentItem, String> paymentItemDao = dbHelper.getDao(PaymentItem.class);
        QueryBuilder<PaymentItem, String> paymentItemQB = paymentItemDao.queryBuilder();
        paymentItemQB.selectColumns(PaymentItem.$.paymentUuid);
        Where<PaymentItem, String> where = paymentItemQB.where().eq(PaymentItem.$.statusFlag, StatusFlag.VALID);
        if (paySource != null) {
            where.and().eq(PaymentItem.$.paySource, paySource);
        }
        if (payModeId != null) {
            where.and().eq(PaymentItem.$.payModeId, payModeId);
        }
        return paymentItemQB;
    }
}
