package com.zhongmei.bty.basemodule.auth.application;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;

public class CustomerApplication extends AuthorizedApplication {

    @CustomerAppConfig.CustomerBussinessType
    public static int mCustomerBussinessType = CustomerAppConfig.CustomerBussinessType.DINNER;


    public static final String PERMISSION_CUSTOMER = "pos:gk";


    public static final String PERMISSION_CUSTOMER_CREATE = "CUSTOMER_ADD";


    public static final String PERMISSION_CUSTOMER_EDIT = "CUSTOMER_MODIFY";


    public static final String PERMISSION_CUSTOMER_UPDATE = "pos:gk:update";


    public static final String PERMISSION_CUSTOMER_STORE = "CUSTOMER_RECHARGE";


    public static final String PERMISSION_CUSTOMER_STORE_CANCEL = "pos:gk:store:cancel";


    public static final String PERMISSION_CUSTOMER_SELL_CARD = "pos:gk:sell:card";


    public static final String PERMISSION_CUSTOMER_CARD_ENABLE = "pos:gk:card:enable";


    public static final String PERMISSION_CUSTOMER_SELL_CARD_REFUND = "pos:gk:sell:card:refund";


    public static final String PERMISSION_MEMBER_INTEGRAL_MODIFY = "pos:gk:integral_modify";


    public static final String PERMISSION_MEMBER_LEVEL_MODIFY = "pos:gk:level_modify";


    public static final String PERMISSION_MEMBER_MOBILE_MODIFY = "pos:gk:mobile_modify";

    public CustomerApplication(String appCode) {
        super(appCode);
    }

    public CustomerApplication() {
        super(AuthorizedApplication.APP_CODE_CUSTOMER);
    }

    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_CUSTOMER);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.customer);
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
        setActivityName(PathURI.URI_CUSTOMER);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
