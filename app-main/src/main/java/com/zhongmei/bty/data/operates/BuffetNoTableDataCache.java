package com.zhongmei.bty.data.operates;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.entity.vo.BuffetTradeVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 * 自助餐订单数据提供器
 */

public class BuffetNoTableDataCache {
    private final int MSG_WHAT_QUERY_TRADE = 0x01;

    private BuffetNotableDataChangeObserver observer;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private OnBuffetRefreshListener onBuffetRefreshListener;


    private BuffetNoTableDataCache() {
        observer = new BuffetNotableDataChangeObserver();
    }

    private static class LazySingletHolder {
        public static final BuffetNoTableDataCache INSTANCE = new BuffetNoTableDataCache();
    }

    public static void onCreate(OnBuffetRefreshListener listener) {
        LazySingletHolder.INSTANCE._onCreate(listener);

    }

    public void _onCreate(OnBuffetRefreshListener listener) {
        this.onBuffetRefreshListener = listener;
        DatabaseHelper.Registry.register(observer);
        initHanlder();
        mHandler.sendEmptyMessage(MSG_WHAT_QUERY_TRADE);//
    }

    public static void onDestory() {
        LazySingletHolder.INSTANCE._onDestory();
    }


    public void _onDestory() {
        DatabaseHelper.Registry.unregister(observer);
        quitHandler();
    }

    private void initHanlder() {
        mHandlerThread = new HandlerThread("WORK_HANDLER");
        mHandlerThread.start();
        mHandler = new QueryDataHandler(mHandlerThread.getLooper());
    }

    private void quitHandler() {
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


    public class BuffetNotableDataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelperManager.getUri(Trade.class))
                    || uris.contains(DBHelperManager.getUri(TradeBuffetPeople.class))
                    || uris.contains(DBHelperManager.getUri(TradeDeposit.class))
                    || uris.contains(DBHelperManager.getUri(TradeDepositPayRelation.class))
                    || uris.contains(DBHelperManager.getUri(PaymentItem.class))) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessage(MSG_WHAT_QUERY_TRADE);
            }
        }
    }


    class QueryDataHandler extends Handler {


        public QueryDataHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            queryNoTableTrades();
            super.handleMessage(msg);
        }
    }


    /**
     * 查询所有的自助餐无桌订单
     */
    private void queryNoTableTrades() {
        List<BuffetTradeVo> listTradeVo = new ArrayList<>();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            //查询所有业务类型为Buffet的订单信息
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            List<Trade> tradeListAll = tradeDao.queryBuilder()
                    .selectColumns(Trade.$.uuid,
                            Trade.$.id,
                            Trade.$.serverUpdateTime,
                            Trade.$.serverCreateTime,
                            Trade.$.tradeStatus,
                            Trade.$.sourceId,
                            Trade.$.saleAmount,
                            Trade.$.tradeAmount,
                            Trade.$.tradePayStatus
                    ).orderBy(Trade.$.serverCreateTime, false)
                    .where()
                    .eq(Trade.$.businessType, BusinessType.BUFFET)
                    .and()
                    .in(Trade.$.tradeStatus, TradeStatus.CONFIRMED, TradeStatus.UNPROCESSED, TradeStatus.FINISH)
//                    .in(Trade.$.tradeStatus,TradeStatus.FINISH)
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(Trade.$.tradeType, TradeType.SELL)
                    .query();

            List<Long> tradeIdsList = new ArrayList<Long>();
            for (Trade trade : tradeListAll) {
                tradeIdsList.add(trade.getId());
            }


            Dao<TradeTable, String> tradeTableDao = helper.getDao(TradeTable.class);
            List<TradeTable> tradeTableListAll = tradeTableDao.queryBuilder().selectColumns(TradeTable.$.tradeId)
                    .where()
                    .eq(TradeTable.$.selfTableStatus, TableStatus.OCCUPIED)
                    .and()
                    .in(TradeTable.$.tradeId, tradeIdsList.toArray())
                    .query();

            Set<Long> setTableTradele = new HashSet<>();
            if (Utils.isNotEmpty(tradeTableListAll)) {
                for (TradeTable tradeTable : tradeTableListAll) {
                    setTableTradele.add(tradeTable.getTradeId());
                }
            }


            //根据订单id查询对应的extra信息
            Dao<TradeExtra, String> tradeExtraDao = helper.getDao(TradeExtra.class);
            List<TradeExtra> tradeExtrasListAll = tradeExtraDao.queryBuilder()
                    .selectColumns(TradeExtra.$.tradeId,
                            TradeExtra.$.serialNumber,
                            TradeExtra.$.isPrinted,
                            TradeExtra.$.hasServing)
                    .where()
                    .in(TradeExtra.$.tradeId, tradeIdsList.toArray())
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .query();


            Map<Long, TradeExtra> mapTradeExtraTmp = new HashMap<>();
            for (TradeExtra tradeExtra : tradeExtrasListAll) {
                mapTradeExtraTmp.put(tradeExtra.getTradeId(), tradeExtra);
            }

            //根据订单id查询套餐TradeItem
            Dao<TradeItem, String> itemDao = helper.getDao(TradeItem.class);
            List<TradeItem> tradeItemListAll = itemDao.queryBuilder()
                    .selectColumns(TradeItem.$.id,
                            TradeItem.$.tradeId,
                            TradeItem.$.skuName,
                            TradeItem.$.quantity
                    )
                    .where()
                    .in(TradeItem.$.tradeId, tradeIdsList.toArray())
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .query();

            Map<Long, List<TradeItemVo>> mapTradeItemListTmp = new HashMap<>();
            for (TradeItem tradeItem : tradeItemListAll) {
                if (!mapTradeItemListTmp.containsKey(tradeItem.getTradeId())) {
                    mapTradeItemListTmp.put(tradeItem.getTradeId(), new ArrayList<TradeItemVo>());
                }

                TradeItemVo tradeItemVo = new TradeItemVo();
                tradeItemVo.setTradeItem(tradeItem);
                mapTradeItemListTmp.get(tradeItem.getTradeId()).add(tradeItemVo);
            }

            //根据订单id查询押金数据
            Dao<TradeDeposit, String> depositDao = helper.getDao(TradeDeposit.class);
            List<TradeDeposit> tradeDepositListAll = depositDao.queryBuilder()
                    .selectColumns(TradeDeposit.$.tradeId,
                            TradeDeposit.$.depositRefund,
                            TradeDeposit.$.depositPay
                    )
                    .where()
                    .in(TradeItem.$.tradeId, tradeIdsList.toArray())
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID)
                    .query();

            Map<Long, TradeDeposit> mapTradeDepositTmp = new HashMap<>();
            for (TradeDeposit tradeDeposit : tradeDepositListAll) {
                mapTradeDepositTmp.put(tradeDeposit.getTradeId(), tradeDeposit);
            }

            // 订单押金关系
            List<TradeDepositPayRelation> tradeDepositPayRelations = helper.getDao(TradeDepositPayRelation.class)
                    .queryBuilder()
                    .orderBy(TradeDepositPayRelation.$.serverCreateTime, false)
                    .where()
                    .in(TradeDepositPayRelation.$.tradeId, tradeIdsList.toArray())
                    .query();


            Map<Long, List<TradeDepositPayRelation>> mapTradeDepositPayRelation = new HashMap<>();
            List<Long> paymentItemIds = new ArrayList<>();
            if (Utils.isNotEmpty(tradeDepositPayRelations)) {
                for (TradeDepositPayRelation depositPayRelation : tradeDepositPayRelations) {
                    if (!mapTradeDepositPayRelation.containsKey(depositPayRelation.getTradeId())) {
                        mapTradeDepositPayRelation.put(depositPayRelation.getTradeId(), new ArrayList<TradeDepositPayRelation>());
                    }

                    mapTradeDepositPayRelation.get(depositPayRelation.getTradeId()).add(depositPayRelation);
                    paymentItemIds.add(depositPayRelation.getPaymentItemId());
                }
            }

            //查询押金支付信息
            List<PaymentItem> tradeDepositPaymentItem = null;
            if (Utils.isNotEmpty(tradeDepositPayRelations)) {
                tradeDepositPaymentItem = helper.getDao(PaymentItem.class)
                        .queryBuilder()
                        .where()
                        .in(PaymentItem.$.id, paymentItemIds.toArray())
                        .query();
            }

            Map<Long, PaymentItem> mapPaymentItems = new HashMap<>();
            if (Utils.isNotEmpty(tradeDepositPaymentItem)) {
                for (PaymentItem paymentItem : tradeDepositPaymentItem) {
                    mapPaymentItems.put(paymentItem.getId(), paymentItem);
                }
            }


            Dao<TradeBuffetPeople, Long> tradeBuffetPeopleDao = helper.getDao(TradeBuffetPeople.class);
            List<TradeBuffetPeople> tradeBuffetPeoples = tradeBuffetPeopleDao.queryBuilder().selectColumns(
                    TradeBuffetPeople.$.id,
                    TradeBuffetPeople.$.cartePrice,
                    TradeBuffetPeople.$.carteNormsName,
                    TradeBuffetPeople.$.carteNormsId,
                    TradeBuffetPeople.$.peopleCount,
                    TradeBuffetPeople.$.tradeId
            ).where().in(TradeBuffetPeople.$.tradeId, tradeIdsList.toArray())
                    .and()
                    .eq(Trade.$.statusFlag, StatusFlag.VALID).query();
            Map<Long, List<TradeBuffetPeople>> mapBuffetPeople = new HashMap<>();
            for (TradeBuffetPeople tradeBuffetPeople : tradeBuffetPeoples) {
                if (!mapBuffetPeople.containsKey(tradeBuffetPeople.getTradeId())) {
                    mapBuffetPeople.put(tradeBuffetPeople.getTradeId(), new ArrayList<TradeBuffetPeople>());
                }
                mapBuffetPeople.get(tradeBuffetPeople.getTradeId()).add(tradeBuffetPeople);
            }


            //将所有的数据进行组装，返回订单列表
            for (Trade trade : tradeListAll) {
                //如果没有菜品
                if (!mapTradeExtraTmp.containsKey(trade.getId()) || !mapTradeItemListTmp.containsKey(trade.getId()) || setTableTradele.contains(trade.getId())) {
                    continue;
                }

                //如果没有押金的
                if (trade.getTradeStatus().value() == TradeStatus.FINISH.value() && !isNeedReturnDeposit(mapTradeDepositPayRelation.get(trade.getId()), mapPaymentItems)) {
                    continue;
                }

                BuffetTradeVo tradeVo = new BuffetTradeVo();
                tradeVo.setTrade(trade);
                tradeVo.setTradeExtra(mapTradeExtraTmp.get(trade.getId()));
                tradeVo.setTradeItemList(mapTradeItemListTmp.get(trade.getId()));
                tradeVo.setTradeItemList(mapTradeItemListTmp.get(trade.getId()));
                tradeVo.setTradeDeposit(mapTradeDepositTmp.get(trade.getId()));
                tradeVo.setTradeBuffetPeoples(mapBuffetPeople.get(trade.getId()));
                listTradeVo.add(tradeVo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }


        if (onBuffetRefreshListener != null) {
            onBuffetRefreshListener.onBuffetRefresh(listTradeVo);
        }
    }


    /**
     * 判断是否需要退押金
     *
     * @param listPayRelation
     * @param mapPayment
     * @return
     */
    private boolean isNeedReturnDeposit(List<TradeDepositPayRelation> listPayRelation, Map<Long, PaymentItem> mapPayment) {
        boolean isNeedReturnDeposit = false;

        if (Utils.isEmpty(listPayRelation)) {
            return false;
        }

        for (TradeDepositPayRelation tradeDepositPayRelation : listPayRelation) {
            PaymentItem paymentItem = mapPayment.get(tradeDepositPayRelation.getPaymentItemId());
            if (paymentItem == null) {
                continue;
            }

            if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                isNeedReturnDeposit = true;
                continue;
            }

            if (paymentItem.getPayStatus() == TradePayStatus.REFUNDED) {
                isNeedReturnDeposit = false;
                break;
            }
        }
        return isNeedReturnDeposit;
    }


    public interface OnBuffetRefreshListener {
        void onBuffetRefresh(List<BuffetTradeVo> listTradeVo);
    }

}
