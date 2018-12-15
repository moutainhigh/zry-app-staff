package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class QueueApplication extends AuthorizedApplication {
    /**
     * 排队权限
     */
    public static final String PERMISSION_QUEUE = "pos:pd";

    public QueueApplication() {
        super(AuthorizedApplication.APP_CODE_QUEUE);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_QUEUE);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.queue_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.queue_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.queue_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(700);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_QUEUE);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
