package com.zhongmei.bty.cashier.tradedeal;

/**
 * @Date：2015年9月14日 下午3:21:45
 * @Description: 订单处理后台服务工具类
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class TradeDealServiceUtil {
    private static final String TAG = TradeDealServiceUtil.class.getSimpleName();

    /*public static void startTradeDealService(Context context) {
        Intent intent = new Intent(context, TradeDealService.class);
        context.startService(intent);
    }*/

    /*public static void stopTradeDealService(Context context) {
        Intent intent = new Intent(context, TradeDealService.class);
        context.stopService(intent);
    }*/

    public static String generateAutoAcceptSettingKey(long id) {
        return "sp_auto_accept_item_switch_" + id;
    }
}
