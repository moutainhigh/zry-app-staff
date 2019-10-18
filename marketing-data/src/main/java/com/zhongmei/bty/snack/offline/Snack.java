package com.zhongmei.bty.snack.offline;


import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;


public class Snack {
    private static final String TAG = Snack.class.getSimpleName() + "--->";

    private static ISnack snack;

    public static void init(ISnack snack) {
        Snack.snack = snack;
    }

    public static void release() {
        snack.release();
    }


    public static boolean netWorkAvailable() {
        return snack.netWorkAvailable();
    }

    public static boolean netWorkUnavailable() {
        return snack.netWorkUnavailable();
    }

    public static boolean isOfflineTrade(TradeVo tradeVo) {
        return snack.isOfflineTrade(tradeVo);
    }

    public static boolean isOfflineTrade(Trade trade) {
        return snack.isOfflineTrade(trade);
    }

    public static boolean isSnackBusiness(TradeVo tradeVo) {
        return snack.isSnackBusiness(tradeVo);
    }

    public static boolean isSnackBusiness(BusinessType businessType) {
        return snack.isSnackBusiness(businessType);
    }

    public static boolean isSnackBusiness(Trade trade) {
        return snack.isSnackBusiness(trade);
    }

    public static boolean isOfflineEnable() {
        return snack.isOfflineEnable();
    }
}
