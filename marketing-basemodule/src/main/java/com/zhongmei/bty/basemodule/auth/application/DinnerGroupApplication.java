package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;


public class DinnerGroupApplication extends AuthorizedApplication {

    public static final String PERMISSION_GROUPING = "pos:tc";


    public static final String PERMISSION_GROUP_MODIFY_DISH = "pos:tc:modify_dish";

    public DinnerGroupApplication() {
        super(AuthorizedApplication.APP_CODE_DINNER_GROUP);
    }

    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_GROUPING);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.group_app_name);
    }

    @Override
    public void setIcon() {
            }

    @Override
    public void setLockIcon() {
            }

    @Override
    public void setSort() {
        setSort(400);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_GROUP);
    }

    @Override
    public void setHasNewShareKey() {
    }

}
