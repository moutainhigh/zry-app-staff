package com.zhongmei.bty.data.operates.impl;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.notifycenter.bean.NotifyOtherClientPayedVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.bty.basemodule.shopmanager.utils.DateTimeUtil;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.SenderOrderItem;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePendingVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.bty.basemodule.trade.enums.TradeDeliveryStatus;
import com.zhongmei.bty.basemodule.trade.message.TradeBatchUnbindCouponReq;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.utils.TradeManageUtils;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.util.Standard;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.CollectionUtils;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeReturnInfoReturnStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.util.Checks;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;


public class TradeDalImpl extends AbstractOpeartesImpl implements TradeDal {

    private static final String TAG = TradeDalImpl.class.getName();

    public TradeDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void batchUnbindCoupon(TradeBatchUnbindCouponReq tradeBatchUnbindCouponReq) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            String tradeUuid = tradeBatchUnbindCouponReq.getTradeUuid();
            List<String> tradePrivilegeUuids = tradeBatchUnbindCouponReq.getTradePrivilegeUuids();

            Trade trade = null;
                        if (Utils.isNotEmpty(tradePrivilegeUuids)) {
                TradePrivilege tradePrivilege = getTradePrivilege(tradePrivilegeUuids.get(0));
                tradeUuid = tradeUuid == null ? tradePrivilege.getTradeUuid() : tradeUuid;
                trade = getTrade(tradeUuid);
            }

                        if (trade == null) {
                throw new Exception();
            }

            List<TradeItem> relatedTradeItems = new ArrayList<TradeItem>();
            for (String tradePrivilegeUuid : tradePrivilegeUuids) {
                                TradePrivilege tradePrivilege = getTradePrivilege(tradePrivilegeUuid);
                                if (tradePrivilege == null) {
                    throw new Exception();
                }

                BigDecimal privilegeAmount = tradePrivilege.getPrivilegeAmount().abs();
                trade.setPrivilegeAmount(privilegeAmount.subtract(trade.getPrivilegeAmount().abs()));
                trade.setTradeAmount(trade.getTradeAmount().abs().add(privilegeAmount));
                trade.setTradeAmountBefore(trade.getTradeAmountBefore().abs().add(privilegeAmount));

                String tradeItemUuid = tradePrivilege.getTradeItemUuid();
                if (null != tradeItemUuid && !tradeItemUuid.isEmpty()) {
                    TradeItem tradeItem = getTradeItem(tradeItemUuid);
                    if (null != tradeItem) {
                                                tradeItem.setPropertyAmount(tradeItem.getPropertyAmount().abs().subtract(privilegeAmount));
                        tradeItem.setActualAmount(tradeItem.getActualAmount().abs().add(privilegeAmount));
                        relatedTradeItems.add(tradeItem);
                    }
                }

                                Dao<TradePrivilege, String> tradePrivilegeDao = helper.getDao(TradePrivilege.class);
                UpdateBuilder<TradePrivilege, String> updateBuilder = tradePrivilegeDao.updateBuilder();
                updateBuilder.updateColumnValue(TradePrivilege.$.statusFlag, StatusFlag.INVALID);
                updateBuilder.updateColumnValue(TradePrivilege.$.serverUpdateTime, Standard.Time.currentTime());
                updateBuilder.where().eq(TradePrivilege.$.uuid, tradePrivilegeUuid);
                updateBuilder.update();
            }

                        if (Utils.isNotEmpty(relatedTradeItems)) {
                for (TradeItem tradeItem : relatedTradeItems) {
                    Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
                    UpdateBuilder<TradeItem, String> updateBuilder = tradeItemDao.updateBuilder();
                    updateBuilder.updateColumnValue(TradeItem.$.serverUpdateTime, Standard.Time.currentTime());
                    updateBuilder.updateColumnValue(TradeItem.$.propertyAmount, tradeItem.getPropertyAmount());
                    updateBuilder.updateColumnValue(TradeItem.$.actualAmount, tradeItem.getActualAmount());
                    updateBuilder.where().eq(TradeItem.$.uuid, tradeItem.getUuid());
                    updateBuilder.update();
                }
            }

                        trade.setServerUpdateTime(Standard.Time.currentTime());
            DBHelperManager.saveEntities(helper, Trade.class, true, trade);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeCustomer> findTradeCustomer(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
            return tradeCustomerDao.queryBuilder()
                    .where()
                    .eq(TradeCustomer.$.tradeUuid, tradeUuid).and().eq(TradeCustomer.$.statusFlag, StatusFlag.VALID)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemProperty> findTradeItemProperty(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            List<TradeItem> tradeItems = tradeItemDao.queryBuilder()
                    .where()
                    .eq(TradeItem.$.tradeUuid, tradeUuid).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID)
                    .query();

            List<TradeItemProperty> tradeItemProperties = new ArrayList<TradeItemProperty>();
            if (Utils.isNotEmpty(tradeItems)) {
                Dao<TradeItemProperty, String> tradeItemPropertyDao = helper.getDao(TradeItemProperty.class);
                for (TradeItem tradeItem : tradeItems) {
                    List<TradeItemProperty> subTradeItemProperties = tradeItemPropertyDao.queryBuilder()
                            .where()
                            .eq(TradeItemProperty.$.tradeItemUuid, tradeItem.getUuid()).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID)
                            .query();
                    tradeItemProperties.addAll(subTradeItemProperties);
                }
            }

            return tradeItemProperties;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradePlanActivity> findTradePlanActivity(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradePlanActivity, String> dao = helper.getDao(TradePlanActivity.class);
            QueryBuilder<TradePlanActivity, String> qb = dao.queryBuilder();
            qb.where().eq(TradePlanActivity.$.tradeUuid, tradeUuid).and().eq(TradePlanActivity.$.statusFlag, StatusFlag.VALID);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemPlanActivity> findTradeItemPlanActivity(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItemPlanActivity, String> dao = helper.getDao(TradeItemPlanActivity.class);
            QueryBuilder<TradeItemPlanActivity, String> qb = dao.queryBuilder();
            qb.where().eq(TradeItemPlanActivity.$.tradeUuid, tradeUuid).and().eq(TradeItemPlanActivity.$.statusFlag, StatusFlag.VALID);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeReasonRel> findTradeReasonRel(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeReasonRel, String> dao = helper.getDao(TradeReasonRel.class);
            QueryBuilder<TradeReasonRel, String> qb = dao.queryBuilder();
            qb.where().eq(TradeReasonRel.$.relateUuid, tradeUuid).and().eq(TradeReasonRel.$.statusFlag, StatusFlag.VALID);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemExtra> findTradeItemExtra(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            List<TradeItem> tradeItems = tradeItemDao.queryBuilder()
                    .where()
                    .eq(TradeItem.$.tradeUuid, tradeUuid).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID)
                    .query();

            List<TradeItemExtra> tradeItemExtras = new ArrayList<TradeItemExtra>();
            if (Utils.isNotEmpty(tradeItems)) {
                Dao<TradeItemExtra, String> tradeItemExtraDao = helper.getDao(TradeItemExtra.class);
                for (TradeItem tradeItem : tradeItems) {
                    List<TradeItemExtra> subTradeItemExtras = tradeItemExtraDao.queryBuilder()
                            .where()
                            .eq(TradeItemExtra.$.tradeItemUuid, tradeItem.getUuid()).and().eq(TradeItemExtra.$.statusFlag, StatusFlag.VALID)
                            .query();
                    tradeItemExtras.addAll(subTradeItemExtras);
                }
            }

            return tradeItemExtras;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemLog> findTradeItemLog(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            List<TradeItem> tradeItems = tradeItemDao.queryBuilder()
                    .where()
                    .eq(TradeItem.$.tradeUuid, tradeUuid).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID)
                    .query();

            List<TradeItemLog> tradeItemLogs = new ArrayList<TradeItemLog>();
            if (Utils.isNotEmpty(tradeItems)) {
                Dao<TradeItemLog, String> tradeItemLogDao = helper.getDao(TradeItemLog.class);
                for (TradeItem tradeItem : tradeItems) {
                    List<TradeItemLog> subTradeItemLogs = tradeItemLogDao.queryBuilder()
                            .where()
                            .eq(TradeItemLog.$.tradeItemUuid, tradeItem.getUuid()).and().eq(TradeItemLog.$.statusFlag, StatusFlag.VALID)
                            .query();
                    tradeItemLogs.addAll(subTradeItemLogs);
                }
            }

            return tradeItemLogs;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Payment getPayment(String paymentUuid) throws Exception {
        Checks.verifyNotNull(paymentUuid, "paymentUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Payment, String> paymentDao = helper.getDao(Payment.class);
            return paymentDao.queryBuilder().where().eq(Payment.$.uuid, paymentUuid).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeVo findTrade(String tradeUuid)
            throws Exception {
        return findTrade(tradeUuid, true);
    }

    @Override
    public Trade findOnlyTrade(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade trade = tradeDao.queryForId(tradeUuid);
            return trade;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeVo findTrade(String tradeUuid, boolean onlyValid)
            throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade trade = tradeDao.queryForId(tradeUuid);
            return findTradeVo(helper, trade, onlyValid);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeVo findTrade(Long tradeId)
            throws Exception {
        return findTrade(tradeId, true);
    }

    @Override
    public TradeVo findTrade(Long tradeId, boolean onlyValid)
            throws Exception {
        Checks.verifyNotNull(tradeId, "tradeId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade trade = tradeDao.queryBuilder().where().eq(Trade.$.id, tradeId).queryForFirst();
            return findTradeVo(helper, trade, onlyValid);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public TradeVo findTrade(Trade trade)
            throws Exception {
        Checks.verifyNotNull(trade, "trade");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return findTradeVo(helper, trade);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradePaymentVo findTradPayment(String tradeUuid)
            throws Exception {
        return findTradPayment(tradeUuid, true);
    }

    @Override
    public TradePaymentVo findTradPayment(String tradeUuid, boolean onlyValid)
            throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        long start = System.currentTimeMillis();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade trade = tradeDao.queryForId(tradeUuid);
            TradeVo tradeVo = findTradeVo(helper, trade, onlyValid);
            List<PaymentVo> paymentVoList = listPaymentVo(helper, tradeUuid);
            TradePaymentVo vo = new TradePaymentVo();
            vo.setTradeVo(tradeVo);
            vo.setPaymentVoList(paymentVoList);
            return vo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradePaymentVo findTradPayment(Trade trade)
            throws Exception {
        Checks.verifyNotNull(trade, "trade");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            TradeVo tradeVo = findTradeVo(helper, trade);
            List<PaymentVo> paymentVoList = listPaymentVo(helper, trade.getUuid());
            TradePaymentVo vo = new TradePaymentVo();
            vo.setTradeVo(tradeVo);
            vo.setPaymentVoList(paymentVoList);
            return vo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PaymentVo> listPayment(String tradeUuid)
            throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return listPaymentVo(helper, tradeUuid);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PaymentVo> listPayment(String tradeUuid, PaymentType paymentType)
            throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return listPaymentVo(helper, tradeUuid, paymentType);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Pair<Integer, List<TradeVo>> listPending()
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<TradeVo> tradeVoList = new ArrayList<>();
        int count = 0;
        try {
            clearExpiredPending(helper);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where().eq(Trade.$.tradeStatus, TradeStatus.TEMPORARY).and().eq(Trade.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(Trade.$.clientCreateTime, false);
            List<Trade> trades = qb.query();
            count = trades.size();
            if (Utils.isNotEmpty(trades)) {
                int size = Math.min(count, 10);
                for (int i = 0; i < size; i++) {
                    TradeVo tradeVo = findTradeVo(helper, trades.get(i));
                    tradeVoList.add(tradeVo);
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return new Pair<>(count, tradeVoList);
    }

    @Override
    public List<TradeVo> loadMore(TradePendingVo lastVo) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<TradeVo> tradeVoList = new ArrayList<>();
        try {
            clearExpiredPending(helper);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();
            where.eq(Trade.$.tradeStatus, TradeStatus.TEMPORARY).and().eq(Trade.$.statusFlag, StatusFlag.VALID);
            Trade lastTrade = lastVo.getTradeVo().getTrade();
            if (lastTrade != null) {
                where.and().lt(Trade.$.clientCreateTime, lastTrade.getClientCreateTime());
            }
            qb.orderBy(Trade.$.clientCreateTime, false);
            qb.limit(5L);
            List<Trade> trades = qb.query();
            if (Utils.isNotEmpty(trades)) {
                int size = trades.size();
                for (int i = 0; i < size; i++) {
                    TradeVo tradeVo = findTradeVo(helper, trades.get(i));
                    tradeVoList.add(tradeVo);
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return tradeVoList;
    }

    @Override
    public long countPending()
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            clearExpiredPending(helper);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where().eq(Trade.$.tradeStatus, TradeStatus.TEMPORARY).and().eq(Trade.$.statusFlag, StatusFlag.VALID);
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public long countUnProcessed(BusinessType businessType)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
            calendar.add(Calendar.DAY_OF_MONTH, -2);            long minBizDate = calendar.getTimeInMillis();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();

            Where<Trade, String> where1 = where.eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED);
            Where<Trade, String> where2 = where.ge(Trade.$.bizDate, minBizDate);
            Where<Trade, String> where3 = where.eq(Trade.$.statusFlag, StatusFlag.VALID);

            Where<Trade, String> payWhere = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                    where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE));

            Where<Trade, String> businessWhere;
            switch (businessType) {
                case DINNER:
                    businessWhere = where.eq(Trade.$.businessType, BusinessType.DINNER);
                    where.and(where1, where2, where3, payWhere, businessWhere);
                    break;
                case SNACK:
                    businessWhere = where.or(where.eq(Trade.$.businessType, BusinessType.SNACK),
                            where.eq(Trade.$.businessType, BusinessType.TAKEAWAY));
                    where.and(where1, where2, where3, payWhere, businessWhere);
                    break;
                case TAKEAWAY:
                    businessWhere = where.eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                    where.and(where1, where2, where3, payWhere, businessWhere);
                    break;
                default:
                    break;
            }
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public long countUnProcessedAndPizzaHut(BusinessType businessType)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
            calendar.add(Calendar.DAY_OF_MONTH, -2);            long minBizDate = calendar.getTimeInMillis();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();

            Where<Trade, String> payWhere = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                    where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE));

            Where<Trade, String> where1 =
                    where.or(where.and(payWhere, where.eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)),
                            where.eq(Trade.$.sourceChild, SourceChild.PIZZAHUT)
                                    .and()
                                    .eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED)
                                    .and()
                                    .eq(Trade.$.tradePayStatus, TradePayStatus.UNPAID));
            Where<Trade, String> where2 = where.ge(Trade.$.bizDate, minBizDate);
            Where<Trade, String> where3 = where.eq(Trade.$.statusFlag, StatusFlag.VALID);

            Where<Trade, String> businessWhere;
            switch (businessType) {
                case DINNER:
                    businessWhere = where.eq(Trade.$.businessType, BusinessType.DINNER);
                    where.and(where1, where2, where3, businessWhere);
                    break;
                case SNACK:
                    businessWhere = where.or(where.eq(Trade.$.businessType, BusinessType.SNACK),
                            where.eq(Trade.$.businessType, BusinessType.TAKEAWAY));
                    where.and(where1, where2, where3, businessWhere);
                    break;
                case TAKEAWAY:
                    businessWhere = where.eq(Trade.$.businessType, BusinessType.TAKEAWAY);
                    where.and(where1, where2, where3, businessWhere);
                    break;
                default:
                    break;
            }
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long countUnProcessed()
            throws Exception {
        return countUnProcessed(BusinessType.SNACK);
    }

    @Override
    public List<Trade> listRefundFailed()
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PaymentItem, String> paymentItemDao = helper.getDao(PaymentItem.class);
            QueryBuilder<PaymentItem, String> paymentItemQb = paymentItemDao.queryBuilder();
            paymentItemQb.selectColumns(PaymentItem.$.paymentId)
                    .where()
                    .eq(PaymentItem.$.payStatus, TradePayStatus.REFUND_FAILED)
                    .and()
                    .in(PaymentItem.$.payModeId, PayModeId.WEIXIN_PAY.value(), PayModeId.ALIPAY.value(), PayModeId.BAIFUBAO.value())
                    .and()
                    .eq(PaymentItem.$.statusFlag, StatusFlag.VALID);

            Dao<Payment, String> paymentDao = helper.getDao(Payment.class);
            QueryBuilder<Payment, String> paymentQb = paymentDao.queryBuilder();
            paymentQb.selectColumns(Payment.$.relateId)
                    .where()
                    .in(Payment.$.id, paymentItemQb)
                    .and()
                    .eq(Payment.$.paymentType, PaymentType.TRADE_REFUND)
                    .and()
                    .isNotNull(Payment.$.relateId)
                    .and()
                    .eq(Payment.$.statusFlag, StatusFlag.VALID);

            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            return tradeDao.queryBuilder()
                    .where()
                    .in(Trade.$.id, paymentQb)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .query();

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void insert(final TradeVo tradeVo)
            throws Exception {
        tradeVo.getTrade().setBizDate(DateTimeUtils.getCurrentDayStart());
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call()
                        throws Exception {
                    Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                    tradeDao.create(tradeVo.getTrade());
                    if (tradeVo.getTradeExtra() != null) {
                        helper.getDao(TradeExtra.class).create(tradeVo.getTradeExtra());
                    }
                    if (tradeVo.getTradeDeposit() != null) {
                        helper.getDao(TradeDeposit.class).create(tradeVo.getTradeDeposit());
                    }
                    if (tradeVo.getTradePrivileges() != null) {
                        Dao<TradePrivilege, String> tpDao = helper.getDao(TradePrivilege.class);
                        for (TradePrivilege tp : tradeVo.getTradePrivileges()) {
                            tpDao.create(tp);
                        }
                    }


                    List<WeiXinCouponsVo> weiXinCouponsVoList = tradeVo.getmWeiXinCouponsVo();
                    if (weiXinCouponsVoList != null) {
                        Dao<TradePrivilege, String> tpDao = helper.getDao(TradePrivilege.class);
                        for (WeiXinCouponsVo weiXinCouponsVo : weiXinCouponsVoList) {
                            tpDao.create(weiXinCouponsVo.getmTradePrivilege());
                        }
                    }

                                        List<CouponPrivilegeVo> couponPrivilegeVoList = tradeVo.getCouponPrivilegeVoList();

                    if (couponPrivilegeVoList != null) {
                        Dao<TradePrivilege, String> tpDao = helper.getDao(TradePrivilege.class);
                        for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                            tpDao.create(couponPrivilegeVo.getTradePrivilege());
                        }
                    }

                    if (tradeVo.getIntegralCashPrivilegeVo() != null) {
                        helper.getDao(TradePrivilege.class)
                                .create(tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege());
                    }

                    if (tradeVo.getTradeCustomerList() != null) {
                        Dao<TradeCustomer, String> tcDao = helper.getDao(TradeCustomer.class);
                        for (TradeCustomer tradeCustomer : tradeVo.getTradeCustomerList()) {
                            tcDao.create(tradeCustomer);
                        }
                    }
                    if (tradeVo.getTradeItemList() != null) {
                        Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
                        for (TradeItemVo tiVo : tradeVo.getTradeItemList()) {
                            tiDao.create(tiVo.getTradeItem());
                            if (tiVo.getTradeItemPrivilege() != null) {
                                helper.getDao(TradePrivilege.class).create(tiVo.getTradeItemPrivilege());
                            }
                            if (tiVo.getTradeItemPropertyList() != null) {
                                Dao<TradeItemProperty, String> tipDao = helper.getDao(TradeItemProperty.class);
                                for (TradeItemProperty tip : tiVo.getTradeItemPropertyList()) {
                                    tipDao.create(tip);
                                }
                            }
                            if (tiVo.getCouponPrivilegeVo() != null) {
                                helper.getDao(TradePrivilege.class).create(tiVo.getCouponPrivilegeVo().getTradePrivilege());
                            }
                        }
                    }
                    if (tradeVo.getTradeReasonRelList() != null) {
                        Dao<TradeReasonRel, String> trlDao = helper.getDao(TradeReasonRel.class);
                        for (TradeReasonRel tradeReasonRel : tradeVo.getTradeReasonRelList()) {
                            trlDao.create(tradeReasonRel);
                        }
                    }

                    if (tradeVo.getTradeTableList() != null) {
                        Dao<TradeTable, String> ttDao = helper.getDao(TradeTable.class);
                        for (TradeTable tradeTable : tradeVo.getTradeTableList()) {
                            ttDao.create(tradeTable);
                        }
                    }

                    if (tradeVo.getTradePlanActivityList() != null) {
                        Dao<TradePlanActivity, String> tpaDao = helper.getDao(TradePlanActivity.class);
                        for (TradePlanActivity tradePlanActivity : tradeVo.getTradePlanActivityList()) {
                            tpaDao.create(tradePlanActivity);
                        }
                    }

                    if (tradeVo.getTradeItemPlanActivityList() != null) {
                        Dao<TradeItemPlanActivity, String> tipaDao = helper.getDao(TradeItemPlanActivity.class);
                        for (TradeItemPlanActivity tradeItemPlanActivity : tradeVo.getTradeItemPlanActivityList()) {
                            tipaDao.create(tradeItemPlanActivity);
                        }
                    }

                    if (tradeVo.getTradeItemExtraList() != null) {
                        Dao<TradeItemExtra, String> tipaDao = helper.getDao(TradeItemExtra.class);
                        for (TradeItemExtra tradeItemExtra : tradeVo.getTradeItemExtraList()) {
                            tipaDao.create(tradeItemExtra);
                        }
                    }

                    return null;
                }
            };
            helper.callInTransaction(callable);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void delete(final String tradeUuid)
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call()
                        throws Exception {
                    TradeManageUtils.delete(helper, tradeUuid);
                    return null;
                }
            };
            helper.callInTransaction(callable);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TakeOutVo findTradeVoTakeOutNew()
            throws Exception {

        throw new Exception("findTradeVoTakeOutNew not supported");
    }

    @Override
    public List<TradeVo> findTradeVoTakeOut()
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where()
                    .eq(Trade.$.deliveryType, DeliveryType.SEND)
                    .and()
                    .ne(Trade.$.businessType, BusinessType.DINNER)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .in(Trade.$.tradeStatus, TradeStatus.UNPROCESSED, TradeStatus.CONFIRMED, TradeStatus.FINISH)
                    .and()
                    .eq(Trade.$.tradeType, TradeType.SELL)
                    .and()
                    .gt(Trade.$.serverCreateTime, cad.getTime().getTime());
            qb.orderBy(Trade.$.serverCreateTime, false);
            List<Trade> tradeList = qb.query();
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
            for (Trade trade : tradeList) {
                                if (trade.getTradePayForm().equalsValue(TradePayForm.ONLINE.value())
                        && trade.getTradePayStatus().equalsValue(TradePayStatus.UNPAID.value())) {
                    continue;
                }
                TradeVo vo = findTradeAndExtra(helper, trade);
                tradeVoList.add(vo);
            }
            return tradeVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> findAutoRejectRecord(long startTime, long endTime, long maxRows) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();
            where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID),
                    where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE))
                    .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                    .and().eq(Trade.$.businessType, BusinessType.TAKEAWAY)
                    .and().eq(Trade.$.tradeType, TradeType.SELL)
                    .and().eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                    .and().gt(Trade.$.serverCreateTime, startTime)
                    .and().lt(Trade.$.serverCreateTime, endTime)
                    .and().eq(Trade.$.sourceId, SourceId.WECHAT);
            qb.orderBy(Trade.$.serverCreateTime, false);
            qb.limit(maxRows);
            List<Trade> trades = qb.query();

                        List<TradeVo> tradeVos = new ArrayList<TradeVo>();
            if (Utils.isNotEmpty(trades)) {
                for (Trade trade : trades) {
                    TradeVo tradeVo = new TradeVo();
                    tradeVo.setTrade(trade);
                    tradeVos.add(tradeVo);
                }
            }
            return tradeVos;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> findTradeVoList(TradeDeliveryStatus status)
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);

            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> qb = tradeExtraDao.queryBuilder();
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
            Where<TradeExtra, String> where = qb.where();
            switch (status) {
                case UN_PROCESS:                case WAITINT_DELIVERY:                    where.eq(TradeExtra.$.deliveryStatus, DeliveryStatus.WAITINT_DELIVERY);
                    break;
                case DELIVERYING:                    where.eq(TradeExtra.$.deliveryStatus, DeliveryStatus.DELIVERYING);
                    break;
                case REAL_DELIVERY:                    where.eq(TradeExtra.$.deliveryStatus, DeliveryStatus.REAL_DELIVERY);
                    break;
                default:
                    break;
            }
            where.and().gt(Trade.$.serverCreateTime, cad.getTime().getTime());
            qb.orderBy(Trade.$.serverCreateTime, false);
            List<TradeExtra> tradeExtraList = qb.query();
            for (TradeExtra extra : tradeExtraList) {
                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                Trade trade = tradeDao.queryForId(extra.getTradeUuid());
                if (trade != null) {
                    if (DeliveryType.SEND.equalsValue(trade.getDeliveryType().value())
                            && TradeType.SELL.equalsValue(trade.getTradeType().value())
                            && StatusFlag.VALID.equalsValue(trade.getStatusFlag().value())) {
                        TradeVo tradeVo = findTradeVo(helper, trade);
                        if (status == TradeDeliveryStatus.UN_PROCESS) {                            if (TradeStatus.UNPROCESSED.equalsValue(trade.getTradeStatus().value())) {
                                tradeVoList.add(tradeVo);
                            }
                        } else if (status == TradeDeliveryStatus.WAITINT_DELIVERY) {                                                        if (TradeStatus.CONFIRMED.equalsValue(trade.getTradeStatus().value())) {
                                tradeVoList.add(tradeVo);
                            }
                        } else if (status == TradeDeliveryStatus.REAL_DELIVERY) {                            if (DateTimeUtils.getCurrentDayStart() == trade.getBizDate()) {
                                tradeVoList.add(tradeVo);
                            }
                        } else {
                            tradeVoList.add(tradeVo);
                        }
                    }
                }
            }
            return tradeVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> findTradeVoList(TradeStatus status)
            throws SQLException {
        return findTradeVoList(status, null);
    }

    @Override
    public List<TradeVo> findTradeVoList(TradeStatus status, SourceId sourceId)
            throws SQLException {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List<Trade> tradeList = findTradeList(status, sourceId);
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
            for (Trade trade : tradeList) {
                TradeVo tradeVo = findTradeVo(helper, trade);
                tradeVoList.add(tradeVo);
                break;
            }

            return tradeVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Trade> findTradeList(TradeStatus status)
            throws SQLException {
        return findTradeList(status, null);
    }

    @Override
    public List<Trade> findTradeList(TradeStatus status, SourceId sourceId)
            throws SQLException {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);

            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();
            if (sourceId != null) {
                where.eq(Trade.$.sourceId, sourceId).and();
            }
            where.eq(Trade.$.tradeType, TradeType.SELL).and().eq(Trade.$.statusFlag, StatusFlag.VALID).and();
            if (status != null) {
                where.eq(Trade.$.tradeStatus, status).and();
            }

            where.gt(Trade.$.serverCreateTime, cad.getTime().getTime());
            qb.orderBy(Trade.$.serverCreateTime, false);
            List<Trade> tradeList = qb.query();
                        if (status == TradeStatus.UNPROCESSED) {
                for (int i = tradeList.size() - 1; i >= 0; i--) {
                    Trade trade = tradeList.get(i);
                    if (trade.getTradePayStatus() == TradePayStatus.UNPAID
                            && trade.getTradePayForm() == TradePayForm.ONLINE) {
                        tradeList.remove(i);
                    }
                }
            }
            return tradeList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> findPizzaHutTradeVoList()
            throws SQLException {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            List<Trade> tradeList = findPizzaHutTradeList();
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
            for (Trade trade : tradeList) {
                if (TradeType.SELL.equalsValue(trade.getTradeType().value())
                        && StatusFlag.VALID.equalsValue(trade.getStatusFlag().value())) {
                    TradeVo tradeVo = findTradeVo(helper, trade);
                    tradeVoList.add(tradeVo);
                }
            }
            return tradeVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Trade> findPizzaHutTradeList()
            throws SQLException {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);

            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();
            Where<Trade, String> where = qb.where();
                        where.eq(Trade.$.sourceId, SourceId.KIOSK).and().eq(Trade.$.sourceChild, SourceChild.PIZZAHUT);
            where.and().eq(Trade.$.tradeStatus, TradeStatus.CONFIRMED).and().eq(Trade.$.tradePayStatus,
                    TradePayStatus.UNPAID);
            where.and().gt(Trade.$.serverCreateTime, cad.getTime().getTime());
            qb.orderBy(Trade.$.serverCreateTime, false);

            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeVo findTradeVoByPhone(String phone)
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);
            QueryBuilder<TradeCustomer, String> qb = tradeCustomerDao.queryBuilder();
            qb.where()
                    .eq(TradeCustomer.$.customerPhone, phone)
                    .and()
                    .eq(TradeCustomer.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .gt(TradeCustomer.$.serverCreateTime, cad.getTime().getTime());
            qb.orderBy(TradeCustomer.$.serverUpdateTime, false);
            List<TradeCustomer> tradeCustomerList = qb.query();
            List<String> tradeUuids = new ArrayList<String>();
            if (tradeCustomerList != null && tradeCustomerList.size() > 0) {
                for (TradeCustomer customer : tradeCustomerList) {
                    tradeUuids.add(customer.getTradeUuid());
                }
            }
            if (tradeUuids != null && tradeUuids.size() > 0) {
                Dao<TradeExtra, String> extraDao = helper.getDao(TradeExtra.class);
                QueryBuilder<TradeExtra, String> extraQb = extraDao.queryBuilder();
                extraQb.where().in(TradeExtra.$.tradeUuid, tradeUuids);
                extraQb.orderBy(TradeExtra.$.serverUpdateTime, false);
                TradeExtra extra = extraQb.queryForFirst();
                if (extra != null) {
                    return findTrade(extra.getTradeUuid());
                }
            }
            return null;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> findTradeVoBySenderId(String senderId)
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> qb = tradeExtraDao.queryBuilder();
            List<TradeVo> tradeVoList = new ArrayList<TradeVo>();

            qb.where().eq(TradeExtra.$.deliveryUserId, senderId).and().between(TradeExtra.$.deliveryStatus, 0, 2);

            qb.orderBy(Trade.$.clientCreateTime, false);

            List<TradeExtra> tradeExtraList = qb.query();
            if (tradeExtraList != null) {
                for (TradeExtra extra : tradeExtraList) {
                    Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                    Trade trade = tradeDao.queryForId(extra.getTradeUuid());
                    if (DeliveryType.SEND.equalsValue(trade.getDeliveryType().value())
                            && TradeType.SELL.equalsValue(trade.getTradeType().value())) {
                        TradeVo tradeVo = findTradeVo(helper, trade);
                        tradeVoList.add(tradeVo);
                    }
                }
            }
            return tradeVoList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    private TradeVo findTradeAndExtra(DatabaseHelper helper, Trade trade)
            throws Exception {
        TradeVo tradeVo = null;
        if (trade != null) {
            tradeVo = new TradeVo();
            tradeVo.setTrade(trade);
                        TradeExtra tradeExtra = helper.getDao(TradeExtra.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeExtra.$.tradeUuid, trade.getUuid())
                    .queryForFirst();
            tradeVo.setTradeExtra(tradeExtra);
                        List<TradeCustomer> tradeCustomerList =
                    helper.getDao(TradeCustomer.class).queryForEq(TradeCustomer.$.tradeUuid, trade.getUuid());
            tradeVo.setTradeCustomerList(tradeCustomerList);
        }
        return tradeVo;
    }

    @Override
    public List<SenderOrderItem> findSenderOrderByUserId(String userId)
            throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Dao<TradeExtra, String> extraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> extraQb = extraDao.queryBuilder();
            extraQb.where()
                    .eq(TradeExtra.$.deliveryUserId, userId)
                    .and()
                    .eq(TradeExtra.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .gt(TradeExtra.$.serverCreateTime, cad.getTime().getTime());
            extraQb.orderBy(TradeExtra.$.serverCreateTime, false);
            List<TradeExtra> tradeExtraList = extraQb.query();
            List<SenderOrderItem> senderOrderList = new ArrayList<SenderOrderItem>();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            for (TradeExtra extra : tradeExtraList) {
                Trade trade = tradeDao.queryForId(extra.getTradeUuid());
                if ((TradeStatus.CONFIRMED.equalsValue(trade.getTradeStatus().value())
                        || TradeStatus.FINISH.equalsValue(trade.getTradeStatus().value()))
                        && TradeType.SELL.equalsValue(trade.getTradeType().value())) {
                    SenderOrderItem item = new SenderOrderItem();
                    TradeVo vo = new TradeVo();
                    vo.setTrade(trade);
                    vo.setTradeExtra(extra);
                                        List<TradeCustomer> tradeCustomerList =
                            helper.getDao(TradeCustomer.class).queryForEq(TradeCustomer.$.tradeUuid, trade.getUuid());
                    vo.setTradeCustomerList(tradeCustomerList);
                    List<PaymentVo> paymentList = listPaymentVo(helper, extra.getTradeUuid());
                    item.setTraderVo(vo);
                    item.setPaymentList(paymentList);
                    senderOrderList.add(item);
                }
            }
            return senderOrderList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeStatusLog findTradeStatusLog(String uuid)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeStatusLog, String> tradeDao = helper.getDao(TradeStatusLog.class);
            QueryBuilder<TradeStatusLog, String> qb = tradeDao.queryBuilder();
            qb.where().eq(TradeStatusLog.$.tradeUuid, uuid).and().in(TradeStatusLog.$.tradeStatus,
                    TradeStatus.CONFIRMED,
                    TradeStatus.FINISH);
            TradeStatusLog log = qb.queryForFirst();
            return log;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeStatusLog> findTradeStatusLog(String tradeUuid, TradeStatus tradeStatus) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeStatusLog, String> dao = helper.getDao(TradeStatusLog.class);
            QueryBuilder<TradeStatusLog, String> qb = dao.queryBuilder();
            Where<TradeStatusLog, String> where = qb.where();
            where = where.eq(TradeStatusLog.$.tradeUuid, tradeUuid).and().eq(TradeStatusLog.$.statusFlag, StatusFlag.VALID);
            if (tradeStatus != null) {
                where.and().eq(TradeStatusLog.$.tradeStatus, tradeStatus);
            }
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PaymentVo> listAdjustPayment()
            throws Exception {
                Calendar cad = Calendar.getInstance();
        cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
        cad.add(Calendar.DAY_OF_MONTH, -3);
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Payment, String> dao = helper.getDao(Payment.class);
            Dao<PaymentItem, String> itemDao = helper.getDao(PaymentItem.class);
            List<PaymentVo> voList = new ArrayList<PaymentVo>();
            QueryBuilder<Payment, String> paymentBuild = dao.queryBuilder();
            paymentBuild.where().eq(Payment.$.paymentType, PaymentType.ADJUST).and().gt(Payment.$.serverCreateTime,
                    cad.getTime().getTime());
            paymentBuild.orderBy(Payment.$.serverCreateTime, false);
            List<Payment> paymentList = paymentBuild.query();
            for (Payment payment : paymentList) {
                PaymentVo vo = new PaymentVo();
                vo.setPayment(payment);
                List<PaymentItem> itemList = itemDao.queryForEq(PaymentItem.$.paymentUuid, payment.getUuid());
                if (itemList != null && itemList.size() > 0) {
                    reSortItems(itemList);
                }
                vo.setPaymentItemList(itemList);
                voList.add(vo);
            }
            return voList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Trade findRepeatTrade(String tradeUuid)
            throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where()
                    .eq(Trade.$.relateTradeUuid, tradeUuid)
                    .and()
                    .eq(Trade.$.tradeType, TradeType.SELL_FOR_REPEAT)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }


    private static void clearExpiredPending(final DatabaseHelper helper)
            throws Exception {
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call()
                    throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
                calendar.add(Calendar.DAY_OF_MONTH, -2);                long minBizDate = calendar.getTimeInMillis();
                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
                qb.selectColumns(Trade.$.uuid);
                qb.where()
                        .eq(Trade.$.tradeStatus, TradeStatus.TEMPORARY)
                        .and()
                        .eq(Trade.$.statusFlag, StatusFlag.VALID)
                        .and()
                        .lt(Trade.$.bizDate, minBizDate);
                List<Trade> tradeList = qb.query();
                for (Trade trade : tradeList) {
                    TradeManageUtils.delete(helper, trade.getUuid());
                }
                return null;
            }
        };
        helper.callInTransaction(callable);
    }

    private TradeVo findTradeVo(DatabaseHelper helper, Trade trade)
            throws SQLException {
        return findTradeVo(helper, trade, true);
    }


    @Override
    public List<Trade> listTradeByUUID(List<String> uuids) throws Exception {
                Checks.verifyNotNull(uuids, "uuids");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where()
                    .in(Trade.$.uuid, uuids);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeVo> getTradeVosByTrades(List<Trade> trades) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return getTradeVosByTrades(trades, helper, false);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }


    private List<TradeVo> getTradeVosByTrades(List<Trade> trades, DatabaseHelper helper, boolean onlyValid) throws Exception {
        if (Utils.isEmpty(trades)) {
            return null;
        }

        Map<String, Trade> mapTrade = new HashMap<>();
        List<String> tradeUUIIDs = new ArrayList<String>();
        List<String> relateTradeUUIDs = new ArrayList<>();
        List<Long> tradeIDs = new ArrayList<Long>();
        List<Long> relateTradeIds = new ArrayList<>();

        for (Trade trade : trades) {
            tradeUUIIDs.add(trade.getUuid());
            mapTrade.put(trade.getUuid(), trade);
            if (trade.getId() != null) {
                tradeIDs.add(trade.getId());
                relateTradeIds.add(getRelateId(trade));
            }
            if (trade.getRelateTradeUuid() != null) {
                relateTradeUUIDs.add(trade.getRelateTradeUuid());
            }
        }

                List<TradeExtra> tradeExtras = helper.getDao(TradeExtra.class)
                .queryBuilder()
                .where()
                .in(TradeExtra.$.tradeUuid, tradeUUIIDs)
                .query();

        Map<String, TradeExtra> mapTradeExtra = new HashMap<>();
        for (TradeExtra tradeExtra : tradeExtras) {
            mapTradeExtra.put(tradeExtra.getTradeUuid(), tradeExtra);
        }


                List<TradeDeposit> tradeDeposits = helper.getDao(TradeDeposit.class)
                .queryBuilder()
                .where()
                .in(TradeDeposit.$.tradeUuid, tradeUUIIDs)
                .query();

        Map<String, TradeDeposit> mapTradeDeposit = new HashMap<>();
        for (TradeDeposit tradeDeposit : tradeDeposits) {
            mapTradeDeposit.put(tradeDeposit.getTradeUuid(), tradeDeposit);
        }

                List<TradeUser> tradeUsers = helper.getDao(TradeUser.class)
                .queryBuilder()
                .where()
                .in(TradeUser.$.tradeId, tradeIDs)
                .and().isNull(TradeUser.$.tradeItemId)                .query();

        Map<Long, TradeUser> mapTradeUser = new HashMap<>();
        for (TradeUser tradeUser : tradeUsers) {
            mapTradeUser.put(tradeUser.getTradeId(), tradeUser);
        }

                List<TradeDepositPayRelation> tradeDepositPayRelations = helper.getDao(TradeDepositPayRelation.class)
                .queryBuilder()
                .orderBy(TradeDepositPayRelation.$.serverCreateTime, false)
                .where()
                .in(TradeDepositPayRelation.$.tradeUuid, tradeUUIIDs)
                .and().eq(TradeDepositPayRelation.$.depositType, 1)
                .query();

        Map<String, TradeDepositPayRelation> mapTradeDepositPayRelation = new HashMap<>();
        Map<Long, PaymentItem> mapDepositPaymentItem = new HashMap<>();

        if (Utils.isNotEmpty(tradeDepositPayRelations)) {
            List<Long> depositPayRelationPaymentItemsIds = new ArrayList<Long>();
            for (TradeDepositPayRelation relation : tradeDepositPayRelations) {
                depositPayRelationPaymentItemsIds.add(relation.getPaymentItemId());
            }

            List<PaymentItem> paymentItem = helper.getDao(PaymentItem.class)
                    .queryBuilder()
                    .where()
                    .in(PaymentItem.$.id, depositPayRelationPaymentItemsIds)
                    .and().eq(PaymentItem.$.payStatus, TradePayStatus.PAID)
                    .query();

            for (PaymentItem item : paymentItem) {
                mapDepositPaymentItem.put(item.getId(), item);
            }

            for (TradeDepositPayRelation depositRelation : tradeDepositPayRelations) {                if (mapDepositPaymentItem.containsKey(depositRelation.getPaymentItemId())) {
                    mapTradeDepositPayRelation.put(depositRelation.getTradeUuid(), depositRelation);
                }
            }
        }


                List<TradeCustomer> tradeCustomerList = helper.getDao(TradeCustomer.class)
                .queryBuilder()
                .orderBy(TradeCustomer.$.serverUpdateTime, false)
                .where()
                .in(TradeCustomer.$.tradeUuid, tradeUUIIDs)
                .query();

        Map<String, List<TradeCustomer>> mapTradeCustomer = new HashMap<>();
        for (TradeCustomer tradeCustomer : tradeCustomerList) {
            if (!mapTradeCustomer.containsKey(tradeCustomer.getTradeUuid())) {
                mapTradeCustomer.put(tradeCustomer.getTradeUuid(), new ArrayList<TradeCustomer>());
            }
            mapTradeCustomer.get(tradeCustomer.getTradeUuid()).add(tradeCustomer);
        }


                List<TradeBuffetPeople> tradeBuffetPeoplesList = helper.getDao(TradeBuffetPeople.class)
                .queryBuilder().where()
                .in(TradeBuffetPeople.$.tradeUuid, tradeUUIIDs)
                .query();

        Map<String, List<TradeBuffetPeople>> mapTradeBuffetPeoples = new HashMap<>();
        for (TradeBuffetPeople tradeBuffetPeople : tradeBuffetPeoplesList) {
            if (!mapTradeBuffetPeoples.containsKey(tradeBuffetPeople.getTradeUuid())) {
                mapTradeBuffetPeoples.put(tradeBuffetPeople.getTradeUuid(), new ArrayList<TradeBuffetPeople>());
            }
            mapTradeBuffetPeoples.get(tradeBuffetPeople.getTradeUuid()).add(tradeBuffetPeople);
        }

                List<TradeTable> tradeTableList = helper.getDao(TradeTable.class).queryBuilder().where().in(TradeTable.$.tradeUuid, tradeUUIIDs).query();

        Map<String, List<TradeTable>> mapTradeTable = new HashMap<>();
        for (TradeTable tradeTable : tradeTableList) {
            if (!mapTradeTable.containsKey(tradeTable.getTradeUuid())) {
                mapTradeTable.put(tradeTable.getTradeUuid(), new ArrayList<TradeTable>());
            }
            mapTradeTable.get(tradeTable.getTradeUuid()).add(tradeTable);
        }


                List<TradePlanActivity> tradePlanActivitys = helper.getDao(TradePlanActivity.class)
                .queryBuilder()
                .where()
                .in(TradePlanActivity.$.tradeUuid, tradeUUIIDs)
                .and()
                .eq(TradePlanActivity.$.statusFlag, StatusFlag.VALID)
                .query();

        Map<String, List<TradePlanActivity>> mapTradePlanActivitys = new HashMap<>();
        List<String> tradePlanUUIDs = new ArrayList<String>();
        for (TradePlanActivity tradePlanActivity : tradePlanActivitys) {
            if (tradePlanActivity.getUuid() != null) {
                tradePlanUUIDs.add(tradePlanActivity.getUuid());
            }
            if (!mapTradePlanActivitys.containsKey(tradePlanActivity.getTradeUuid())) {
                mapTradePlanActivitys.put(tradePlanActivity.getTradeUuid(), new ArrayList<TradePlanActivity>());
            }
            mapTradePlanActivitys.get(tradePlanActivity.getTradeUuid()).add(tradePlanActivity);
        }

                List<TradeItemPlanActivity> tradeItemPlanActivities = helper.getDao(TradeItemPlanActivity.class)
                .queryBuilder()
                .where()
                .in(TradeItemPlanActivity.$.relUuid, tradePlanUUIDs)
                .and()
                .eq(TradeItemPlanActivity.$.statusFlag, StatusFlag.VALID)
                .query();
        Map<Long, List<TradeItemPlanActivity>> mapTradeItemPlanActivity = new HashMap<>();
        for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivities) {
            if (!mapTradeItemPlanActivity.containsKey(tradeItemPlanActivity.getTradeId())) {
                mapTradeItemPlanActivity.put(tradeItemPlanActivity.getTradeId(), new ArrayList<TradeItemPlanActivity>());
            }
            mapTradeItemPlanActivity.get(tradeItemPlanActivity.getTradeId()).add(tradeItemPlanActivity);
        }

                        List<TradePrivilege> tpList = helper.getDao(TradePrivilege.class)
                .queryBuilder()
                .orderBy(TradePrivilege.$.privilegeType, true)
                .where()
                .in(TradePrivilege.$.tradeUuid, tradeUUIIDs)
                .query();

        List<Long> privilegeRuleIds = new ArrayList<>();
        List<Long> extraChargeIds = new ArrayList<>();
        for (TradePrivilege tradePrivilege : tpList) {
            if (tradePrivilege.getPromoId() == null || tradePrivilege.getPromoId() <= 0) {
                continue;
            }

            if (tradePrivilege.getPrivilegeType() == PrivilegeType.INTEGRALCASH) {
                privilegeRuleIds.add(tradePrivilege.getPromoId());
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL
                    || tradePrivilege.getPrivilegeType() == PrivilegeType.SERVICE) {
                extraChargeIds.add(tradePrivilege.getPromoId());
            }

        }

        List<ExtraCharge> extraCharges = helper.getDao(ExtraCharge.class).queryBuilder().where()
                .eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                .and()
                .eq(ExtraCharge.$.enabledFlag, Bool.YES)
                .and()
                .ne(ExtraCharge.$.code, ExtraManager.BUFFET_OOUTTIME_CODE)
                .and()
                .in(ExtraCharge.$.id, extraChargeIds)
                .query();
                ExtraCharge minConsumCharge = helper.getDao(ExtraCharge.class).queryBuilder().where()
                .eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                .and()
                .eq(ExtraCharge.$.enabledFlag, Bool.YES)
                .and()
                .eq(ExtraCharge.$.code, ExtraManager.BUFFET_MIN_CONSUM)
                .queryForFirst();
        Map<Long, ExtraCharge> mapExtraCharge = new HashMap<>();
        for (ExtraCharge extraCharge : extraCharges) {
            mapExtraCharge.put(extraCharge.getId(), extraCharge);
        }

                List<CrmCustomerLevelRights> memberRules = helper.getDao(CrmCustomerLevelRights.class).queryBuilder().where().in(CrmCustomerLevelRights.$.id, privilegeRuleIds).query();
        Map<Long, CrmCustomerLevelRights> mapMemberRule = new HashMap<>();
        for (CrmCustomerLevelRights memberRule : memberRules) {
            mapMemberRule.put(memberRule.getId(), memberRule);
        }

                List<EcCardLevelSetting> cardRules = helper.getDao(EcCardLevelSetting.class).queryBuilder().where().in(EcCardLevelSetting.$.id, privilegeRuleIds).query();
        Map<Long, EcCardLevelSetting> mapCardRule = new HashMap<>();
        for (EcCardLevelSetting cardRule : cardRules) {
            mapCardRule.put(cardRule.getId(), cardRule);
        }


        List<TradePrivilegeExtra> tpExtraList = helper.getDao(TradePrivilegeExtra.class)
                .queryBuilder()
                .orderBy(TradePrivilegeExtra.$.serverUpdateTime, true)
                .where()
                .in(TradePrivilegeExtra.$.tradeUuid, tradeUUIIDs)
                .and()
                .eq(TradePrivilegeExtra.$.statusFlag, StatusFlag.VALID)
                .query();

        Map<Long, TradePrivilegeExtra> mapTpExtra = new HashMap<>();
        Map<String, Map<Long, TradePrivilegeExtra>> mapTradePrivilegeExtra = new HashMap<>();
        for (TradePrivilegeExtra tradePrivilegeExtra : tpExtraList) {
            if (!mapTradePrivilegeExtra.containsKey(tradePrivilegeExtra.getTradeUuid())) {
                mapTradePrivilegeExtra.put(tradePrivilegeExtra.getTradeUuid(), new HashMap<Long, TradePrivilegeExtra>());
            }

            mapTradePrivilegeExtra.get(tradePrivilegeExtra.getTradeUuid()).put(tradePrivilegeExtra.getTradePrivilegeId(), tradePrivilegeExtra);
            mapTpExtra.put(tradePrivilegeExtra.getTradePrivilegeId(), tradePrivilegeExtra);
        }

        Map<String, List<CouponPrivilegeVo>> mapCouponPrivilege = new HashMap<>();        Map<String, IntegralCashPrivilegeVo> mapIntegralPrivilege = new HashMap<>();        Map<String, List<WeiXinCouponsVo>> mapWeiXinCouponPrivilege = new HashMap<>();        Map<String, BanquetVo> mapBanquetPrivelegeo = new HashMap<>();        Map<String, Map<Long, ExtraCharge>> mapTradeExtraCharge = new HashMap<>();        Map<String, List<TradePrivilege>> mapTradePrivilege = new HashMap<>();        Map<String, TradePrivilege> mapTradeItemPrivilege = new HashMap<>();        for (TradePrivilege tp : tpList) {
            if (TextUtils.isEmpty(tp.getTradeItemUuid())) {
                                if (tp.getPrivilegeType() == PrivilegeType.COUPON) {
                    CouponPrivilegeVo couponPrivilege = new CouponPrivilegeVo();
                    couponPrivilege.setTradePrivilege(tp);
                    if (mapTpExtra.get(tp.getId()) != null) {
                        couponPrivilege.setTradePrivilegeExtra(mapTpExtra.get(tp.getId()));
                    }
                    if (tp.getPromoId() != null) {
                        Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                        Coupon coupon = couponDao.queryForId(tp.getPromoId());
                        couponPrivilege.setCoupon(coupon);
                    }

                    Coupon coupon = couponPrivilege.getCoupon();
                    if (coupon != null) {
                                                if (coupon.getCouponType() == CouponType.GIFT) {
                            if (mapTrade.get(tp.getTradeUuid()).getTradeAmount().compareTo(coupon.getFullValue()) >= 0) {
                                couponPrivilege.setActived(true);
                            }
                        } else {
                                                        if (couponPrivilege.getTradePrivilege()
                                    .getPrivilegeAmount()
                                    .compareTo(BigDecimal.ZERO) != 0) {
                                couponPrivilege.setActived(true);
                            }
                        }
                    }

                    if (!mapCouponPrivilege.containsKey(tp.getTradeUuid())) {
                        mapCouponPrivilege.put(tp.getTradeUuid(), new ArrayList<CouponPrivilegeVo>());
                    }
                    mapCouponPrivilege.get(tp.getTradeUuid()).add(couponPrivilege);
                } else if (tp.getPrivilegeType() == PrivilegeType.INTEGRALCASH) {
                    IntegralCashPrivilegeVo integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
                    integralCashPrivilegeVo.setTradePrivilege(tp);
                    integralCashPrivilegeVo.setIntegral(tp.getPrivilegeValue());
                    integralCashPrivilegeVo.setActived(true);
                    if (mapTpExtra.get(tp.getId()) != null) {
                        integralCashPrivilegeVo.setTradePrivilegeExtra(mapTpExtra.get(tp.getId()));
                    }
                                        if (tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) == 0
                            && tp.getPrivilegeValue().compareTo(BigDecimal.ZERO) == 0) {
                        integralCashPrivilegeVo.setActived(false);
                    }

                                        try {
                        Long ruleId = tp.getPromoId();
                        if (ruleId != null && ruleId > 0 && Utils.isNotEmpty(mapTradeCustomer.get(tp.getTradeUuid()))) {
                            List<TradeCustomer> tradeCustomers = mapTradeCustomer.get(tp.getTradeUuid());
                            for (TradeCustomer tradeCustomer : tradeCustomers) {
                                if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
                                    integralCashPrivilegeVo.setRule(mapMemberRule.get(ruleId));
                                } else if (tradeCustomer.getCustomerType() == CustomerType.CARD) {
                                    integralCashPrivilegeVo.setRule(mapCardRule.get(ruleId));
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    mapIntegralPrivilege.put(tp.getTradeUuid(), integralCashPrivilegeVo);
                } else if (tp.getPrivilegeType() == PrivilegeType.WECHAT_CARD_COUPONS) {
                    WeiXinCouponsVo weiXinCouponsVo = new WeiXinCouponsVo();
                    weiXinCouponsVo.setmTradePrivilege(tp);
                    weiXinCouponsVo.setActived(true);
                    if (mapTpExtra.get(tp.getId()) != null) {
                        weiXinCouponsVo.setTradePrivilegeExtra(mapTpExtra.get(tp.getId()));
                    }
                                        if (tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) == 0
                            && tp.getPrivilegeValue().compareTo(BigDecimal.ZERO) == 0) {
                        weiXinCouponsVo.setActived(false);
                    }

                    if (!mapWeiXinCouponPrivilege.containsKey(tp.getTradeUuid())) {
                        mapWeiXinCouponPrivilege.put(tp.getTradeUuid(), new ArrayList<WeiXinCouponsVo>());
                    }
                    mapWeiXinCouponPrivilege.get(tp.getTradeUuid()).add(weiXinCouponsVo);
                } else if (tp.getPrivilegeType() == PrivilegeType.BANQUET) {
                                        BanquetVo banquetVo = new BanquetVo();
                    banquetVo.setTradePrivilege(tp);
                    mapBanquetPrivelegeo.put(tp.getTradeUuid(), banquetVo);
                } else {
                    if (tp.getPrivilegeType() == PrivilegeType.ADDITIONAL
                            || tp.getPrivilegeType() == PrivilegeType.SERVICE) {
                        if (tp.getPromoId() != null) {
                            ExtraCharge extraCharge = mapExtraCharge.get(tp.getPromoId());
                            if (!mapTradeExtraCharge.containsKey(tp.getTradeUuid())) {
                                mapTradeExtraCharge.put(tp.getTradeUuid(), new HashMap<Long, ExtraCharge>());
                            }
                            if (extraCharge != null) {
                                mapTradeExtraCharge.get(tp.getTradeUuid()).put(tp.getPromoId(), cloneExtraCharge(extraCharge));
                            }
                        }
                    }

                    if (!mapTradePrivilege.containsKey(tp.getTradeUuid())) {
                        mapTradePrivilege.put(tp.getTradeUuid(), new ArrayList<TradePrivilege>());
                    }
                    mapTradePrivilege.get(tp.getTradeUuid()).add(tp);
                }
            } else {
                                                mapTradeItemPrivilege.put(tp.getTradeItemUuid(), tp);
            }
        }

                Map<Long, List<TradePromotion>> mapTradePromotion = new HashMap<>();
        if (Utils.isNotEmpty(tradeIDs)) {
            List<TradePromotion> tradePromotions = helper.getDao(TradePromotion.class).queryBuilder()
                    .orderBy(TradePromotion.$.serverUpdateTime, false)
                    .where()
                    .in(TradePromotion.$.tradeId, tradeIDs)
                    .and()
                    .eq(TradePromotion.$.statusFlag, StatusFlag.VALID)
                    .query();

            for (TradePromotion tradePromotion : tradePromotions) {
                if (!mapTradePromotion.containsKey(tradePromotion.getTradeId())) {
                    mapTradePromotion.put(tradePromotion.getTradeId(), new ArrayList<TradePromotion>());
                }
                mapTradePromotion.get(tradePromotion.getTradeId()).add(tradePromotion);
            }
        }


                Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
        QueryBuilder<TradeItem, String> tiQb = tiDao.queryBuilder();
        Where<TradeItem, String> tiWhere = tiQb.where();
        tiWhere.in(TradeItem.$.tradeUuid, tradeUUIIDs);
        if (onlyValid) {
            tiWhere.and().eq(TradeItem.$.statusFlag, StatusFlag.VALID);
        }
        tiQb.orderBy(TradeItem.$.sort, true);
        List<TradeItem> tradeItems = tiQb.query();


        List<Long> tradeItemIds = new ArrayList<>();
        List<String> tradeItemsUUIDs = new ArrayList<String>();
        for (TradeItem tradeItem : tradeItems) {
                        if (tradeItem.getInvalidType() == InvalidType.SUB_BATCH_VITURAL && tradeItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            tradeItemsUUIDs.add(tradeItem.getUuid());
            if (tradeItem.getId() != null) {
                tradeItemIds.add(tradeItem.getId());
            }
        }

                Dao<TradeItemMainBatchRel, String> tradeItemMainBatchRelDao = helper.getDao(TradeItemMainBatchRel.class);
        List<TradeItemMainBatchRel> tradeItemMainBatchRels = tradeItemMainBatchRelDao.queryBuilder().where().in(TradeItemMainBatchRel.$.mainTradeId, tradeIDs).query();

        Map<Long, List<TradeItemMainBatchRel>> mapTradeItemMainBatchs = new HashMap<>();
        for (TradeItemMainBatchRel tradeItemMainBatchRel : tradeItemMainBatchRels) {
            if (!mapTradeItemMainBatchs.containsKey(tradeItemMainBatchRel.getMainItemId())) {
                mapTradeItemMainBatchs.put(tradeItemMainBatchRel.getMainItemId(), new ArrayList<TradeItemMainBatchRel>());
            }

            mapTradeItemMainBatchs.get(tradeItemMainBatchRel.getMainItemId()).add(tradeItemMainBatchRel);
        }

        Dao<TradeItemMainBatchRelExtra, String> tradeItemMainBatchRelExtraDao = helper.getDao(TradeItemMainBatchRelExtra.class);
        List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras = tradeItemMainBatchRelExtraDao.queryBuilder().where().in(TradeItemMainBatchRelExtra.$.mainTradeId, tradeIDs).query();
        Map<Long, List<TradeItemMainBatchRelExtra>> tradeItemMainBatchRelExtraMap = new HashMap<>();
        for (TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra : tradeItemMainBatchRelExtras) {
            if (!tradeItemMainBatchRelExtraMap.containsKey(tradeItemMainBatchRelExtra.getMainTradeId())) {
                tradeItemMainBatchRelExtraMap.put(tradeItemMainBatchRelExtra.getMainTradeId(), new ArrayList<TradeItemMainBatchRelExtra>());
            }

            tradeItemMainBatchRelExtraMap.get(tradeItemMainBatchRelExtra.getMainTradeId()).add(tradeItemMainBatchRelExtra);
        }


                Dao<TradeItemExtraDinner, Long> seatDao = helper.getDao(TradeItemExtraDinner.class);
        List<TradeItemExtraDinner> listSeats = seatDao.queryBuilder().where().in(TradeItemExtraDinner.$.trade_item_id, tradeItemIds)
                .and().eq(TradeItemExtraDinner.$.statusFlag, StatusFlag.VALID).query();

        Map<Long, TradeItemExtraDinner> mapTradeItemExtraDinner = new HashMap<>();
        Map<Long, List<TradeItemExtraDinner>> mapTradeItemExtraDinners = new HashMap<>();
        for (TradeItemExtraDinner listSeat : listSeats) {
            if (!mapTradeItemExtraDinner.containsKey(listSeat.getTradeItemId())) {
                mapTradeItemExtraDinners.put(listSeat.getTradeItemId(), new ArrayList<TradeItemExtraDinner>());
            }

            mapTradeItemExtraDinner.put(listSeat.getTradeItemId(), listSeat);
            mapTradeItemExtraDinners.get(listSeat.getTradeItemId()).add(listSeat);
        }


                Dao<TradeItemProperty, String> tipDao = helper.getDao(TradeItemProperty.class);
        Map<String, List<TradeItemProperty>> tradeItemPropertyListMap = new HashMap<String, List<TradeItemProperty>>();
        List<TradeItemProperty> TradeItemPropertyList =
                tipDao.queryBuilder().where().in(TradeItemProperty.$.tradeItemUuid, tradeItemsUUIDs).query();
        for (TradeItemProperty property : TradeItemPropertyList) {
            if (tradeItemPropertyListMap.get(property.getTradeItemUuid()) != null) {
                tradeItemPropertyListMap.get(property.getTradeItemUuid()).add(property);
            } else {
                List<TradeItemProperty> itemProperties = new ArrayList<TradeItemProperty>();
                itemProperties.add(property);
                tradeItemPropertyListMap.put(property.getTradeItemUuid(), itemProperties);
            }
        }

                Dao<TradeReasonRel, String> reasonRelDao = helper.getDao(TradeReasonRel.class);
        List<TradeReasonRel> reasionList = reasonRelDao.queryBuilder()
                .orderBy(TradeReasonRel.$.serverUpdateTime, true)
                .where()
                                                .in(TradeReasonRel.$.relateUuid, tradeItemsUUIDs)
                .and()
                .eq(TradeReasonRel.$.statusFlag, StatusFlag.VALID)
                .query();

        Map<String, List<TradeReasonRel>> mapTradeReasons = new HashMap<>();
        for (TradeReasonRel tradeReasonRel : reasionList) {
            if (!mapTradeReasons.containsKey(tradeReasonRel.getRelateUuid())) {
                mapTradeReasons.put(tradeReasonRel.getRelateUuid(), new ArrayList<TradeReasonRel>());
            }
            mapTradeReasons.get(tradeReasonRel.getRelateUuid()).add(tradeReasonRel);
        }


                Dao<TradeItemOperation, String> dao = helper.getDao(TradeItemOperation.class);
        QueryBuilder<TradeItemOperation, String> qb = dao.queryBuilder();
        qb.where().in(TradeItemOperation.$.tradeItemId, tradeItemIds);
        qb.orderBy(TradeItemOperation.$.serverUpdateTime, true);
        List<TradeItemOperation> tradeItemOperations = qb.query();

        Map<Long, List<TradeItemOperation>> mapTradeItemOperations = new HashMap<>();
        for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
            if (!mapTradeItemOperations.containsKey(tradeItemOperation.getTradeItemId())) {
                mapTradeItemOperations.put(tradeItemOperation.getTradeItemId(), new ArrayList<TradeItemOperation>());
            }
            mapTradeItemOperations.get(tradeItemOperation.getTradeItemId()).add(tradeItemOperation);
        }


                List<TradeItemExtra> tradeItemExtras = helper.getDao(TradeItemExtra.class)
                .queryBuilder().orderBy(TradeItemExtra.$.clientUpdateTime, false)
                .where()
                .in(TradeItemExtra.$.tradeItemUuid, tradeItemsUUIDs)
                .query();

        Map<String, TradeItemExtra> mapTradeItemExtra = new HashMap<>();
        for (TradeItemExtra tradeItemExtra : tradeItemExtras) {
            mapTradeItemExtra.put(tradeItemExtra.getTradeItemUuid(), tradeItemExtra);
        }


                Map<String, MealShellVo> mapMealShellVo = new HashMap<>();
        Map<String, List<TradeItemVo>> mapTradeItemVo = new HashMap<>();
        for (TradeItem tradeItem : tradeItems) {
            if (tradeItem.getType() == DishType.MEAL_SHELL || tradeItem.getType() == DishType.BUFFET_COMBO_SHELL) {
                MealShellVo mealHellVo = new MealShellVo();
                mealHellVo.setTradeItem(tradeItem);
                mapMealShellVo.put(tradeItem.getTradeUuid(), mealHellVo);
                continue;
            }
            if (tradeItem.getInvalidType() == InvalidType.SUB_BATCH_VITURAL && tradeItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            TradeItemVo tiVo = new TradeItemVo();
            tiVo.setTradeItem(tradeItem);
            tiVo.setTradeItemMainBatchRelList(mapTradeItemMainBatchs.get(tradeItem.getId()));            tiVo.setTradeItemExtraDinner(mapTradeItemExtraDinner.get(tradeItem.getId()));


            tiVo.setTradeItemExtra(mapTradeItemExtra.get(tradeItem.getUuid()));
                        TradePrivilege tradePrivilege = mapTradeItemPrivilege.get(tradeItem.getUuid());

            if (tradePrivilege != null) {
                if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == PrivilegeType.COUPON) {
                    CouponPrivilegeVo couponPrivilegeVo = new CouponPrivilegeVo();
                    couponPrivilegeVo.setTradePrivilege(tradePrivilege);
                    if (tradePrivilege.getPromoId() != null) {
                        Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                        Coupon coupon = couponDao.queryForId(tradePrivilege.getPromoId());
                        couponPrivilegeVo.setCoupon(coupon);
                        Dao<CoupRule, Long> coupRuleDao = helper.getDao(CoupRule.class);
                        List<CoupRule> coupRuleList = coupRuleDao.queryForEq(CoupRule.$.couponId, tradePrivilege.getPromoId());
                    }
                    if (mapTpExtra.containsKey(tradePrivilege.getId())) {
                        couponPrivilegeVo.setTradePrivilegeExtra(mapTpExtra.get(tradePrivilege.getId()));
                    }
                    tiVo.setCouponPrivilegeVo(couponPrivilegeVo);
                } else {
                    tiVo.setTradeItemPrivilege(tradePrivilege);
                }
            }

                        List<TradeItemProperty> tips = tradeItemPropertyListMap.get(tradeItem.getUuid());

            tiVo.setTradeItemPropertyList(tips);

            tiVo.setReasonRelList(mapTradeReasons.get(tradeItem.getUuid()));

            tiVo.setTradeItemOperations(mapTradeItemOperations.get(tradeItem.getId()));

            if (!mapTradeItemVo.containsKey(tradeItem.getTradeUuid())) {
                mapTradeItemVo.put(tradeItem.getTradeUuid(), new ArrayList<TradeItemVo>());
            }
            mapTradeItemVo.get(tradeItem.getTradeUuid()).add(tiVo);
        }


        Map<String, List<TradeItemVo>> mapBuffetPeople = new HashMap<>();
        if (Utils.isNotEmpty(tradeBuffetPeoplesList)) {
            for (TradeBuffetPeople tradeBuffetPeople : tradeBuffetPeoplesList) {
                MealShellVo mealShellVo = mapMealShellVo.get(tradeBuffetPeople.getTradeUuid());
                if (mealShellVo == null) {
                    continue;
                }

                TradeItemVo tiVo = new TradeItemVo();
                TradeItem tradeItem = new TradeItem();
                tradeItem.setId(-1l);
                tradeItem.setDishName(mealShellVo.getSkuName() + "-" + tradeBuffetPeople.getCarteNormsName());
                tradeItem.setPrice(tradeBuffetPeople.getCartePrice());
                tradeItem.setQuantity(tradeBuffetPeople.getPeopleCount());
                tiVo.setTradeItem(tradeItem);

                if (!mapBuffetPeople.containsKey(tradeBuffetPeople.getTradeUuid())) {
                    mapBuffetPeople.put(tradeBuffetPeople.getTradeUuid(), new ArrayList<TradeItemVo>());
                }
                mapBuffetPeople.get(tradeBuffetPeople.getTradeUuid()).add(tiVo);
            }
        }


                List<TradeReasonRel> tradeReasonRelList = helper.getDao(TradeReasonRel.class)
                .queryBuilder()
                .orderBy(TradeReasonRel.$.serverUpdateTime, false)
                .where()
                .in(TradeReasonRel.$.relateUuid, tradeUUIIDs)
                .query();

        Map<String, List<TradeReasonRel>> mapTradeReasonRel = new HashMap<>();
        for (TradeReasonRel tradeReasonRel : tradeReasonRelList) {
            if (!mapTradeReasonRel.containsKey(tradeReasonRel.getRelateUuid())) {
                mapTradeReasonRel.put(tradeReasonRel.getRelateUuid(), new ArrayList<TradeReasonRel>());
            }
            mapTradeReasonRel.get(tradeReasonRel.getRelateUuid()).add(tradeReasonRel);
        }


        Map<Long, List<TradeReasonRel>> mapTradeRelateReasonRel = new HashMap<>();
        if (Utils.isNotEmpty(relateTradeIds)) {
            List<TradeReasonRel> tradeRelateReasonRelList = helper.getDao(TradeReasonRel.class)
                    .queryBuilder()
                    .orderBy(TradeReasonRel.$.serverUpdateTime, false)
                    .where()
                    .in(TradeReasonRel.$.relateId, relateTradeIds)
                    .query();

            for (TradeReasonRel tradeReasonRel : tradeRelateReasonRelList) {
                if (!mapTradeRelateReasonRel.containsKey(tradeReasonRel.getRelateUuid())) {
                    mapTradeRelateReasonRel.put(tradeReasonRel.getRelateId(), new ArrayList<TradeReasonRel>());
                }
                mapTradeRelateReasonRel.get(tradeReasonRel.getRelateId()).add(tradeReasonRel);
            }
        }


                List<TradeCreditLog> tradeCreditLogList = helper.getDao(TradeCreditLog.class)
                .queryBuilder()
                .where()
                .in(TradeCreditLog.$.tradeId, tradeIDs)
                .query();

        Map<Long, List<TradeCreditLog>> mapTradeCreditLog = new HashMap<>();
        for (TradeCreditLog tradeCreditLog : tradeCreditLogList) {
            if (!mapTradeCreditLog.containsKey(tradeCreditLog.getTradeId())) {
                mapTradeCreditLog.put(tradeCreditLog.getTradeId(), new ArrayList<TradeCreditLog>());
            }

            mapTradeCreditLog.get(tradeCreditLog.getTradeId()).add(tradeCreditLog);
        }


                List<TradeStatusLog> startTradeStatusLogs = helper.getDao(TradeStatusLog.class)
                .queryBuilder()
                .orderBy(TradeStatusLog.$.serverCreateTime, true)
                .where()
                .in(TradeStatusLog.$.tradeUuid, tradeUUIIDs)
                .and()
                .eq(TradeStatusLog.$.tradeStatus, TradeStatus.FINISH)
                .and()
                .eq(TradeStatusLog.$.tradePayStatus, TradePayStatus.PAID)
                .query();

        Map<String, List<TradeStatusLog>> mapTradeStatusLog = new HashMap<>();
        for (TradeStatusLog startTradeStatusLog : startTradeStatusLogs) {
            if (!mapTradeStatusLog.containsKey(startTradeStatusLog.getTradeUuid())) {
                mapTradeStatusLog.put(startTradeStatusLog.getTradeUuid(), new ArrayList<TradeStatusLog>());
            }

            mapTradeStatusLog.get(startTradeStatusLog.getTradeUuid()).add(startTradeStatusLog);
        }

                List<TradeStatusLog> endTradeStatusLogs = helper.getDao(TradeStatusLog.class)
                .queryBuilder()
                .where()
                .in(TradeStatusLog.$.tradeUuid, tradeUUIIDs)
                .and()
                .eq(TradeStatusLog.$.tradeStatus, -1)
                .query();

        for (TradeStatusLog endTradeStatusLog : endTradeStatusLogs) {
            if (!mapTradeStatusLog.containsKey(endTradeStatusLog.getTradeUuid())) {
                mapTradeStatusLog.put(endTradeStatusLog.getTradeUuid(), new ArrayList<TradeStatusLog>());
            }

            mapTradeStatusLog.get(endTradeStatusLog.getTradeUuid()).add(endTradeStatusLog);
        }


                List<TradeReceiveLog> tradeReceiveLog = helper.getDao(TradeReceiveLog.class)
                .queryBuilder()
                .where()
                .in(TradeReceiveLog.$.tradeUuid, tradeUUIIDs)
                .and()
                .eq(TradeStatusLog.$.statusFlag, StatusFlag.VALID)
                .query();


        Map<String, TradeReceiveLog> mapTradeReceiveLog = new HashMap<>();
        for (TradeReceiveLog receiveLog : tradeReceiveLog) {
            mapTradeReceiveLog.put(receiveLog.getTradeUuid(), receiveLog);
        }

                List<TradeGroupInfo> tradeGropuInfos = helper.getDao(TradeGroupInfo.class)
                .queryBuilder()
                .where().in(TradeGroupInfo.$.tradeUuid, tradeUUIIDs).or().in(TradeGroupInfo.$.tradeUuid, relateTradeUUIDs).query();

        Map<String, TradeGroupInfo> mapTradeGroupInfo = new HashMap<>();
        for (TradeGroupInfo tradeGropuInfo : tradeGropuInfos) {
            mapTradeGroupInfo.put(tradeGropuInfo.getTradeUuid(), tradeGropuInfo);
        }

                List<TradeTax> tradeTaxList = helper.getDao(TradeTax.class)
                .queryBuilder()
                .where().in(TradeTax.$.tradeId, tradeIDs).and().eq(TradeTax.$.statusFlag, StatusFlag.VALID).query();

        Map<Long, List<TradeTax>> mapTradeTax = new HashMap<>();
        for (TradeTax tradeTax : tradeTaxList) {
            if (mapTradeTax.get(tradeTax.getTradeId()) == null) {
                mapTradeTax.put(tradeTax.getTradeId(), new ArrayList<TradeTax>());
            }
            mapTradeTax.get(tradeTax.getTradeId()).add(tradeTax);
        }

                List<TradeInitConfig> tradeInitConfigList = helper.getDao(TradeInitConfig.class)
                .queryBuilder()
                .where().in(TradeInitConfig.$.trade_id, tradeIDs).and().eq(TradeInitConfig.$.statusFlag, StatusFlag.VALID).query();
        Map<Long, List<TradeInitConfig>> mapTradeInitConfig = new HashMap<>();
        for (TradeInitConfig initConfig : tradeInitConfigList) {
            if (mapTradeInitConfig.get(initConfig.getTradeId()) == null) {
                mapTradeInitConfig.put(initConfig.getTradeId(), new ArrayList<TradeInitConfig>());
            }
            mapTradeInitConfig.get(initConfig.getTradeId()).add(initConfig);
        }
                List<TradeEarnestMoney> tradeEarnestMonies = helper.getDao(TradeEarnestMoney.class)
                .queryBuilder()
                .where().in(TradeEarnestMoney.$.tradeId, tradeIDs).and().eq(TradeEarnestMoney.$.statusFlag, StatusFlag.VALID).query();
        Map<Long, List<TradeEarnestMoney>> mapTradeEarnestMoney = new HashMap<>();
        if (tradeEarnestMonies != null && tradeEarnestMonies.size() > 0) {
            for (TradeEarnestMoney tradeEarnestMoney : tradeEarnestMonies) {
                if (mapTradeEarnestMoney.get(tradeEarnestMoney.getTradeId()) == null) {
                    mapTradeEarnestMoney.put(tradeEarnestMoney.getTradeId(), new ArrayList<TradeEarnestMoney>());
                }
                mapTradeEarnestMoney.get(tradeEarnestMoney.getTradeId()).add(tradeEarnestMoney);
            }
        }

                List<TradeVo> tradeVos = new ArrayList<>();
        for (Trade trade : trades) {
            TradeVo tradeVo = new TradeVo();
            tradeVo.setTrade(trade);            tradeVo.setTradeExtra(mapTradeExtra.get(trade.getUuid()));            tradeVo.setTradeDeposit(mapTradeDeposit.get(trade.getUuid()));            tradeVo.setTradeUser(mapTradeUser.get(trade.getId()));
            TradeDepositPayRelation tradeDepositPayRelation = mapTradeDepositPayRelation.get(trade.getUuid());
            if (tradeDepositPayRelation != null) {
                tradeVo.setTradeDepositPayRelation(tradeDepositPayRelation);                tradeVo.setTradeDepositPaymentItem(mapDepositPaymentItem.get(tradeDepositPayRelation.getPaymentItemId()));            }

            tradeVo.setTradeCustomerList(mapTradeCustomer.get(trade.getUuid()));            tradeVo.setTradeBuffetPeoples(mapTradeBuffetPeoples.get(trade.getUuid()));            tradeVo.setTradeTableList(mapTradeTable.get(trade.getUuid()));            tradeVo.setTradePlanActivityList(mapTradePlanActivitys.get(trade.getUuid()));            tradeVo.setTradeItemPlanActivityList(mapTradeItemPlanActivity.get(trade.getId()));            tradeVo.setTradePrivilegeExtraMap(mapTradePrivilegeExtra.get(trade.getUuid()));            tradeVo.setmWeiXinCouponsVo(mapWeiXinCouponPrivilege.get(trade.getUuid()));            tradeVo.setBanquetVo(mapBanquetPrivelegeo.get(trade.getUuid()));            tradeVo.setExtraChargeMap(mapTradeExtraCharge.get(trade.getUuid()));            tradeVo.setMinconExtraCharge(minConsumCharge);
            tradeVo.setTradePrivileges(mapTradePrivilege.get(trade.getUuid()));            tradeVo.setTradePromotions(mapTradePromotion.get(trade.getId()));            tradeVo.setCouponPrivilegeVoList(mapCouponPrivilege.get(trade.getUuid()));            tradeVo.setIntegralCashPrivilegeVo(mapIntegralPrivilege.get(trade.getUuid()));            tradeVo.setMealHullVo(mapMealShellVo.get(trade.getUuid()));            tradeVo.setTradeItemExtraDinners(mapTradeItemExtraDinners.get(trade.getId()));            tradeVo.setTradeItemList(mapTradeItemVo.get(trade.getUuid()));            tradeVo.setTradeBuffetPeopleTradeItems(mapBuffetPeople.get(trade.getUuid()));            tradeVo.setTradeItemMainBatchRelExtraList(tradeItemMainBatchRelExtraMap.get(trade.getId()));
            if (trade.getTradeStatus() == TradeStatus.TEMPORARY
                    || Snack.isOfflineTrade(tradeVo)) {
                tradeVo.setTradeReasonRelList(mapTradeReasonRel.get(trade.getUuid()));            } else {
                tradeVo.setTradeReasonRelList(mapTradeRelateReasonRel.get(getRelateId(trade)));            }

            tradeVo.setTradeCreditLogList(mapTradeCreditLog.get(trade.getId()));            tradeVo.setTradeStatusLogList(mapTradeStatusLog.get(trade.getUuid()));            tradeVo.setTradeReceiveLog(mapTradeReceiveLog.get(trade.getUuid()));            tradeVo.setTradeGroup(mapTradeGroupInfo.get(trade.getUuid()));
            tradeVo.setTradeTaxs(mapTradeTax.get(trade.getId()));
            tradeVo.setTradeInitConfigs(mapTradeInitConfig.get(trade.getId()));
            tradeVo.setTradeEarnestMoneys(mapTradeEarnestMoney.get(trade.getId()));            tradeVos.add(tradeVo);
        }

        return tradeVos;
    }


    private ExtraCharge cloneExtraCharge(ExtraCharge extraCharge) {
        if (extraCharge == null) {
            return null;
        }

        ExtraCharge newExtraeCharge = new ExtraCharge();
        try {
            Beans.copyProperties(extraCharge, newExtraeCharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newExtraeCharge;
    }

    private TradeVo findTradeVo(DatabaseHelper helper, Trade trade, boolean onlyValid)
            throws SQLException {
        if (trade == null) {
            return null;
        }

        TradeVo tradeVo = new TradeVo();
        tradeVo.setTrade(trade);


                if (trade.getId() != null) {
            TradeUser tradeUser = helper.getDao(TradeUser.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeUser.$.tradeId, trade.getId())
                    .and().eq(TradeUser.$.tradeItemId, 0)
                    .queryForFirst();
            tradeVo.setTradeUser(tradeUser);

            List<TradeUser> tradeUserList = helper.getDao(TradeUser.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeUser.$.tradeId, trade.getId()).and().eq(TradeUser.$.tradeItemId, 0).query();
            tradeVo.setTradeUsers(tradeUserList);
        }



                List<TradeCustomer> tradeCustomerList = helper.getDao(TradeCustomer.class)
                .queryBuilder()
                .orderBy(TradeCustomer.$.serverUpdateTime, false)
                .where()
                .eq(TradeCustomer.$.tradeUuid, trade.getUuid())
                .query();
        tradeVo.setTradeCustomerList(tradeCustomerList);


                List<TradeTable> tradeTableList =
                helper.getDao(TradeTable.class).queryForEq(TradeTable.$.tradeUuid, trade.getUuid());
        tradeVo.setTradeTableList(tradeTableList);
                List<TradePlanActivity> tradePlanActivitys = helper.getDao(TradePlanActivity.class)
                .queryBuilder()
                .where()
                .eq(TradePlanActivity.$.tradeUuid, trade.getUuid())
                .and()
                .eq(TradePlanActivity.$.statusFlag, StatusFlag.VALID)
                .query();
                        if (Utils.isEmpty(tradePlanActivitys)) {
            tradePlanActivitys = new ArrayList<>();        }
        List<String> tradePlanActivityUuidList = new ArrayList<String>();
        for (TradePlanActivity tradePlanActivity : tradePlanActivitys) {
            if (tradePlanActivity.getUuid() != null) {
                tradePlanActivityUuidList.add(tradePlanActivity.getUuid());
            }

        }
        tradeVo.setTradePlanActivityList(tradePlanActivitys);
                List<TradeItemPlanActivity> tradeItemPlanActivities = helper.getDao(TradeItemPlanActivity.class)
                .queryBuilder()
                .where()
                .in(TradeItemPlanActivity.$.relUuid, tradePlanActivityUuidList.toArray())
                .and()
                .eq(TradeItemPlanActivity.$.statusFlag, StatusFlag.VALID)
                .query();
        if (Utils.isEmpty(tradeItemPlanActivities)) {
            tradeItemPlanActivities = new ArrayList<>();        }

        tradeVo.setTradeItemPlanActivityList(tradeItemPlanActivities);

                List<TradePrivilege> tpList = helper.getDao(TradePrivilege.class)
                .queryBuilder()
                .orderBy(TradePrivilege.$.privilegeType, true)
                .where()
                .eq(TradePrivilege.$.tradeUuid, trade.getUuid())
                .and()
                .eq(TradePrivilege.$.statusFlag, StatusFlag.VALID)
                .query();

        List<TradePrivilegeExtra> tpExtraList = helper.getDao(TradePrivilegeExtra.class)
                .queryBuilder()
                .orderBy(TradePrivilegeExtra.$.serverUpdateTime, true)
                .where()
                .eq(TradePrivilegeExtra.$.tradeUuid, trade.getUuid())
                .and()
                .eq(TradePrivilegeExtra.$.statusFlag, StatusFlag.VALID)
                .query();

        Map<Long, TradePrivilegeExtra> privilegeExtraMap = new HashMap<Long, TradePrivilegeExtra>();
        if (Utils.isNotEmpty(tpExtraList)) {
            for (TradePrivilegeExtra privilegeExtra : tpExtraList) {
                privilegeExtraMap.put(privilegeExtra.getTradePrivilegeId(), privilegeExtra);
            }
        }

        tradeVo.setTradePrivilegeExtraMap(privilegeExtraMap);

        IntegralCashPrivilegeVo integralCashPrivilegeVo = null;
                List<TradePrivilege> itemTpList = new ArrayList<>();
        List<CouponPrivilegeVo> couponPrivilegeVoList = new ArrayList<CouponPrivilegeVo>();
        for (TradePrivilege tp : tpList) {
            if (TextUtils.isEmpty(tp.getTradeItemUuid())) {
                                if (tp.getPrivilegeType() == PrivilegeType.COUPON) {
                    CouponPrivilegeVo couponPrivilege = new CouponPrivilegeVo();
                    couponPrivilege.setTradePrivilege(tp);
                    if (privilegeExtraMap.get(tp.getId()) != null) {
                        couponPrivilege.setTradePrivilegeExtra(privilegeExtraMap.get(tp.getId()));
                    }
                    if (tp.getCouponId() != null) {
                        Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                        Coupon coupon = couponDao.queryForId(tp.getCouponId());
                        couponPrivilege.setCoupon(coupon);
                        Dao<CoupRule, Long> coupRuleDao = helper.getDao(CoupRule.class);
                    }

                    Coupon coupon = couponPrivilege.getCoupon();
                    if (coupon != null) {
                                                if (coupon.getCouponType() == CouponType.GIFT) {
                            if (trade.getTradeAmount().compareTo(coupon.getFullValue()) >= 0) {
                                couponPrivilege.setActived(true);
                            }
                        } else {
                                                        if (couponPrivilege.getTradePrivilege()
                                    .getPrivilegeAmount()
                                    .compareTo(BigDecimal.ZERO) != 0) {
                                couponPrivilege.setActived(true);
                            }
                        }
                    }
                    couponPrivilegeVoList.add(couponPrivilege);
                } else if (tp.getPrivilegeType() == PrivilegeType.INTEGRALCASH) {
                    integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
                    integralCashPrivilegeVo.setTradePrivilege(tp);
                    integralCashPrivilegeVo.setActived(true);
                    if (privilegeExtraMap.get(tp.getId()) != null) {
                        integralCashPrivilegeVo.setTradePrivilegeExtra(privilegeExtraMap.get(tp.getId()));
                    }
                                        if (tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) == 0
                            && tp.getPrivilegeValue().compareTo(BigDecimal.ZERO) == 0) {
                        integralCashPrivilegeVo.setActived(false);
                    }

                                        try {
                        Long ruleId = tp.getPromoId();
                        if (ruleId != null && ruleId > 0) {
                            for (TradeCustomer tradeCustomer : tradeCustomerList) {
                                if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
                                    CustomerScoreRule rule = DBHelperManager.queryById(CustomerScoreRule.class, ruleId);
                                    integralCashPrivilegeVo.setRule(rule);

                                    CustomerScoreRule limitRule=CustomerManager.getInstance().getIntegerLimitRule();
                                    if(limitRule!=null){
                                        integralCashPrivilegeVo.setMaxInteger(new BigDecimal(limitRule.getConvertValue()));
                                    }
                                    break;
                                } else if (tradeCustomer.getCustomerType() == CustomerType.CARD) {
                                    DBHelperManager.queryById(CrmCustomerLevelRights.class, ruleId);
                                    EcCardLevelSetting rule = DBHelperManager.queryById(EcCardLevelSetting.class, ruleId);
                                    integralCashPrivilegeVo.setRule(rule);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else if (tp.getPrivilegeType() == PrivilegeType.WECHAT_CARD_COUPONS) {
                    WeiXinCouponsVo weiXinCouponsVo = new WeiXinCouponsVo();
                    weiXinCouponsVo.setmTradePrivilege(tp);
                    weiXinCouponsVo.setActived(true);
                    if (privilegeExtraMap.get(tp.getId()) != null) {
                        weiXinCouponsVo.setTradePrivilegeExtra(privilegeExtraMap.get(tp.getId()));
                    }
                                        if (tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) == 0
                            && tp.getPrivilegeValue().compareTo(BigDecimal.ZERO) == 0) {
                        weiXinCouponsVo.setActived(false);
                    }

                    if (tradeVo.getmWeiXinCouponsVo() == null) {
                        tradeVo.setmWeiXinCouponsVo(new ArrayList<WeiXinCouponsVo>());
                    }
                    tradeVo.getmWeiXinCouponsVo().add(weiXinCouponsVo);
                } else if (tp.getPrivilegeType() == PrivilegeType.BANQUET) {
                                        BanquetVo banquetVo = new BanquetVo();
                    banquetVo.setTradePrivilege(tp);
                    tradeVo.setBanquetVo(banquetVo);
                } else {
                    if (tp.getPrivilegeType() == PrivilegeType.ADDITIONAL || tp.getPrivilegeType() == PrivilegeType.SERVICE) {
                        if (tp.getPromoId() != null) {
                            Dao<ExtraCharge, String> dao = helper.getDao(ExtraCharge.class);
                            QueryBuilder<ExtraCharge, String> qb = dao.queryBuilder();
                            ExtraCharge extraCharge = qb.where()
                                    .eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                                    .and()
                                    .eq(ExtraCharge.$.enabledFlag, Bool.YES)
                                    .and()
                                    .eq(ExtraCharge.$.id, tp.getPromoId())
                                    .queryForFirst();
                            if (tradeVo.getExtraChargeMap() == null) {
                                tradeVo.setExtraChargeMap(new HashMap<Long, ExtraCharge>());
                            }
                            if (extraCharge != null) {
                                if (extraCharge.getCode().equalsIgnoreCase("ZZCSF")) {

                                } else {
                                    tradeVo.getExtraChargeMap().put(tp.getPromoId(), extraCharge);
                                }
                            }
                        }
                    }

                    if (tradeVo.getTradePrivileges() == null) {
                        tradeVo.setTradePrivileges(new ArrayList<TradePrivilege>());
                    }
                    tradeVo.getTradePrivileges().add(tp);
                }
            } else {
                                                itemTpList.add(tp);
            }
        }


                if (trade.getId() != null) {
            Dao<TradePromotion, String> tradePromotionDao = helper.getDao(TradePromotion.class);
            List<TradePromotion> tradePromotions = tradePromotionDao.queryBuilder()
                    .orderBy(TradePromotion.$.serverUpdateTime, false)
                    .where()
                    .eq(TradePromotion.$.tradeId, trade.getId())
                    .and()
                    .eq(TradePromotion.$.statusFlag, StatusFlag.VALID)
                    .query();
            tradeVo.setTradePromotions(tradePromotions);
        }

                tradeVo.setCouponPrivilegeVoList(couponPrivilegeVoList);
                tradeVo.setIntegralCashPrivilegeVo(integralCashPrivilegeVo);

        List<TradeItemVo> tiVoList = new ArrayList<TradeItemVo>();

        Dao<TradeItemProperty, String> tipDao = helper.getDao(TradeItemProperty.class);
        Dao<TradeReasonRel, String> reasonRelDao = helper.getDao(TradeReasonRel.class);
        Dao<TradeItem, String> tiDao = helper.getDao(TradeItem.class);
        QueryBuilder<TradeItem, String> tiQb = tiDao.queryBuilder();
        Where<TradeItem, String> tiWhere = tiQb.where();
        tiWhere.eq(TradeItem.$.tradeUuid, trade.getUuid());
        if (onlyValid) {
            tiWhere.and().eq(TradeItem.$.statusFlag, StatusFlag.VALID);
        }
        tiQb.orderBy(TradeItem.$.sort, true);
        List<TradeItem> tis = tiQb.query();

        List<String> tradeItemUuidList = new ArrayList<String>();
        List<Long> tradeItemIdList = new ArrayList<Long>();
        for (TradeItem tradeItem : tis) {
            if (tradeItem.getType() == DishType.MEAL_SHELL || tradeItem.getType() == DishType.BUFFET_COMBO_SHELL) {
                MealShellVo mealHellVo = new MealShellVo();
                mealHellVo.setTradeItem(tradeItem);
                tradeVo.setMealHullVo(mealHellVo);
                continue;
            }
            tradeItemUuidList.add(tradeItem.getUuid());
            if (tradeItem.getId() != null) {
                tradeItemIdList.add(tradeItem.getId());
            }
        }






                Map<String, List<TradeItemProperty>> tradeItemPropertyListMap = new HashMap<String, List<TradeItemProperty>>();
        List<TradeItemProperty> TradeItemPropertyList =
                tipDao.queryBuilder().where().in(TradeItemProperty.$.tradeItemUuid, tradeItemUuidList.toArray()).query();

        for (TradeItemProperty property : TradeItemPropertyList) {
            if (tradeItemPropertyListMap.get(property.getTradeItemUuid()) != null) {
                tradeItemPropertyListMap.get(property.getTradeItemUuid()).add(property);
            } else {
                List<TradeItemProperty> itemProperties = new ArrayList<TradeItemProperty>();
                itemProperties.add(property);
                tradeItemPropertyListMap.put(property.getTradeItemUuid(), itemProperties);
            }
        }

                Map<String, List<TradeReasonRel>> tradeReasonRelMap = new HashMap<String, List<TradeReasonRel>>();
        List<TradeReasonRel> reasionList = reasonRelDao.queryBuilder()
                .orderBy(TradeReasonRel.$.serverUpdateTime, true)
                .where()
                                                .in(TradeReasonRel.$.relateUuid, tradeItemUuidList.toArray())
                .and()
                .eq(TradeReasonRel.$.statusFlag, StatusFlag.VALID)
                .query();
        if (reasionList != null) {
            for (TradeReasonRel reasonRel : reasionList) {
                List<TradeReasonRel> nreasonRelList = tradeReasonRelMap.get(reasonRel.getRelateUuid());
                if (nreasonRelList == null) {
                    nreasonRelList = new ArrayList<TradeReasonRel>();
                    tradeReasonRelMap.put(reasonRel.getRelateUuid(), nreasonRelList);
                }
                nreasonRelList.add(reasonRel);
            }
        }
        Map<Long, List<TradeItemOperation>> tradeItemOperationListMap = new HashMap<Long, List<TradeItemOperation>>();
        Dao<TradeItemOperation, String> dao = helper.getDao(TradeItemOperation.class);
        QueryBuilder<TradeItemOperation, String> qb = dao.queryBuilder();
        qb.where().in(TradeItemOperation.$.tradeItemId, tradeItemIdList.toArray());
        qb.orderBy(TradeItemOperation.$.serverUpdateTime, true);
        List<TradeItemOperation> tradeItemOperations = qb.query();
        for (TradeItemOperation operation : tradeItemOperations) {
            if (tradeItemOperationListMap.get(operation.getTradeItemId()) != null) {
                tradeItemOperationListMap.get(operation.getTradeItemId()).add(operation);
            } else {
                List<TradeItemOperation> list = new ArrayList<TradeItemOperation>();
                list.add(operation);
                tradeItemOperationListMap.put(operation.getTradeItemId(), list);
            }
        }

                Map<Long, List<TradeUser>> itemUserMap = new HashMap<>();
        if (tradeVo.getTrade().getId() != null) {
            List<TradeUser> tradeItemUserList = helper.getDao(TradeUser.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeUser.$.tradeId, tradeVo.getTrade().getId())
                    .and().ne(TradeUser.$.tradeItemId, 0)
                    .query();
            if (Utils.isNotEmpty(tradeItemUserList)) {
                for (TradeUser tradeItemUser : tradeItemUserList) {
                    if (itemUserMap.get(tradeItemUser.getTradeItemId()) == null) {
                        itemUserMap.put(tradeItemUser.getTradeItemId(), new ArrayList<TradeUser>());
                    }
                    itemUserMap.get(tradeItemUser.getTradeItemId()).add(tradeItemUser);
                }
            }
        }
                for (TradeItem tradeItem : tis) {
            if (tradeItem.getType() == DishType.MEAL_SHELL || tradeItem.getType() == DishType.BUFFET_COMBO_SHELL) {
                continue;
            }
            TradeItemVo tiVo = new TradeItemVo();
            tiVo.setTradeItem(tradeItem);


                        TradeItemExtra tradeItemExtra = helper.getDao(TradeItemExtra.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeItemExtra.$.tradeItemUuid, tradeItem.getUuid())
                    .queryForFirst();
            if (tradeItemExtra != null) {
                tiVo.setTradeItemExtra(tradeItemExtra);
            }
                        Iterator<TradePrivilege> iterator = itemTpList.iterator();
            while (iterator.hasNext()) {
                TradePrivilege tradePrivilege = iterator.next();                  if (!tradePrivilege.getTradeItemUuid().equals(tradeItem.getUuid()))
                    continue;
                if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == PrivilegeType.COUPON) {
                    CouponPrivilegeVo couponPrivilegeVo = new CouponPrivilegeVo();
                    couponPrivilegeVo.setTradePrivilege(tradePrivilege);
                    if (tradePrivilege.getPromoId() != null) {
                        Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                        Coupon coupon = couponDao.queryForId(tradePrivilege.getPromoId());
                        couponPrivilegeVo.setCoupon(coupon);
                    }
                    if (privilegeExtraMap.get(tradePrivilege.getId()) != null) {
                        couponPrivilegeVo.setTradePrivilegeExtra(privilegeExtraMap.get(tradePrivilege.getId()));
                    }
                    tiVo.setCouponPrivilegeVo(couponPrivilegeVo);
                } else if (tradePrivilege != null && tradePrivilege.getPrivilegeType() == PrivilegeType.CARD_SERVICE) {
                    CardServicePrivilegeVo cardServicePrivilegeVo = new CardServicePrivilegeVo();
                    cardServicePrivilegeVo.setTradePrivilege(tradePrivilege);


                    tiVo.setCardServicePrivilegeVo(cardServicePrivilegeVo);
                } else if (tradePrivilege != null && tradePrivilege.isAppletPrivilege()) {
                    AppletPrivilegeVo appletPrivilegeVo = new AppletPrivilegeVo();
                    appletPrivilegeVo.setTradePrivilege(tradePrivilege);
                    tiVo.setAppletPrivilegeVo(appletPrivilegeVo);
                } else {
                    tiVo.setTradeItemPrivilege(tradePrivilege);
                }
                iterator.remove();
            }


                        List<TradeItemProperty> tips = tradeItemPropertyListMap.get(tradeItem.getUuid());

            tiVo.setTradeItemPropertyList(tips);
                        List<TradeReasonRel> reasonList = tradeReasonRelMap.get(tradeItem.getUuid());
            if (reasonList != null)
                tiVo.setReasonRelList(reasonList);

            List<TradeItemOperation> tradeItemOperationList = tradeItemOperationListMap.get(tradeItem.getId());
            tiVo.setTradeItemOperations(tradeItemOperationList);

            tiVo.setTradeItemUserList(itemUserMap.get(tradeItem.getId()));
            tiVoList.add(tiVo);
        }

                                                                                                                                                                                                                                                                tradeVo.setTradeItemList(tiVoList);



        long startTradeReasonRel = System.currentTimeMillis();
        List<TradeReasonRel> tradeReasonRelList = new ArrayList<>();
        if (trade.getTradeStatus() == TradeStatus.TEMPORARY
                || Snack.isOfflineTrade(trade)) {
            tradeReasonRelList = helper.getDao(TradeReasonRel.class)
                    .queryBuilder()
                    .orderBy(TradeReasonRel.$.serverUpdateTime, false)
                    .where()
                    .eq(TradeReasonRel.$.relateUuid, trade.getUuid())
                    .query();
        } else {
            tradeReasonRelList = helper.getDao(TradeReasonRel.class)
                    .queryBuilder()
                    .orderBy(TradeReasonRel.$.serverUpdateTime, false)
                    .where()
                    .eq(TradeReasonRel.$.relateId, getRelateId(trade))
                    .query();
        }


        tradeVo.setTradeReasonRelList(tradeReasonRelList);
                if (trade.getId() != null) {
            List<TradeCreditLog> tradeCreditLogList = helper.getDao(TradeCreditLog.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeCreditLog.$.tradeId, trade.getId())
                    .query();

            tradeVo.setTradeCreditLogList(tradeCreditLogList);
        }



        return tradeVo;
    }


    @Override
    public List<TradeTable> getTradeTablesByMainTrade(DatabaseHelper helper, Long tradeId) throws SQLException {
        List<TradeTable> tradeTableList = new ArrayList<>();
        List<TradeMainSubRelation> tradeMainSubRelationList = helper.getDao(TradeMainSubRelation.class).queryForEq(TradeMainSubRelation.$.mainTradeId, tradeId);
        if (null != tradeMainSubRelationList && tradeMainSubRelationList.size() > 0) {
            List<Long> subTradeId = new ArrayList<>();
            for (TradeMainSubRelation tradeMainSubRelation : tradeMainSubRelationList) {
                subTradeId.add(tradeMainSubRelation.getSubTradeId());
            }
            tradeTableList = helper.getDao(TradeTable.class).queryBuilder().where().in(TradeTable.$.tradeId, subTradeId).query();
        }

        return tradeTableList;
    }

    private static Long getRelateId(Trade trade) {
        TradeStatus tradeStatus = trade.getTradeStatus();
                Long tradeId = (tradeStatus == TradeStatus.RETURNED && trade.getTradeType() != TradeType.SPLIT
                && trade.getRelateTradeId() != null && trade.getTradeType() != TradeType.SELL_FOR_REPEAT) ? trade.getRelateTradeId() : trade.getId();

        return tradeId;
    }

    private static String getRelateUuid(Trade trade) {
        TradeStatus tradeStatus = trade.getTradeStatus();
                String tradeUuid = (tradeStatus == TradeStatus.RETURNED && !TextUtils.isEmpty(trade.getRelateTradeUuid()))
                ? trade.getRelateTradeUuid() : trade.getUuid();

        return tradeUuid;
    }

        private static List<PaymentVo> listPaymentVo(DatabaseHelper helper, String tradeUuid)
            throws Exception {
        return listPaymentVo(helper, tradeUuid, null);
    }

    private static List<PaymentVo> listPaymentVo(DatabaseHelper helper, String tradeUuid, PaymentType paymentType)
            throws Exception {
        Dao<Payment, String> dao = helper.getDao(Payment.class);
        Dao<PaymentItem, String> itemDao = helper.getDao(PaymentItem.class);
        Dao<PaymentItemGroupon, String> discountTicketDao = helper.getDao(PaymentItemGroupon.class);
        Dao<PaymentItemExtra, String> itemExtraDao = helper.getDao(PaymentItemExtra.class);
        Dao<RefundExceptionReason, Long> refundFailedDao = helper.getDao(RefundExceptionReason.class);
        List<PaymentVo> voList = new ArrayList<PaymentVo>();
        QueryBuilder<Payment, String> paymentBuild = dao.queryBuilder();
        if (paymentType == null) {
            paymentBuild.where().eq(Payment.$.relateUuid, tradeUuid).and().in(Payment.$.statusFlag, StatusFlag.VALID);
        } else {
            paymentBuild.where().eq(Payment.$.relateUuid, tradeUuid).and().in(Payment.$.statusFlag, StatusFlag.VALID).and().eq(Payment.$.paymentType, paymentType);
        }
        paymentBuild.orderBy(Payment.$.serverCreateTime, true);
        List<Payment> paymentList = paymentBuild.query();
        for (Payment payment : paymentList) {
            PaymentVo vo = new PaymentVo();
            vo.setPayment(payment);

            List<PaymentItem> listPaymentItems = itemDao.queryForEq(PaymentItem.$.paymentUuid, payment.getUuid());

            List<Long> paymentItemIdList = new ArrayList<>();
            List<String> paymentItemUuidList = new ArrayList<>();
                        for (PaymentItem paymentItem : listPaymentItems) {
                paymentItemIdList.add(paymentItem.getId());
                paymentItemUuidList.add(paymentItem.getUuid());
                if (PayModeId.MEITUAN_TUANGOU.value().equals(paymentItem.getPayModeId())) {
                    List<PaymentItemGroupon> tmpListPaymentItemGroupon = discountTicketDao.queryForEq(PaymentItemGroupon.$.paymentItemId, paymentItem.getId());
                    if (tmpListPaymentItemGroupon != null && tmpListPaymentItemGroupon.size() > 0)
                        paymentItem.setPaymentItemGroupon(tmpListPaymentItemGroupon.get(0));
                }
            }

            vo.setPaymentItemList(listPaymentItems);

            List<PaymentItemExtra> itemExtraList =
                    itemExtraDao.queryBuilder().where().in(PaymentItemExtra.$.paymentItemUuid, paymentItemUuidList).query();
            vo.setPaymentItemExtraList(itemExtraList);

                        CollectionUtils.filterNull(paymentItemIdList);

            voList.add(vo);
        }
        return voList;
    }


    private static void reSortItems(List<PaymentItem> list) {
        final Map<String, Long> map = new HashMap<String, Long>();
        for (PaymentItem item : list) {
            long payModeId = item.getPayModeId();
            if (payModeId == -3) {                map.put(item.getUuid(), 1L);
            } else if (payModeId == -4) {                map.put(item.getUuid(), 2L);
            } else if (payModeId == -1) {                map.put(item.getUuid(), 3L);
            } else if (payModeId == -5) {                map.put(item.getUuid(), 4L);
            } else if (payModeId == -6) {                map.put(item.getUuid(), 5L);
            } else if (payModeId == -7) {                map.put(item.getUuid(), 6L);
            } else if (payModeId == -2) {                map.put(item.getUuid(), 8L);
            } else if (payModeId == -8) {                map.put(item.getUuid(), 9L);
            } else {                map.put(item.getUuid(), item.getPayModeId());
            }
        }
        Collections.sort(list, new Comparator<PaymentItem>() {
            @Override
            public int compare(PaymentItem lhs, PaymentItem rhs) {
                return map.get(lhs.getUuid()).compareTo(map.get(rhs.getUuid()));
            }
        });
    }


    @Override
    public TradePaymentVo findTradPaymentByRelateTradeUuid(String relateTradeUuid)
            throws Exception {
        Checks.verifyNotNull(relateTradeUuid, "relateTradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            List<Trade> trades = tradeDao.queryBuilder()
                    .where()
                    .eq(Trade.$.relateTradeUuid, relateTradeUuid)
                    .and()
                    .eq(Trade.$.tradeType, TradeType.SELL_FOR_REPEAT)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .query();

            Trade trade = null;
            if (trades != null && trades.size() > 0) {
                trade = trades.get(0);
            }
            if (trade != null) {
                TradePaymentVo vo = new TradePaymentVo();
                TradeVo tradeVo = findTradeVo(helper, trade);
                List<PaymentVo> paymentVoList = listPaymentVo(helper, trade.getUuid());
                vo.setTradeVo(tradeVo);
                vo.setPaymentVoList(paymentVoList);
                return vo;
            } else {
                return null;
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeItem findRejectTradeItem(String relateTradeItemUuid)
            throws Exception {
        Checks.verifyNotNull(relateTradeItemUuid, "relateTradeItemUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeDao = helper.getDao(TradeItem.class);
            QueryBuilder<TradeItem, String> qb = tradeDao.queryBuilder();
            qb.where().eq(TradeItem.$.uuid, relateTradeItemUuid);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }

    @Override
    public TradeVo findTradeById(long id)
            throws Exception {

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();

            Trade trade = qb.where().eq(Trade.$.id, id).query().get(0);

            return findTradeVo(helper, trade, true);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<DiscountShop> findDiscountByType(DiscountType type)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DiscountShop, String> dicountDao = helper.getDao(DiscountShop.class);
            QueryBuilder<DiscountShop, String> qb = dicountDao.queryBuilder();
            List<DiscountShop> discountList = qb.where()
                    .eq(DiscountShop.$.type, type)
                    .and()
                    .eq(DiscountShop.$.enabledFlag, 1)
                    .and()
                    .eq(DiscountShop.$.statusFlag, StatusFlag.VALID)
                    .query();
            return discountList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Trade> listSplitTrades(String sourceTradeUuid)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            clearExpiredPending(helper);
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            qb.where()
                    .eq(Trade.$.relateTradeUuid, sourceTradeUuid)
                    .and()
                    .eq(Trade.$.tradeType, TradeType.SPLIT)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(Trade.$.clientCreateTime, false);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public String findTradeItemBrandTypeByTradeUUID(String tradeItemSkuUUID) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DishShop, String> dao = helper.getDao(DishShop.class);
            QueryBuilder<DishShop, String> qb = dao.queryBuilder();
            DishShop dishShop = qb.selectColumns(DishShop.$.dishTypeId).where().eq(DishShop.$.uuid, tradeItemSkuUUID).queryForFirst();
            if (dishShop == null) {                                return BaseApplication.getInstance().getString(R.string.third_party_sources);
            } else {
                Dao<DishBrandType, String> typeDao = helper.getDao(DishBrandType.class);
                QueryBuilder<DishBrandType, String> typeQb = typeDao.queryBuilder();
                DishBrandType type = typeQb.selectColumns(DishBrandType.$.name)
                        .where()
                        .eq(DishBrandType.$.id, dishShop.getDishTypeId())
                        .queryForFirst();
                if (type != null && !TextUtils.isEmpty(type.getName())) {
                    return type.getName();
                } else {
                                        return BaseApplication.getInstance().getString(R.string.third_party_sources);
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }








    public TradeItem findTradeItem(Long id)
            throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            TradeItem tradeItem = tradeItemDao.queryBuilder().where().eq(TradeItem.$.id, id).queryForFirst();

            return tradeItem;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItem> findTradeItem(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            return tradeItemDao.queryBuilder()
                    .where()
                    .eq(TradeItem.$.tradeUuid, tradeUuid).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradePrivilege> findTradePrivilege(String tradeUuid, boolean onlyValid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradePrivilege, String> dao = helper.getDao(TradePrivilege.class);
            QueryBuilder<TradePrivilege, String> queryBuilder = dao.queryBuilder();
            Where<TradePrivilege, String> where = queryBuilder.where();
            where = where.eq(TradePrivilege.$.tradeUuid, tradeUuid);
            if (onlyValid) {
                where.and().eq(TradePrivilege.$.statusFlag, StatusFlag.VALID);
            }
            return queryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    public TradeItem findTradeItemByUUID(String uuid) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            TradeItem tradeItem = tradeItemDao.queryBuilder().where().eq(TradeItem.$.uuid, uuid).queryForFirst();

            return tradeItem;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeReturnInfo> queryTradeReturnInfo()
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeReturnInfo, String> dao = helper.getDao(TradeReturnInfo.class);
            QueryBuilder<TradeReturnInfo, String> qb = dao.queryBuilder();
            return qb.where()
                    .eq(TradeReturnInfo.$.returnStatus, TradeReturnInfoReturnStatus.APPLY)
                    .and()
                    .eq(TradeReturnInfo.$.statusFlag, StatusFlag.VALID)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeReturnInfo> queryTradeAgreeReturnInfo() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeReturnInfo, String> dao = helper.getDao(TradeReturnInfo.class);
            QueryBuilder<TradeReturnInfo, String> qb = dao.queryBuilder();
            return qb.where()
                    .eq(TradeReturnInfo.$.returnStatus, TradeReturnInfoReturnStatus.AGREE)
                    .and()
                    .eq(TradeReturnInfo.$.statusFlag, StatusFlag.VALID)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeReturnInfo findTradeReturnInfo(Long tradeId)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeReturnInfo, String> dao = helper.getDao(TradeReturnInfo.class);
            QueryBuilder<TradeReturnInfo, String> qb = dao.queryBuilder();
            qb.orderBy(TradeReturnInfo.$.serverCreateTime, false);
            return qb.where()
                    .eq(TradeReturnInfo.$.returnStatus, TradeReturnInfoReturnStatus.APPLY)
                    .and()
                    .eq(TradeReturnInfo.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(TradeReturnInfo.$.tradeId, tradeId)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Invoice findInvoice(Long tradeId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Invoice, String> dao = helper.getDao(Invoice.class);
            QueryBuilder<Invoice, String> qb = dao.queryBuilder();
            qb.orderBy(Invoice.$.serverUpdateTime, false);
            return qb.where()
                    .eq(Invoice.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(Invoice.$.orderId, tradeId)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Invoice findInvoice(String uuid) throws Exception {
        Checks.verifyNotEmpty(uuid, "uuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Invoice, String> dao = helper.getDao(Invoice.class);
            QueryBuilder<Invoice, String> qb = dao.queryBuilder();
            qb.orderBy(Invoice.$.serverUpdateTime, false);
            return qb.where()
                    .eq(Invoice.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(Invoice.$.uuid, uuid)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Trade getTrade(Long tradeId)
            throws SQLException {
        Checks.verifyNotNull(tradeId, "tradeId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            return tradeDao.queryBuilder().where().eq(Trade.$.id, tradeId).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    //根据工作台查询订单
    @Override
    public Trade getTradeByTableId(Long tableId) throws SQLException {
        Checks.verifyNotNull(tableId, "tradeId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try{
            Dao<TradeTable,Long> tradeTableDao=helper.getDao(TradeTable.class);
            TradeTable tradeTable=tradeTableDao.queryBuilder().orderBy(TradeTable.$.serverCreateTime,false).where().eq(TradeTable.$.tableId,tableId).and().eq(TradeTable.$.statusFlag,StatusFlag.VALID).queryForFirst();

            if(tradeTable==null){
                return null;
            }

            return getTrade(tradeTable.getTradeId());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<Trade> getTrade(List<Long> tradeIds, List<TradeStatus> tradeStatuses) throws SQLException {
        Checks.verifyNotEmpty(tradeIds, "tradeIds");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            if (Utils.isNotEmpty(tradeStatuses)) {
                return tradeDao.queryBuilder().where().in(Trade.$.id, tradeIds).and().in(Trade.$.tradeStatus, tradeStatuses).query();
            } else {
                return tradeDao.queryBuilder().where().in(Trade.$.id, tradeIds).query();
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Trade getTrade(String tradeUuid) throws SQLException {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            return tradeDao.queryBuilder().where().eq(Trade.$.uuid, tradeUuid).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeExtra getTradeExtra(String tradeUuid) throws Exception {
        Checks.verifyNotNull(tradeUuid, "tradeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            return tradeExtraDao.queryBuilder().where().eq(TradeExtra.$.tradeUuid, tradeUuid).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeExtra getTradeExtra(Long tradeId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            return tradeExtraDao.queryBuilder().where().eq(TradeExtra.$.tradeId, tradeId).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeExtra> getTradeExtra(List<Long> tradeIds) throws SQLException {
        Checks.verifyNotEmpty(tradeIds, "tradeIds");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            return tradeExtraDao.queryBuilder().where().in(TradeExtra.$.tradeId, tradeIds).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItem> listTradeItem(Iterable<Long> tradeItemIds)
            throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            return tradeItemDao.queryBuilder().where().in(TradeItem.$.id, tradeItemIds).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeDeposit getTradeDepositByUuid(String uuid) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
                        TradeDeposit tradeDeposit = helper.getDao(TradeDeposit.class)
                    .queryBuilder()
                    .where()
                    .eq(TradeDeposit.$.tradeUuid, uuid)
                    .queryForFirst();
            return tradeDeposit;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    @Override
    public List<TradeItem> listTradeItemByTradeUuid(String tradeUuid)
            throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            return tradeItemDao.queryBuilder().where().eq(TradeItem.$.tradeUuid, tradeUuid).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItem> listTradeItemByTradeId(Long tradeId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            return tradeItemDao.queryBuilder().where().eq(TradeItem.$.tradeId, tradeId).and().eq(TradeItem.$.statusFlag, StatusFlag.VALID).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long countUntreated() throws Exception {
        return countUntreated(null);
    }

    @Override
    public long countUntreated(SourceId sourceId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();

            Where<Trade, String> where1 = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE))
                    .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                    .and().eq(Trade.$.statusFlag, StatusFlag.VALID);

            if (sourceId != null) {
                Where<Trade, String> sourceIdWhere = where.eq(Trade.$.sourceId, sourceId);
                where.and(where1, sourceIdWhere);
            }

            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long countOtherUntreated(List<SourceId> ids) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> qb = tradeDao.queryBuilder();
            Where<Trade, String> where = qb.where();

            Where<Trade, String> where1 = where.or(where.eq(Trade.$.tradePayStatus, TradePayStatus.PAID), where.eq(Trade.$.tradePayForm, TradePayForm.OFFLINE))
                    .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                    .and().eq(Trade.$.statusFlag, StatusFlag.VALID);
            if (Utils.isNotEmpty(ids)) {
                Where<Trade, String> sourceIdWhere = where.notIn(Trade.$.sourceId, ids);
                where.and(where1, sourceIdWhere);
            }
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Map<Integer, Long> countUntreatedGroupBySource() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            String sql = "select " + Trade.$.sourceId + ", count(id) as count from trade where (" + Trade.$.tradePayStatus
                    + " = " + TradePayStatus.PAID.value() + " or " + Trade.$.tradePayForm + " = " + TradePayForm.OFFLINE.value()
                    + ") and " + Trade.$.tradeStatus + " = " + TradeStatus.UNPROCESSED.value() + " and " + Trade.$.statusFlag +
                    " = " + StatusFlag.VALID.value() + " group by " + Trade.$.sourceId;
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            GenericRawResults<String[]> rawResults = tradeDao.queryRaw(sql);
            List<String[]> results = rawResults.getResults();
            Map<Integer, Long> map = new HashMap<>();
            for (String[] result : results) {
                Integer source = Integer.valueOf(result[0]);
                Long count = Long.valueOf(result[1]);
                map.put(source, count);
            }

            return map;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<NotifyOtherClientPayedVo> findOtherClientPayedVos() throws SQLException, ParseException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Date date = DateTimeUtil.getOpenTime();


                        Dao<PaymentItem, String> paymentItemDao = helper.getDao(PaymentItem.class);
            QueryBuilder<PaymentItem, String> paymentItemQb = paymentItemDao.queryBuilder();
            List<PaymentItem> paymentItems = paymentItemQb.selectColumns(PaymentItem.$.paymentId, PaymentItem.$.paySource).where()
                    .in(PaymentItem.$.paySource, PaySource.PORTAL, PaySource.ON_MOBILE, PaySource.KIOSK)
                    .and().eq(PaymentItem.$.payStatus, TradePayStatus.PAID).and()
                    .ge(PaymentItem.$.serverUpdateTime, date.getTime()).query();

            Map<Long, PaySource> paymentItemMap = new HashMap<>();
            for (PaymentItem paymentItem : paymentItems) {
                paymentItemMap.put(paymentItem.getPaymentId(), paymentItem.getPaySource());
            }

                        Dao<Payment, String> paymentDao = helper.getDao(Payment.class);
            QueryBuilder<Payment, String> paymentQb = paymentDao.queryBuilder();
            List<Payment> payments = paymentQb.selectColumns(Payment.$.id, Payment.$.relateId, Payment.$.paymentTime).where()
                    .in(Payment.$.id, paymentItemMap.keySet()).and().ge(Payment.$.paymentTime, date.getTime()).query();

                        Map<Long, NotifyOtherClientPayedVo.PayContent> tradePayMap = new HashMap<>();
            for (Payment payment : payments) {
                NotifyOtherClientPayedVo.PayContent payContent = new NotifyOtherClientPayedVo.PayContent();
                payContent.paySource = paymentItemMap.get(payment.getId());
                payContent.payTime = payment.getPaymentTime();
                tradePayMap.put(payment.getRelateId(), payContent);
            }

                        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQb = tradeDao.queryBuilder();
            tradeQb.selectColumns(Trade.$.id).where().in(Trade.$.id, tradePayMap.keySet()).and()
                    .eq(Trade.$.businessType, BusinessType.DINNER);

                        Map<Long, String> serialNoMap = new HashMap<>();
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQb = tradeExtraDao.queryBuilder();
            List<TradeExtra> tradeExtras = tradeExtraQb.where().in(TradeTable.$.tradeId, tradeQb).query();
            if (Utils.isNotEmpty(tradeExtras)) {
                for (TradeExtra tradeExtra : tradeExtras) {
                    serialNoMap.put(tradeExtra.getTradeId(), tradeExtra.getSerialNumber());
                }
            }

                        Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
            QueryBuilder<TradeTable, String> tradeTableQb = tradeTableDao.queryBuilder();
            List<TradeTable> tradeTables = tradeTableQb.where().in(TradeTable.$.tradeId, tradeQb).query();
            List<NotifyOtherClientPayedVo> otherClientPayedVos = new ArrayList<>();

            TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
            List<Long> tableIds = new ArrayList<>();
            for (TradeTable tradeTable : tradeTables) {
                tableIds.add(tradeTable.getTableId());
            }
            if (tableIds.size() > 0) {
                                List<CommercialArea> areaList = null;
                List<Tables> tablesList = null;
                Map<Long, CommercialArea> areaMap = new HashMap<Long, CommercialArea>();
                Map<Long, Tables> tablesMap = new HashMap<Long, Tables>();
                try {
                    areaList = tablesDal.listDinnerArea();
                    tablesList = tablesDal.findTablesByIds(tableIds, null);

                } catch (Exception e) {
                    throw new SQLException();
                }
                                if (areaList != null && !areaList.isEmpty()) {
                    for (int i = 0; i < areaList.size(); i++) {
                        areaMap.put(areaList.get(i).getId(), areaList.get(i));
                    }
                }
                                if (tablesList != null && tablesList.size() > 0) {
                    for (int i = 0; i < tablesList.size(); i++) {
                        tablesMap.put(tablesList.get(i).getId(), tablesList.get(i));
                    }
                }
                Tables tb = null;
                CommercialArea area = null;
                                for (TradeTable tradeTable : tradeTables) {
                    NotifyOtherClientPayedVo otherClientPayedVo = new NotifyOtherClientPayedVo();
                    otherClientPayedVo.setUuid(tradeTable.getTradeUuid());
                    otherClientPayedVo.setTradeUuid(tradeTable.getTradeUuid());
                    otherClientPayedVo.setTableName(tradeTable.getTableName());
                                        String serialNo = serialNoMap.get(tradeTable.getTradeId());
                    otherClientPayedVo.setSerialNumber(serialNo);
                    NotifyOtherClientPayedVo.PayContent payContent = tradePayMap.get(tradeTable.getTradeId());
                    if (payContent != null) {
                        if (payContent.payTime != null) {
                            otherClientPayedVo.setPayTime(payContent.payTime);
                        }
                        if (payContent.paySource != null) {
                            otherClientPayedVo.paySource = payContent.paySource;
                        }
                    }
                                        tb = tablesMap.get(tradeTable.getTableId());
                    if (tb != null && tb.getAreaId() != null) {
                        area = areaMap.get(tb.getAreaId());
                        if (area != null) {
                            otherClientPayedVo.areaName = area.getAreaName();
                        }
                    }
                                        otherClientPayedVos.add(otherClientPayedVo);
                }
            }
                        Comparator<NotifyOtherClientPayedVo> comparator = new Comparator<NotifyOtherClientPayedVo>() {
                @Override
                public int compare(NotifyOtherClientPayedVo lhs, NotifyOtherClientPayedVo rhs) {
                    if (lhs.getPayTime() > rhs.getPayTime()) {
                        return -1;
                    } else if (lhs.getPayTime() < rhs.getPayTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };
            Collections.sort(otherClientPayedVos, comparator);

            return otherClientPayedVos;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Long> searchTableWithDish(List<String> dishUuids, BusinessType businessType) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<Long> tableIds = new ArrayList<Long>();
        try {
                        Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            QueryBuilder<TradeItem, String> tradeItemQb = tradeItemDao.queryBuilder();
            tradeItemQb.selectColumns(TradeItem.$.tradeId, TradeItem.$.tradeTableId, TradeItem.$.invalidType);
            tradeItemQb.where().in(TradeItem.$.skuUuid, dishUuids);
            tradeItemQb.groupBy(TradeItem.$.tradeId);

            List<TradeItem> list = tradeItemQb.query();
            List<Long> tradeIds = new ArrayList<Long>();
                        if (list != null && !list.isEmpty()) {
                TradeItem it = null;
                for (int i = 0; i < list.size(); i++) {
                    it = list.get(i);
                    if (it.getInvalidType() == InvalidType.DELETE)
                        continue;
                    tradeIds.add(it.getTradeId());
                }
                                List<BusinessType> businessTypes = new ArrayList<BusinessType>();
                if (businessType == BusinessType.BUFFET) {
                    businessTypes.add(BusinessType.BUFFET);
                } else {
                    businessTypes.add(BusinessType.DINNER);
                    businessTypes.add(BusinessType.GROUP);
                }
                                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                QueryBuilder<Trade, String> tradeQb = tradeDao.queryBuilder();
                tradeQb.selectColumns(Trade.$.id).where().in(Trade.$.id, tradeIds).and()
                        .in(Trade.$.businessType, businessTypes).and().in(Trade.$.tradeStatus, TradeStatus.UNPROCESSED, TradeStatus.CONFIRMED)
                        .and().in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_MAIN, TradeType.UNOIN_TABLE_SUB).and().eq(Trade.$.statusFlag, StatusFlag.VALID);
                List<Trade> tradeList = tradeQb.query();

                Set<Long> tradeIdSet = new HashSet<Long>();
                if (tradeList != null && !tradeList.isEmpty()) {
                    Trade trade = null;
                    for (int i = 0; i < tradeList.size(); i++) {
                        trade = tradeList.get(i);
                                                if (trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                            List<Trade> subTrades = getUnionTradesByTrade(trade);
                            if (subTrades != null && subTrades.size() > 0) {
                                for (Trade subTrade : subTrades) {
                                    tradeIdSet.add(subTrade.getId());                                }
                            }
                        } else {
                            tradeIdSet.add(trade.getId());
                        }
                    }
                }

                                if (!tradeIdSet.isEmpty()) {
                    Dao<TradeTable, Long> tbDao = helper.getDao(TradeTable.class);
                    QueryBuilder<TradeTable, Long> tbQb = tbDao.queryBuilder();
                    tbQb.selectColumns(TradeTable.$.tableId).where().in(TradeTable.$.tradeId, tradeIdSet).and().eq(TradeTable.$.statusFlag, StatusFlag.VALID);
                    List<TradeTable> tradeTables = tbQb.query();
                    if (tradeTables != null && tradeTables.size() > 0) {
                        for (TradeTable tradeTable : tradeTables) {
                            tableIds.add(tradeTable.getTableId());
                        }
                    }
                }
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return tableIds;
    }

    @Override
    public List<DeliveryOrder> listDeliveryCancelOrders() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid);
            tradeExtraQB.where().isNotNull(TradeExtra.$.deliveryUserId);

            Dao<DeliveryOrder, String> deliveryDao = helper.getDao(DeliveryOrder.class);
            return deliveryDao.queryBuilder().orderBy(DeliveryOrder.$.serverUpdateTime, true).where()
                    .eq(DeliveryOrder.$.statusFlag, Bool.YES)
                    .and().eq(DeliveryOrder.$.enableFlag, YesOrNo.YES)
                    .and().eq(DeliveryOrder.$.delivererStatus, DeliveryOrderStatus.DELIVERY_CANCEL)
                    .and().in(DeliveryOrder.$.subDeliveryStatus, DeliveryOrderSubStatus.DELIVERY_MAN_CANCEL, DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_ALLOW, DeliveryOrderSubStatus.DELIVERY_ERROR_RECREATE_FORBID)
                    .and().notIn(DeliveryOrder.$.tradeUuid, tradeExtraQB)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Trade> listWaitingDeliveryOrders() throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {

                        Dao<DeliveryOrder, String> deliveryOrderDao = helper.getDao(DeliveryOrder.class);
            QueryBuilder<DeliveryOrder, String> deliveryOrderQB = deliveryOrderDao.queryBuilder();
            deliveryOrderQB.selectColumns(DeliveryOrder.$.tradeUuid);
            deliveryOrderQB.where()
                    .eq(DeliveryOrder.$.statusFlag, YesOrNo.YES)
                    .and()
                    .eq(DeliveryOrder.$.enableFlag, YesOrNo.YES);

                        Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            QueryBuilder<TradeExtra, String> tradeExtraQB = tradeExtraDao.queryBuilder();
            tradeExtraQB.selectColumns(TradeExtra.$.tradeUuid).where()
                    .isNull(TradeExtra.$.deliveryUserId)
                    .and().eq(TradeExtra.$.deliveryStatus, DeliveryStatus.WAITINT_DELIVERY)
                    .and().eq(TradeExtra.$.deliveryPlatform, DeliveryPlatform.MERCHANT);

            Dao<Trade, String> trades = helper.getDao(Trade.class);
            return trades.queryBuilder().orderBy(Trade.$.serverCreateTime, false).where()
                    .eq(Trade.$.statusFlag, Bool.YES)
                    .and().ge(Trade.$.bizDate, getMinBizDate())
                    .and().eq(Trade.$.deliveryType, DeliveryType.SEND)
                    .and().in(Trade.$.tradeStatus, TradeStatus.FINISH, TradeStatus.CONFIRMED)
                    .and().in(Trade.$.uuid, tradeExtraQB)
                    .and().notIn(Trade.$.uuid, deliveryOrderQB)
                    .query();
        } finally {

            DBHelperManager.releaseHelper(helper);

        }
    }

        private Long getMinBizDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateTimeUtils.getCurrentDayStart());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        return calendar.getTimeInMillis();
    }

    @Override
    public TradeInvoice getTradeInvoiceById(Long tradeId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeInvoice, String> invoiceDao = helper.getDao(TradeInvoice.class);
            return invoiceDao.queryBuilder().where()
                    .eq(TradeInvoice.$.tradeId, tradeId)
                    .and()
                    .eq(TradeInvoice.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradePrivilege getTradePrivilege(String tradePrivilegeUuid) throws Exception {
        Checks.verifyNotNull(tradePrivilegeUuid, "tradePrivilegeUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradePrivilege, String> dao = helper.getDao(TradePrivilege.class);
            return dao.queryBuilder()
                    .where()
                    .eq(TradePrivilege.$.uuid, tradePrivilegeUuid)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeItem getTradeItem(String tradeItemUuid) throws Exception {
        Checks.verifyNotNull(tradeItemUuid, "tradeItemUuid");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
            return tradeItemDao.queryBuilder().where().eq(TradeItem.$.uuid, tradeItemUuid).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<DeliveryOrderVo> listDeliveryOrderVo(String tradeUuid) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DeliveryOrder, String> deliveryDao = helper.getDao(DeliveryOrder.class);
            QueryBuilder<DeliveryOrder, String> deliveryOrderStringQueryBuilder = deliveryDao.queryBuilder();
            deliveryOrderStringQueryBuilder.orderBy(DeliveryOrder.$.serverUpdateTime, false);
            Where<DeliveryOrder, String> where = deliveryOrderStringQueryBuilder.where().eq(DeliveryOrder.$.tradeUuid, tradeUuid);
            where.and().eq(DeliveryOrder.$.statusFlag, Bool.YES);
            List<DeliveryOrder> deliveryOrders = deliveryOrderStringQueryBuilder.query();
            if (Utils.isNotEmpty(deliveryOrders)) {
                List<DeliveryOrderVo> deliveryOrderVos = new ArrayList<DeliveryOrderVo>();
                for (DeliveryOrder deliveryOrder : deliveryOrders) {
                    Dao<DeliveryOrderRecord, String> deliveryOrderRecordDao = helper.getDao(DeliveryOrderRecord.class);
                    QueryBuilder<DeliveryOrderRecord, String> qb = deliveryOrderRecordDao.queryBuilder();
                    Where<DeliveryOrderRecord, String> deliveryOrderRecordWhere = qb.where();
                    deliveryOrderRecordWhere.eq(DeliveryOrderRecord.$.deliveryOrderUuid, deliveryOrder.getUuid());
                    qb.orderBy(Trade.$.serverCreateTime, false);
                    List<DeliveryOrderRecord> deliveryOrderRecords = qb.query();

                    DeliveryOrderVo deliveryOrderVo = new DeliveryOrderVo();
                    deliveryOrderVo.setDeliveryOrder(deliveryOrder);
                    deliveryOrderVo.setDeliveryOrderRecords(deliveryOrderRecords);
                    deliveryOrderVos.add(deliveryOrderVo);
                }

                return deliveryOrderVos;
            }

            return Collections.emptyList();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Trade> getDinnerNotFinishTrade() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQb = tradeDao.queryBuilder();
            return tradeQb.where()
                    .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB)
                    .and()
                    .in(Trade.$.businessType, BusinessType.DINNER, BusinessType.GROUP, BusinessType.BUFFET)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeMainSubRelation> tradeMainSubRelationList() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeMainSubRelation, String> dao = helper.getDao(TradeMainSubRelation.class);
            TradeMainSubRelation tradeMainSubRelation = new TradeMainSubRelation();
            return dao.queryBuilder().query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    public List<Trade> getUnionTradesByTrade(Trade trade) throws Exception {
        if (trade == null || trade.getId() == null) {
            return null;
        }
        List<Trade> tradeList = null;
        Long mainTradeId = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {

            Dao<TradeMainSubRelation, Long> rDao = helper.getDao(TradeMainSubRelation.class);
            if (trade.getTradeType() == TradeType.UNOIN_TABLE_SUB) {                 QueryBuilder<TradeMainSubRelation, Long> rQb = rDao.queryBuilder();
                TradeMainSubRelation tradeMainSubRelation = rQb.where().eq(TradeMainSubRelation.$.subTradeId, trade.getId()).and()
                        .eq(Trade.$.statusFlag, StatusFlag.VALID).queryForFirst();

                if (tradeMainSubRelation != null)
                    mainTradeId = tradeMainSubRelation.getMainTradeId();

            } else {                mainTradeId = trade.getId();
            }

            if (mainTradeId == null) {
                return null;
            }
                        QueryBuilder<TradeMainSubRelation, Long> rQb2 = rDao.queryBuilder();
            List<TradeMainSubRelation> relationList = rQb2.where().eq(TradeMainSubRelation.$.mainTradeId, mainTradeId).and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID).query();

            Set<Long> idSet = new HashSet<Long>();
            idSet.add(mainTradeId);
            if (relationList != null && relationList.size() > 0) {
                for (TradeMainSubRelation relation : relationList) {
                    idSet.add(relation.getSubTradeId());
                }
            }
                        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            QueryBuilder<Trade, String> tradeQb = tradeDao.queryBuilder();
            tradeList = tradeQb.where().in(Trade.$.id, idSet).and().eq(Trade.$.statusFlag, StatusFlag.VALID).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return tradeList;
    }


    @Override
    public TradeMainSubRelation getTradeMainSubRelationBySubTrade(Trade subTrade) throws Exception {
        if (subTrade == null || subTrade.getId() == null) {
            return null;
        }

        return getTradeMainSubRelationBySubTrade(subTrade.getId());
    }

    @Override
    public List<TradeMainSubRelation> getTradeSubRelationByMainTrade(Long mainTradeId) throws Exception {
        if (mainTradeId == null) {
            return null;
        }

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeMainSubRelation, Long> rDao = helper.getDao(TradeMainSubRelation.class);
                        QueryBuilder<TradeMainSubRelation, Long> rQb = rDao.queryBuilder();
            List<TradeMainSubRelation> tradeMainSubRelation = rQb.where().eq(TradeMainSubRelation.$.mainTradeId, mainTradeId).and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID).query();

            return tradeMainSubRelation;

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private TradeMainSubRelation getTradeMainSubRelationBySubTrade(Long subTradeId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeMainSubRelation, Long> rDao = helper.getDao(TradeMainSubRelation.class);
                        QueryBuilder<TradeMainSubRelation, Long> rQb = rDao.queryBuilder();
            TradeMainSubRelation tradeMainSubRelation = rQb.where().eq(TradeMainSubRelation.$.subTradeId, subTradeId).and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID).queryForFirst();

            return tradeMainSubRelation;

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemMainBatchRel> getTradeItemMainBatchRelListBySubTradeId(Long subTradeId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItemMainBatchRel, Long> itemRelDao = helper.getDao(TradeItemMainBatchRel.class);
            QueryBuilder<TradeItemMainBatchRel, Long> itemRelQb = itemRelDao.queryBuilder();
            List<TradeItemMainBatchRel> itemRelList = itemRelQb.where().eq(TradeItemMainBatchRel.$.subTradeId, subTradeId)
                    .and().eq(TradeItemMainBatchRel.$.statusFlag, StatusFlag.VALID).query();

            return itemRelList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtraListBySubTradeId(Long subTradeId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItemMainBatchRelExtra, Long> itemRelDao = helper.getDao(TradeItemMainBatchRelExtra.class);
            QueryBuilder<TradeItemMainBatchRelExtra, Long> itemRelQb = itemRelDao.queryBuilder();
            List<TradeItemMainBatchRelExtra> itemRelList = itemRelQb.where().eq(TradeItemMainBatchRelExtra.$.subTradeId, subTradeId)
                    .and().eq(TradeItemMainBatchRelExtra.$.statusFlag, StatusFlag.VALID).query();

            return itemRelList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public TradeExtraSecrecyPhone findTradeExtraSecrecyPhone(Long tradeExtraId) throws Exception {
        Checks.verifyNotNull(tradeExtraId, "tradeExtraId");
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeExtraSecrecyPhone, String> dao = helper.getDao(TradeExtraSecrecyPhone.class);
            QueryBuilder<TradeExtraSecrecyPhone, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(TradeExtraSecrecyPhone.$.serverUpdateTime, false);
            return queryBuilder.where().eq(TradeExtraSecrecyPhone.$.tradeExtraId, tradeExtraId).and().eq(TradeExtraSecrecyPhone.$.statusFlag, StatusFlag.VALID).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public Map<Long, BigDecimal> getTradeItemQuantityMap(Long subTradeId) throws Exception {
        Map<Long, BigDecimal> quantityMap = new HashMap<>();
        DatabaseHelper helper = DBHelperManager.getHelper();
        Dao<TradeItemMainBatchRel, String> dao = helper.getDao(TradeItemMainBatchRel.class);
        QueryBuilder<TradeItemMainBatchRel, String> queryBuilder = dao.queryBuilder();
        List<TradeItemMainBatchRel> tradeItemMainBatchRelList = queryBuilder.where()
                .eq(TradeItemMainBatchRel.$.subTradeId, subTradeId)
                .and()
                .eq(TradeItemMainBatchRel.$.statusFlag, StatusFlag.VALID)
                .query();
        if (Utils.isNotEmpty(tradeItemMainBatchRelList)) {
            for (TradeItemMainBatchRel t : tradeItemMainBatchRelList) {
                quantityMap.put(t.getSubItemId(), t.getTradeItemNum());
            }
        }
        return quantityMap;
    }

    @Override
    public TradeInvoiceNo findTradeInvoiceNoByTradeId(Long tradeId) {
        TradeInvoiceNo tradeInvoiceNo = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeInvoiceNo, String> dao = helper.getDao(TradeInvoiceNo.class);
            QueryBuilder<TradeInvoiceNo, String> queryBuilder = dao.queryBuilder();

            tradeInvoiceNo = queryBuilder.where().eq(TradeInvoiceNo.$.tradeId, tradeId).and().eq(TradeExtraSecrecyPhone.$.statusFlag, StatusFlag.VALID).queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return tradeInvoiceNo;
    }
}
