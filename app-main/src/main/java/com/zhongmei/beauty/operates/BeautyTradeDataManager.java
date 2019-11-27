package com.zhongmei.beauty.operates;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.customer.bean.TaskQueryReq;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.bty.customer.util.CustomerContants;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.beauty.entity.UnpaidTradeVo;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BeautyTradeDataManager {
    private static final String TAG = BeautyTradeDataManager.class.getSimpleName();


    public int queryCustomerNumber(DatabaseHelper helper) throws Exception {
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        QueryBuilder tradeBuilder = tradeDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        tradeBuilder.where().eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and()
                .gt(Trade.$.serverCreateTime, date)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN);
        return (int) tradeBuilder.countOf();
    }


    public int queryReserverNumber(DatabaseHelper helper) throws Exception {
        Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
        QueryBuilder bookingBuilder = bookingDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        bookingBuilder.where()
                .gt(Booking.$.serverCreateTime, date)
                .and()
                .eq(Booking.$.statusFlag, StatusFlag.VALID);
        return (int) bookingBuilder.countOf();
    }


    public int queryUnDealReserverNumber(DatabaseHelper helper) throws Exception {
        Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
        QueryBuilder bookingBuilder = bookingDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        bookingBuilder.where()
                .gt(Booking.$.serverCreateTime, date)
                .and()
                .eq(Booking.$.statusFlag, StatusFlag.VALID)
                .and()
                .eq(Booking.$.orderStatus, BookingOrderStatus.UNPROCESS);
        return (int) bookingBuilder.countOf();
    }


    public int queryTradeNumber(DatabaseHelper helper) throws Exception {
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        QueryBuilder tradeBuilder = tradeDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        tradeBuilder.where().eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and().
                gt(Trade.$.serverCreateTime, date)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .eq(Trade.$.tradeType, TradeType.SELL);
        List<Trade> trades = tradeBuilder.query();
        return (int) tradeBuilder.countOf();
    }


    public int queryMemberNumber() {
        return CustomerUtil.getRegistMemberNumber();
    }


    public int queryTodayReserverNumber(DatabaseHelper helper) throws Exception {
        Dao<Booking, String> bookingTradeDao = helper.getDao(Booking.class);
        QueryBuilder tradeBuilder = bookingTradeDao.queryBuilder();

        long startTime = DateTimeUtils.getDayStart(new Date());
        long endTime = DateTimeUtils.getDayEnd(new Date());

        tradeBuilder.where()
                .eq(Booking.$.orderStatus, BookingOrderStatus.UNARRIVED)
                .and()
                .gt(Booking.$.startTime, startTime)
                .and()
                .lt(Booking.$.startTime, endTime)
                .and()
                .eq(Booking.$.statusFlag, StatusFlag.VALID);
        return (int) tradeBuilder.countOf();
    }


    public int queryUnPaidTradeNumber(DatabaseHelper helper) throws Exception {
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        QueryBuilder tradeBuilder = tradeDao.queryBuilder();
        tradeBuilder.where().eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and()
                .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN);
        return (int) tradeBuilder.countOf();
    }


    public List<UnpaidTradeVo> queryUnpaidTrades(BusinessType businessType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<UnpaidTradeVo> listUnpaidTradeVos = new ArrayList<>();

        try {
//            List<Long> tradeIds = getTradeIdByUser(Session.getAuthUser(), helper);
//
//            if(Utils.isEmpty(tradeIds)){
//                return listUnpaidTradeVos;
//            }


            List<Trade> trades = getUNFinishTradeByBusinessType(null, businessType, helper);
            List<TradeCustomer> customers = getCustomerByTrades(trades, helper);
            List<TradeExtra> extras = getTradeExtrasByTrades(trades, helper);

            Map<Long, List<TradeCustomer>> mapTradeCustomer = getTradeCustomerMap(customers);
            Map<Long, List<TradeItemVo>> mapTradeItemVos = getTradeItemVosByTrades(trades, helper);

            for (Trade trade : trades) {
                UnpaidTradeVo tradeVo = new UnpaidTradeVo();
                tradeVo.setTrade(trade);
                tradeVo.setmTradeCustomer(mapTradeCustomer.get(trade.getId()));
                tradeVo.setTradeItemList(mapTradeItemVos.get(trade.getId()));
                listUnpaidTradeVos.add(tradeVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listUnpaidTradeVos;
    }


    private List<Trade> getUNFinishTradeByBusinessType(List<Long> tradeIds, BusinessType businessType, DatabaseHelper helper) throws Exception {
        if (helper == null) {
            throw new Exception("DatabaseHelper is null!");
        }
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);

        QueryBuilder tradeBuilder = tradeDao.queryBuilder();
        tradeBuilder.selectColumns(Trade.$.uuid,
                Trade.$.id,
                Trade.$.serialNumber,
                Trade.$.serverUpdateTime,
                Trade.$.serverCreateTime,
                Trade.$.tradeStatus,
                Trade.$.sourceId,
                Trade.$.saleAmount,
                Trade.$.tradeAmount,
                Trade.$.deliveryType,
                Trade.$.tradePayStatus,
                Trade.$.tradeType,
                Trade.$.businessType
        );

        tradeBuilder.where().eq(Trade.$.businessType, businessType)
                .and()
                .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN)
//                .and()
//                .in(Trade.$.id, tradeIds)
        ;


        return tradeBuilder.query();

    }


    public List<TradeExtra> getTradeExtrasByTrades(List<Trade> trades, DatabaseHelper helper) throws Exception {
        if (helper == null) {
            throw new Exception("DatabaseHelper is null!");
        }

        if (Utils.isEmpty(trades)) {
            return null;
        }

        Set<Long> tradeIds = new HashSet<>();
        for (Trade trade : trades) {
            tradeIds.add(trade.getId());
        }

        Dao<TradeExtra, Long> extraDao = helper.getDao(TradeExtra.class);
        QueryBuilder queryBuilder = extraDao.queryBuilder().selectColumns(TradeExtra.$.tradeId, TradeExtra.$.serialNumber, TradeExtra.$.serverUpdateTime);
        List<TradeExtra> tradeExtraList = queryBuilder.where().eq(TradeExtra.$.statusFlag, StatusFlag.VALID).and().in(TradeExtra.$.tradeId, tradeIds).query();

        return tradeExtraList;
    }


    private List<TradeCustomer> getCustomerByTrades(List<Trade> trades, DatabaseHelper helper) throws Exception {
        if (helper == null) {
            throw new Exception("DatabaseHelper is null!");
        }

        if (Utils.isEmpty(trades)) {
            return null;
        }

        Set<Long> tradeIds = new HashSet<>();
        for (Trade trade : trades) {
            tradeIds.add(trade.getId());
        }


        Dao<TradeCustomer, String> tradeCustomerDao = helper.getDao(TradeCustomer.class);

        QueryBuilder customerBuilder = tradeCustomerDao.queryBuilder();
        customerBuilder.selectColumns(TradeCustomer.$.id,
                TradeCustomer.$.customerName,
                TradeCustomer.$.customerPhone,
                TradeCustomer.$.tradeId,
                TradeCustomer.$.tradeUuid,
                TradeCustomer.$.customerSex);

        customerBuilder.where().eq(TradeCustomer.$.statusFlag, StatusFlag.VALID).and()
                .eq(TradeCustomer.$.customerType, CustomerType.MEMBER).or().eq(TradeCustomer.$.customerType, CustomerType.CARD).and()
                .in(TradeCustomer.$.tradeId, tradeIds);


        return customerBuilder.query();
    }

    /**
     * 根据当前登陆的服务员，查询服务员对应的订单信息。
     * 相关表trade_user,trade_item_user
     *
     * @param user
     * @param helper
     * @return
     * @throws Exception
     */
    private List<Long> getTradeIdByUser(AuthUser user, DatabaseHelper helper) throws Exception {
        if (helper == null) {
            throw new Exception("DatabaseHelper is null!");
        }

        Dao<TradeUser, String> tradeUserDao = helper.getDao(TradeUser.class);

        QueryBuilder customerBuilder = tradeUserDao.queryBuilder();
        customerBuilder.selectColumns(TradeUser.$.id,
                TradeUser.$.tradeId);

        customerBuilder.where().eq(TradeUser.$.statusFlag, StatusFlag.VALID).and().eq(TradeUser.$.userId, user.getId());

        List<TradeUser> tradeUsers = customerBuilder.query();


        Set<Long> tradeIds = new HashSet<>();
        if (Utils.isNotEmpty(tradeUsers)) {
            for (TradeUser tradeUser : tradeUsers) {
                tradeIds.add(tradeUser.getTradeId());
            }
        }

        return new ArrayList<>(tradeIds);
    }


    private Map<Long, List<TradeItemVo>> getTradeItemVosByTrades(List<Trade> trades, DatabaseHelper helper) throws Exception {
        if (helper == null) {
            throw new Exception("DatabaseHelper is null!");
        }

        if (Utils.isEmpty(trades)) {
            return null;
        }

        Map<Long, List<TradeItemVo>> mapTradeItemVos = new HashMap<>();
        Set<Long> tradeIds = new HashSet<>();
        for (Trade trade : trades) {
            tradeIds.add(trade.getId());
        }

        Dao<TradeItem, String> tradeItemDao = helper.getDao(TradeItem.class);
        List<TradeItem> listTradeItem = tradeItemDao.queryBuilder().where().eq(TradeItem.$.statusFlag, StatusFlag.VALID).and().in(TradeItem.$.tradeId, tradeIds).query();

        if (Utils.isEmpty(listTradeItem)) {
            return mapTradeItemVos;
        }


        for (TradeItem tradeItem : listTradeItem) {

            if (!mapTradeItemVos.containsKey(tradeItem.getTradeId())) {
                mapTradeItemVos.put(tradeItem.getTradeId(), new ArrayList<TradeItemVo>());
            }

            TradeItemVo vo = new TradeItemVo();
            vo.setTradeItem(tradeItem);

            mapTradeItemVos.get(tradeItem.getTradeId()).add(vo);
        }

        return mapTradeItemVos;
    }


    private Map<Long, List<TradeCustomer>> getTradeCustomerMap(List<TradeCustomer> customers) {
        Map<Long, List<TradeCustomer>> mapTradeCustomer = new HashMap<>();

        if (Utils.isEmpty(customers)) {
            return mapTradeCustomer;
        }

        for (TradeCustomer customer : customers) {
            if (!mapTradeCustomer.containsKey(customer.getTradeId())) {
                mapTradeCustomer.put(customer.getTradeId(), new ArrayList<TradeCustomer>());
            }
            mapTradeCustomer.get(customer.getTradeId()).add(customer);
        }

        return mapTradeCustomer;
    }

    private Map<Long, TradeExtra> getTradeExtraMap(List<TradeExtra> extras) {
        Map<Long, TradeExtra> mapTradeExtra = new HashMap<>();

        if (Utils.isEmpty(extras)) {
            return mapTradeExtra;
        }

        for (TradeExtra extra : extras) {
            mapTradeExtra.put(extra.getTradeId(), extra);
        }

        return mapTradeExtra;
    }


    public PaymentVo getPaymentVo(String tradeUuid) {
        if (!TextUtils.isEmpty(tradeUuid)) {
            try {
                List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
                TradeDal dal = OperatesFactory.create(TradeDal.class);
                paymentVos = dal.listPayment(tradeUuid);
                if (paymentVos != null && paymentVos.size() > 0) {
                    PaymentVo paymentVo = new PaymentVo();
                    paymentVo.setPaymentItemList(paymentVos.get(0).getPaymentItemList());
                    Payment payment = paymentVos.get(0).getPayment();
                    paymentVo.setPayment(payment);

                    return paymentVo;
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        return null;
    }

    public void getTaskNumberSync(YFResponseListener listener) {
        TaskQueryReq taskQueryReq = new TaskQueryReq();
        taskQueryReq.setPageNo(1);
        taskQueryReq.setPageSize(20);
        taskQueryReq.setRemindTime(getDate(0).getTime());
        taskQueryReq.setStatus(1);
        taskQueryReq.setType(1);

        BeautyCustomerOperates operates = OperatesFactory.create(BeautyCustomerOperates.class);
        operates.getTaskList(taskQueryReq, listener);
    }

    private Date getDate(int afterDay) {
        Long curTime = System.currentTimeMillis();
        Long tarTime = curTime + (afterDay * 24 * 60 * 60 * 1000);
        return new Date(tarTime);
    }


}
