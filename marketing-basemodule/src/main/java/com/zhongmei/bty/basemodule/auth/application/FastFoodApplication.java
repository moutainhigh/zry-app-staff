package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class FastFoodApplication extends AuthorizedApplication {

    /**
     * 快餐权限
     */
    public static final String PERMISSION_FASTFOOD = "pos:kc";
    /**
     * 快餐开台权限
     */
    public static final String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";
    /**
     * 快餐收银权限
     */
    public static final String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";
    /**
     * 快餐开钱箱权限
     */
    public static final String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";
    /**
     * 快餐自定义抹零权限
     */
    public static final String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";

    /**
     * 快餐自定义抹零金额权限
     */
    public static final String PERMISSION_FASTFOOD_MALING_AMOUNT_LIMIT = "pos:kc:maling:amount";
    /**
     * 快餐抹零权限
     */
    public static final String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";
    /**
     * 快餐接受拒绝权限
     */
    public static final String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";
    /**
     * 快餐作废权限
     */
    public static final String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";
    /**
     * 快餐退货权限
     */
    public static final String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";
    /**
     * 快餐补打权限
     */
    public static final String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";
    /**
     * 快餐交接权限
     */
    public static final String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";
    /**
     * 快餐历史交接权限
     */
    public static final String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";
    /**
     * 快餐校准权限
     */
    public static final String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";

    /**
     * 快餐优惠权限
     */
    public static final String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";
    /**
     * 快餐打折优惠权限
     */
    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";
    /**
     * 快餐折让权限
     */
    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";
    /**
     * 快餐免单权限
     */
    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";
    /**
     * 快餐赠送权限
     */
    public static final String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";
    /**
     * 快餐关账权限
     */
    public static final String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";

    /**
     * 快餐关账历史权限
     */
    public static final String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";
    /**
     * 快餐通知中心重试pos交易权限
     */
    public static final String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";

    /**
     * 收支编辑权限
     */
    public static final String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

    public static final String PERMISSION_DISCOUND = "discount";
    public static final String PERMISSION_REBATE = "rebate";

    /**
     * 快餐反结账权限
     */
    public static final String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";

    /**
     * 报表中心权限
     */
    public static final String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";

    /**
     * 快餐送餐权限
     */
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
        //setIcon(R.drawable.cashier_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        //setLockIcon(R.drawable.cashier_app_lock_icon);
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
