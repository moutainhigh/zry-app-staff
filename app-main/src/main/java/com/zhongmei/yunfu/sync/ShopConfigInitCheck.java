package com.zhongmei.yunfu.sync;

import android.app.Application;

import com.zhongmei.bty.common.util.UpdateManager;
import com.zhongmei.util.SettingManager;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;


public class ShopConfigInitCheck extends AbsInitCheck {

    public ShopConfigInitCheck() {
        super(CheckType.ConfigInitCheck);
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

                        SettingManager.init(appContext);
        UpdateManager.getInstance().init(appContext);             }




}
