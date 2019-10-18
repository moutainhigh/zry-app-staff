package com.zhongmei.bty.cashier.util;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

public class TradeSourceUtils {

    public static boolean isTradeFromWeixin(DinnertableTradeVo dinnertableTradeVo) {
        return isTradeFromSourceId(dinnertableTradeVo, SourceId.WECHAT);
    }

    private static boolean isTradeFromSourceId(DinnertableTradeVo dinnertableTradeVo, SourceId sourceId) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getDinnertableTrade() == null || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            return false;
        }

        return dinnertableTradeVo.getTradeVo().getTrade().getSource() == sourceId;
    }


    public static boolean isOtherPlatformTrade(DinnertableTradeVo tradeVo) {
        return isTradeUnProcessed(tradeVo, SourceId.WECHAT)
                || isTradeUnProcessed(tradeVo, SourceId.XIN_MEI_DA)
                || isTradeUnProcessed(tradeVo, SourceId.KOU_BEI)
                || isTradePayedUnAcceptFromBAIDURICE(tradeVo)
                || isTradePayedUnAcceptFromFAMILIAR(tradeVo)
                || isTradePayedUnAcceptFromOpenPlatform(tradeVo);
    }


    public static boolean isTradePayedUnAcceptFromBAIDURICE(DinnertableTradeVo dinnertableTradeVo) {

        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null
                || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            return false;
        }

        Trade trade = dinnertableTradeVo.getTradeVo().getTrade();

        if (trade.getTradeStatus() == TradeStatus.UNPROCESSED
                && trade.getSource() == SourceId.BAIDU_RICE
                && trade.getTradePayStatus() == TradePayStatus.PAID) {
            return true;
        }
        return false;
    }

    public static boolean isTradeFromWeixinUnproccess(TradeVo tradeVo) {
        return isTradeUnProcessed(tradeVo, SourceId.WECHAT);
    }


    public static boolean isTradePayedUnAcceptFromFAMILIAR(DinnertableTradeVo dinnertableTradeVo) {
        return isTradeUnProcessed(dinnertableTradeVo, SourceId.FAMILIAR);
    }



    public static boolean isTradePayedUnAcceptFromOpenPlatform(DinnertableTradeVo dinnertableTradeVo) {
        return isTradeUnProcessed(dinnertableTradeVo, SourceId.OPEN_PLATFORM);
    }



    public static boolean isTradeUnProcessed(DinnertableTradeVo dinnertableTradeVo, SourceId sourceId) {
        if (dinnertableTradeVo == null
                || dinnertableTradeVo.getDinnertableTrade() == null
                || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            return false;
        }

        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        Trade trade = tradeVo.getTrade();
        if (dinnertableTradeVo.getDinnertableTrade().getTradeStatus() == TradeStatus.UNPROCESSED) {
            return sourceId == null || sourceId == trade.getSource();
        }
        return false;
    }

    public static boolean isTradeUnProcessed(TradeVo tradeVo, SourceId sourceId) {
        if (tradeVo == null || tradeVo.getTrade() == null) {
            return false;
        }

        Trade trade = tradeVo.getTrade();
        if (trade.getTradeStatus() == TradeStatus.UNPROCESSED) {
            return sourceId == null || sourceId == trade.getSource();
        }
        return false;
    }


    public static boolean isTradePayedAcceptedFromBAIDURICE(DinnertableTradeVo dinnertableTradeVo) {
        return isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.BAIDU_RICE);
    }

    public static boolean isTradePayedFromShuKe(DinnertableTradeVo dinnertableTradeVo) {
        return isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.FAMILIAR);
    }

    public static boolean isTradePayedFromOpenFlatform(DinnertableTradeVo dinnertableTradeVo) {
        return isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.OPEN_PLATFORM);
    }


    public static boolean isTradePayedAndConfirmed(DinnertableTradeVo dinnertableTradeVo, SourceId sourceId) {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null
                || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            return false;
        }

        Trade trade = dinnertableTradeVo.getTradeVo().getTrade();
        return isTradePayedAndConfirmed(trade, sourceId);
    }

    public static boolean isTradePayedAndConfirmed(Trade trade, SourceId sourceId) {
        if (trade.getTradeStatus() == TradeStatus.CONFIRMED && trade.getTradePayStatus() == TradePayStatus.PAID) {
            return trade.getSource() == sourceId;
        }
        return false;
    }
}
