package com.zhongmei.bty.cashier.dal.impl.db;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.dal.OrderCenterDal;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder.$;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.changeAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.faceAmount;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.paySource;
import static com.zhongmei.yunfu.db.entity.trade.PaymentItem.$.payStatus;

/**
 * Created by demo on 2018/12/15
 */
public class OrderCenterDBDalImpl extends AbstractOpeartesImpl implements OrderCenterDal {

    private static final long PAGE_SIZE = 15L;

    public OrderCenterDBDalImpl(ImplContext context) {
        super(context);
    }

    private <D extends Dao<E, ?>, E> D getDao(DBHelperManager dbHelper, Class<E> classType) throws SQLException {
        return DBHelperManager.getHelper().getDao(classType);
    }

    @Override
    public List<Trade> queryTrade(int childTab, int position, String keyword,
                                  FilterCondition condition, Trade lastData,
                                  BusinessType... businessTypes) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        if (businessTypes != null && businessTypes.length == 1
                && businessTypes[0] == BusinessType.DINNER) {
            return queryTradeForDinner(childTab, position, keyword, condition, lastData);
        }
        try {
            Dao<Trade, String> tradeDao = dbHelper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            createTradeWhere(dbHelper, tradeQB, childTab, position, keyword, condition, lastData);
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
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return Collections.emptyList();
    }

    @Override
    public long countOf(int tab) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQB = tradeDao.queryBuilder();
            createTradeWhere(helper, tradeQB, tab, -1, null, null, null);
            return tradeQB.countOf();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return 0L;
    }


    private Where<Trade, String> createTradeWhere(DatabaseHelper dbHelper, QueryBuilder<Trade, String> tradeQB, int childTab, int position, String keyword, FilterCondition condition, Trade lastData) throws Exception {
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
                Trade.$.sourceChild,
                Trade.$.serverUpdateTime);

        Where<Trade, String> where = tradeQB.where();
        where.eq(Trade.$.statusFlag, StatusFlag.VALID).and().ge(Trade.$.bizDate, getMinBizDate());
        String localDevId = BaseApplication.getInstance().getDeviceIdenty();
        //(本机下单||非离线订单)=过滤非本机的离线订单
        where.and().or(where.eq(Trade.$.deviceIdenty, localDevId), where.ne(Trade.$.sourceChild, SourceChild.OFFLINE.value()));
        where.and().in(Trade.$.businessType, BusinessType.SNACK, BusinessType.TAKEAWAY);
        if (lastData != null) {
            if (childTab == DbQueryConstant.UNPROCESSED_NEW_ORDER
                    || childTab == DbQueryConstant.UNPROCESSED_CANCEL_REQUEST
                    || childTab == DbQueryConstant.SALES_UNPAID) {
                where.and().ge(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.uuid, lastData.getUuid());
            } else {
                where.and().le(Trade.$.serverUpdateTime, lastData.getServerUpdateTime())
                        .and().ne(Trade.$.uuid, lastData.getUuid());
            }
        }
        if (!TextUtils.isEmpty(keyword)) {
            Where<Trade, String> searchWhere = null;
            if (childTab == DbQueryConstant.UNPROCESSED_ALL) {
                switch (position) {
                    case 0://全部

                        searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(dbHelper, keyword)),
                                where.in(Trade.$.uuid, getTakeNumberByTradeExtra(dbHelper, keyword)));
                        break;
                    case 1://流水号
                        searchWhere = where.in(Trade.$.uuid, getSerialNumberQueryBuilder(dbHelper, keyword));
                        break;
                    case 2://手机号
                        searchWhere = where.or(where.in(Trade.$.uuid, getReceiverPhoneQueryBuilder(dbHelper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(dbHelper, keyword)));
                        break;
                    case 3:// 号牌
                        // v8.12.0 号牌只查询POS下单
                        searchWhere = where.eq(Trade.$.sourceId, SourceId.POS).and().in(Trade.$.uuid, getNumberPlateQueryBuilder(dbHelper, keyword));
                        break;
                    case 4: // 取餐号
                        // v8.12.0 查询非POS下单
                        searchWhere = where.ne(Trade.$.sourceId, SourceId.POS).and().in(Trade.$.uuid, getTakeNumberByTradeExtra(dbHelper, keyword));
                        break;
                }
            } else {
                switch (position) {
                    case 0://全部
                        if (Utils.isNum(keyword)) {
                            searchWhere = getTradeSearchWhere(dbHelper, keyword, where);
                        } else {
                            searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper, keyword)),
                                    where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(dbHelper, keyword)),
                                    where.in(Trade.$.uuid, getTakeNumberByTradeExtra(dbHelper, keyword)));
                        }
                        break;
                    case 1://流水号
                        searchWhere = where.in(Trade.$.uuid, getSerialNumberQueryBuilder(dbHelper, keyword));
                        break;
                    case 2://订单金额
                        if (Utils.isNum(keyword)) {
                            searchWhere = getTradeAmountSearchWhere(keyword, where);
                        }
                        break;
                    case 3://手机号
                        searchWhere = where.or(where.in(Trade.$.uuid, getReceiverPhoneQueryBuilder(dbHelper, keyword)),
                                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(dbHelper, keyword)));
                        break;
                    case 4:// 号牌
                        // v8.12.0 号牌只查询POS下单
                        searchWhere = where.eq(Trade.$.sourceId, SourceId.POS).in(Trade.$.uuid, getNumberPlateQueryBuilder(dbHelper, keyword));
                        break;
                    case 5: // 取餐号
                        // v8.12.0 查询非POS下单
                        searchWhere = where.ne(Trade.$.sourceId, SourceId.POS).and().in(Trade.$.uuid, getTakeNumberByTradeExtra(dbHelper, keyword));
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

            if (Utils.isNotEmpty(condition.getSourceIds())) {
                where.and().in(Trade.$.sourceId, condition.getSourceIds());
            }

            if (Utils.isNotEmpty(condition.getTradeStatus())) {
                where.and().in(Trade.$.tradeStatus, condition.getTradeStatus());
            }

            if (condition.isExcludePosOrder()) {
                where.and().ne(Trade.$.sourceId, SourceId.POS);
            }
            if (condition.isExcludeOfflineOrder()) {
                where.and().ne(Trade.$.sourceChild, SourceChild.OFFLINE);
            }

            if (condition.getDeliveryStatus() != null) {
                where.and().in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper, condition.getDeliveryStatus(), condition.isHasBindDeliveryUser()));
            } else if (Utils.isNotEmpty(condition.getDeliveryOrderStatuses())) {
                //单独处理待下发状态，解决当tradeExtra表没有及时更新的情况下(有deliveryOrder数据，但是没有来得及同步tradeExtra表)，只查询tradeExtra表会查询出待接单的订单
                if (condition.getDeliveryOrderStatuses().size() == 1
                        && condition.getDeliveryOrderStatuses().contains(DeliveryOrderStatus.WAITING_CREATE)) { //只有待下发
                    //tradeExtra为待下发
                    Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                    QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                    tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                    tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.WAITINT_DELIVERY)
                            .and().isNull(TradeExtra.$.deliveryUserId)
                            .and().eq(TradeExtra.$.deliveryPlatform, DeliveryPlatform.MERCHANT);
                    //所有deliveryOrder记录
                    Dao<DeliveryOrder, String> deliveryOrderDao = dbHelper.getDao(DeliveryOrder.class);
                    QueryBuilder<DeliveryOrder, String> deliveryOrderQB = deliveryOrderDao.queryBuilder();
                    deliveryOrderQB.selectColumns(DeliveryOrder.$.tradeUuid);
                    deliveryOrderQB.where()
                            .eq(DeliveryOrder.$.statusFlag, YesOrNo.YES)
                            .and()
                            .eq(DeliveryOrder.$.enableFlag, YesOrNo.YES);
                    //没有deliveryOrder记录并且tradeExtra也是待下发
                    Where<Trade, String> deliverStatusWhere = where
                            .and(where.and(where.notIn(Trade.$.uuid, deliveryOrderQB)
                                    , where.in(Trade.$.uuid, tradeExtraQB))
                                    , where.or(where.eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED), where.eq(Trade.$.tradeStatus, TradeStatus.FINISH)))
                            .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                            .and().eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                    where.and(where, deliverStatusWhere);
                } else {
                    //老配送状态
                    QueryBuilder<TradeExtra, String> deliveryStatusQueryBuilder = getDeliveryStatusQueryBuilder(dbHelper, condition.getDeliveryOrderStatuses());
                    //新配送状态
                    QueryBuilder<DeliveryOrder, String> deliveryOrderStatusQueryBuilder = getDeliveryOrderStatusQueryBuilder(dbHelper, condition.getDeliveryOrderStatuses(), condition.getDeliveryOrderSubStatuses());
                    if (deliveryStatusQueryBuilder == null) {
                        Where<Trade, String> deliverStatusWhere = where.and(where.in(Trade.$.uuid, deliveryOrderStatusQueryBuilder), where.or(where.eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED), where.eq(Trade.$.tradeStatus, TradeStatus.FINISH)))
                                .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                                .and().eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                        where.and(where, deliverStatusWhere);
                    } else {
                        Where<Trade, String> deliverStatusWhere = where.and(where.or(where.in(Trade.$.uuid, deliveryStatusQueryBuilder), where.in(Trade.$.uuid, deliveryOrderStatusQueryBuilder)), where.or(where.eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED), where.eq(Trade.$.tradeStatus, TradeStatus.FINISH)))
                                .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                                .and().eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                        where.and(where, deliverStatusWhere);
                    }
                }
            }

            if (condition.getPaySource() != null || condition.getPayModeId() != null) {
                where.and().in(Trade.$.uuid, getPaymentQueryBuilder(dbHelper, condition.getPaySource(), condition.getPayModeId()));
            }

            if (condition.getDeliveryInfo() != null) {
                if (condition.getDeliveryInfo().length > 0) {
                    Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                    QueryBuilder<TradeExtra, String> tradeExtraBuilder = tradeExtraDao.queryBuilder();
                    tradeExtraBuilder.selectColumns(TradeExtra.$.tradeId);
                    tradeExtraBuilder.where().in(TradeExtra.$.deliveryUserId, condition.getDeliveryInfo());
                    where.and().in(Trade.$.id, tradeExtraBuilder);
                }
            }

        }

        Where<Trade, String> tabWhere;
        switch (childTab) {
            case DbQueryConstant.UNPROCESSED_ALL:
                tabWhere = where.or(
                        where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.ne(Trade.$.tradePayForm, TradePayForm.ONLINE)).and()
                                .eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED),
                        where.in(Trade.$.id, getTradeReturnInfoQueryBuilder(dbHelper)),
                        where.or(where.and(where.eq(Trade.$.tradeStatus, TradeStatus.CANCELLED),
                                (where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE).or()
                                        .in(Trade.$.tradePayStatus, TradePayStatus.REFUNDED, TradePayStatus.PAID)),
                                where.ne(Trade.$.sourceId, SourceId.POS)), where.eq(Trade.$.tradeStatus, TradeStatus.REFUSED)));
                break;
            case DbQueryConstant.UNPROCESSED_NEW_ORDER:
                tabWhere = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE))
                        .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED);
                break;
            case DbQueryConstant.UNPROCESSED_CANCEL_REQUEST:
                tabWhere = where.in(Trade.$.id, getTradeReturnInfoQueryBuilder(dbHelper));
                break;
            case DbQueryConstant.UNPROCESSED_INVALID:
                tabWhere = where.and(where.eq(Trade.$.tradeStatus, TradeStatus.CANCELLED),
                        (where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE).or()
                                .in(Trade.$.tradePayStatus, TradePayStatus.REFUNDED, TradePayStatus.PAID)),
                        where.ne(Trade.$.sourceId, SourceId.POS))
                        .or().eq(Trade.$.tradeStatus, TradeStatus.REFUSED);
                break;
            case DbQueryConstant.SALES_ALL:
                tabWhere = where.or(where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT).and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID).and().eq(Trade.$.tradePayForm, TradePayForm.OFFLINE),
                        where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT).and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                        where.in(Trade.$.tradeType, TradeType.REFUND, TradeType.REFUND_FOR_REPEAT),
                        where.eq(Trade.$.tradeStatus, TradeStatus.INVALID).and().eq(Trade.$.tradePayForm, TradePayForm.OFFLINE),
                        where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAYING));
                break;
            case DbQueryConstant.SALES_UNPAID:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID)
                        .and().eq(Trade.$.tradePayForm, TradePayForm.OFFLINE);
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
                tabWhere = where.eq(Trade.$.tradeStatus, TradeStatus.INVALID).and().eq(Trade.$.tradePayForm, TradePayForm.OFFLINE);
                break;
            case DbQueryConstant.SALES_PAYING:
                tabWhere = where.in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.CREDIT)
                        .and().eq(Trade.$.tradePayStatus, TradePayStatus.PAYING);
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument tab");
        }
        where.and(where, tabWhere);

        return where;
    }

    //获取最小营业日
    private Long getMinBizDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        return calendar.getTimeInMillis();
    }

    public List<Trade> queryTradeForDinner(int childTab, int position, String keyword, FilterCondition condition, Trade lastData) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = dbHelper.getDao(Trade.class);
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

            Where<Trade, String> where = tradeQB.where();
            where.in(Trade.$.businessType, BusinessType.DINNER);
            where.and().eq(Trade.$.statusFlag, StatusFlag.VALID);
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
                            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                            tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                            Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
                            QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                            tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                            tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                            Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
                            QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                            tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                            tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                            searchWhere = where.or(where.in(Trade.$.uuid, tradeExtraQB),
                                    where.in(Trade.$.uuid, tradeTableQB),
                                    where.in(Trade.$.uuid, tradeCustomerQB));
                        }
                        break;
                        case 1: {
                            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                            tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeExtraQB);
                        }
                        break;
                        case 2: {
                            Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
                            QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                            tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                            tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeCustomerQB);
                        }
                        break;
                        case 3: {
                            Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
                            QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                            tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                            tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeTableQB);
                        }
                    }
                } else {
                    switch (position) {
                        case 0://全部
                            if (Utils.isNum(keyword)) {
                                BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
                                Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                                QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                                tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                                tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                                Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
                                QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                                tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                                tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                                Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
                                QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                                tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                                tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                                searchWhere = where.or(where.in(Trade.$.uuid, tradeExtraQB),
                                        where.in(Trade.$.uuid, tradeTableQB),
                                        where.in(Trade.$.uuid, tradeCustomerQB),
                                        where.eq(Trade.$.tradeAmount, amount));
                            } else {
                                Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                                QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                                tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                                tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                                Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
                                QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                                tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                                tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                                Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
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
                            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
                            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
                            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
                            tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeExtraQB);
                        }
                        break;
                        case 2://订单金额
                            if (Utils.isNum(keyword)) {
                                BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
                                searchWhere = where.eq(Trade.$.tradeAmount, amount);
                            }
                            break;
                        case 3: {
                            Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
                            QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
                            tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
                            tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeCustomerQB);
                        }
                        break;
                        case 4: {
                            Dao<TradeTable, String> tradeTableDao = dbHelper.getDao(TradeTable.class);
                            QueryBuilder<TradeTable, String> tradeTableQB = tradeTableDao.queryBuilder();
                            tradeTableQB.selectColumns(TradeTable.$.tradeUuid);
                            tradeTableQB.where().like(TradeTable.$.tableName, "%" + keyword + "%");
                            searchWhere = where.in(Trade.$.uuid, tradeTableQB);
                        }
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

                if (Utils.isNotEmpty(condition.getTradeStatus())) {
                    where.and().in(Trade.$.tradeStatus, condition.getTradeStatus());
                }

                if (condition.getPaySource() != null || condition.getPayModeId() != null) {
                    where.and().in(Trade.$.uuid, getPaymentQueryBuilder(dbHelper, condition.getPaySource(), condition.getPayModeId()));
                }

                if (condition.getUpdatorId() != 0) {
                    Dao<Payment, String> paymentDao = dbHelper.getDao(Payment.class);
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
                            where.in(Trade.$.tradeType, TradeType.SELL, TradeType.SELL_FOR_REPEAT, TradeType.SPLIT).and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED, TradeStatus.CREDIT, TradeStatus.WRITEOFF, TradeStatus.SQUAREUP).and().eq(Trade.$.tradePayStatus, TradePayStatus.PAID).and().in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper)),
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
                            .and().in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper))
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
                case DbQueryConstant.UNPROCESSED_CANCEL_REQUEST:
                    tabWhere = where.in(Trade.$.id, getTradeReturnInfoQueryBuilder(dbHelper));
                    break;
                default:
                    throw new IllegalArgumentException("Illegal Argument tab");
            }
            where.and(where, tabWhere);

            return tradeQB.query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return Collections.emptyList();
    }

    @Override
    public List<PaymentVo> getPaymentVos(String tradeUUID) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        List<PaymentVo> voList = new ArrayList<>();
        try {
            Dao<Payment, String> dao = dbHelper.getDao(Payment.class);
            QueryBuilder<Payment, String> paymentBuild = dao.queryBuilder();
            paymentBuild.selectColumns(Payment.$.uuid, Payment.$.serverUpdateTime, Payment.$.paymentType);
            //paymentBuild.where().eq(Payment.$.relateUuid, tradeUuid)
            //        .and().eq(Payment.$.isPaid, Bool.YES);
            paymentBuild.where().eq(Payment.$.relateUuid, tradeUUID);
            paymentBuild.orderBy(Payment.$.serverCreateTime, false);
            List<Payment> paymentList = paymentBuild.query();

            Dao<PaymentItem, String> itemDao = dbHelper.getDao(PaymentItem.class);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }
        return voList;
    }

    @Override
    public TradeExtra getTradeExtra(String tradeUUID) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            return dbHelper.getDao(TradeExtra.class)
                    .queryBuilder()
                    .selectColumns(TradeExtra.$.id,
                            TradeExtra.$.deliveryStatus,
                            TradeExtra.$.deliveryAddress,
                            TradeExtra.$.receiverPhone,
                            TradeExtra.$.serialNumber,
                            TradeExtra.$.thirdSerialNo,
                            TradeExtra.$.numberPlate,
                            TradeExtra.$.openIdenty,
                            TradeExtra.$.expectTime,
                            TradeExtra.$.serverUpdateTime,
                            TradeExtra.$.callDishStatus,
                            TradeExtra.$.deliveryUserId,
                            TradeExtra.$.thirdTranNo,
                            TradeExtra.$.deliveryPlatform)
                    .where()
                    .eq(TradeExtra.$.tradeUuid, tradeUUID)
                    .queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return null;
    }

    @Override
    public TradeExtraSecrecyPhone getTradeExtraSecrecyPhone(Long tradeExtraId) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            return dbHelper.getDao(TradeExtraSecrecyPhone.class)
                    .queryBuilder().orderBy(TradeExtraSecrecyPhone.$.serverUpdateTime, false)
                    .selectColumns(TradeExtraSecrecyPhone.$.virtualPhone,
                            TradeExtraSecrecyPhone.$.virtualPhoneExt)
                    .where()
                    .eq(TradeExtraSecrecyPhone.$.tradeExtraId, tradeExtraId)
                    .queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return null;
    }

    @Override
    public TradeDeposit getTradeDeposit(String tradeUUID) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            return dbHelper.getDao(TradeDeposit.class)
                    .queryBuilder()
                    .selectColumns(TradeDeposit.$.depositRefund)
                    .where()
                    .eq(TradeExtra.$.tradeUuid, tradeUUID)
                    .queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return null;
    }

    @Override
    public List<TradeTable> getTradeTables(String tradeUUID) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            return dbHelper.getDao(TradeTable.class)
                    .queryBuilder()
                    .selectColumns(TradeTable.$.tableName)
                    .where()
                    .eq(TradeTable.$.tradeUuid, tradeUUID)
                    .query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return Collections.emptyList();
    }

    @Override
    public List<TradeCustomer> getTradeCustomer(String tradeUUID) {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            return dbHelper.getDao(TradeCustomer.class)
                    .queryBuilder()
                    .selectColumns(TradeCustomer.$.customerType,
                            TradeCustomer.$.customerPhone)
                    .where()
                    .eq(TradeCustomer.$.tradeUuid, tradeUUID)
                    .query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }

        return Collections.emptyList();
    }

    @Override
    public List<DeliveryOrderVo> getDeliveryOrder(String tradeUUID) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DeliveryOrder, String> deliveryDao = helper.getDao(DeliveryOrder.class);
            List<DeliveryOrder> deliveryOrders = deliveryDao.queryBuilder()
                    .orderBy($.serverUpdateTime, false)
                    .where().eq(DeliveryOrder.$.tradeUuid, tradeUUID)
                    .query();
            if (Utils.isNotEmpty(deliveryOrders)) {
                List<DeliveryOrderVo> deliveryOrderVos = new ArrayList<DeliveryOrderVo>();
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
                    deliveryOrderVo.setDeliveryOrder(deliveryOrder);
                    deliveryOrderVos.add(deliveryOrderVo);
                }
                return deliveryOrderVos;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return Collections.emptyList();
    }

    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyWord + "%")
                .or().like(TradeExtra.$.receiverPhone, "%" + keyWord + "%")
                .or().like(TradeExtra.$.numberPlate, "%" + keyWord + "%"); // v8.7.0 添加号牌查询

        return tradeExtraQB;
    }

    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper helper, DeliveryStatus deliveryStatus, boolean hasBindDeliveryUser) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        Where<TradeExtra, String> where = tradeExtraQB.where();
        where.eq(TradeExtra.$.deliveryStatus, deliveryStatus);
        if (!hasBindDeliveryUser) {
            where.and().isNull(TradeExtra.$.deliveryUserId)
                    .and().eq(TradeExtra.$.deliveryPlatform, DeliveryPlatform.MERCHANT);
        }
        return tradeExtraQB;
    }

    private QueryBuilder<TradeCustomer, String> getTradeCustomerQueryBuilder(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeCustomer, String> tradeCustomerDao = dbHelper.getDao(TradeCustomer.class);
        QueryBuilder<TradeCustomer, String> tradeCustomerQB = tradeCustomerDao.queryBuilder();
        tradeCustomerQB.selectColumns(TradeCustomer.$.tradeUuid);
        tradeCustomerQB.where().like(TradeCustomer.$.customerPhone, "%" + keyWord + "%");

        return tradeCustomerQB;
    }

    private QueryBuilder<TradeExtra, String> getSerialNumberQueryBuilder(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.serialNumber, "%" + keyWord + "%");
        return tradeExtraQB;
    }

    private QueryBuilder<TradeExtra, String> getReceiverPhoneQueryBuilder(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.receiverPhone, "%" + keyWord + "%");
        return tradeExtraQB;
    }

    /**
     * v8.7.0 根据号牌查询
     */
    private QueryBuilder<TradeExtra, String> getNumberPlateQueryBuilder(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.numberPlate, "%" + keyWord + "%");
        return tradeExtraQB;
    }

    /**
     * v8.12.0 桌台号 非POS下为取餐号
     */
    private QueryBuilder<TradeExtra, String> getTakeNumberByTradeExtra(DatabaseHelper dbHelper, String keyWord) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().like(TradeExtra.$.numberPlate, "%" + keyWord + "%");
        return tradeExtraQB;
    }


    private Where<Trade, String> getTradeSearchWhere(DatabaseHelper dbHelper, String keyword, Where<Trade, String> where) throws Exception {
        Where<Trade, String> searchWhere;
        BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
        searchWhere = where.or(where.in(Trade.$.uuid, getTradeExtraQueryBuilder(dbHelper, keyword)),
                where.in(Trade.$.uuid, getTradeCustomerQueryBuilder(dbHelper, keyword)), where.eq(Trade.$.tradeAmount, amount));
        return searchWhere;
    }

    private Where<Trade, String> getTradeAmountSearchWhere(String keyword, Where<Trade, String> where) throws Exception {
        Where<Trade, String> searchWhere;
        BigDecimal amount = MathDecimal.round(new BigDecimal(keyword), 2);
        searchWhere = where.eq(Trade.$.tradeAmount, amount);
        return searchWhere;
    }

    private QueryBuilder<TradeExtra, String> getDeliveryStatusQueryBuilder(DatabaseHelper dbHelper, List<DeliveryOrderStatus> deliveryStatuses) throws Exception {
        if (deliveryStatuses.contains(DeliveryOrderStatus.REAL_DELIVERY)) {//配送完成
            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.SQUARE_UP).or().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.REAL_DELIVERY);
            return tradeExtraQB;
        } else if (deliveryStatuses.contains(DeliveryOrderStatus.DELIVERYING)) {//配送中
            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.DELIVERYING);
            return tradeExtraQB;
        } else if (deliveryStatuses.contains(DeliveryOrderStatus.WAITING_PICK_UP)) {//待配送-已派单
            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.WAITINT_DELIVERY)
                    .and().isNotNull(TradeExtra.$.deliveryUserId);
            return tradeExtraQB;
        } else if (deliveryStatuses.contains(DeliveryOrderStatus.WAITING_ACCEPT)) {//待配送-未派单
            Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.WAITINT_DELIVERY)
                    .and().isNull(TradeExtra.$.deliveryUserId)
                    .and().eq(TradeExtra.$.deliveryPlatform, DeliveryPlatform.MERCHANT);
            return tradeExtraQB;
        }

        return null;
    }

    private QueryBuilder<DeliveryOrder, String> getDeliveryOrderStatusQueryBuilder(DatabaseHelper dbHelper, List<DeliveryOrderStatus> deliveryOrderStatuses, List<DeliveryOrderSubStatus> deliveryOrderSubStatuses) throws Exception {
        Dao<DeliveryOrder, String> deliveryOrderDao = dbHelper.getDao(DeliveryOrder.class);
        QueryBuilder<DeliveryOrder, String> deliveryOrderErrorQB = deliveryOrderDao.queryBuilder();
        deliveryOrderErrorQB.selectColumns(DeliveryOrder.$.tradeUuid);
        if (Utils.isNotEmpty(deliveryOrderSubStatuses)) {
            deliveryOrderErrorQB.where()
                    .eq(DeliveryOrder.$.statusFlag, YesOrNo.YES)
                    .and().eq(DeliveryOrder.$.enableFlag, YesOrNo.YES)
                    .and().in(DeliveryOrder.$.delivererStatus, deliveryOrderStatuses)
                    .and().in(DeliveryOrder.$.subDeliveryStatus, deliveryOrderSubStatuses);
        } else {
            deliveryOrderErrorQB.where()
                    .eq(DeliveryOrder.$.statusFlag, YesOrNo.YES)
                    .and().eq(DeliveryOrder.$.enableFlag, YesOrNo.YES)
                    .and().in(DeliveryOrder.$.delivererStatus, deliveryOrderStatuses);
        }

        return deliveryOrderErrorQB;
    }

    private QueryBuilder<Payment, String> getPaymentQueryBuilder(DatabaseHelper dbHelper, PaySource paySource, PayModeId payModeId) throws Exception {
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

    private QueryBuilder<TradeReturnInfo, String> getTradeReturnInfoQueryBuilder(DatabaseHelper dbHelper) throws Exception {
        Dao<TradeReturnInfo, String> tradeReturnInfoDao = dbHelper.getDao(TradeReturnInfo.class);
        QueryBuilder<TradeReturnInfo, String> tradeReturnInfoQB = tradeReturnInfoDao.queryBuilder();
        tradeReturnInfoQB.selectColumns(TradeReturnInfo.$.tradeId);
        tradeReturnInfoQB.where().eq(TradeReturnInfo.$.returnStatus, TradeReturnInfoReturnStatus.APPLY)
                .and().eq(TradeReturnInfo.$.statusFlag, StatusFlag.VALID);

        return tradeReturnInfoQB;
    }

    private QueryBuilder<TradeExtra, String> getTradeExtraQueryBuilder(DatabaseHelper dbHelper) throws Exception {
        Dao<TradeExtra, String> tradeExtraDao = dbHelper.getDao(TradeExtra.class);
        QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
        tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
        tradeExtraQB.where().isNotNull(TradeExtra.$.serialNumber);

        return tradeExtraQB;
    }

    @Override
    public VerifyKoubeiOrder getVerifyKoubeiOrder(String tradeUuid) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(VerifyKoubeiOrder.class)
                    .queryBuilder()
                    .where()
                    .eq(VerifyKoubeiOrder.$.tradeUuid, tradeUuid)
                    .and()
                    .eq(VerifyKoubeiOrder.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }
}
