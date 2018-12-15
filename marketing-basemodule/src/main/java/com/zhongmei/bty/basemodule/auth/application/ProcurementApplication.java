package com.zhongmei.bty.basemodule.auth.application;


import android.content.Intent;
import android.net.Uri;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.commonbusiness.constants.Configure;

/**
 * 采购市场
 */
public class ProcurementApplication extends AuthorizedApplication {

    public ProcurementApplication() {
        super(AuthorizedApplication.APP_CODE_NONE);
    }

    @Override
    public void initMainPermission() {
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.procurement_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.procurement_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.procurement_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(1320);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_PROCUREMENT);
    }

    @Override
    public Intent getIntent() {
        //return super.getIntent();
        Intent intent = super.getIntent();
        intent.setData(Uri.parse(Configure.CAIGOU_SERVER_HOST));
        return intent;
    }

    @Override
    public void setHasNewShareKey() {
    }
}
