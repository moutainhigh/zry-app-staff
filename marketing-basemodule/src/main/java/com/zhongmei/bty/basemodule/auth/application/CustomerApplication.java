package com.zhongmei.bty.basemodule.auth.application;

import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;

public class CustomerApplication extends AuthorizedApplication {

    @CustomerAppConfig.CustomerBussinessType
    public static int mCustomerBussinessType = CustomerAppConfig.CustomerBussinessType.DINNER;

    /**
     * 顾客权限
     */
    public static final String PERMISSION_CUSTOMER = "pos:gk";

    /**
     * 顾客创建权限
     */
    public static final String PERMISSION_CUSTOMER_CREATE = "CUSTOMER_ADD";

    /**
     * 顾客编辑权限
     */
    public static final String PERMISSION_CUSTOMER_EDIT = "CUSTOMER_MODIFY";

    /**
     * 顾客升级权限
     */
    public static final String PERMISSION_CUSTOMER_UPDATE = "pos:gk:update";

    /**
     * 顾客储值权限
     */
    public static final String PERMISSION_CUSTOMER_STORE = "CUSTOMER_RECHARGE";

    /**
     * 储值记录撤销权限
     */
    public static final String PERMISSION_CUSTOMER_STORE_CANCEL = "pos:gk:store:cancel";

    /**
     * 售卡权限
     */
    public static final String PERMISSION_CUSTOMER_SELL_CARD = "pos:gk:sell:card";

    /**
     * 实体卡激活权限
     */
    public static final String PERMISSION_CUSTOMER_CARD_ENABLE = "pos:gk:card:enable";

    /**
     * 售卡退货权限
     */
    public static final String PERMISSION_CUSTOMER_SELL_CARD_REFUND = "pos:gk:sell:card:refund";

    /**
     * 顾客会员积分补录/扣除
     */
    public static final String PERMISSION_MEMBER_INTEGRAL_MODIFY = "pos:gk:integral_modify";

    /**
     * 会员修改会员等级
     */
    public static final String PERMISSION_MEMBER_LEVEL_MODIFY = "pos:gk:level_modify";

    /**
     * 会员修改手机号
     */
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
        //setIcon(R.drawable.customer_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        //setLockIcon(R.drawable.customer_app_lock_icon);
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
