package com.zhongmei.bty.customer.util;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;

/**
 * 金城网络请求地址
 *
 * @version v8.1
 * @since 2017.09.13.
 */
public class JCAddressUtil {

    /**
     * 金城售卡记录
     */
    public static String getJCSaleOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/entitycard/jincheng/queryAllByShop");
    }

    /**
     * 金城充值酒局
     */
    public static String getJCChargeOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/member/storeMemberValueHistory");
    }

    /**
     * 金城退卡记录
     */
    public static String getJcRefundCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/entitycard/jincheng/card_refund");
    }
}
