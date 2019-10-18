package com.zhongmei.bty.cashier.tradedeal;


public class TradeDealServiceUtil {
    private static final String TAG = TradeDealServiceUtil.class.getSimpleName();





    public static String generateAutoAcceptSettingKey(long id) {
        return "sp_auto_accept_item_switch_" + id;
    }
}
