package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class DinnerApplication extends AuthorizedApplication {

    public static final String PERMISSION_DINNER = "pos:zc";


    public static final String PERMISSION_DINNER_CREATE = "pos:zc:create";


    public static final String PERMISSION_DINNER_MERGE = "pos:zc:merge";


    public static final String PERMISSION_DINNER_TRANFER = "pos:zc:tranfer";


    public static final String PERMISSION_DINNER_CASH = "pos:zc:cash";


    public static final String PERMISSION_DINNER_MONEYBOX = "pos:zc:moneybox";


    public static final String PERMISSION_DINNER_MALING = "pos:zc:maling";

    public static final String PERMISSION_DINNER_AUTO_MALING = "pos:zc:automaling";


    public static final String PERMISSION_DINNER_MALING_AMOUNT_LIMIT = "pos:zc:maling:amount";

    public static final String PERMISSION_DINNER_ACCEPT = "pos:zc:accept";


    public static final String PERMISSION_DINNER_INVALID = "pos:zc:invalid";


    public static final String PERMISSION_DINNER_REFUND = "pos:zc:refund";


    public static final String PERMISSION_DINNER_HANDOVER = "pos:zc:handover";


    public static final String PERMISSION_DINNER_HANDOVER_LAST = "pos:zc:handover:last";


    public static final String PERMISSION_DINNER_PRIVILEDGE = "pos:zc:privilege";


    public static final String PERMISSION_DINNER_PRIVILEDGE_DISCOUNT = "pos:zc:privilege:discount";


    public static final String PERMISSION_DINNER_PRIVILEDGE_REBATE = "pos:zc:privilege:rebate";


    public static final String PERMISSION_DINNER_PRIVILEDGE_FREE = "pos:zc:privilege:free";

    public static final String PERMISSION_DINNER_PRIVILEDGE_PRESENT = "pos:zc:privilege:present";

    public static final String PERMISSION_DINNER_PRIVILEDGE_BANQUET = "pos:zc:privilege:banquet";

    public static final String PERMISSION_DINNER_REPAY = "pos:zc:repeat";


    public static final String PERMISSION_DINNER_CLOSING = "pos:zc:closing";


    public static final String PERMISSION_DINNER_CLOSING_HISTORY = "pos:zc:closing:history";


    public static final String PERMISSION_DINNER_RETURN_GOODS = "pos:zc:return:goods";


    public static final String PERMISSION_DINNER_NOTIFY_POS_RETRY = "pos:zc:notify:pos:retry";


    public static final String PERMISSION_DINNER_SPLIT = "pos:zc:split";


    public static final String PERMISSION_DINNER_CREDIT = "pos:zc:credit";


    public static final String PERMISSION_PAYMENTS_EDIT = "pos:zc:paymentsedit";


    public static final String PERMISSION_SELECT_WAITER = "pos:zx:selectwaiter";


    public static final String PERMISSION_DINNER_MOVE_DISH = "pos:zc:movedish";


    public static final String PERMISSION_DINNER_REPORT_FORM = "pos:zc:reportform";


    public static final String PERMISSION_DINNER_TAKEOUT_CLEAR = "pos:zc:clear";

    public static final String PERMISSION_DINNER_HANDOVER_CALIBRATE = "pos:zc:handover:calibrate";


    public static final String PERMISSION_DINNER_PREREPRINT_SETTING = "pos:zc:preprint_setting";


    public static final String PERMISSION_DINNER_QUANTITY = "pos:zc:weight_input";


    public static final String PERMISSION_DINNER_CHANGEPRICE = "pos:zc:changeprice";


    public static final String PERMISSION_DISCOUND = "discount";
    public static final String PERMISSION_REBATE = "rebate";



    public static final String PERMISSION__DINNER_DELETE_DISH = "pos:zc:delete_dish";


    public static final String PERMISSION_DINNER_BATCH_DELETE_OR_RETURN = "pos:zc:batch_delete_or_return";


    public static final String PERMISSION_DINNER_BUSINESS_CHARGE = "pos:zc:business_charge";


    public static final String PERMISSION_DINNER_MODIFY_DISH = "pos:zc:modify_dish";


    public static final String PERMISSION_DINNER_MODIFY_PRICE = "pos:zc:modify_price";

    public DinnerApplication() {
        super(AuthorizedApplication.APP_CODE_DINNER);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_DINNER);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.dinner_app_name);
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
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_DINNER);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
