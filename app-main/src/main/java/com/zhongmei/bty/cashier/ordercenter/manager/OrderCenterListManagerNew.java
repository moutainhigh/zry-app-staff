package com.zhongmei.bty.cashier.ordercenter.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.commonbusiness.manager.SystemSettingsManager;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.bty.cashier.dal.OrderCenterDal;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OrderCenterListManagerNew extends OrderCenterListManager {

    private static final String TAG = OrderCenterListManagerNew.class.getSimpleName();

    @Override
    public List<TradePaymentVo> search(int tab, int position, String keyword, FilterCondition condition, Trade lastData) {
        int childTab = DbQueryConstant.UNPROCESSED_ALL;
        if (tab == DbQueryConstant.SALES) {
            childTab = DbQueryConstant.SALES_ALL;
        }
        OrderCenterDal orderCenterDBDal = OperatesFactory.create(OrderCenterDal.class);
        List<Trade> trades = orderCenterDBDal.queryTrade(childTab, position, keyword, condition, lastData);
        List<TradePaymentVo> tradePaymentVos = new ArrayList<>();
        for (Trade trade : trades) {
            String tradeUUID = trade.getUuid();
            TradePaymentVo tradePaymentVo = new TradePaymentVo();
            TradeVo tradeVo = new TradeVo();
            tradeVo.setTrade(trade);
            TradeExtra tradeExtra = orderCenterDBDal.getTradeExtra(tradeUUID);
            tradeVo.setTradeExtra(tradeExtra);
            if (tradeExtra != null && tradeExtra.getId() != null) {
                tradeVo.setTradeExtraSecrecyPhone(orderCenterDBDal.getTradeExtraSecrecyPhone(tradeExtra.getId()));
            }
            tradeVo.setTradeDeposit(orderCenterDBDal.getTradeDeposit(tradeUUID));
            tradeVo.setTradeTableList(orderCenterDBDal.getTradeTables(tradeUUID));
            tradeVo.setTradeCustomerList(orderCenterDBDal.getTradeCustomer(tradeUUID));
            tradePaymentVo.setTradeVo(tradeVo);
            tradePaymentVo.setDeliveryOrderVoList(orderCenterDBDal.getDeliveryOrder(tradeUUID));
            tradePaymentVo.setPaymentVoList(orderCenterDBDal.getPaymentVos(tradeUUID));
            tradePaymentVos.add(tradePaymentVo);
        }
                if ((childTab == DbQueryConstant.UNPROCESSED_ALL && position == 4) || (childTab == DbQueryConstant.SALES_ALL && position == 5)) {
            List<TradePaymentVo> vos = new ArrayList<>();
            for (TradePaymentVo tradePaymentVo : tradePaymentVos) {
                TradeVo tradeVo = tradePaymentVo.getTradeVo();
                                List<TradeTable> tradeTables = tradeVo.getTradeTableList();
                if (EmptyUtils.isNotEmpty(tradeTables)) {
                    for (TradeTable tradeTable : tradeTables) {
                        String tableName = tradeTable.getTableName();
                        if (tableName.contains(keyword)) {
                            vos.add(tradePaymentVo);
                            break;
                        }
                    }
                    continue;
                }
                                TradeExtra extra = tradeVo.getTradeExtra();
                String numberPlate = extra.getNumberPlate();
                if (!TextUtils.isEmpty(numberPlate)) {
                    if (numberPlate.contains(keyword)) {
                        vos.add(tradePaymentVo);
                    }
                    continue;
                }
                String serialNumber = extra.getSerialNumber();
                if (!TextUtils.isEmpty(serialNumber) && serialNumber.contains(keyword)) {
                    vos.add(tradePaymentVo);
                }
            }
            return vos;
        }
        return tradePaymentVos;
    }

    @Override
    public List<TradePaymentVo> loadData(int childTab, FilterCondition condition, Trade lastData) {
        OrderCenterDal orderCenterDBDal = OperatesFactory.create(OrderCenterDal.class);
        List<Trade> trades = orderCenterDBDal.queryTrade(childTab, -1, null, condition, lastData,
                BusinessType.SNACK, BusinessType.TAKEAWAY);
        try {
            List<TradePaymentVo> tradePaymentVos = new ArrayList<>();
            for (Trade trade : trades) {
                String tradeUUID = trade.getUuid();
                List<PaymentVo> paymentVos = orderCenterDBDal.getPaymentVos(tradeUUID);

                Long lastClosingTime = SystemSettingsManager.queryLastClosingAccountRecord();
                boolean isClosedOrder = isClosedOrder(paymentVos, lastClosingTime);

                if (!condition.isCleanMode() || !isClosedOrder) {
                    TradePaymentVo tradePaymentVo = new TradePaymentVo();
                    TradeVo tradeVo = new TradeVo();
                    tradeVo.setTrade(trade);
                    TradeExtra tradeExtra = orderCenterDBDal.getTradeExtra(tradeUUID);
                    tradeVo.setTradeExtra(tradeExtra);
                    if (tradeExtra != null && tradeExtra.getId() != null) {
                        tradeVo.setTradeExtraSecrecyPhone(orderCenterDBDal.getTradeExtraSecrecyPhone(tradeExtra.getId()));
                    }
                    tradeVo.setTradeDeposit(orderCenterDBDal.getTradeDeposit(tradeUUID));
                    tradeVo.setTradeTableList(orderCenterDBDal.getTradeTables(tradeUUID));
                    tradeVo.setTradeCustomerList(orderCenterDBDal.getTradeCustomer(tradeUUID));
                    tradePaymentVo.setTradeVo(tradeVo);
                    tradePaymentVo.setDeliveryOrderVoList(orderCenterDBDal.getDeliveryOrder(tradeUUID));
                    tradePaymentVo.setPaymentVoList(paymentVos);
                    tradePaymentVos.add(tradePaymentVo);
                    tradeVo.setVerifyKoubeiOrder(orderCenterDBDal.getVerifyKoubeiOrder(tradeUUID));
                }
            }

            return tradePaymentVos;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public long countOrder(int tab) {
        OrderCenterDal orderCenterDBDal = OperatesFactory.create(OrderCenterDal.class);
        return orderCenterDBDal.countOf(tab);
    }
}
