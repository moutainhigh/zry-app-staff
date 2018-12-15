package com.zhongmei.bty.splash.check;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.data.VersionInfo;

/**
 * 打印升级检查
 *
 * @created 2017/05/09
 */
public class PrintUpdateCheck extends UpdateCheck {

    public PrintUpdateCheck(Context context) {
        super(context, context.getString(R.string.level_up_check), ServerAddressUtil.getInstance().getPrintUpdateCheckApi(), true);
    }

    @Override
    protected void onSuccess(VersionInfo response) {
        //MainApplication.getInstance().initPrintVersionInfo(response);
        ShopInfoCfg.getInstance().setPrintVersionInfo(response);
        success(mContext.getString(R.string.print_update_check_success));
    }

    @Override
    protected void onError(String errorMsg) {
        error(mContext.getString(R.string.print_update_check_error, errorMsg));
    }
}
