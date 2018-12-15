package com.zhongmei.yunfu.sync;

import android.app.Application;

import com.zhongmei.bty.common.util.UpdateManager;
import com.zhongmei.util.SettingManager;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;

/**
 * 获取门店配置信息
 * Created by demo on 2018/12/15
 */
public class ShopConfigInitCheck extends AbsInitCheck {

    public ShopConfigInitCheck() {
        super(CheckType.ConfigInitCheck);
        //Application appContext = MainApplication.getInstance();
        //initPluginSDK(appContext);
    }

    @Override
    public void handleCheck(InitCheckCallback callback) {
        checkProgress(this, 10, MainApplication.getInstance().getString(R.string.login_check_config));
        waitRandom();
        checkProgress(this, 100, MainApplication.getInstance().getString(R.string.login_check_config));
    }

    @Override
    protected void onInit() {
        super.onInit();
        Application appContext = MainApplication.getInstance();
        //initPluginSDK(appContext);

        // 需要放在initCommonUtil()之后执行，因为initShopInfo()需要读取SharedPreferenceUtil里面的数据
        //initLocalInfo();
        SettingManager.init(appContext);
//        DBManager.getDefault().init(appContext);
        UpdateManager.getInstance().init(appContext); // Luo for update the APK
        //ContractManager.getInstance().requestContractExpireData(null);
    }

    /**
     * 初始化第三方的插件
     *
     * @param appContext
     */
    /*private void initPluginSDK(Application appContext) {
        //百度地址
        try {
            SDKInitializer.initialize(appContext);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/

}
