package com.zhongmei.bty.common.util;

import android.content.Context;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.orderdish.Bean.TableTradeInfo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.util.ArrayList;
import java.util.List;


public class IntentOrderUtil {

    public interface OnStartActivityCallback {
        void onStartActivity(boolean success);
    }


    public static void startDinnerMainActivity(final Context context, final String tradeUuid, final OnStartActivityCallback callback) {
        TaskContext.execute(new SimpleAsyncTask<Boolean>() {
            @Override
            public Boolean doInBackground(Void... params) {
                try {
                                        TablesDal tableDal = OperatesFactory.create(TablesDal.class);
                    TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
                    TradeVo tradeVo = mTradeDal.findTrade(tradeUuid, false);
                    DinnerTableInfo dinnerTable = tableDal.getDinnerTableByTradeVo(tradeVo);
                    DinnertableTradeInfo dinnertableTradeInfo = DinnertableTradeInfo.create(tradeVo, dinnerTable);
                    DinnerShoppingCart.getInstance().resetOrderFromTable(dinnertableTradeInfo, true);


                    TableTradeInfo tableTradeInfo = new TableTradeInfo();
                    tableTradeInfo.setSerailNumber(dinnertableTradeInfo.getSerialNumber());
                    tableTradeInfo.setSpendTime(dinnertableTradeInfo.getSpendTime());
                    tableTradeInfo.setStatus(tradeVo);
                    tableTradeInfo.setTableSeats(dinnerTable.getTableId() == null ? null : tableDal.listTableSeatsByTableId(dinnerTable.getTableId()));
                    tableTradeInfo.setTradeType(dinnertableTradeInfo.getTradeType());
                    DinnerShoppingCart.getInstance().getDinnertableTradeInfo().setiDinnertableTrade(tableTradeInfo);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null) {
                    callback.onStartActivity(aVoid);
                }
                if (aVoid) {
                                    }
            }
        });
    }


    public static void startUnionMainActivity(final Context context, final Trade mainTrade, final List<Trade> subTradeList, final OnStartActivityCallback callback) {
        TaskContext.execute(new SimpleAsyncTask<Boolean>() {
            @Override
            public Boolean doInBackground(Void... params) {
                try {
                                        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                    TradeVo mainTradeVo = tradeDal.findTrade(mainTrade);
                    List<Trade> mainAndSubTradeList = Utils.asList(mainTrade);
                    mainAndSubTradeList.addAll(subTradeList);
                    List<TradeVo> mainAndSubTradeVos = tradeDal.getTradeVosByTrades(mainAndSubTradeList);

                    List<DinnertableTradeInfo> subTradeInfos = new ArrayList<>();
                    DinnertableTradeInfo mainTradeInfo = DinnerShoppingCart.getTableTradeInfo(mainAndSubTradeVos, subTradeInfos);
                    DinnerShoppingCart.getInstance().initTableTradeInfo(mainTradeInfo, mainTradeInfo);
                    DinnerShoppingCart.getInstance().resetOrderFromTable(mainTradeInfo, mainTradeInfo, false);


                    TableTradeInfo tableTradeInfo = new TableTradeInfo();
                    tableTradeInfo.setSerailNumber(mainTradeVo.getTradeExtra().getSerialNumber());
                    tableTradeInfo.setSpendTime(0);
                    tableTradeInfo.setStatus(mainTradeVo);
                    tableTradeInfo.setTableSeats(null);
                    tableTradeInfo.setTradeType(TradeType.UNOIN_TABLE_MAIN);
                    DinnerShoppingCart.getInstance().getDinnertableTradeInfo().setiDinnertableTrade(tableTradeInfo);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            public void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null) {
                    callback.onStartActivity(aVoid);
                }
                if (aVoid) {
                                    }
            }
        });
    }
}
