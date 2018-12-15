package com.zhongmei.bty.dinner.util;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeType;

public class UnionUtil {

    public static boolean isUnionTrade(TradeType tradeType) {
        return tradeType == TradeType.UNOIN_TABLE_MAIN || tradeType == TradeType.UNOIN_TABLE_SUB;
    }

    public static boolean isUnionMainTrade(TradeType tradeType) {
        return tradeType == TradeType.UNOIN_TABLE_MAIN;
    }

    public static boolean isUnionSubTrade(TradeType tradeType) {
        return tradeType == TradeType.UNOIN_TABLE_SUB;
    }

    public static boolean isUnionSubTrade(Trade trade) {
        return trade != null && trade.getTradeType() == TradeType.UNOIN_TABLE_SUB;
    }

}
