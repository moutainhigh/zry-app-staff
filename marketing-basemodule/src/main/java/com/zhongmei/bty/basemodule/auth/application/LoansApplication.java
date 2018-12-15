package com.zhongmei.bty.basemodule.auth.application;


import android.content.Intent;
import android.net.Uri;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;

/**
 * 采购市场
 */
public class LoansApplication extends AuthorizedApplication {

    public static final String PERMISSION_LOANS = "pos:loans";

    public LoansApplication() {
        super(AuthorizedApplication.APP_CODE_NONE);
    }

    @Override
    public void initMainPermission() {
        //setMainPermission(PERMISSION_LOANS);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.loans_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.loans_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.loans_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(1310);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_LOANS);
    }

    @Override
    public Intent getIntent() {
        //return super.getIntent();
        String openStoreUrl = ServerAddressUtil.getPortalURLHost();
        return super.getIntent().setData(Uri.parse(openStoreUrl));
    }

    @Override
    public void setHasNewShareKey() {
    }
}
