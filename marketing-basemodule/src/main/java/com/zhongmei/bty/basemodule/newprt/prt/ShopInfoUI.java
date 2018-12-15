package com.zhongmei.bty.basemodule.newprt.prt;

import android.content.Context;

import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

public abstract class ShopInfoUI {

    public static String getUIShopId(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_id) + ShopInfoCfg.getInstance().getShopInfo().getShopId();
    }

    public static String getUICommercialName(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_name) + ShopInfoCfg.getInstance().getShopInfo().getCommercialName();
    }

    public static String getUICommercialGroupId(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_group_id) + ShopInfoCfg.getInstance().getShopInfo().getBrandId();
    }

    public static String getUICommercialGroupName(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_group_name) + ShopInfoCfg.getInstance().getShopInfo().getBrandName();
    }

    public static String getUIcommercialAddress(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_address) + ShopInfoCfg.getInstance().getShopInfo().getCommercialAddress();
    }

    public static String getUICommercialPhone(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_phone) + ShopInfoCfg.getInstance().getShopInfo().getCommercialPhone();
    }

    public static String getUItabletNumber(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_settings_pos_id) + ShopInfoCfg.getInstance().getShopInfo().getTabletNumberFormat();
    }

    public static String getUISyncUrl(Context context) {
        return context.getString(com.zhongmei.yunfu.commonmodule.R.string.commonmodule_shop_server) + SwitchServerManager.getInstance().getServerKey();
    }
}
