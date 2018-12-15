package com.zhongmei.yunfu.sync;

import android.os.AsyncTask;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.SalesPromotionCache;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.session.core.user.UserCurrency;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ICurrency;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

/**
 * 门店缓存配置
 * Created by demo on 2018/12/15
 */
public class ShopCacheInitCheck extends AbsInitCheck {

    public ShopCacheInitCheck() {
        super(CheckType.CacheInitCheck);
    }

    @Override
    public void handleCheck(InitCheckCallback callback) {
        checkProgress(ShopCacheInitCheck.this, 80, MainApplication.getInstance().getString(R.string.init_shop_cache));
        waitRandom();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DishCache.refresh();
                SalesPromotionCache.init();
                //Calm3PrtConfigManager.init();
                ServerSettingCache.getInstance().refreshAll();
                //NewOrderlistenerService.startService(MainApplication.getInstance());
                ICurrency currency = new UserCurrency(ShopInfoCfg.getInstance().currencyId);
                ShopInfoCfg.getInstance().setCurrency(currency);
                checkProgress(ShopCacheInitCheck.this, 100, BaseApplication.getInstance().getString(R.string.init_shop_cache_success));
                checkComplete(ShopCacheInitCheck.this, true, null);
                //IPrintHelper.Holder.registerInstance(DinnerPrintHelper.getInstance());
            }
        });
    }
}
