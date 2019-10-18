package com.zhongmei.bty.basemodule.auth.application;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class PhoneApplication extends AuthorizedApplication {

    public static final String PERMISSION_PHONE = "pos:dh";

    public PhoneApplication() {
        super(AuthorizedApplication.APP_CODE_CALL);
    }

    @Override
    public void initMainPermission() {
                setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.phone_app_name);
    }

    @Override
    public void setIcon() {
            }

    @Override
    public void setLockIcon() {
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
