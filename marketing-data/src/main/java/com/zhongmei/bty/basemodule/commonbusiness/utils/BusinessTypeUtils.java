package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;



public class BusinessTypeUtils {


    public static String RETAIL_TPPE = "78";

    public static boolean isRetail() {
        return ShopInfoCfg.getInstance().getShopInfo().getMainMealCode().startsWith(RETAIL_TPPE);
    }
}
