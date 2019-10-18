package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.yunfu.basemodule.R;

public class BeautyApplication extends AuthorizedApplication {

    public static final String PERMISSION_BEAUTY = "pos:beauty";


    public static final String PERMISSION_BEAUTY_CASH = "ORDER_ACCOUNTING";


    public static final String PERMISSION_BEAUTY_CREATE_TRADE = "ORDER_CREATE";


    public static final String PERMISSION_BEAUTY_MONEYBOX = "pos:bs:moneybox";


    public static final String PERMISSION_BEAUTY_MALING = "POS_MALING";

    public static final String PERMISSION_BEAUTY_AUTO_MALING = "POS_AUTOMALING";


    public static final String PERMISSION_BEAUTY_PRIVILEDGE = "POS_PRIVILEGE";


    public static final String PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT = "POS_PRIVILEGE_DISCOUNT";


    public static final String PERMISSION_BEAUTY_PRIVILEDGE_REBATE = "POS_PRIVILEGE_REBATE";


    public static final String PERMISSION_BEAUTY_PRIVILEDGE_FREE = "POS_PRIVILEGE_FREE";

    public static final String PERMISSION_BEAUTY_PRIVILEDGE_PRESENT = "POS_PRIVILEGE_PRESENT";

    public static final String PERMISSION_BEAUTY_PRIVILEDGE_BANQUET = "pos:bs:privilege:banquet";


    public static final String PERMISSION_BEAUTY_INVALID = "POS_INVALID_TRADE";


    public static final String PERMISSION_BEAUTY_REFUND = "POS_REFUND";


    public static final String PERMISSION_BEAUTY_HANDOVER = "pos:bs:handover";


    public static final String PERMISSION_BEAUTY_HANDOVER_LAST = "pos:bs:handover:last";

    public static final String PERMISSION_BEAUTY_REPAY = "pos:bs:repeat";


    public static final String PERMISSION_PAYMENTS_EDIT = "pos:bs:paymentsedit";


    public static final String PERMISSION_BEAUTY_CLOSING = "pos:bs:closing";


    public static final String PERMISSION_BEAUTY_CLOSING_HISTORY = "pos:bs:closing:history";


    public static final String PERMISSION_BEAUTY_ACCEPT = "pos:bs:accept";


    public static final String PERMISSION_BEAUTY_REPORT_FORM = "POS_REPORT";



    public static final String PERMISSION_BEAUTY_CREATE_RESERVER = "POS_CREATE_RESERVER";



    public static final String PERMISSION_BEAUTY_SHOP_MANAGE = "POS_SHOP_MANAGE";



    public static final String PERMISSION_BEAUTY_ACCEPT_OR_REFUSE_RESERVER = "POS_ACCEPT_REFUSE_RESERVER";


    public static final String PERMISSION_BEAUTY_MODIFY_RESERVER = "POS_REFRESH_RESERVER";


    public static final String PERMISSION_CUSTOMER_LOGIN = "USER_LOGIN";

    public BeautyApplication() {
        super(AuthorizedApplication.APP_CODE_BEAUTY);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_BEAUTY);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.beauty_app_name);
    }

    @Override
    public void setIcon() {
            }

    @Override
    public void setLockIcon() {
            }

    @Override
    public void setSort() {
        setSort(100);
    }

    @Override
    public void setPackageName() {
        setPackageName(PackageURI.BTY);
    }

    @Override
    public void setActivityName() {
        setActivityName("com.zhongmei.beauty.BeautyMainActivity_");
    }

    @Override
    public void setHasNewShareKey() {
    }
}
