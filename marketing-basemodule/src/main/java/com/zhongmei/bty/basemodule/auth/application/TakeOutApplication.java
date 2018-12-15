package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class TakeOutApplication extends BaseApplication {
    /**
     * 外卖权限
     */
    public static final String PERMISSION_TAKEOUT = "pos:wm";

    /**
     * 外卖接受拒绝权限
     */
    public static final String PERMISSION_TAKEOUT_ACCEPT = "pos:wm:accept";

    /**
     * 外卖作废权限
     */
    public static final String PERMISSION_TAKEOUT_INVALID = "pos:wm:invalid";

    /**
     * 外卖退货权限
     */
    public static final String PERMISSION_TAKEOUT_REFUND = "pos:wm:refund";

    /**
     * 外卖清账权限
     */
    public static final String PERMISSION_TAKEOUT_CLEAR = "pos:wm:clear";

    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_TAKEOUT);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.takeout_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.takeout_app_icon);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.takeout_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(3);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_DELIVERY_SERVICE);
    }

    @Override
    public void setHasNewShareKey() {
    }

}
