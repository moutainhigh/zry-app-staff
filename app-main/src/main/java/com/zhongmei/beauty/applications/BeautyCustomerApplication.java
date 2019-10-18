package com.zhongmei.beauty.applications;

import com.zhongmei.bty.basemodule.auth.application.AuthorizedApplication;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;


public class BeautyCustomerApplication extends CustomerApplication {


    public static final String PERMISSION_CUSTOMER = "pos:beauty:gk";


    public BeautyCustomerApplication() {
        super(AuthorizedApplication.APP_CODE_BEAUTY_CUSTOMER);
    }

    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_CUSTOMER);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle("美业会员");
    }

    @Override
    public void setIcon() {
            }

    @Override
    public void setLockIcon() {
            }

    @Override
    public void setSort() {
        setSort(600);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName("com.zhongmei.beauty.customer.BeautyCustomerActivity_");
    }

    @Override
    public void setHasNewShareKey() {
    }
}
