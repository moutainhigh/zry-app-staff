package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class BookingApplication extends AuthorizedApplication {
    /**
     * 预订权限
     */
    public static final String PERMISSION_BOOKING = "pos:yd";

    public BookingApplication() {
        super(AuthorizedApplication.APP_CODE_BOOKING);
    }

    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_BOOKING);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.booking);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.booking_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.booking_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(800);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_BOOKING);
    }

    @Override
    public void setHasNewShareKey() {
    }

}
