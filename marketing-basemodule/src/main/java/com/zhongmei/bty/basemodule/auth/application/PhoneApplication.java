package com.zhongmei.bty.basemodule.auth.application;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class PhoneApplication extends AuthorizedApplication {
    /**
     * 电话权限
     */
    public static final String PERMISSION_PHONE = "pos:dh";

    public PhoneApplication() {
        super(AuthorizedApplication.APP_CODE_CALL);
    }

    @Override
    public void initMainPermission() {
        //setMainPermission(PERMISSION_PHONE);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.phone_app_name);
    }

    @Override
    public void setIcon() {
        //setIcon(R.drawable.phone_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        //setLockIcon(R.drawable.phone_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(1100);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_PHONE);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
