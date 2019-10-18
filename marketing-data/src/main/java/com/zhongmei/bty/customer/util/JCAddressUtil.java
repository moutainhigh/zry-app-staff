package com.zhongmei.bty.customer.util;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;


public class JCAddressUtil {


    public static String getJCSaleOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/entitycard/jincheng/queryAllByShop");
    }


    public static String getJCChargeOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/member/storeMemberValueHistory");
    }


    public static String getJcRefundCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey().concat("/CalmRouter/v1/entitycard/jincheng/card_refund");
    }
}
