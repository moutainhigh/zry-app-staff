package com.zhongmei.bty.cashier.util;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradePayStatus;

public class TradeStatusUtil {


    public static boolean checkPayStatus(IDinnertableTrade iDinnertableTrade) {
        if (iDinnertableTrade.getTradePayStatus() == TradePayStatus.PAYING) {
            return false;
        }

        return true;
    }


    public static boolean checkPayStatus(Trade trade) {
        if (trade.getTradePayStatus() == TradePayStatus.PAYING) {
            return false;
        }
        return true;
    }


    public static boolean checkPrePayStatus(IDinnertableTrade iDinnertableTrade) {
        return iDinnertableTrade.getTradePayStatus() == TradePayStatus.PREPAID;
    }


    public static boolean checkDespoit(DinnertableTradeVo dinnertableTradeVo) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null) {
            return false;
        }
        return dinnertableTradeVo.getTradeVo().isPaidTradeposit();
    }


    public static boolean checkDespoit(TradeVo tradeVo) {
        if (tradeVo == null) {
            return false;
        }
        return tradeVo.isPaidTradeposit();
    }


    public static boolean checkPayStatus(DinnertableTradeVo dinnertableTradeVo) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null)
            return true;
        Trade trade = dinnertableTradeVo.getTradeVo().getTrade();
        return checkPayStatus(trade);
    }

}
