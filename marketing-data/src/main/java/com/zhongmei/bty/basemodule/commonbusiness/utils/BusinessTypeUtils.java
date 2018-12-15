package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;

/**
 * Created by demo on 2018/12/15
 */

public class BusinessTypeUtils {

    /**
     * 78表示零售
     */
    public static String RETAIL_TPPE = "78";

    public static boolean isRetail() {
        return ShopInfoCfg.getInstance().getShopInfo().getMainMealCode().startsWith(RETAIL_TPPE);
    }
}
