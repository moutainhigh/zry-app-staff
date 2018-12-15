package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class BakeryApplication extends AuthorizedApplication {
    /**
     * 美业权限
     */
    public static final String PERMISSION_BAKERY = "pos:hb";


    public BakeryApplication() {
        super(AuthorizedApplication.APP_CODE_BAKERY);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_BAKERY);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.bakery_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.bakery_app_icon);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.bakery_app_icon_locked);
    }

    @Override
    public void setSort() {
        setSort(100);
    }

    @Override
    public void setPackageName() {
        setPackageName(PackageURI.BTY);
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_BAKERY);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
