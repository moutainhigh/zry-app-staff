package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

/**
 * 团餐
 */
public class DinnerGroupApplication extends AuthorizedApplication {
    /**
     * 团餐权限  pos:tc
     */
    public static final String PERMISSION_GROUPING = "pos:tc";

    /**
     * 团餐修改菜品权限
     */
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
        setIcon(R.drawable.dinner_group_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.dinner_group_app_icon_lock);
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
