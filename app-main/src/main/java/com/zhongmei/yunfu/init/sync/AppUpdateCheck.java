package com.zhongmei.yunfu.init.sync;

import android.content.Context;

import com.zhongmei.bty.splash.check.UpdateCheck;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.data.VersionInfo;


public class AppUpdateCheck extends UpdateCheck {

    public AppUpdateCheck(Context context) {
        super(context, context.getString(R.string.level_up_check), ServerAddressUtil.getInstance().getUpdateCheckApi(), true);
    }

    @Override
    protected void onSuccess(VersionInfo response) {
        ShopInfoCfg.getInstance().setAppVersionInfo(response);
                        SharedPreferenceUtil.getSpUtil().putBoolean(response.getVersionCode() + "_remind", response.hasUpdate());
        success(mContext.getString(R.string.update_check_success));
    }

    @Override
    protected void onError(String errorMsg) {
        error(mContext.getString(R.string.update_check_error, errorMsg));
    }
}
