package com.zhongmei.beauty.operates;

import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 * 为UI提供数据支持
 */

public class BeautyTradeDataManager {
    private static final String TAG = BeautyTradeDataManager.class.getSimpleName();


    /**
     * 查询到店人数
     *
     * @param helper
     * @return
     */
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

    /**
     * 查询预约单数
     *
     * @param helper
     * @return
     */
    public int queryReserverNumber(DatabaseHelper helper) throws Exception{
        Dao<Booking, String> bookingDao = helper.getDao(Booking.class);
        QueryBuilder bookingBuilder = bookingDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        bookingBuilder.where().eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and()
                .gt(Trade.$.serverCreateTime, date)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Booking.$.orderStatus, BookingOrderStatus.UNARRIVED);
        return (int) bookingBuilder.countOf();
    }

    /**
     * 查询今日订单数
     *
     * @param helper
     * @return
     */
    public int queryTradeNumber(DatabaseHelper helper) throws Exception {
        Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
        QueryBuilder tradeBuilder = tradeDao.queryBuilder();

        long date = DateTimeUtils.getDayStart(new Date());

        tradeBuilder.where().eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and()
                .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED)
                .and()
                .eq(Trade.$.statusFlag, StatusFlag.VALID)
                .and()
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN);
        return (int) tradeBuilder.countOf();
    }

    /**
     * 查询新增会员数
     *
     * @return
     */
    public int queryMemberNumber() {
        //通过接口返回数据，缓存到share，本地创建会员添加成功后更新数据，数据结构：年月日:新增会员数量
        return CustomerUtil.getRegistMemberNumber();
    }

    /**
     * 查询未处理的今日预约订单
     *
     * @return
     */
    public int queryTodayReserverNumber(DatabaseHelper helper) throws Exception {
        //查询预订单数据
        Dao<Booking, String> bookingTradeDao = helper.getDao(Booking.class);
        QueryBuilder tradeBuilder = bookingTradeDao.queryBuilder();

        long startTime = DateTimeUtils.getDayStart(new Date());
        long endTime = DateTimeUtils.getDayEnd(new Date());

        tradeBuilder.where()/*.eq(Trade.$.businessType, BusinessType.BEAUTY)
                .and()*/
                .eq(Booking.$.orderStatus, BookingOrderStatus.UNARRIVED)
                .and()
                .gt(Booking.$.startTime, startTime)
                .and()
                .lt(Booking.$.startTime, endTime)
                .and()
                .eq(Booking.$.statusFlag, StatusFlag.VALID);
        return (int) tradeBuilder.countOf();
    }


    /**
     * 查询未处理的订单
     *
     * @return
     */
    public List<UnpaidTradeVo> queryUnpaidTrades(BusinessType businessType) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        List<UnpaidTradeVo> listUnpaidTradeVos = new ArrayList<>();

        try {
            List<Trade> trades = getUNFinishTradeByBusinessType(businessType, helper);
            List<TradeCustomer> customers = getCustomerByTrades(trades, helper);
            List<TradeExtra> extras = getTradeExtrasByTrades(trades, helper);

            Map<Long, List<TradeCustomer>> mapTradeCustomer = getTradeCustomerMap(customers);
//            Map<Long,TradeExtra> mapTradeExtra=getTradeExtraMap(extras);
            Map<Long, List<TradeItemVo>> mapTradeItemVos = getTradeItemVosByTrades(trades, helper);

            for (Trade trade : trades) {
                UnpaidTradeVo tradeVo = new UnpaidTradeVo();
                tradeVo.setTrade(trade);
//                tradeVo.setTradeExtra(mapTradeExtra.get(trade.getId()));
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

    /**
     * 根据页态获取订单
     *
     * @param businessType
     * @param helper
     * @return
     * @throws Exception
     */
    private List<Trade> getUNFinishTradeByBusinessType(BusinessType businessType, DatabaseHelper helper) throws Exception {
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
                .in(Trade.$.tradeType, TradeType.SELL, TradeType.UNOIN_TABLE_SUB, TradeType.UNOIN_TABLE_MAIN);


        return tradeBuilder.query();

    }

    /**
     * 根据订单查询TradeExtras
     *
     * @param trades
     * @param helper
     * @return
     * @throws Exception
     */
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

    /**
     * 获取订单对应的会员
     *
     * @param trades
     * @param helper
     * @return
     * @throws Exception
     */
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


    /**
     * 将订单会员组织成map集合，方便组装数据
     *
     * @param customers
     * @return
     */
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

    /**
     * 获取paymentvo
     *
     * @param tradeUuid
     * @return
     */
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


}
