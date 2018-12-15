package com.zhongmei.bty.cashier.util;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradePayStatus;

public class TradeStatusUtil {

    /**
     * 检测订单是不是支付中状态
     *
     * @param iDinnertableTrade
     * @return
     */
    public static boolean checkPayStatus(IDinnertableTrade iDinnertableTrade) {
        if (iDinnertableTrade.getTradePayStatus() == TradePayStatus.PAYING) {
            return false;
        }

        return true;
    }

    /**
     * 检查支付状态，如果在支付中，不允许做其他的操作，只允许结账
     *
     * @return true 检查通过
     */
    public static boolean checkPayStatus(Trade trade) {
        if (trade.getTradePayStatus() == TradePayStatus.PAYING) {
            return false;
        }
        return true;
    }

    /**
     * 检查支付状态，如果在支付中，不允许做其他的操作，只允许结账
     *
     * @return true 检查通过
     */
    public static boolean checkPrePayStatus(IDinnertableTrade iDinnertableTrade) {
        return iDinnertableTrade.getTradePayStatus() == TradePayStatus.PREPAID;
    }

    /**
     * 检查押金是否已经支付
     *
     * @return
     */
    public static boolean checkDespoit(DinnertableTradeVo dinnertableTradeVo) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null) {
            return false;
        }
        return dinnertableTradeVo.getTradeVo().isPaidTradeposit();
    }

    /**
     * 检查押金是否已经支付
     *
     * @return
     */
    public static boolean checkDespoit(TradeVo tradeVo) {
        if (tradeVo == null) {
            return false;
        }
        return tradeVo.isPaidTradeposit();
    }

    /**
     * 检查支付状态，如果在支付中，不允许做其他的操作，只允许结账
     *
     * @return true 检查通过
     */
    public static boolean checkPayStatus(DinnertableTradeVo dinnertableTradeVo) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null)
            return true;
        Trade trade = dinnertableTradeVo.getTradeVo().getTrade();
        return checkPayStatus(trade);
    }

}
