package com.zhongmei.beauty.applications;


import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.AuthorizedApplication;

public class BeautySettingApplication extends AuthorizedApplication {
    /**
     * 美业权限 pos:beauty:settings
     */
    public static final String PERMISSION_BEAUTY = "pos:beauty:settings";


    public BeautySettingApplication() {
        super(AuthorizedApplication.APP_CODE_BEAUTY_setting);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_BEAUTY);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.beauty_app_setting);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.dinner_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.dinner_app_lock_icon);
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
        setActivityName("com.zhongmei.beauty.BeautySettingActivity_");
    }

    @Override
    public void setHasNewShareKey() {
    }
}
