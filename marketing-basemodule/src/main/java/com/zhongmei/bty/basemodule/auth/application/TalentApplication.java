package com.zhongmei.bty.basemodule.auth.application;

import android.content.Intent;

import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class TalentApplication extends AuthorizedApplication {
    /**
     * 员工模块权限
     */
    public static final String PERMISSION_TALENT = "";

    /**
     * 绑定人脸授权
     */
    public static final String PERMISSION_TALENT_BIND_FACE = "pos:talent:bind:face";

    /**
     * 员工管理
     */
    public static final String PERMISSION_TALENT_MANAGE = "pos:talent:manage";

    public TalentApplication() {
        super(AuthorizedApplication.APP_CODE_NONE);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_TALENT);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.app_name_talent);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.talent_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.talent_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(1200);
    }

    @Override
    public void setPackageName() {
        setPackageName(PackageURI.BTY);
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_BAKERY);
        Intent intent = new Intent("com.zhongmei.bty.talent.faceattence");
        setIntent(intent);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
