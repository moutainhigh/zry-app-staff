package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class FastFoodApplication extends AuthorizedApplication {


    public static final String PERMISSION_FASTFOOD = "pos:kc";

    public static final String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";

    public static final String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";

    public static final String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";

    public static final String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";


    public static final String PERMISSION_FASTFOOD_MALING_AMOUNT_LIMIT = "pos:kc:maling:amount";

    public static final String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";

    public static final String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";

    public static final String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";

    public static final String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";

    public static final String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";

    public static final String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";

    public static final String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";

    public static final String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";


    public static final String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";

    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";

    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";

    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";

    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";

    public static final String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";


    public static final String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";

    public static final String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";


    public static final String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

    public static final String PERMISSION_DISCOUND = "discount";
    public static final String PERMISSION_REBATE = "rebate";


    public static final String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";


    public static final String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";


    public static final String PERMISSION_FASTFOOD_SC = "pos:kc:sc";

    public FastFoodApplication() {
        super(AuthorizedApplication.APP_CODE_FAST_FOOD);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_FASTFOOD);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.cashier_app_name);
    }

    @Override
    public void setIcon() {
            }

    @Override
    public void setLockIcon() {
            }

    @Override
    public void setSort() {
        setSort(200);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_SNACK);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
